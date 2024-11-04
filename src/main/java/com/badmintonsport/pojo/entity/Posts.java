package com.badmintonsport.pojo.entity;

import lombok.Data;

@Data
public class Posts {
    private int post_id;
    private String title;
    private String content;
    private String video_url;
    private String images;
    private String created_at;
    private int user_id;
}
