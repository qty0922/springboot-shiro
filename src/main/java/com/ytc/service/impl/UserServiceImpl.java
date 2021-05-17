/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: UserServiceImpl
 * Author:   15065
 * Date:     2020/9/5 11:18
 * Description:
 * History:
 * qty               <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.ytc.service.impl;

import com.ytc.dao.UserDao;
import com.ytc.model.User;
import com.ytc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 15065
 * @create 2020/9/5
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User queryUserByName(String userName) {
        return userDao.queryUserByName(userName);
    }

    @Override
    public User queryUserById(Integer userId) {
        return userDao.queryUserById(userId);
    }
}
