/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: UserController
 * Author:   15065
 * Date:     2020/9/4 20:48
 * Description:
 * History:
 * qty               <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.ytc.controller;

import com.ytc.model.PermsMap;
import com.ytc.model.User;
import com.ytc.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 15065
 * @create 2020/9/4
 * @since 1.0.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PermsMap permsMap;

    @Autowired
    private User user;

    /**
     * 测试方法
     */
    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        System.out.println("usercontroller.hello()");
        return "ok";
    }

    @RequestMapping("add")
    public String add(){
        return "user/add";
    }

    @RequestMapping("update")
    public String update(){
        return "user/update";
    }

    @RequestMapping("toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 测试thymeleaf
     */
    @RequestMapping("testThymeleaf")
    public String testThymeleaf(Model model){

        model.addAttribute("name","程序员");
        return "test";
    }

    /**
     * 登录的逻辑处理
     */
    @RequestMapping("login")
    public String login(User user,Model model){
        System.out.println(user);
        /**
         * 使用shiro编写认证操作
         */
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),user.getUserPwd());
        //3.执行登录方法
        try {
            //登录成功
            subject.login(token);
            return "test";
        }catch (UnknownAccountException e){
            //登录名失败：用户名不存在
            model.addAttribute("msg","用户名不存在");
            return "404";
        }catch (IncorrectCredentialsException e) {
            //登录名失败：密码错误
            model.addAttribute("msg", "密码错误");
            return "404";
        }
    }

    @RequestMapping("noAuth")
    public String noAuth(){
        return "noAuth";
    }

    @RequestMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return "success";
    }

    @RequestMapping("getPerms")
    @ResponseBody
    public String getPerms(){
        //权限过滤链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        List<Map<String, String>> perms = permsMap.getPerms();
        perms.forEach(perm->filterChainDefinitionMap.put(perm.get("url"),perm.get("permission")));
        System.out.println(perms.toString());
        System.out.println("permsMap"+filterChainDefinitionMap.toString());
        return perms.toString();
    }

    @RequestMapping("getUsers")
    @ResponseBody
    public String getUsers(){
        String userId = user.getUserId().toString();
        return userId;
    }
}
