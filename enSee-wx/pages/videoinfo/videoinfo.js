var vedioUtil = require("../../utils/videoutil.js");

const app = getApp()

Page({
  data: {
    cover: "cover",
    src : "",
    videoInfo : {},
    serverUrl: "",
    userLikeVideo : false
  },

  videoContent: {},

  onLoad: function(params) {
    var _this = this;
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo();
    var videoInfo = JSON.parse(params.videoInfo);
    console.log(videoInfo);
    _this.setData({
      src: serverUrl + videoInfo.videoPath,
      videoInfo: videoInfo,
      serverUrl: serverUrl
    })
    _this.videoContent = wx.createVideoContext("myVideo", _this);
    // 如果用户未登录，设置id为空
    var userId = "";
    if(user != undefined || user != null || user != ''){
      userId = user.id;
    }

    // 请求后端，获取用户是否点赞
    wx.request({
      url: serverUrl + '/user/queryIsLiked?userId=' + userId 
          + '&videoId=' + videoInfo.id,
      method: 'POST',
      header: {
        "content-type" : "application/json"
      },
      success: function(res){
        // 成功，设置userLikeVideo为返回结果，true or false
        console.log(res);
        if(res.data.status == 200){
          _this.setData({
            userLikeVideo:res.data.data
          })
        }
      }
    })
  },

  onShow: function() {
    var _this = this;
    _this.videoContent.play();
  },

  onHide: function() {
    var _this = this;
    _this.videoContent.pause();
  },

  // 上传视频
  upload:function(){
    var user = app.getGlobalUserInfo();
    var info = JSON.stringify(this.data.videoInfo);
    if (user.id == undefined || user.id == '' || user.id == null) {
      wx.navigateTo({
        url: '../userLogin/login?from=upload&info=' + info,
      })
    }else{
      vedioUtil.uploadVideo();
    }
  },

  // 查看视频作者信息
  showPublisher : function(){
    // 跳转mine页面
    var publishId = this.data.videoInfo.userId;
    wx.navigateTo({
      url: '../mine/mine?publishId=' + publishId,
    })
  },

  // 转跳搜索页面
  showSearch: function() {
    wx.navigateTo({
      url: '../searchVideo/searchVideo',
    })
  },

  // 返回主页
  showIndex: function(){
    wx.redirectTo({
      url: '../index/index',
    })
  },

  // 去個人中心
  showMine : function(){
    var user = app.getGlobalUserInfo();
    if(user.id == undefined || user.id == '' || user.id == null){
      wx.navigateTo({
        url: '../userLogin/login',
      })
    }else{
      wx.navigateTo({
        url: '../mine/mine',
      })
    }
  },

  // 用户点赞视频
  likeVideoOrNot : function(){
    var _this = this;
    var videoInfo = _this.data.videoInfo;
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo();
    if (user.id == undefined || user.id == '' || user.id == null) {
      wx.navigateTo({
        url: '../userLogin/login',
      })
      return;
    }

    // 拼接url地址
    var url = serverUrl + "/video/userLikeVideo?userId=" + user.id
        + "&videoId=" + videoInfo.id
        + "&videoCreateId=" + videoInfo.userId;
    if (_this.data.userLikeVideo){
      // 如果已经喜欢了视频，需要改为取消点赞
      var url = serverUrl + "/video/userUnLikeVideo?userId=" + user.id
        + "&videoId=" + videoInfo.id
        + "&videoCreateId=" + videoInfo.userId;
    }

    wx.request({
      url: url,
      method: 'POST',
      header:{
        "content-type": "application/json"
      },
      success: function(res){
        // 成功将点赞状态修改
        _this.setData({
          userLikeVideo: !_this.data.userLikeVideo
        })
      }
    })
  }

})