package com.badmintonsport.pojo.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private int userId;
    private String  phonenumber;
    private String password;
}
