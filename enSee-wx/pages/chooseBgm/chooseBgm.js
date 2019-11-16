const app = getApp()

Page({
  data: {
    bgmList: [],
    serverUrl: "",
    videoObj: {}
  },

  onLoad: function(params) {
    var _this = this;
    var serverUrl = app.serverUrl;

    // 保存传来的视频信息
    console.log(params);
    _this.setData({
      videoObj: params
    });
    wx.showLoading({
      title: '加载中...',
    });
    // 请求，获取所有的bgm
    wx.request({
      url: serverUrl + '/bgm/list',
      method: 'POST',
      header: {
        "content-type": "application/json"
      },
      success: function(res) {
        console.log(res.data);
        wx.hideLoading();
        if (res.data.status == 200) {
          _this.setData({
            bgmList: res.data.data,
            serverUrl: serverUrl
          })
        } else {
          wx.showToast({
            title: '出错了，联系管理员',
            icon: 'none',
            duration: 3000
          })
        }
      }
    })
  },

  //上传视频
  upload: function(e) {
    var _this = this;
    var serverUrl = app.serverUrl;
    var bgmId = e.detail.value.bgmId;
    var desc = e.detail.value.desc;
    var userInfo = app.getGlobalUserInfo();
    // 上传
    wx.showLoading({
      title: '上传中...',
    })
    wx.uploadFile({
      url: serverUrl + '/video/upload',
      formData: {
        userId: userInfo.id,
        bgmId: bgmId,
        desc: desc,
        videoSeconds: _this.data.videoObj.duration,
        videoWidth: _this.data.videoObj.width,
        videoHeight: _this.data.videoObj.height
      },
      filePath: _this.data.videoObj.tempFilePath,
      name: 'file',
      header: {
        "content-type": "application/json"
      },
      success: function(res) {
        wx.hideLoading();
        var data = JSON.parse(res.data);
        console.log(data);
        if (data.status == 200) {
          // 上传封面
          wx.showToast({
            title: '上传成功',
          });
          wx.navigateBack({
            delta: 1
          })
        } else {
          wx.showToast({
            title: '上传失败',
            icon: 'none'
          })
        }

      }
    })
  }
})