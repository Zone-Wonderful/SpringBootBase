package com.zone.modules.system.auth.service;

import com.zone.modules.system.auth.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author ZoneWonderful
 * @since 2020-05-01
 */
public interface RoleService extends IService<Role> {
    /**
     * 通过 用户名获取角色编码
     * @param userName
     * @return
     */
    Set<String> getRoleCodeByUserName(@Param("userName") String userName);
}
