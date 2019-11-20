package com.enbuys.mapper;

import com.enbuys.pojo.Comments;
import com.enbuys.pojo.vo.CommentsVO;
import com.enbuys.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentsMapper extends MyMapper<Comments> {

    List<CommentsVO> queryComments(@Param("videoId") String videoId);
}