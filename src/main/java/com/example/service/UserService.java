package com.example.service;

import com.example.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 用户校验码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);
}
