const app = getApp()

Page({
    data: {
    },

    doLogin : function(e){
        var valueObj = e.detail.value;
        var username = valueObj.username;
        var password = valueObj.password;

        if(username.length ==0 || password.length == 0){
            wx.showToast({
                title: '用户名或密码不能为空',
                icon:'none',
                duration: 2000
            })
        }else{
            var serverUrl = app.serverUrl;
            wx.showLoading({
                title: '请等待',
            })
            wx.request({
                url: serverUrl + '/user/login',
                method : 'POST',
                data:{
                    "username":username,
                    "password":password
                },
                header: {
                    'content-type': 'application/json'
                },
                success: function (res) {
                    console.log(res.data);
                    wx.hideLoading();
                    // 登录成功
                    if (res.data.status == 200) {
                        wx.showToast({
                            title: '登录成功',
                            duration: 2000
                        }),
                        app.userInfo = res.data.data,
                          wx.navigateTo({
                            url: '../mine/mine',
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
        }
    },

    goRegistPage:function(){
        wx.redirectTo({
            url: '../userRegist/regist',
        })
    }
})