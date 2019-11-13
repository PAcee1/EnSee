package com.enbuys;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = "com.enbuys.mapper")
@ComponentScan(basePackages = {"org.n3r.idworker","com.enbuys"})
public class MainAppliction {

    public static void main(String[] args) {
        SpringApplication.run(MainAppliction.class,args);
    }
}
