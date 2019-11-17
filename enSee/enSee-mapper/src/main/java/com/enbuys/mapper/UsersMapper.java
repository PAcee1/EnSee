package com.enbuys.mapper;

import com.enbuys.pojo.Users;
import com.enbuys.utils.MyMapper;


public interface UsersMapper extends MyMapper<Users> {

    /**
     * 添加用户视频喜欢总数量
     * @param userId
     */
    void addUserLikeCounts(String userId);

    /**
     * 减少用户视频喜欢总数量
     * @param userId
     */
    void reduceUserLikeCounts(String userId);

    /**
     * 增加用户关注数
     * @param userId
     */
    void addFollowCounts(String userId);
    /**
     * 增加用户粉丝数
     * @param userId
     */
    void addFansCounts(String userId);
    /**
     * 减少用户关注数
     * @param userId
     */
    void reduceFollowCounts(String userId);
    /**
     * 减少用户粉丝数
     * @param userId
     */
    void reduceFansCounts(String userId);

}