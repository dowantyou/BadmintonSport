package com.badmintonsport.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@TableName(value ="user")
@Data
public class Users implements Serializable {

    @TableId(type = IdType.AUTO)
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
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double dimension;
}
