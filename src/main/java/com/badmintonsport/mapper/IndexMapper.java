package com.badmintonsport.mapper;

import com.badmintonsport.pojo.entity.Carousel;
import com.badmintonsport.pojo.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IndexMapper {

    //获取首页轮播图的信息
    @Select("select image_id,image_url from carousel ")
    List<Carousel> getCarousel();

    //获取首页新闻的信息
    @Select("select news_id,title,content,news_image,published_at,user_id" +
            ",username from news ")
    List<News> getNews();
}
