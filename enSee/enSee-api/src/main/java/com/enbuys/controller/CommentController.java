package com.enbuys.controller;

import com.enbuys.pojo.Comments;
import com.enbuys.service.CommentService;
import com.enbuys.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "留言接口",tags = "留言api")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "保存留言",notes = "保存留言接口")
    @PostMapping("/save")
    public JsonResult saveComment(@RequestBody Comments comments){
        commentService.saveComment(comments);
        return JsonResult.ok();
    }

    @ApiOperation(value = "分页查询留言",notes = "分页查询留言接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "视频id",name = "videoId",required = true,
                    paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "第几页",name = "page",required = false,
                    paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(value = "每页数量",name = "size",required = false,
                    paramType = "query",dataType = "Integer"),
    })
    @PostMapping("/query")
    public JsonResult queryComments(String videoId,Integer page,Integer size){
        if(StringUtils.isBlank(videoId)){
            return JsonResult.errorMsg("");
        }
        if(page == null){
            page = 1;
        }
        if(size == null){
            size = 5;
        }
        return JsonResult.ok(commentService.queryComments(videoId,page,size));
    }
}
