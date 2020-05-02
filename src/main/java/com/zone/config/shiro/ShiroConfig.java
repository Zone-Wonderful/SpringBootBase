package com.zone.config.shiro;


import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ShiroConfig:shiro 配置类,配置哪些拦截,哪些不拦截,哪些授权等等各种配置都在这里
 * <p>
 * 很多都是老套路,按照这个套路配置就行了
 *
 * @author: ZoneWonderful
 * @date: 2020/5/2
 */

@Configuration
public class ShiroConfig {
    /**
     * 注入安全过滤器
     *
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

//        filterChainDefinitionMap.put("/**", "anon");



        //博客页面获取文章列表请求排除
        filterChainDefinitionMap.put("/article/getArticleList/**", "anon");
        //博客界面 获取文章详情请求排除
        filterChainDefinitionMap.put("/article/queryById/**", "anon");
        //更新浏览量
        filterChainDefinitionMap.put("/article/updateVisit/**", "anon");
        // 获取标签列表
        filterChainDefinitionMap.put("/tag/getTagList/**", "anon");
        // 获取笑话段子
        filterChainDefinitionMap.put("/joke/getJokeOne/**", "anon");
        // 获取图片
        filterChainDefinitionMap.put("/Img/getImg/**", "anon");


        //cas验证登录
        filterChainDefinitionMap.put("/cas/client/validateLogin", "anon");
        // 配置不会被拦截的链接 顺序判断

        filterChainDefinitionMap.put("/auth/login", "anon");//登录接口
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/**/*.svg", "anon");
        filterChainDefinitionMap.put("/**/*.pdf", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");


        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        filterChainDefinitionMap.put("/**", "jwt");

        // 未授权界面返回JSON
//        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
//        shiroFilterFactoryBean.setLoginUrl("/sys/common/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 注入安全管理器
     *
     * @param myRealm
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager(MyRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-
         * StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }
}
