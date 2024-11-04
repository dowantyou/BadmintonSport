package com.badmintonsport.service.impl;

import com.badmintonsport.mapper.IndexMapper;
import com.badmintonsport.pojo.entity.Carousel;
import com.badmintonsport.pojo.entity.News;
import com.badmintonsport.pojo.vo.GetNewsInformationVO;
import com.badmintonsport.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceimpl implements IndexService {
    @Autowired
    private IndexMapper indexMapper;

    //获取首页资讯的推荐新闻信息
    @Override
    public GetNewsInformationVO getNewsInformation() {
        //将轮播图和新闻信息封装到GetNewsInformationVO中
        GetNewsInformationVO getNewsInformationVO = new GetNewsInformationVO();
        getNewsInformationVO.setCarousel(getCarousel());
        getNewsInformationVO.setNews(getNews());
        return getNewsInformationVO;
    }

    //获取首页轮播图的信息
    @Override
    public List<Carousel> getCarousel() {
        return indexMapper.getCarousel();
    }

    //获取首页新闻的信息
    @Override
    public List<News> getNews() {
        return indexMapper.getNews();
    }
}
