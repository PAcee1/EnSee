package com.enbuys.controller;

import com.enbuys.pojo.Bgm;
import com.enbuys.service.BgmService;
import com.enbuys.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
