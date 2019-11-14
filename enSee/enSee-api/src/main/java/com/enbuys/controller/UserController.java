package com.enbuys.controller;

import com.enbuys.pojo.Users;
import com.enbuys.pojo.vo.UsersVo;
import com.enbuys.service.UserService;
import com.enbuys.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        UsersVo usersVo = new UsersVo();
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
        String fileSpace = "D:/Projects/EnSee-image";
        // 保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/face";

        // 判断文件是否存在
        if(files != null && files.length != 0 ){
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;

            // 获取文件名
            String fileName = files[0].getOriginalFilename();
            if(!StringUtils.isEmpty(fileName)){
                // 拼接最终路径
                String path = fileSpace + uploadPathDB + "/" + fileName;
                // 设置数据库保存的路径
                uploadPathDB += ("/" + fileName);

                File file = new File(path);
                // 判断路径中父文件夹是否存在，不存在创建
                if(file.getParentFile() != null || !file.getParentFile().isDirectory()){
                    file.getParentFile().mkdirs();
                }

                // 存在，保存图片到本地
                try {
                    fileOutputStream = new FileOutputStream(file);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    return JsonResult.ok("头像更新失败");
                } finally {
                    if(fileOutputStream!=null){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
            }else {
                return JsonResult.ok("头像更新失败");
            }
        }else {
            return JsonResult.ok("头像更新失败");
        }

        // 图片上传成功，保存图片到数据库中
        Users users = new Users();
        users.setId(userId);
        users.setFaceImage(uploadPathDB);
        userService.updateUserById(users);

        return JsonResult.ok(uploadPathDB);
    }

    private UsersVo setUserRedisSession(Users user){
        // 创建无状态Session，保存到redis中，返回userVo
        String uniqueToken = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION + ":" + user.getId(),uniqueToken,60*30);
        UsersVo userVo = new UsersVo();
        BeanUtils.copyProperties(user,userVo);
        userVo.setUserToken(uniqueToken);
        return userVo;
        }

    }
