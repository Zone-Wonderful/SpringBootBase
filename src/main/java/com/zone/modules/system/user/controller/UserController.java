package com.zone.modules.system.user.controller;


import com.zone.base.Result;
import com.zone.base.util.PasswordUtil;
import com.zone.base.util.oConvertUtils;
import com.zone.modules.system.user.entity.User;
import com.zone.modules.system.user.mapper.UserMapper;
import com.zone.modules.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ZoneWonderful
 * @since 2020-05-01
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/getUserList")
    public Result<?> getUserList() {
        List<User> userList = userService.getUserList();
        return Result.ok(userList);
    }

    @PostMapping("/add")
    public Result<?> add(User user) {
        String salt = oConvertUtils.randomGen(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(user.getUsername(),user.getPassword(),salt);
        user.setPassword(passwordEncode);
        userService.addUserWithRole(user,null);
        return Result.ok("添加成功");
    }
}

