package com.badmintonsport.pojo.vo;

import com.badmintonsport.pojo.entity.Carousel;
import com.badmintonsport.pojo.entity.News;
import lombok.Data;

import java.util.List;

@Data
public class GetNewsInformationVO {
    private List<Carousel> carousel;
    private List<News> news;
}
