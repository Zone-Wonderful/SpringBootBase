package com.zone.modules.system.auth.mapper;

import com.zone.modules.system.auth.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author ZoneWonderful
 * @since 2020-05-01
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 通过 用户名获取角色编码
     * @param userName
     * @return
     */
    List<String> getRoleCodeByUserName( @Param("userName") String userName);
}
