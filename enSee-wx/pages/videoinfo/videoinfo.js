var vedioUtil = require("../../utils/videoutil.js");

const app = getApp()

Page({
  data: {
    cover: "cover"
  },

  videoContent: {},

  onLoad: function() {
    var _this = this;
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
  }

})