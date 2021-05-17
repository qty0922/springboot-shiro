/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: UserRealm
 * Author:   15065
 * Date:     2020/9/4 21:21
 * Description:
 * History:
 * qty               <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.ytc.shiro;

import com.ytc.model.User;
import com.ytc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 〈自定义Realm〉<br>
 * 〈〉
 *
 * @author 15065
 * @create 2020/9/4
 * @since 1.0.0
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 执行授权逻辑
     *  授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,
     *  负责在应用程序中决定用户的访问控制的方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("shiro--执行授权逻辑");

        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //添加资源的授权字符串
        //硬编码
        //info.addStringPermission("user:add:*");

        //获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        //到数据库去查询当前登录用户的授权字符串
        User userR = userService.queryUserById(user.getUserId());
        info.addStringPermission(userR.getPerms());

        return info;
    }


    /**
     * 执行认证逻辑
     *  认证回调函数，登录信息和用户验证信息验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("--执行认证逻辑");

        //编写shiro判断逻辑，判断用户名和密码
        //1.判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User userRet = userService.queryUserByName(token.getUsername());

        if(userRet==null){
            //用户名不存在
            return null;//shiro底层会抛出：UnknownAccountException
        }

        //2.判断密码
        //最后的比对需要交给安全管理器,三个参数进行初步的简单认证信息对象的包装,由安全管理器进行包装运行
        String password = userRet.getUserPwd();
        return new SimpleAuthenticationInfo(userRet,password, getName());
    }
}
