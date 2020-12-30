package com.railwaycrossing;

import com.alibaba.fastjson.JSONObject;
import com.railwaycrossing.controller.BalusterController;
import com.railwaycrossing.controller.InstructionController;
import com.railwaycrossing.dao.UserDao;
import com.railwaycrossing.entity.Instruction;
import com.railwaycrossing.exception.UpdateException;
import com.railwaycrossing.service.*;
import com.railwaycrossing.utils.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
class RailwayCrossingApplicationTests {

    @Resource
    UserDao userDao;

    @Resource
    BalusterService balusterService;

    @Resource
    BalusterController balusterController;

    @Resource
    CrossingService crossingService;

    @Resource
    InstructionService instructionService;

    @Resource
    InstructionController instructionController;

    @Resource
    LocomotiveService locomotiveService;

    @Resource
    RailwayBlinkerService railwayBlinkerService;

    @Resource
    RoadBlinkerService roadBlinkerService;

    @Resource
    UserService userService;

    @Test
    void test3() {

        JSONObject aa = new JSONObject();

        aa.put("userAccount", "275518");

        JSONObject bb = userService.listOneUser(aa);

        System.out.println(bb.get("lastLoginTime"));



    }


    @Test
    void test1() {

        JSONObject aa = userDao.listOneUser("275518");

        Object cc = aa.getString("lastLoginTime");

        System.out.println(cc);

//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//
//        id = sdf.format(date);

    }

    @Test
    void test() {

        JSONObject object = new JSONObject();

        String srcPassword = "666";

        // 处理密码加密
        // 1. 根据UUID生成盐值，这里的盐值统一大写
        String salt = UUID.randomUUID().toString().toUpperCase();

        // 3. 用MD5对密码进行加密, 获得新密码
        String md5Password = MD5Utils.getMd5Password(srcPassword, salt);

        // 获取前端界面输入的密码作为原始密码
        object.put("userAccount", "275518");

        object.put("userName", "Sun");

        // 4. 将加密后的密码存放到数据库中
        object.put("password", md5Password);

        // 5. 将盐值存放到数据库中
        object.put("salt", salt);

        object.put("roleId", "1");

        userDao.saveUser(object);

    }

    @Test
    void testBaluster() throws UpdateException {
        JSONObject message = new JSONObject();
        //message.put("page",2);
        //message.put("limit",5);
        balusterService.updateStatusByCrossingMode(2,false);
        //System.out.println(jsonObject);
    }

    @Test
    void testInstruct() throws Exception {
        JSONObject message = new JSONObject();
        message.put("instructionId",1);
        //message.put("crossingId",1);
        //message.put("locomotiveId",1);
        message.put("instructionContent", "允许通过");
        //message.put("instructionTime",new Date());
        //message.put("passTime",10);
        //message.put("valid",1);
        JSONObject jsonObject = instructionController.listByCondition(message);
        System.out.println(jsonObject);
    }

    @Test
    void testBaluster2() {
        JSONObject message = new JSONObject();
        message.put("crossingId", 1);
        balusterController.queryPageByCondition(message);
    }

}
