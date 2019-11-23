package com.enbuys.controller;

import com.enbuys.enums.VideoStatusEnum;
import com.enbuys.pojo.Bgm;
import com.enbuys.pojo.Videos;
import com.enbuys.service.BgmService;
import com.enbuys.service.VideoService;
import com.enbuys.utils.FetchVideoCover;
import com.enbuys.utils.FileUpload;
import com.enbuys.utils.JsonResult;
import com.enbuys.utils.MergeVideoMp3;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/video")
@Api(value = "视频相关接口",tags = "视频相关Api")
public class VideoController extends BasicController {

    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;


    @ApiOperation(value="上传视频", notes="上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="bgmId", value="背景音乐id", required=false,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoSeconds", value="背景音乐播放长度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoWidth", value="视频宽度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoHeight", value="视频高度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="desc", value="视频描述", required=false,
                    dataType="String", paramType="form")
    })
    @PostMapping(value="/upload", headers="content-type=multipart/form-data")
    public JsonResult upload(String userId,
                                  String bgmId, double videoSeconds,
                                  int videoWidth, int videoHeight,
                                  String desc,
                                  @ApiParam(value="短视频", required=true)
                                          MultipartFile file) throws Exception {

        if (StringUtils.isEmpty(userId)) {
            return JsonResult.errorMsg("用户id不能为空...");
        }

        // 文件保存的命名空间
        String fileSpace = FILE_SPACE;
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";

        // 上传视频文件,返回最终路径
        Map<String, Object> map = FileUpload.upload(file, fileSpace, uploadPathDB);
        if(map == null){
            return JsonResult.errorMsg("上传失败");
        }
        //最终视频文件的绝对路径
        String fileFinalPath = (String) map.get("fileFinalPath");
        uploadPathDB = (String) map.get("uploadPathDB");

        // 如果选择了bgm 需要合并音视频
        String mergeVideoDBPath = "";
        if (StringUtils.isNotBlank(bgmId)){
            // 数据库查找bgm路径
            Bgm bgm = bgmService.findBgmById(bgmId);
            String BgmFinalPath = fileSpace + bgm.getPath(); // bgm的最终路径

            String mergeVideoName = UUID.randomUUID().toString() + ".mp4"; // 转换后视频名称
            mergeVideoDBPath = uploadPathDB + mergeVideoName; // 转换后数据库存放路径
            String mergeVideoFinalPath = fileSpace + "/" + mergeVideoDBPath;// 转换后绝对路径

            MergeVideoMp3 mergeVideoMp3 = new MergeVideoMp3(FFMPEG_PATH);
            mergeVideoMp3.convertor(fileFinalPath,BgmFinalPath,videoSeconds,mergeVideoFinalPath);
        }

        // 需要对视频封面进行保存，采用截取视频第一秒的一帧
        FetchVideoCover fetchVideoCover = new FetchVideoCover(FFMPEG_PATH);
        // 截图数据库保存路径
        String coverPathDB = "/" + userId + "/video/" + UUID.randomUUID().toString() + ".jpg";
        fetchVideoCover.getCover(fileFinalPath,fileSpace + coverPathDB);

        // 保存Video信息到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setVideoDesc(desc);
        video.setVideoHeight(videoHeight);
        video.setVideoSeconds((float) videoSeconds);
        video.setVideoWidth(videoWidth);
        video.setVideoPath(mergeVideoDBPath==""?uploadPathDB:mergeVideoDBPath);
        video.setUserId(userId);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());
        video.setCoverPath(coverPathDB);
        String videoId = videoService.save(video);

        return JsonResult.ok(videoId);
    }

    @ApiOperation(value = "查询所有视频",notes = "查询所有视频接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "查询关键词",name = "videoDesc",required = true,
                    paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "是否保存",name = "isSaveRecord",required = false,
                    paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(value = "第几页",name = "page",required = false,
                    paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(value = "每页数量",name = "size",required = false,
                    paramType = "query",dataType = "Integer"),
    })
    @PostMapping("/queryAll")
    public JsonResult queryAll(String videoDesc,Integer isSaveRecord,
                               String userId,String likeType,
                               Integer page,Integer size){
        // 如果为null，默认不保存
        if(isSaveRecord == null) {
            isSaveRecord = 0;
        }
        if(page == null){
            page = 1;
        }
        if(size == null){
            size = 5;
        }
        return JsonResult.ok(videoService.queryAllVideosVO(videoDesc,isSaveRecord,userId,likeType,page,size));
    }

    @ApiOperation(value = "查询热搜词",notes = "查询热搜词接口")
    @PostMapping("/queryHotList")
    public JsonResult queryHotRecords(){
        return JsonResult.ok(videoService.queryHotRecords());
    }

    @ApiOperation(value = "用户点赞",notes = "用户点赞接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户Id",name = "userId",required = true,
                    paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "视频Id",name = "videoId",required = true,
                    paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "视频创建者Id",name = "videoCreateId",required = true,
                    paramType = "query",dataType = "String")
    })
    @PostMapping("/userLikeVideo")
    public JsonResult userLikeVideo(String userId,String videoId,String videoCreateId){
        videoService.userLikeVideo(userId,videoId,videoCreateId);
        return JsonResult.ok();
    }

    @ApiOperation(value = "用户取消点赞",notes = "用户取消点赞接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户Id",name = "userId",required = true,
                    paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "视频Id",name = "videoId",required = true,
                    paramType = "query",dataType = "String"),
            @ApiImplicitParam(value = "视频创建者Id",name = "videoCreateId",required = true,
                    paramType = "query",dataType = "String")
    })
    @PostMapping("/userUnLikeVideo")
    public JsonResult userUnLikeVideo(String userId,String videoId,String videoCreateId){
        videoService.userUnLikeVideo(userId,videoId,videoCreateId);
        return JsonResult.ok();
    }

    @ApiOperation(value = "删除视频",notes = "删除视频接口")
    @ApiImplicitParam(value = "视频Id",name = "videoId",required = true,
                    paramType = "query",dataType = "String")
    @PostMapping("delete")
    public JsonResult deleteVideo(String videoId){
        if(StringUtils.isBlank(videoId) || StringUtils.isBlank(videoId)){
            return JsonResult.errorMsg("");
        }
        videoService.deleteVideo(videoId);
        return JsonResult.ok();
    }

}
