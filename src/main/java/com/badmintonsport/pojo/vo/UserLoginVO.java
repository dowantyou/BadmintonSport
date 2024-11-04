package com.badmintonsport.pojo.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private int userId;
    private String  phonenumber ;
    private String password;
    private String token ;
}
