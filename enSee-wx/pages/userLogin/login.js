const app = getApp()

Page({
  data: {
    from: '',
    info: ""
  },

  onLoad: function(param) {
    var from = param.from;
    var info = param.info;
    if (from == null || from == '' || from == undefined) {
      from = '';
    }
    if (info == null || info == '' || info == undefined) {
      info = "";
    }
    this.setData({
      from: from,
      info: info
    });
    console.log(from);
  },

  doLogin: function(e) {
    var _this = this;
    var valueObj = e.detail.value;
    var username = valueObj.username;
    var password = valueObj.password;
    var from = _this.data.from;
    var info = _this.data.info;

    if (username.length == 0 || password.length == 0) {
      wx.showToast({
        title: '用户名或密码不能为空',
        icon: 'none',
        duration: 2000
      })
    } else {
      var serverUrl = app.serverUrl;
      wx.showLoading({
        title: '请等待',
      })
      wx.request({
        url: serverUrl + '/user/login',
        method: 'POST',
        data: {
          "username": username,
          "password": password
        },
        header: {
          'content-type': 'application/json'
        },
        success: function(res) {
          console.log(res.data);
          wx.hideLoading();
          // 登录成功
          if (res.data.status == 200) {
            wx.showToast({
              title: '登录成功',
              duration: 2000
            }),
            //app.userInfo = res.data.data,
            // 设置全局缓存user
            app.setGlobalUserInfo(res.data.data);
            if(from != '' && from == "upload"){
              wx.navigateBack({
                url: '../videoinfo/videoinfo?videoInfo=' + info,
                delta: 1
              })
            }else{
              wx.redirectTo({
                url: '../mine/mine',
              })
            }
          } else if (res.data.status == 500) {
            wx.showToast({
              title: res.data.msg,
              icon: 'none',
              duration: 2000
            })
          }
        }
      })
    }
  },

  goRegistPage: function() {
    wx.redirectTo({
      url: '../userRegist/regist',
    })
  }
})