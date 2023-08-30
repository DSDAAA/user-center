package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.model.domain.User;
import com.example.model.domain.request.UserLoginRequest;
import com.example.model.domain.request.UserRegisterRequest;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.contant.UserConstant.ADMIN_ROLE;
import static com.example.contant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        long userId = currentUser.getId();
        //todo 校验用户是否合法
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    @PostMapping("login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        //仅管理员可查询
        if (isAdmin(request)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(this::apply).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        //仅管理员可查询
        if (isAdmin(request)) {
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    private User apply(User user) {
        user.setUserPassword(null);
        return userService.getSafetyUser(user);
    }
}
