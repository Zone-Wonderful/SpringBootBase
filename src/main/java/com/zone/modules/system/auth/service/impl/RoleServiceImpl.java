package com.zone.modules.system.auth.service.impl;

import com.zone.modules.system.auth.entity.Role;
import com.zone.modules.system.auth.mapper.RoleMapper;
import com.zone.modules.system.auth.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author ZoneWonderful
 * @since 2020-05-01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Set<String> getRoleCodeByUserName(String userName) {
        return new HashSet<>(roleMapper.getRoleCodeByUserName(userName));
    }
}
