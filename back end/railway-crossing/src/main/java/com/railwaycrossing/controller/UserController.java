package com.railwaycrossing.controller;

import com.alibaba.fastjson.JSONObject;
import com.railwaycrossing.exception.UpdateException;
import com.railwaycrossing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping(value = "/updatePassword")
    public JSONObject updatePassword(@RequestBody JSONObject message) {

        return userService.updatePassword(message);

    }


    @PostMapping(value = "/updateUser")
    public JSONObject updateUser(@RequestBody JSONObject message) throws UpdateException {

        return userService.updateUser(message);

    }


    @PostMapping(value = "/register")
    public JSONObject register(@RequestBody JSONObject message) {

        return userService.register(message);

    }


    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public JSONObject logout(@RequestBody JSONObject message) {     // 退出

        return userService.logout(message);

    }

    @RequestMapping(value = {"/listOneUser"}, method = RequestMethod.POST)
    public JSONObject listOneUser(@RequestBody JSONObject message) {

        return userService.listOneUser(message);

    }


//    @RequestMapping(value = {"/updateLoginTime"}, method = RequestMethod.POST)
//    public JSONObject updateLoginTime(@RequestBody JSONObject message) {
//
//        return userService.updateLoginTime(message);
//
//    }

}