package com.badmintonsport.controller;

import com.badmintonsport.common.result.Result;
import com.badmintonsport.pojo.vo.GetNewsInformationVO;
import com.badmintonsport.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private IndexService indexService;

    //获取首页资讯的推荐新闻信息
    @RequestMapping("/getNewsInformation")
    public Result<GetNewsInformationVO> getNewsInformation() {
        return Result.success(indexService.getNewsInformation());
    }
}
