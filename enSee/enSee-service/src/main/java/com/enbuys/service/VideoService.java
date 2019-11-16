package com.enbuys.service;

import com.enbuys.pojo.Bgm;
import com.enbuys.pojo.Videos;
import com.enbuys.utils.PagedResult;

import java.util.List;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
public interface VideoService {

    String save(Videos videos);

    void updateVideo(String videoId, String coverPath);

    PagedResult queryAllVideosVO(String videoDesc,Integer isSaveRecord,Integer page,Integer size);

    List<String> queryHotRecords();
}
