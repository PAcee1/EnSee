package com.enbuys.controller;

import com.enbuys.pojo.Users;
import com.enbuys.service.UserService;
import com.enbuys.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户controller
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户接口",tags = "用户注册登录Api")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @return
     */
    @ApiOperation(value = "用户注册",notes = "用户注册接口")
    @PostMapping("/regist")
    public JsonResult regist(@RequestBody Users users){
        // 判断用户名密码是否为空
        if(StringUtils.isEmpty(users.getUsername()) || StringUtils.isEmpty(users.getPassword())){
            return JsonResult.errorMsg("用户名或密码不能为空");
        }

        // 校验用户名是否存在
        Boolean isExist = userService.findUserNameIsExist(users.getUsername());

        // 保存用户
        if(!isExist) {
            Users user = userService.saveUser(users);
            user.setPassword("");
            return JsonResult.ok(user);
        }else{
            return JsonResult.errorMsg("用户名已存在");
        }
    }

}
