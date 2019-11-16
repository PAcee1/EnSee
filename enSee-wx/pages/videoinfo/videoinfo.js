var vedioUtil = require("../../utils/videoutil.js");

const app = getApp()

Page({
  data: {
    cover: "cover",
    src : "",
    videoInfo : {},
    serverUrl: ""
  },

  videoContent: {},

  onLoad: function(params) {
    var _this = this;
    var videoInfo = JSON.parse(params.videoInfo);
    console.log(videoInfo);
    _this.setData({
      src : app.serverUrl + videoInfo.videoPath,
      videoInfo: videoInfo,
      serverUrl: app.serverUrl 
    })
    _this.videoContent = wx.createVideoContext("myVideo", _this);
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
    vedioUtil.uploadVideo();
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
  }

})