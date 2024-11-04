package com.badmintonsport.service.impl;

import com.badmintonsport.common.constant.MessageConstant;
import com.badmintonsport.common.constant.PasswordConstant;
import com.badmintonsport.common.exception.AccountNotFoundException;
import com.badmintonsport.common.exception.PasswordErrorException;
import com.badmintonsport.common.result.Result;
import com.badmintonsport.mapper.UserMapper;
import com.badmintonsport.pojo.dto.UserLoginDTO;
import com.badmintonsport.pojo.entity.Posts;
import com.badmintonsport.pojo.entity.Users;
import com.badmintonsport.pojo.vo.GetUserVO;
import com.badmintonsport.pojo.vo.PostsVO;
import com.badmintonsport.pojo.vo.UserLoginVO;
import com.badmintonsport.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int register(UserLoginDTO userLoginDTO) {
        System.out.println(userLoginDTO);

       //判断用户是否存在
       if(ifexituser(userLoginDTO.getPhonenumber())==1) {
           //将密码进行MD5加密
           userLoginDTO.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
           userMapper.register(userLoginDTO);
           return userLoginDTO.getUserId();
       }

           //该用户已存在
           return -1;


    }

    //查询改该手机号的用户是否存在
    public int ifexituser(String phoneNumber) {
        if(userMapper.ifexituser(phoneNumber)==null) {
            //该用户不存在
            return 1;
        }
        //该用户存在
        return -1;
    }

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        String password = userLoginDTO.getPassword();
        UserLoginVO userLoginvo = userMapper.login(userLoginDTO);
        //处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if(userLoginvo==null)
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        //密码比对
        //  后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(userLoginvo.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        userLoginvo.setPassword(null);
        return userLoginvo;
    }

    //获取用户个人信息
    @Override
    public GetUserVO getUser(UserLoginDTO userLoginDTO) {
        GetUserVO getUserVO = userMapper.getUser(userLoginDTO);
        getUserVO.setPosts(getPosts(userLoginDTO));
        return getUserVO;
    }

    @Override
    public List<Posts> getPosts(UserLoginDTO userLoginDTO) {
        return userMapper.getPosts(userLoginDTO);
    }


}
