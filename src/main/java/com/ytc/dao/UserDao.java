package com.ytc.dao;

import com.ytc.model.User;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserDao {
    User queryUserByName(@RequestParam("userName") String userName);

    User queryUserById(@RequestParam("userId") Integer userId);
}
