package com.enbuys.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello(){
        return "enSee hello!";
    }

    @RequestMapping("hello1")
    public String hello1(){
        return "enSee hello1!";
    }
}
