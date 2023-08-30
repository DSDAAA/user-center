package com.example.model.domain.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 31912417163120793L;
    private String userAccount;
    private String userPassword;
}
