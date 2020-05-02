package com.zone.modules.system.user.mapper;

import com.zone.modules.system.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ZoneWonderful
 * @since 2020-05-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
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
    User getUserByUserName(@Param("userName") String userName);
}
