package com.badmintonsport.controller;

import com.badmintonsport.common.constant.MessageConstant;
import com.badmintonsport.common.exception.AccountNotFoundException;
import com.badmintonsport.common.properties.JwtProperties;
import com.badmintonsport.common.result.Result;
import com.badmintonsport.common.utils.JwtUtil;
import com.badmintonsport.pojo.dto.UserLoginDTO;
import com.badmintonsport.pojo.entity.Posts;
import com.badmintonsport.pojo.vo.GetUserVO;
import com.badmintonsport.pojo.vo.UserLoginVO;
import com.badmintonsport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    //用户注册
    @RequestMapping("/register")
    public Result register(@RequestBody UserLoginDTO userLoginDTO) {
        System.out.println(userLoginDTO);
        int userId = userService.register(userLoginDTO);
        if (userId == -1) {
            return Result.error("该手机号已被注册，请登录");
        }
        return Result.success(userId);
    }


    //用户登录
    @RequestMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginvo= userService.login(userLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        String UserId = "UserId";
        claims.put(UserId, userLoginvo.getUserId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        userLoginvo.setToken(token);
        return Result.success(userLoginvo);
    }

    //获取用户个人信息
    @RequestMapping("/getUser")
    public Result<GetUserVO> getUser(@RequestBody UserLoginDTO userLoginDTO) {
        return Result.success(userService.getUser(userLoginDTO));
    }

    //获取用户发布的帖子
    @RequestMapping("/getPosts")
    public Result<List<Posts>> getPosts(@RequestBody UserLoginDTO userLoginDTO) {
        return Result.success(userService.getPosts(userLoginDTO));
    }
}
