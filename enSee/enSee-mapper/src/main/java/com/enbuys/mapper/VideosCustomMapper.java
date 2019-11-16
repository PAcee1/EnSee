package com.enbuys.mapper;

import com.enbuys.pojo.vo.VideosVO;
import com.enbuys.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosCustomMapper extends MyMapper<VideosVO> {

    List<VideosVO> queryAllVideosVO(@Param("videoDesc") String videoDesc);

}