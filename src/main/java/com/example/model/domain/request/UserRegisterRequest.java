package com.example.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author dunston
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 31912417163120793L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
