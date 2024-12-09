package com.badmintonsport.mapper;

import com.badmintonsport.pojo.dto.UserLoginDTO;
import com.badmintonsport.pojo.entity.Posts;
import com.badmintonsport.pojo.entity.Users;
import com.badmintonsport.pojo.vo.GetUserVO;
import com.badmintonsport.pojo.vo.PostsVO;
import com.badmintonsport.pojo.vo.UserLoginVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<Users> {

    //获取第一个用户
    Users getFirstUser();
    Users selectById(Long userId);

    //用户注册
    void register(UserLoginDTO usersLoginDTO);

    //查询用户是否存在
    @Select("select * from user where phonenumber=#{phonenumber}")
    Users ifexituser(String phoneNumber);

    //用户登录
    @Select("select user_id,password from user where phonenumber=#{phonenumber}")
    UserLoginVO login(UserLoginDTO userLoginDTO);

    //获取用户个人信息
    @Select("select user_id,account,profile_picture,background_image," +
            "signature,age,followers_count,following_count,likes_count," +
            "favorites_count,likes_count,favorites_count,phonenumber," +
            "password from user where user_id=#{userId}")
    GetUserVO getUser(UserLoginDTO userLoginDTO);

    //获取用户发布的帖子
    @Select("select post_id,title,content" +
            ",video_url,images,created_at" +
            " from posts where user_id=#{userId}")
    List<Posts> getPosts(UserLoginDTO userLoginDTO);
}
