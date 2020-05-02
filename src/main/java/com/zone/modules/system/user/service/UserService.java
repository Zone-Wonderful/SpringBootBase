package com.zone.modules.system.user.service;

import com.zone.base.Result;
import com.zone.modules.system.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ZoneWonderful
 * @since 2020-05-01
 */
public interface UserService extends IService<User> {
    /**
     * 获取用户列表
     * @return
     */
    List<User> getUserList();

    /**
     * 根据用户名获取用户
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);


    /**
     * 添加用户和用户角色关系
     * @param user
     * @param roles
     */
    public void addUserWithRole(User user,String roles);

    /**
     * 校验用户是否有效
     * @param user
     * @return
     */
    public Result checkUserIsEffective(User user);

    /**
     *
     * @param username
     * @return
     */
    public User getUserByName(String username);
}
