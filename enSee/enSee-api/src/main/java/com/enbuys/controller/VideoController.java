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

    /**
     * 查询全部视频列表
     * @param desc 搜索时使用，查询描述
     * @param isSaveRecord 用来判断是否保存到搜索词汇
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/queryAll")
    public JsonResult queryAll(String videoDesc,Integer isSaveRecord,
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
        return JsonResult.ok(videoService.queryAllVideosVO(videoDesc,isSaveRecord,page,size));
    }

    /**
     * 搜索热点词
     * @return
     */
    @PostMapping("/queryHotList")
    public JsonResult queryHotRecords(){
        return JsonResult.ok(videoService.queryHotRecords());
    }


}
