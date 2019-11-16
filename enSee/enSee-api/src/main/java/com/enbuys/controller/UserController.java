package com.enbuys.controller;

import com.enbuys.pojo.Users;
import com.enbuys.pojo.vo.UsersVO;
import com.enbuys.service.UserService;
import com.enbuys.utils.FileUpload;
import com.enbuys.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
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

        UsersVO userVo = setUserRedisSession(user);

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

        UsersVO userVo = setUserRedisSession(user);

        return JsonResult.ok(userVo);
    }

    @ApiOperation(value = "用户登出",notes = "用户登出接口")
    @ApiImplicitParam(value = "用户Id",name = "userId",required = true,
            paramType = "query",dataType = "String")
    @PostMapping("/logout")
    public JsonResult logout(String userId){
        if(StringUtils.isEmpty(userId)){
            return JsonResult.errorMsg("用户ID为空，登出失败");
        }
        redis.del(USER_REDIS_SESSION + ":" +userId);
        return JsonResult.ok("登出成功");
    }

    @ApiOperation(value = "用户信息",notes = "获取用户信息接口")
    @ApiImplicitParam(value = "用户Id",name = "userId",required = true,
            paramType = "query",dataType = "String")
    @PostMapping("/getUserInfo")
    public JsonResult getUserInfo(String userId){
        if(StringUtils.isEmpty(userId)){
            return JsonResult.errorMsg("用户ID为空，获取失败");
        }

        Users user = userService.queryUserById(userId);
        UsersVO usersVo = new UsersVO();
        BeanUtils.copyProperties(user,usersVo);
        return JsonResult.ok(usersVo);
    }

    @ApiOperation(value = "用户更新头像",notes = "用户更新头像接口")
    @ApiImplicitParam(value = "用户Id",name = "userId",required = true,
            paramType = "query",dataType = "String")
    @PostMapping("/uploadFace")
    public JsonResult uploadFace(String userId,
                         @RequestParam("file") MultipartFile[] files) throws Exception{
        if(StringUtils.isEmpty(userId)){
            return JsonResult.errorMsg("头像更新失败");
        }
        // 定义上传到的文件夹路径
        String fileSpace = FILE_SPACE;
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/face";

        // 调用接口上传图片
        Map<String, Object> map = FileUpload.upload(files[0], fileSpace, uploadPathDB);
        if(map == null){
            return JsonResult.errorMsg("上传失败");
        }
        String fileFinalPath = (String) map.get("fileFinalPath");
        uploadPathDB = (String) map.get("uploadPathDB");


        // 图片上传成功，保存图片到数据库中
        Users users = new Users();
        users.setId(userId);
        users.setFaceImage(uploadPathDB);
        userService.updateUserById(users);

        return JsonResult.ok(uploadPathDB);
    }

    /**
     * 保存用户无状态Session到Redis中
     * @param user
     * @return
     */
    private UsersVO setUserRedisSession(Users user){
        // 创建无状态Session，保存到redis中，返回userVo
        String uniqueToken = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION + ":" + user.getId(),uniqueToken,60*30);
        UsersVO userVo = new UsersVO();
        BeanUtils.copyProperties(user,userVo);
        userVo.setUserToken(uniqueToken);
        return userVo;
        }

    }
