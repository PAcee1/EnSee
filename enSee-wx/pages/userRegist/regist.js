const app = getApp()

Page({
    data: {

    },
    doRegist : function(e){
        var formObj = e.detail.value;
        var username = formObj.username;
        var password = formObj.password;
        if(username.length == 0 || password.length == 0){
            wx.showToast({
                title: '用户名或密码不能为空',
                icon : 'none',
                duration: 2000
            })
        }else{
            var serverUrl = app.serverUrl;
            wx.request({
                url: serverUrl + '/user/regist',
                method: 'POST',
                data: {
                    username : username,
                    password: password
                },
                header:{
                    'content-type': 'application/json'
                },
                success : function(res){
                    console.log(res.data);
                    // 注册成功
                    if(res.data.status == 200){
                        wx.showToast({
                            title: '注册成功',
                            duration: 2000
                        }),
                        app.userInfo = res.data.data
                    }else if(res.data.status == 500){
                        wx.showToast({
                            title: res.data.msg,
                            icon: 'none',
                            duration: 2000
                        })
                    }
                }
            })
        }
    }
})