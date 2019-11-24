package com.enbuys.service.impl;

import com.enbuys.mapper.BgmMapper;
import com.enbuys.mapper.UsersReportMapper;
import com.enbuys.pojo.Bgm;
import com.enbuys.pojo.UsersReport;
import com.enbuys.service.BgmService;
import com.enbuys.service.ReportService;
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
@Service("reportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UsersReportMapper usersReportMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reportVideo(UsersReport report) {
        // 插入id与创建时间
        report.setId(sid.nextShort());
        report.setCreateDate(new Date());
        usersReportMapper.insert(report);
    }
}
