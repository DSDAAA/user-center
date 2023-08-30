package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.domain.User;
import com.example.service.UserService;
import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.contant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 13180
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-08-28 19:35:41
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    @Resource
    private UserMapper userMapper;
    /**
     * 混淆密码
     */
    private final String SALT = "Dunston";

    /**
     * 用户登录态
     */


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1.校验
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, planetCode)) {
            //todo 修改位自定义异常
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        //todo 账户不能包含特殊字符
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //插入数据
        User user = new User();
        user.setUsername(userAccount);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    /**
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户登录
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        //todo 账户不能包含特殊字符
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccound cannot match userPassword");
            return null;
        }
        //3.用户脱敏
        User saftyUser = getSafetyUser(user);
        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, saftyUser);
        return saftyUser;
    }

    @Override
    public User getSafetyUser(User orginUser) {
        //判空
        if (orginUser == null) {
            return null;
        }
        //用户脱敏
        User saftyUser = new User();
        saftyUser.setId(orginUser.getId());
        saftyUser.setUsername(orginUser.getUsername());
        saftyUser.setUserAccount(orginUser.getUserAccount());
        saftyUser.setAvatarUrl(orginUser.getAvatarUrl());
        saftyUser.setGender(orginUser.getGender());
        saftyUser.setPhone(orginUser.getPhone());
        saftyUser.setEmail(orginUser.getEmail());
        saftyUser.setUserRole(orginUser.getUserRole());
        saftyUser.setUserStatus(orginUser.getUserStatus());
        saftyUser.setCreateTime(orginUser.getCreateTime());
        return saftyUser;
    }
}




