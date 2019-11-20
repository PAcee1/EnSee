package com.enbuys.service;

import com.enbuys.pojo.Comments;
import com.enbuys.utils.PagedResult;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
public interface CommentService {

    void saveComment(Comments comments);

    PagedResult queryComments(String videoId, Integer page, Integer size);
}
