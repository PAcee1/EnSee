package com.enbuys.service;

import com.enbuys.pojo.Users;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
public interface UserService {

    Users queryUserById(String userId);

    Boolean findUserNameIsExist(String username);

    Users saveUser(Users users);

    Users findUserByPassword(Users users);

    void updateUserById(Users users);

    Boolean isLiked(String userId,String videoId);

    void addFollowFans(String userId,String fansId);

    void reduceFollowFans(String userId,String fansId);

    Boolean isFollow(String userId,String fansId);
}
