package com.badmintonsport.service;


import com.badmintonsport.common.result.Result;
import com.badmintonsport.pojo.dto.UserLoginDTO;
import com.badmintonsport.pojo.entity.Posts;
import com.badmintonsport.pojo.entity.Users;
import com.badmintonsport.pojo.vo.GetUserVO;
import com.badmintonsport.pojo.vo.PostsVO;
import com.badmintonsport.pojo.vo.UserLoginVO;
import com.badmintonsport.pojo.vo.UsersVO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService extends IService<Users> {
     int register(UserLoginDTO userLoginDTO);

    UserLoginVO login(UserLoginDTO userLoginDTO);

    GetUserVO getUser(UserLoginDTO userLoginDTO);

    List<Posts> getPosts(UserLoginDTO userLoginDTO);

    /**
     * 功能描述:将用户批量添加到redis中
     * @MethodName: shopDataToRedis
     * @MethodParam: []
     * @Return: void
     * @Author: yyalin
     * @CreateDate: 2023/8/17 17:12
     */
    void UserDataToRedis();

    /**
     * 功能描述:根据用户位置搜索附近用户
     * @param radius
     * @param loginUser
     * @return
     */
    List<UsersVO> searchNearby(int radius, Users loginUser);

//    Users getLoginUser(HttpServletRequest request);
}
