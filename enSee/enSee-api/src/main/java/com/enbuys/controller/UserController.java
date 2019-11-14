package com.enbuys.controller;

import com.enbuys.pojo.Users;
import com.enbuys.pojo.vo.UsersVo;
import com.enbuys.service.UserService;
import com.enbuys.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 用户controller
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户接口",tags = "用户Api")
public class UserController extends BasicController {

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
        if(isExist) {
            return JsonResult.errorMsg("用户名已存在");
        }
        Users user = userService.saveUser(users);
        user.setPassword("");

        UsersVo userVo = setUserRedisSession(user);

        return JsonResult.ok(userVo);
    }

    @ApiOperation(value = "用户登录",notes = "用户登录接口")
    @PostMapping("/login")
    public JsonResult login(@RequestBody Users users){
        if(StringUtils.isEmpty(users.getUsername()) || StringUtils.isEmpty(users.getPassword())){
            return  JsonResult.errorMsg("用户名或密码不能为空");
        }

        // 校验用户名与密码是否匹配
        Users user = userService.findUserByPassword(users);

        if(user == null){
            return JsonResult.errorMsg("用户名或密码错误");
        }

        UsersVo userVo = setUserRedisSession(user);

        return JsonResult.ok(userVo);
    }

    @ApiOperation(value = "用户注销",notes = "用户注销接口")
    @ApiImplicitParam(value = "用户Id",name = "userId",required = true,
            paramType = "query",dataType = "String")
    @PostMapping("/logout")
    public JsonResult logout(String userId){
        redis.del(USER_REDIS_SESSION + ":" +userId);
        return JsonResult.ok("删除成功");
    }

    private UsersVo setUserRedisSession(Users user){
        // 创建无状态Session，保存到redis中，返回userVo
        String uniqueToken = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION + ":" + user.getId(),uniqueToken,1000*60*30);
        UsersVo userVo = new UsersVo();
        BeanUtils.copyProperties(user,userVo);
        userVo.setUserToken(uniqueToken);
        return userVo;
    }

}
