var videoUtil = require("../../utils/videoutil.js");
const app = getApp()

Page({
  data: {
    faceUrl: "../resource/images/noneface.png",
    fansCounts: 0,
    followCounts: 0,
    receiveLikeCounts: 0,
    id : '',
    nickname: "昵称",
    isSearch: "false",
    isFollow : false
  },

  onLoad: function(params) {
    // 页面加载时，请求后端获取用户信息
    var _this = this;
    var userInfo = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    var publishId = params.publishId;

    var userId = userInfo.id;
    // 判断是否是视频页面跳转过来，或本人查看
    if(publishId != undefined && publishId != '' 
      && publishId != null && publishId!= userId){
      // 如果符合条件，拦截器不做登录拦截
      userId = publishId;
      _this.setData({
        isSearch : "true"
      })
    }
    wx.showLoading({
      title: '加载中...',
    });

    // 请求
    wx.request({
      url: serverUrl + '/user/getUserInfo?userId=' + userId
        + "&fansId=" + userInfo.id,
      method: 'POST',
      header: {
        "context-type": "application/json",
        "userId": userInfo.id,
        "userToken": userInfo.userToken,
        "isSearch" : _this.data.isSearch
      },
      // 成功将数据绑定
      success: function(res) {
        var data = res.data;
        console.log(data);
        wx.hideLoading();
        if (data.status == 200) {
          var userInfo = data.data;
          // 第一次注册没有头像处理
          var faceUrl = "../resource/images/noneface.png";
          if (userInfo.faceImage != null || userInfo.faceImage != "" ||
            userInfo.faceImage != undefined) {
            faceUrl = serverUrl + userInfo.faceImage;
          }
          _this.setData({
            faceUrl: faceUrl,
            fansCounts: userInfo.fansCounts,
            followCounts: userInfo.followCounts,
            receiveLikeCounts: userInfo.receiveLikeCounts,
            nickname: userInfo.nickname,
            id : userInfo.id,
            isFollow : userInfo.isFollow
          })
        }else if(data.status == 502){
          // 未登录被拦截
          wx.showToast({
            title: data.msg,
            icon: 'none',
            duration: 3000,
            success: function () {
              wx.redirectTo({
                url: '../userLogin/login',
              })
            }
          })
        }else{
          wx.showToast({
            title: '系统异常，请联系管理员',
            icon: 'none',
            duration: 3000
          })
        }
      },
      fail: function(res){
        wx.showToast({
          title: '系统异常，请联系管理员',
          icon: 'none',
          duration: 3000
        })
      }
    })
  },

  // 点击关注或取消关注按钮
  followMe: function(e){
    var _this = this;
    console.log(e);
    var serverUrl = app.serverUrl;
    var fansInfo = app.getGlobalUserInfo();
    var userId = _this.data.id;
    // 判断是否登录
    if (fansInfo.id == undefined || fansInfo.id == '' || fansInfo.id == null) {
      wx.navigateTo({
        url: '../userLogin/login?from=upload&info=' + info,
      });
      return ;
    }
    var followType = e.currentTarget.dataset.followtype;
    // 如果是0，则关注，如果是1则取消关注
    var url = serverUrl + "/user/addFollowFans?userId=" + userId
        + "&fansId=" + fansInfo.id;
    if(followType == '1'){
      url = serverUrl + "/user/reduceFollowFans?userId=" + userId
        + "&fansId=" + fansInfo.id;
    }
    
    wx.request({
      url: url,
      method: 'POST',
      header: {
        "content-type": "application/json",
        "userId": fansInfo.id,
        "userToken": fansInfo.userToken
      },
      success: function (res) {
        if(res.data.status == 200){
          // 关注成功提示消息
          wx.showToast({
            title: res.data.msg,
            success: function () {
              _this.setData({
                isFollow: !_this.data.isFollow
              })
            }
          })
        } else if (res.data.status == 502) {
          wx.showToast({
            title: res.data.msg,
            icon: 'none',
            duration: 3000,
            success: function () {
              wx.redirectTo({
                url: '../userLogin/login',
              })
            }
          })
        }
      }
    })
  },

  // 登出
  logout: function() {
    var userInfo = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    wx.request({
      url: serverUrl + '/user/logout?userId=' + userInfo.id,
      method: 'POST',
      header: {
        "content-type": "application/json",
        "userId": userInfo.id,
        "userToken" : userInfo.userToken
      },
      success: function(res) {
        console.log(res.data);
        if (res.data.status == 200) {
          wx.showToast({
              title: '登出成功',
              duration: 2000
            }),
            //app.userInfo = null;
            wx.removeStorageSync("userInfo");
          // 登出成功跳转到登录页面
          wx.redirectTo({
            url: '../userLogin/login',
          })
        } else if (res.data.status == 502) {
          wx.showToast({
            title: res.data.msg,
            icon: 'none',
            duration: 3000,
            success: function () {
              wx.redirectTo({
                url: '../userLogin/login',
              })
            }
          })
        }else {
          wx.showToast({
            title: res.data.msg,
            icon: 'none',
            duration: 2000
          })
        }
      },
      fail: function (res) {
        wx.showToast({
          title: '系统异常，请联系管理员',
          icon: 'none',
          duration: 3000
        })
      }
    })
  },

  // 修改头像
  changeFace: function() {
    // 暂存this，防止到下面作用域不正确
    var _this = this;
    // 调用api，让用户选择照片
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album'],
      success(res) {
        // tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFilePaths;
        console.log(tempFilePaths);
        var userInfo = app.getGlobalUserInfo();
        var serverUrl = app.serverUrl;
        wx.showLoading({
          title: '上传中...',
        })
        // 成功调用上传文件接口
        wx.uploadFile({
          url: serverUrl + '/user/uploadFace?userId=' + userInfo.id,
          filePath: tempFilePaths[0],
          header:{
            "userId": userInfo.id,
            "userToken": userInfo.userToken
          },
          name: 'file',
          success(res) {
            wx.hideLoading();
            var data = JSON.parse(res.data);
            console.log(data);
            if (data.status == 200) {
              wx.showToast({
                title: '更换头像成功',
              })
              // 将头像路径替换
              var url = data.data;
              _this.setData({
                faceUrl: serverUrl + url
              })
            } else if (res.data.status == 502) {
              wx.showToast({
                title: res.data.msg,
                icon: 'none',
                duration: 3000,
                success: function () {
                  wx.redirectTo({
                    url: '../userLogin/login',
                  })
                }
              })
            }else {
              wx.showToast({
                title: '更换头像失败',
                icon: 'none'
              })
            }
          },
          fail: function (res) {
            wx.showToast({
              title: '系统异常，请联系管理员',
              icon: 'none',
              duration: 3000
            })
          }
        })
      }
    })
  },

  // 上传视频
  uploadVideo: function() {
    videoUtil.uploadVideo();
  }

})