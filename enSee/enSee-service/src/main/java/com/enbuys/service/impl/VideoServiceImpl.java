package com.enbuys.service.impl;

import com.enbuys.mapper.*;
import com.enbuys.pojo.SearchRecords;
import com.enbuys.pojo.UsersLikeVideos;
import com.enbuys.pojo.Videos;
import com.enbuys.pojo.vo.VideosVO;
import com.enbuys.service.VideoService;
import com.enbuys.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;
import java.util.UUID;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private VideosCustomMapper videosCustomMapper;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String save(Videos videos) {
        String videoId = Sid.next();
        videos.setId(videoId);
        videosMapper.insertSelective(videos);
        return videoId;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(String videoId, String coverPath) {
        Videos videos  = new Videos();
        videos.setId(videoId);
        videos.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(videos);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult queryAllVideosVO(String videoDesc,Integer isSaveRecord,
                                        String userId,String likeType,
                                        Integer page, Integer size) {
        // 如果isSaveRecord为1，需要保存到搜索词汇中
        if(isSaveRecord == 1){
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setId(UUID.randomUUID().toString());
            searchRecords.setContent(videoDesc);
            searchRecordsMapper.insert(searchRecords);
        }

        PageHelper.startPage(page,size);
        List<VideosVO> videosVOs = videosCustomMapper.queryAllVideosVO(videoDesc,userId,likeType);
        PageInfo pageInfo = new PageInfo(videosVOs);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page); // 当前页数
        pagedResult.setTotal(pageInfo.getPages()); // 总页数
        pagedResult.setRecords(pageInfo.getTotal()); // 总记录数
        pagedResult.setRows(videosVOs);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> queryHotRecords() {
        return searchRecordsMapper.queryHotRecords();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId,String videoCreateId) {
        // 添加中间表
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        String id = Sid.next();
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setId(id);
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideosMapper.insert(usersLikeVideos);

        // 用户表添加
        usersMapper.addUserLikeCounts(videoCreateId);

        // video表添加
        videosCustomMapper.addVideoLikeCounts(videoId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId,String videoCreateId) {
        // 删除中间表
        Example example = new Example(UsersLikeVideos.class);
        Criteria criteria =  example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        usersLikeVideosMapper.deleteByExample(example);

        // 用户表减少
        usersMapper.reduceUserLikeCounts(videoCreateId);

        // video表减少
        videosCustomMapper.reduceVideoLikeCounts(videoId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteVideo(String videoId) {
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setStatus(3);
        videosMapper.updateByPrimaryKeySelective(videos);
    }
}
