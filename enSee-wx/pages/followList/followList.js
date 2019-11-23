const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    serverUrl:'',
    followList:[],
    isFollowType: false,
    listType: false,
    clickUserId: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    var _this = this;
    var userInfo = app.getGlobalUserInfo();
    var type = params.type;
    var clickUserId = params.userId;
    if(type == "follow"){
      _this.setData({
        listType : true
      });
      if (userInfo.id != undefined && userInfo.id != '' &&
           userInfo.id != null && userInfo.id == clickUserId){
        _this.setData({
          isFollowType: true
        });
      }
    }
    //params传递 点开的那个用户的id
    _this.setData({
      clickUserId: clickUserId
    });
    
    // 请求后端获取所有的关注人信息
    _this.queryFollowList(clickUserId);

  },

  showUserInfo: function(e){
    console.log(e);
    var index = e.currentTarget.dataset.arrindex;
    var userId = this.data.followList[index].id;
    wx.navigateTo({
      url: '../mine/mine?publishId=' + userId,
    })
  },

  queryFollowList: function(userId){
    var _this = this;
    var serverUrl = app.serverUrl;
    var url = serverUrl + "/user/findFollow?userId=" + userId;
    if(_this.data.listType == false){
      url = serverUrl + "/user/findFans?userId=" + userId;
    }
    wx.request({
      url: url,
      method: 'POST',
      header: {
        "content-type": "application/json"
      },
      success: function (res) {
        console.log(res);
        if (res.data.status == 200) {
          _this.setData({
            serverUrl: serverUrl,
            followList: res.data.data
          })
        }
      }
    })
  },

  showFollowList: function(){
    var _this = this;
    if(_this.data.listType == true){
      return ;
    }
    _this.setData({
      listType: true
    });
    _this.queryFollowList(_this.data.clickUserId);
  },

  showFansList: function () {
    var _this = this;
    if (_this.data.listType == false) {
      return;
    }
    _this.setData({
      listType: false
    });
    _this.queryFollowList(_this.data.clickUserId);
  },

  // 取消关注
  unFollow : function(e){
    var _this = this;
    console.log(e);
    var index = e.target.dataset.arrindex;
    var followId = this.data.followList[index].id;
    var userInfo = app.getGlobalUserInfo();
    var serverUrl = app.serverUrl;
    wx.request({
      url: serverUrl + "/user/reduceFollowFans?userId=" + followId
        + '&fansId=' + userInfo.id,
      method: 'POST',
      header: {
        "content-type": "application/json",
        "userId": userInfo.id,
        "userToken": userInfo.userToken
      },
      success: function (res) {
        console.log(res);
        if (res.data.status == 200) {
          _this.queryFollowList(userInfo.id);
        } else if (res.data.status == 502) {
          // 未登录被拦截
          wx.showToast({
            title: res.data.msg,
            icon: 'none',
            duration: 2000
          })
          wx.redirectTo({
            url: '../userLogin/login',
          })
        } else {
          wx.showToast({
            title: '系统异常，请联系管理员',
            icon: 'none',
            duration: 3000
          })
        }
      }
    })
  },

  // ListTouch触摸开始
  ListTouchStart(e) {
    this.setData({
      ListTouchStart: e.touches[0].pageX
    })
  },

  // ListTouch计算方向
  ListTouchMove(e) {
    this.setData({
      ListTouchDirection: e.touches[0].pageX - this.data.ListTouchStart > 0 ? 'right' : 'left'
    })
  },

  // ListTouch计算滚动
  ListTouchEnd(e) {
    if (this.data.ListTouchDirection == 'left') {
      this.setData({
        modalName: e.currentTarget.dataset.target
      })
    } else {
      this.setData({
        modalName: null
      })
    }
    this.setData({
      ListTouchDirection: null
    })
  }
})