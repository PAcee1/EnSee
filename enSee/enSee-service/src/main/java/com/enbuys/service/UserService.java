package com.enbuys.service;

import com.enbuys.pojo.Users;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
public interface UserService {

    Boolean findUserNameIsExist(String username);

    Users saveUser(Users users);
}
