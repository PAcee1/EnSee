package com.enbuys.service;

import com.enbuys.pojo.Bgm;
import com.enbuys.pojo.Users;

import java.util.List;

/**
 * @Author: Pace
 * @Data: 2019/11/13 19:25
 * @Version: v1.0
 */
public interface BgmService {

    List<Bgm> queryAll();

    Bgm findBgmById(String id);
}
