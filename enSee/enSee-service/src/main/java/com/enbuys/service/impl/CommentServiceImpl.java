package com.enbuys.service.impl;

import com.enbuys.mapper.CommentsMapper;
import com.enbuys.mapper.UsersReportMapper;
import com.enbuys.pojo.Comments;
import com.enbuys.pojo.UsersReport;
import com.enbuys.pojo.vo.CommentsVO;
import com.enbuys.service.CommentService;
import com.enbuys.service.ReportService;
import com.enbuys.utils.PagedResult;
import com.enbuys.utils.TimeAgoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comments comments) {
        comments.setId(sid.nextShort());
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryComments(String videoId, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<CommentsVO> commentsVOS = commentsMapper.queryComments(videoId);

        for(CommentsVO commentsVO : commentsVOS){
            Date createTime = commentsVO.getCreateTime();
            String format = TimeAgoUtils.format(createTime);
            commentsVO.setTimeAgoStr(format);
        }

        PageInfo<CommentsVO> pageInfo = new PageInfo(commentsVOS);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRows(commentsVOS);
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setTotal(pageInfo.getPages());
        return pagedResult;
    }
}
