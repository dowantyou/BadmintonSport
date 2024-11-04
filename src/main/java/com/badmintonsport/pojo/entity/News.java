package com.badmintonsport.pojo.entity;

import lombok.Data;

@Data
public class News {
    private String newsId;
    private String title;
    private String content;
    private String newsImage;
    private String publishedAt;
    private String userId;
    private String username;
}
