package com.zone.modules.system.auth.service.impl;

import com.zone.modules.system.auth.entity.Menu;
import com.zone.modules.system.auth.mapper.MenuMapper;
import com.zone.modules.system.auth.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author ZoneWonderful
 * @since 2020-05-01
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}
