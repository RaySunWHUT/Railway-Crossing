package com.railwaycrossing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.railwaycrossing.dao.UserDao;
import com.railwaycrossing.entity.User;
import com.railwaycrossing.exception.UpdateException;
import com.railwaycrossing.service.UserService;
import com.railwaycrossing.utils.DateUtils;
import com.railwaycrossing.utils.JSONUtil;
import com.railwaycrossing.utils.JWTUtil;
import com.railwaycrossing.utils.MD5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

import com.railwaycrossing.constants.Constants;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public JSONObject login(JSONObject message) {

        String userAccount = message.getString("userAccount");
        String password = message.getString("password");

        User user = baseMapper.getUserByUserAccount(userAccount);

        String realPassword = user.getPassword();
        String salt = user.getSalt();

        String md5Password = MD5Utils.getMd5Password(password, salt);

        JSONObject msg = null;

        if (!md5Password.equals(realPassword)) {

            msg = JSONUtil.errorJSON("incorrect");

        } else {

            String token = JWTUtil.createToken(userAccount);

            msg = JSONUtil.successJSON(token);

        }

        return msg;

    }

    @Override
    public JSONObject logout(JSONObject message) {

        JSONObject msg = null;

        if (message.get("message").equals("logout")) {

            Subject currentUser = SecurityUtils.getSubject();

            // 注销
            currentUser.logout();

            msg = JSONUtil.successJSON();

        }

        message.remove("message");

        userDao.updateLoginTime(message);

        return msg;

    }


    @Override
    public JSONObject notLogin() {

        return JSONUtil.errorJSON("you haven't login.");

    }

    @Override
    public JSONObject updatePassword(JSONObject message) {

        String userAccount = message.getString("userAccount");

        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("userAccount", userAccount));

        //获取旧密码
        String oldPassword = message.getString("oldPassword");

        // 取出新密码
        String newPassword = message.getString("newPassword");

        // 取出盐值，用于后面的判断
        String salt = user.getSalt();

        // 对输入的原始密码进行加密，再与数据库中的进行匹配
        String md5Password = MD5Utils.getMd5Password(oldPassword, salt);

        // 判断密码是否与数据库中匹配
        if(md5Password.equals(user.getPassword())) {

            // 对新的密码进行加密
            String newMd5Password = MD5Utils.getMd5Password(newPassword, salt);

            user.setPassword(newMd5Password);

            Integer rows = baseMapper.update(user, new QueryWrapper<User>().eq("userAccount", userAccount));

            // 修改失败
            if (rows != 1) {

                return JSONUtil.errorJSON("修改密码时发生未知错误! 修改密码失败! ");

            } else {    // 修改成功！

                return JSONUtil.successJSON();

            }

        } else {    // 原密码输入错误

            return JSONUtil.errorJSON("原密码输入错误！");

        }

    }


    @Override
    public JSONObject unauthorized(String message) {

        return JSONUtil.errorJSON("authority verify failed!");

    }

    @Override
    public JSONObject register(JSONObject object) throws DuplicateKeyException {

        User user = userDao.getUserByUserAccount(object.getString("userAccount"));

        // 获取前端界面输入的密码作为原始密码
        String userAccount = object.getString("userAccount");

        String srcPassword = object.getString("password");

        if (user == null) {

            if (srcPassword == null || userAccount == null) {

                return JSONUtil.errorJSON("用户名或密码不能为空！");

            } else {    // 用户名和密码皆不为空

                // 处理密码加密
                // 1. 根据UUID生成盐值，这里的盐值统一大写
                String salt = UUID.randomUUID().toString().toUpperCase();

                // 3. 用MD5对密码进行加密, 获得新密码
                String md5Password = getMd5Password(srcPassword, salt);

                // 4. 将加密后的密码存放到数据库中
                object.put("password", md5Password);

                // 5. 将盐值存放到数据库中
                object.put("salt", salt);

                object.put("roleId", "1");

                userDao.saveUser(object);

                return JSONUtil.successJSON();

            }

        } else {

            return JSONUtil.errorJSON("注册用户失败! 用户名 \'" + object.getString("userAccount") + "\' 已存在!");

        }

    }


    /**
     * 对原始密码和盐值执行MD5加密
     * @param srcPassword 原始密码
     * @param salt 盐值
     * @return 加密后的密码
     */
    private String getMd5Password(String srcPassword, String salt) {

        String src = salt + srcPassword + salt;

        for (int i = 0; i < 5; i++) {

            src = DigestUtils.md5DigestAsHex(src.getBytes()).toUpperCase();

        }

        return src;

    }


    @Transactional(rollbackFor=UpdateException.class)
    @Override
    public JSONObject updateUser(JSONObject message) throws UpdateException {

        User user = new User();
        String userAccount = message.getString("userAccount");
        if (message.containsKey("lastLoginTime")) {
            user.setLastLoginTime(message.getDate("lastLoginTime"));
        }
        if (message.containsKey("lastLoginLength")) {
            user.setLastLoginLength(message.getInteger("lastLoginLength"));
        }

        int result = baseMapper.update(user, new QueryWrapper<User>().eq("userAccount", userAccount));

        if (result != 1) {
            throw new UpdateException();
        } else {
            return JSONUtil.successJSON();
        }

    }

    @Override
    public JSONObject listOneUser(JSONObject message) {

        JSONObject user = userDao.listOneUser(message.getString("userAccount"));

        if (user != null) {

            Date date = user.getDate("lastLoginTime");
            user.replace("lastLoginTime", DateUtils.format(date));

            return JSONUtil.successJSON(user);

        } else {

            return JSONUtil.errorJSON(Constants.QUERY_FAILED);

        }

    }


//    @Override
//    public JSONObject updateLoginTime(JSONObject lastLoginTime) {
//
//        Integer result = userDao.updateLoginTime(lastLoginTime);
//
//        if (result == 0) {
//
//            return JSONUtil.errorJSON(Constants.QUERY_FAILED);
//
//        } else {
//
//            return JSONUtil.successJSON();
//
//        }
//
//    }

}
