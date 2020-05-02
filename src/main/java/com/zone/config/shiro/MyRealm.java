package com.zone.config.shiro;

import com.zone.base.CommonConstant;
import com.zone.base.SpringContextUtils;
import com.zone.base.util.RedisUtil;
import com.zone.base.util.oConvertUtils;
import com.zone.modules.system.auth.service.RoleService;
import com.zone.modules.system.user.entity.User;
import com.zone.modules.system.user.mapper.UserMapper;
import com.zone.modules.system.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * MyRealm:自定义一个授权
 *
 * @author: ZoneWonderful
 * @date: 2020/5/2
 */

@Component
@Slf4j
public class MyRealm extends AuthorizingRealm {

    //    UserService userService = SpringContextUtils.getBean(UserService.class);
//    RoleService roleService =  SpringContextUtils.getBean(RoleService.class);
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    @Lazy
    private RedisUtil redisUtil;


    /**
     * 必须重写此方法，不然Shiro会报错
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("===============Shiro权限认证开始============ [ roles、permissions]==========");
        String username = null;
        if (principals != null) {
            User user = (User) principals.getPrimaryPrincipal();
            username = user.getUsername();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 设置用户拥有的角色集合，比如“admin,test”
        Set<String> roleSet = roleService.getRoleCodeByUserName(username);
//        info.setRoles(roleSet);
//
//        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
//        Set<String> permissionSet = userService.getUserPermissionsSet(username);
//        info.addStringPermissions(permissionSet);
        log.info("===============Shiro权限认证成功==============");
        return info;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     *
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
//        if (token == null) {
//            log.info("————————身份认证失败——————————IP地址:  "+ SpringContextUtils.getHttpServletRequest().getHeader("x-forwarded-for"));
//            throw new AuthenticationException("token为空!");
//        }
        // 校验token有效性
        User user = this.checkUserTokenIsEffect(token);
        return new SimpleAuthenticationInfo(user, token, getName());
    }
    /**
     * 校验token的有效性
     *
     * @param token
     */
    public User checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token非法无效!");
        }

        // 查询用户信息
        log.info("———校验token是否有效————checkUserTokenIsEffect——————— "+ token);
        User user = userService.getUserByName(username);
        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }
        // 判断用户状态
        if (user.getStatus() != 1) {
            throw new AuthenticationException("账号已被锁定,请联系管理员!");
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, user.getPassword())) {
            throw new AuthenticationException("Token失效，请重新登录!");
        }

        return user;
    }
    /**
     * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 4、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 注意： 前端请求Header中设置Authorization保持不变，校验有效性以缓存中的token为准。
     *       用户过期时间 = Jwt有效时间 * 2。
     *
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String passWord) {
        String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, passWord)) {
                String newAuthorization = JwtUtil.sign(userName, passWord);
                // 设置超时时间
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME *2 / 1000);
                log.info("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— "+ token);
            }
            //update-begin--Author:scott  Date:20191005  for：解决每次请求，都重写redis中 token缓存问题
//			else {
//				// 设置超时时间
//				redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
//				redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
//			}
            //update-end--Author:scott  Date:20191005   for：解决每次请求，都重写redis中 token缓存问题
            return true;
        }
        return false;
    }
}
