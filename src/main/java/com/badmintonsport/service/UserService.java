package com.badmintonsport.service;


import com.badmintonsport.common.result.Result;
import com.badmintonsport.pojo.dto.UserLoginDTO;
import com.badmintonsport.pojo.vo.GetUserVO;
import com.badmintonsport.pojo.vo.PostsVO;
import com.badmintonsport.pojo.vo.UserLoginVO;

import java.util.List;

public interface UserService {
     int register(UserLoginDTO userLoginDTO);

    UserLoginVO login(UserLoginDTO userLoginDTO);

    GetUserVO getUser(UserLoginDTO userLoginDTO);

    List<PostsVO> getPosts(UserLoginDTO userLoginDTO);
}
