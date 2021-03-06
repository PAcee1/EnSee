package com.enbuys.controller;

import com.enbuys.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "user-redis-session";

    public static final String FILE_SPACE = "D:/Projects/EnSee-vedios";

    //public static final String FFMPEG_PATH = "D:\\Major\\ffmpeg\\bin\\ffmpeg.exe";
    public static final String FFMPEG_PATH = "E:\\Major\\ffmpeg\\bin\\ffmpeg.exe";
}
