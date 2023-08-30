package com.example.service;

import com.example.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author 13180
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-08-28 19:35:41
 */

/**
 * @author Dunston
 */
public interface UserService extends IService<User> {
    /**
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 用户校验码
     * @param planetCode
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param orginUser
     * @return
     */
    User getSafetyUser(User orginUser);
}
