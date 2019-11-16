package com.enbuys.service.impl;

import com.enbuys.mapper.BgmMapper;
import com.enbuys.mapper.UsersMapper;
import com.enbuys.pojo.Bgm;
import com.enbuys.pojo.Users;
import com.enbuys.service.BgmService;
import com.enbuys.service.UserService;
import com.enbuys.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
@Service("bgmService")
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryAll() {
        return bgmMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm findBgmById(String id) {
        return bgmMapper.selectByPrimaryKey(id);
    }
}
