package com.badmintonsport.pojo.vo;

import lombok.Data;

@Data
public class UsersVO {
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

    /**
     * 用户距离
     */
    private Double distance;
}
