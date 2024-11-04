package com.badmintonsport.service;

import com.badmintonsport.pojo.entity.Carousel;
import com.badmintonsport.pojo.entity.News;
import com.badmintonsport.pojo.vo.GetNewsInformationVO;

import java.util.List;

public interface IndexService {
    //获取首页资讯的推荐新闻信息
    GetNewsInformationVO getNewsInformation();

    //获取首页轮播图的信息
    List<Carousel> getCarousel();

    //获取首页新闻的信息
    List<News> getNews();
}
