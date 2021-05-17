package com.ytc.service;

import com.ytc.model.User;

public interface UserService {
    User queryUserByName(String userName);
    User queryUserById(Integer userId);
}
