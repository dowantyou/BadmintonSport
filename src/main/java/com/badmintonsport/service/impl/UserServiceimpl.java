package com.badmintonsport.service.impl;

import com.badmintonsport.common.constant.MessageConstant;
import com.badmintonsport.common.constant.PasswordConstant;
import com.badmintonsport.common.constant.RedisConstant;
import com.badmintonsport.common.constant.UserConstant;
import com.badmintonsport.common.context.ErrorCode;
import com.badmintonsport.common.exception.AccountNotFoundException;
import com.badmintonsport.common.exception.BusinessException;
import com.badmintonsport.common.exception.PasswordErrorException;
import com.badmintonsport.common.properties.JwtProperties;
import com.badmintonsport.common.result.Result;
import com.badmintonsport.common.utils.JwtUtil;
import com.badmintonsport.mapper.UserMapper;
import com.badmintonsport.pojo.dto.UserLoginDTO;
import com.badmintonsport.pojo.entity.Posts;
import com.badmintonsport.pojo.entity.Users;
import com.badmintonsport.pojo.vo.GetUserVO;
import com.badmintonsport.pojo.vo.PostsVO;
import com.badmintonsport.pojo.vo.UserLoginVO;
import com.badmintonsport.pojo.vo.UsersVO;
import com.badmintonsport.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceimpl extends ServiceImpl<UserMapper, Users> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
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

    @Override
    public void UserDataToRedis() {
        // 1. 查询用户数据
        List<Users> userGeoList = query().list();
        // 2. 设置key值
        String key = RedisConstant.USER_GEO_KEY;
        // 3. 初始化
        List<RedisGeoCommands.GeoLocation<String>> locationList = new ArrayList<>(userGeoList.size());
        // 4. 写入redis
        for (Users user : userGeoList) {
            locationList.add(new RedisGeoCommands.GeoLocation<>(String.valueOf(user.getUserId()), new Point(user.getLongitude(),
                    user.getDimension()))); // 往locationList添加每个用户的经纬度数据
        }
        stringRedisTemplate.opsForGeo().add(key, locationList); // 将每个用户的经纬度信息写入Redis中
    }

    @Override
    public List<UsersVO> searchNearby(int radius, Users loginUser) {
        String geoKey = RedisConstant.USER_GEO_KEY;
        // 1. 获取数据库中第一个用户的经纬度信息
        Users firstUser = this.getFirstUser(); // 获取数据库中第一个用户
        if (firstUser == null || firstUser.getLongitude() == null || firstUser.getDimension() == null) {
            return null; // 如果第一个用户信息为空，返回 null
        }

        // 获取第一个用户的经纬度
        Double longitude = firstUser.getLongitude(); // 经度
        Double dimension = firstUser.getDimension(); // 纬度

        // 2. 检查经纬度是否为空
        if (longitude == null || dimension == null) {
            return null;
        }

        // 3. 设置查询半径
        Distance geoRadius = new Distance(radius, RedisGeoCommands.DistanceUnit.KILOMETERS);
        Circle circle = new Circle(new Point(longitude, dimension), geoRadius);

        // 4. 从 Redis 中查询附近的用户（Geo 命令）
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo().radius(geoKey, circle);

        // 5. 获取附近用户的ID列表
        List<Long> userIdList = new ArrayList<>();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
            String id = result.getContent().getName();
            if (!String.valueOf(loginUser.getUserId()).equals(id)) { // 排除当前登录用户
                userIdList.add(Long.parseLong(id));
            }
        }

        // 6. 构造返回的用户信息列表
        List<UsersVO> userVOList = userIdList.stream().map(id -> {
                    // 6.1. 查询用户详细信息
                    Users users = this.getById(id); // 通过ID从数据库查询用户
                    if (users == null) {
                        return null; // 如果用户不存在，返回 null
                    }

                    // 6.2. 创建 UsersVO 对象并复制属性
                    UsersVO usersVO = new UsersVO();
                    BeanUtils.copyProperties(users, usersVO);

                    // 6.3. 计算该用户与当前登录用户的距离
                    Distance distance = stringRedisTemplate.opsForGeo().distance(geoKey, String.valueOf(loginUser.getUserId()), String.valueOf(id),
                            RedisGeoCommands.DistanceUnit.KILOMETERS);
                    if (distance != null) {
                        usersVO.setDistance(distance.getValue()); // 设置距离
                    }

                    return usersVO;
                }).filter(Objects::nonNull) // 过滤掉为 null 的结果
                .collect(Collectors.toList()); // 收集成列表

        return userVOList;
    }

    // 查找第一个用户的方法
    public Users getFirstUser() {
        return userMapper.getFirstUser();
    }

//    @Override
//    public Users getLoginUser(HttpServletRequest request) {
//        String token = request.getHeader("Authorization");
//        if (token == null || token.isEmpty()) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
//        }
//        // 去掉 "Bearer " 前缀
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//        // 解析 token 并获取用户信息
//        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
//        Integer userId = (Integer) claims.get("UserId");
//        if (userId == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
//        }
//        return userMapper.selectById(userId);
//    }

}
