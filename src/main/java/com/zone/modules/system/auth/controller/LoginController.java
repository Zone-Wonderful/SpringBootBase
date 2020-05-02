package com.zone.modules.system.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.zone.base.CommonConstant;
import com.zone.base.Result;
import com.zone.base.util.PasswordUtil;
import com.zone.base.util.RedisUtil;
import com.zone.config.shiro.JwtUtil;
import com.zone.modules.system.auth.entity.LoginModel;
import com.zone.modules.system.user.entity.User;
import com.zone.modules.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: ZoneWonderful
 * @date: 2020/5/1 21:42
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/login")
    public Result<JSONObject> login(LoginModel loginModel) {
        Result<JSONObject> result = new Result<JSONObject>();
        /**
         * 校验验证码
         */


        //1. 校验用户是否有效
        User user = userService.getUserByUserName(loginModel.getUsername());
        result =  userService.checkUserIsEffective(user);
        if (!result.isSuccess()) {
            return result;
        }
        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(loginModel.getUsername(), loginModel.getPassword(), user.getSalt());
        String syspassword = user.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }
        //用户登录信息
        userInfo(user, result);
        return result;
    }
    /**
     * 用户信息
     *
     * @param user
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(User user, Result<JSONObject> result) {
        String syspassword = user.getPassword();
        String username = user.getUsername();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);

        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        obj.put("token", token);
        obj.put("userInfo", user);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

}
