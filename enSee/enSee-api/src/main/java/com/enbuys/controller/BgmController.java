package com.enbuys.controller;

import com.enbuys.pojo.Bgm;
import com.enbuys.pojo.Users;
import com.enbuys.pojo.vo.UsersVo;
import com.enbuys.service.BgmService;
import com.enbuys.service.UserService;
import com.enbuys.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * 用户controller
 */
@RestController
@RequestMapping("/bgm")
@Api(value = "Bgm接口",tags = "背景音乐Api")
public class BgmController extends BasicController {

    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "获取所有bgm",notes = "获取所有bgm接口")
    @PostMapping("/list")
    public JsonResult list(){
        List<Bgm> bgms = bgmService.queryAll();
        return JsonResult.ok(bgms);
    }

}
