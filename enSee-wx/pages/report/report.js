const app = getApp()

Page({
  data: {
    reasonType: "请选择原因",
    reportReasonArray: app.reportReasonArray,
    publishId:"",
    videoId:""
  },

  onLoad:function(params){
    var videoId = params.videoId;
    var publishId = params.publishId;

    this.setData({
      videoId:videoId,
      publishId:publishId
    })
  },

  // 动态展示多选框
  changeMe:function(e){
    console.log(e);
    var index = e.detail.value;
    var reasonType = app.reportReasonArray[index];
    this.setData({
      reasonType: reasonType
    })
  },

  // 提交
  submitReport:function(e){
    var _this = this;
    // 判断是否登录
    var user = app.getGlobalUserInfo();
    if (user.id == undefined || user.id == '' || user.id == null) {
      wx.navigateTo({
        url: '../userLogin/login',
      })
      return;
    }
    var serverUrl = app.serverUrl;
    // 获取表单内容
    console.log(e);
    var index = e.detail.value.reasonIndex;
    var reasonType = app.reportReasonArray[index];
    var reasonContent = e.detail.value.reasonContent;

    var videoId = _this.data.videoId;
    var publishId = _this.data.publishId;
    wx.request({
      url: serverUrl + '/report/video',
      method:'POST',
      data:{
        dealUserId: publishId,
        dealVideoId: videoId,
        title: reasonType,
        content: reasonContent,
        userid: user.id
      },
      header:{
        "content-type": "application/json",
        "userId": user.id,
        "userToken": user.userToken
      },
      success: function(res){
        var data = res.data;
        if(data.status == 200){
          wx.showToast({
            title: '举报成功',
            duration: 2000
          });
          setTimeout(function(){
            wx.navigateBack();
          },2000)
        } else if (data.status == 502) {
          wx.showToast({
            title: data.msg,
            icon: 'none',
            duration: 2000
          });
          setTimeout(function () {
            wx.redirectTo({
              url: '../userLogin/login',
            })
          }, 2000)
        } else {
          wx.showToast({
            title: '出错了，联系管理员',
            icon: 'none',
            duration: 3000
          })
        }
      }
    })
    
  }
})
