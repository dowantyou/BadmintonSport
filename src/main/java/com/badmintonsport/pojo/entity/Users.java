package com.badmintonsport.pojo.entity;

import lombok.Data;

@Data
public class Users {
    private int userId;
    private String account;
    private String profilePicture;
    private String backgroundImage;
    private String signature;
    private String age;
    private String followersCount;
    private String followingCount;
    private String likesCount;
    private String favouritesCount;
    private String phonenumber;
    private String password;
}
