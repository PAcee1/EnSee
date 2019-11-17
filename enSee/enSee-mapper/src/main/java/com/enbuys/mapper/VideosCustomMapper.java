package com.enbuys.mapper;

import com.enbuys.pojo.vo.VideosVO;
import com.enbuys.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosCustomMapper extends MyMapper<VideosVO> {

    /**
     * 查询所有视频接口
     * @param videoDesc
     * @return
     */
    List<VideosVO> queryAllVideosVO(@Param("videoDesc") String videoDesc,
                                    @Param("userId") String userId,
                                    @Param("likeType") String likeType);

    /**
     * 添加视频喜欢数量
     * @param videoId
     */
    void addVideoLikeCounts(String videoId);

    /**
     * 减少视频喜欢数量
     * @param videoId
     */
    void reduceVideoLikeCounts(String videoId);

}