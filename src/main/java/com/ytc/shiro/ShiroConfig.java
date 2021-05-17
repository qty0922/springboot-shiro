/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ShiroConfig
 * Author:   15065
 * Date:     2020/9/4 21:17
 * Description:
 * History:
 * qty               <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.ytc.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 15065
 * @create 2020/9/4
 * @since 1.0.0
 */
@Configuration
public class ShiroConfig {

    /**
     * 创建shiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //修改跳转登录的页面
        shiroFilterFactoryBean.setLoginUrl("/user/toLogin");
        //设置未授权提示的页面，即授权失败跳转的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/noAuth");

        //自定义拦截器
        Map<String, Filter> filtersMap = new HashMap<String, Filter>(1);
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickout", kickoutSessionControlFilter());

        //自定义拦截器
//        filtersMap.put("/**", "myFilter");
        shiroFilterFactoryBean.setFilters(filtersMap);


        //设置shiro内置过滤器
        /**
         * shiro内置过滤器，可以实现权限相关的拦截
         * 常用的过滤器：
         *  anon；无需认证（登录）可以访问
         *  authc：必须认证才可以访问
         *  user：如果使用了rememberMe的功能可以直接访问
         *  perms：该资源必须授权资源权限才可以访问
         *  role：该资源必须得到角色权限才可以访问
         */

        /*
         * rest： 比如/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user：method] ,其中method为post，get，delete等。
         * port： 比如/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal：//serverName：8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString是你访问的url里的？后面的参数。
         * perms：比如/admins/user/**=perms[user：add：*],perms参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，比如/admins/user/**=perms["user：add：*,user：modify：*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法。
         * roles：比如/admins/user/**=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，比如/admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。//要实现or的效果看http://zgzty.blog.163.com/blog/static/83831226201302983358670/
         * anon： 比如/admins/**=anon 没有参数，表示可以匿名使用。
         * authc：比如/admins/user/**=authc表示需要认证才能使用，没有参数
         * authcBasic：比如/admins/user/**=authcBasic没有参数表示httpBasic认证
         * ssl：  比如/admins/user/**=ssl没有参数，表示安全的url请求，协议为https
         * user： 比如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查
         */
        Map<String,String> filterMap = new LinkedHashMap<String, String>();
        /*filterMap.put("/user/add","authc");
        filterMap.put("/user/update","authc");*/

        filterMap.put("/user/testThymeleaf","anon");
        filterMap.put("/user/login","anon");
        filterMap.put("/user/getPerms","anon");
        filterMap.put("/user/getUsers","anon");

        //授权过滤器
        //注意：当前授权拦截后，shiro会自动跳转到未授权页面
        filterMap.put("/user/add","perms[user:add]");
        filterMap.put("/user/update","perms[user:update]");
        filterMap.put("/user/*","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //shiroFilterFactoryBean.setFilters(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     * SecurityManager: 安全管理器，注入有Realm、SessionManager、RememberMeManager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(userRealm);
        //关闭session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     *创建Realm   ---自定义Realm类，编写逻辑方法
     */
    @Bean(name = "userRealm")
    public UserRealm getRealm(){
        return new UserRealm();
    }

    /**
     * 配置ShiroDialect，用于thymelea    f 和 shiro 标签配合使用
     */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

    /**
     * 限制同一账号登录同时登录人数控制
     *
     * @return
     */
    @Bean
    public KickoutRedisFilter kickoutSessionControlFilter() {
        KickoutRedisFilter kickoutRedisFilter = new KickoutRedisFilter();
//        kickoutRedisFilter.setCacheManager(cacheManager());
//        kickoutSessionControlFilter.setSessionManager(sessionManager());
        kickoutRedisFilter.setKickoutAfter(false);
//        kickoutSessionControlFilter.setMaxSession(1);
        kickoutRedisFilter.setMaxSession(1);
        kickoutRedisFilter.setKickoutUrl("/404");
        System.out.println("限制用户同时登录");
        return kickoutRedisFilter;
    }

}
