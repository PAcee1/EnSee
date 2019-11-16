var videoUtil = require("../../utils/videoutil.js");
const app = getApp()

Page({
  data: {
    faceUrl: "../resource/images/noneface.png",
    fansCounts: 0,
    followCounts: 0,
    receiveLikeCounts: 0,
    nickname: "昵称"
  },

  onLoad: function(params) {
    // 页面加载时，请求后端获取用户信息
    var _this = this;
    var userInfo = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '加载中...',
    });

    // 请求
    wx.request({
      url: serverUrl + '/user/getUserInfo?userId=' + user.id,
      method: 'POST',
      header: {
        "context-type": "application/json"
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
            nickname: userInfo.nickname
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
      url: serverUrl + '/user/logout?userId=' + user.id,
      method: 'POST',
      header: {
        "content-type": "application/json"
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
        } else if (res.data.status == 500) {
          wx.showToast({
            title: res.data.msg,
            icon: 'none',
            duration: 2000
          })
        }
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
          url: serverUrl + '/user/uploadFace?userId=' + user.id,
          filePath: tempFilePaths[0],
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
            } else {
              wx.showToast({
                title: '更换头像失败',
                icon: 'none'
              })
            }

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