package com.badmintonsport.pojo.vo;

import com.badmintonsport.pojo.entity.Posts;
import lombok.Data;

import java.util.List;

@Data
public class GetUserVO {
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
    List<Posts> posts;

}
