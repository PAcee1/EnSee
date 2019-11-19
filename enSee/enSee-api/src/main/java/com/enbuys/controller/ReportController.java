package com.enbuys.controller;

import com.enbuys.pojo.UsersReport;
import com.enbuys.service.ReportService;
import com.enbuys.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "举报",tags = "举报api")
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "举报视频",notes = "举报视频接口")
    @PostMapping("/video")
    public JsonResult reportVideo(@RequestBody UsersReport usersReport){
        reportService.reportVideo(usersReport);
        return JsonResult.ok();
    }
}
