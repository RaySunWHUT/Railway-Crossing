package com.railwaycrossing.dao;

import ch.qos.logback.core.db.ConnectionSourceBase;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.railwaycrossing.entity.Locomotive;
import com.railwaycrossing.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {

    User getUserByUserAccount(String userAccount);

//    void register(User newUser);


    Integer saveUser(JSONObject jsonObject);

    JSONObject getRoleByUserAccount(String userAccount);

    JSONObject listOneUser(String userAccount);

    Integer updateLoginTime(JSONObject lastLoginTime);


}
