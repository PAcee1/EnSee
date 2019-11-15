package com.enbuys.service.impl;

import com.enbuys.mapper.BgmMapper;
import com.enbuys.mapper.VideosMapper;
import com.enbuys.pojo.Bgm;
import com.enbuys.pojo.Videos;
import com.enbuys.service.BgmService;
import com.enbuys.service.VideoService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private Sid sid;

    @Override
    public String save(Videos videos) {
        String videoId = Sid.next();
        videos.setId(videoId);
        videosMapper.insertSelective(videos);
        return videoId;
    }

    @Override
    public void updateVideo(String videoId, String coverPath) {
        Videos videos  = new Videos();
        videos.setId(videoId);
        videos.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(videos);
    }
}
