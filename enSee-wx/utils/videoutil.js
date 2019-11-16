// 上传视频
function uploadVideo() {
  var _this = this;
  wx.chooseVideo({
    sourceType: ['album'],
    success(res) {
      console.log(res);
      var duration = res.duration; // 视频长度
      var height = res.height; // 视频高度
      var width = res.width; // 视频宽度
      var tempFilePath = res.tempFilePath; // 微信临时视频路径
      var thumbTempFilePath = res.thumbTempFilePath; // 封面截图

      // 判断视频长度大于10秒，禁止上传
      if (duration > 11) {
        wx.showToast({
          title: '短视频时间大于10秒，不能上传',
          icon: 'none',
          duration: 3000
        })
      } else if (duration < 2) {
        wx.showToast({
          title: '短视频时间小于2秒，不能上传',
          icon: 'none',
          duration: 3000
        })
      } else {
        // 跳转到选择bgm的页面
        wx.navigateTo({
          url: '../chooseBgm/chooseBgm?duration=' + duration +
            '&height=' + height +
            '&width=' + width +
            '&tempFilePath=' + tempFilePath +
            '&thumbTempFilePath=' + thumbTempFilePath,
        })
      }
    }
  })
}

module.exports = {
  uploadVideo: uploadVideo
}