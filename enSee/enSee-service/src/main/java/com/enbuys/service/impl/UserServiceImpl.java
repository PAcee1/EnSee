package com.enbuys.service.impl;

import com.enbuys.mapper.UsersFansMapper;
import com.enbuys.mapper.UsersLikeVideosMapper;
import com.enbuys.mapper.UsersMapper;
import com.enbuys.pojo.Users;
import com.enbuys.pojo.UsersFans;
import com.enbuys.pojo.UsersLikeVideos;
import com.enbuys.service.UserService;
import com.enbuys.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

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
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserById(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        return users;
    }

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
        Criteria criteria = example.createCriteria();
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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserById(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);
    }

    @Override
    public Boolean isLiked(String userId, String videoId) {
        Example example = new Example(UsersLikeVideos.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);
        if( list == null || list.size() < 1 ){
            return false;
        }
        return true;
    }

    @Override
    public void addFollowFans(String userId, String fansId) {
        // 处理中间表
        UsersFans usersFans = new UsersFans();
        usersFans.setId(Sid.next());
        usersFans.setUserId(userId);
        usersFans.setFanId(fansId);
        usersFansMapper.insert(usersFans);

        // 添加粉丝数
        usersMapper.addFansCounts(userId);
        // 添加关注数
        usersMapper.addFollowCounts(fansId);
    }

    @Override
    public void reduceFollowFans(String userId, String fansId) {
        // 处理中间表
        Example example = new Example(UsersFans.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fansId);
        usersFansMapper.deleteByExample(example);

        // 减少粉丝数
        usersMapper.reduceFansCounts(userId);
        // 减少关注数
        usersMapper.reduceFollowCounts(fansId);
    }

    @Override
    public Boolean isFollow(String userId, String fansId) {
        Example example = new Example(UsersFans.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fansId);
        List<UsersFans> list = usersFansMapper.selectByExample(example);
        if( list == null || list.size() < 1 ){
            return false;
        }
        return true;
    }

    @Override
    public void updateName(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);
    }

    @Override
    public List<Users> findFollowUserList(String userId) {
        return usersMapper.findFollowUserList(userId);
    }

    @Override
    public List<Users> findFansUserList(String userId) {
        return usersMapper.findFansUserList(userId);
    }
}
