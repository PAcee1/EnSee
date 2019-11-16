package com.enbuys.service.impl;

import com.enbuys.mapper.BgmMapper;
import com.enbuys.mapper.SearchRecordsMapper;
import com.enbuys.mapper.VideosCustomMapper;
import com.enbuys.mapper.VideosMapper;
import com.enbuys.pojo.Bgm;
import com.enbuys.pojo.SearchRecords;
import com.enbuys.pojo.Videos;
import com.enbuys.pojo.vo.VideosVO;
import com.enbuys.service.BgmService;
import com.enbuys.service.VideoService;
import com.enbuys.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    public PagedResult queryAllVideosVO(String videoDesc,Integer isSaveRecord,Integer page, Integer size) {
        // 如果isSaveRecord为1，需要保存到搜索词汇中
        if(isSaveRecord == 1){
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setId(UUID.randomUUID().toString());
            searchRecords.setContent(videoDesc);
            searchRecordsMapper.insert(searchRecords);
        }

        PageHelper.startPage(page,size);
        List<VideosVO> videosVOs = videosCustomMapper.queryAllVideosVO(videoDesc);
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
}
