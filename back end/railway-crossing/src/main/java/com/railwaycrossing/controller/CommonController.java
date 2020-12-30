package com.railwaycrossing.controller;

import com.alibaba.fastjson.JSONObject;
import com.railwaycrossing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@Slf4j
public class CommonController {

    @Resource
    private UserService userService;

    /**
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(@RequestBody JSONObject jsonObject) {

        return userService.login(jsonObject);

    }

    /**
     * 必须为GET方法
     *
     * @return
     */
    @RequestMapping(value = {"/notLogin"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject notLogin() {

        return userService.notLogin();

    }

    @RequestMapping(path = "/unauthorized/{message}", method = RequestMethod.GET)
    public JSONObject unauthorized(@PathVariable String message) {

        return userService.unauthorized(message);

    }

}
