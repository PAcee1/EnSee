package com.enbuys.service.impl;

import com.enbuys.mapper.UsersMapper;
import com.enbuys.pojo.Users;
import com.enbuys.service.UserService;
import com.enbuys.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Boolean findUserNameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users findUser = usersMapper.selectOne(users);
        return findUser == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users saveUser(Users users) {
        String id = sid.nextShort();
        users.setId(id);
        users.setFansCounts(0);
        users.setNickname(users.getUsername());
        users.setFollowCounts(0);
        users.setReceiveLikeCounts(0);
        try {
            users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        usersMapper.insert(users);
        return users;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users findUserByPassword(Users users) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",users.getUsername());
        try {
            String password = MD5Utils.getMD5Str(users.getPassword());
            criteria.andEqualTo("password",password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Users user = usersMapper.selectOneByExample(example);
        return user;
    }
}
