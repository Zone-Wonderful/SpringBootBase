package com.zone.modules.system.user.service.impl;

import com.zone.base.CommonConstant;
import com.zone.base.Result;
import com.zone.base.util.oConvertUtils;
import com.zone.modules.system.user.entity.User;
import com.zone.modules.system.user.mapper.UserMapper;
import com.zone.modules.system.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ZoneWonderful
 * @since 2020-05-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public User getUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    @Override
    @Transactional
    public void addUserWithRole(User user, String roles) {
        this.save(user);
    }

    /**
     * 校验用户身份有效性
     * @param user
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective(User user) {
        Result<?> result = new Result<Object>();
        if (user == null) {
            result.error500("该用户不存在，请注册");
            return result;
        }
        if (CommonConstant.USER_FREEZE.equals(user.getStatus() ) ){
            result.error500("该用户已冻结");
            return result;
        }
        return result;
    }

    /**
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByName(String username) {
        if(oConvertUtils.isEmpty(username)) {
            return null;
        }
        User user = userMapper.getUserByUserName(username);
        if(user==null) {
            return null;
        }
        return user;
    }
}
