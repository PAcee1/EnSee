const app = getApp()

Page({
  data: {
    page: 1,
    size: 5,
    totalPage: 1,
    videosList: [],
    screenWidth: 350,

    videoDesc: '',
    serverUrl: ""
  },

  onLoad: function(params) {
    var _this = this;
    // 如果是从搜索页进来，保存参数
    var videoDesc = params.search;
    var isSaveRecord = params.isSaveRecord;
    if (isSaveRecord == null || isSaveRecord == '' || isSaveRecord == undefined) {
      isSaveRecord = 0;
    }
    if (videoDesc == null || videoDesc == '' || videoDesc == undefined) {
      videoDesc = "";
    }

    // 系统的屏幕宽度
    var screenWidth = wx.getSystemInfoSync().screenWidth;
    _this.setData({
      screenWidth: screenWidth,
      videoDesc: videoDesc
    });
    var page = _this.data.page;
    // 查询视频
    _this.queryAllVideos(page, isSaveRecord);
  },

  // 上拉加载下一页视频
  onReachBottom: function() {
    var _this = this;
    var page = _this.data.page;
    // 判断当前页是否是最后一页，如果是则不再加载
    if (page == _this.data.totalPage) {
      wx.showToast({
        title: '已经是最后一页，没有视频了',
        icon: 'none',
        duration: 3000
      })
      return;
    }
    page += 1;
    // 查询视频
    _this.queryAllVideos(page, 0);
  },

  // 下拉刷新
  onPullDownRefresh: function() {
    wx.showNavigationBarLoading();
    // 需要将videoDesc置为空
    this.setData({
      videoDesc: ''
    });
    this.queryAllVideos(1, 0);
  },

  // 抽离查询视频的接口
  queryAllVideos: function(page, isSaveRecord) {
    var _this = this;
    // 请求后端，获取videos集合
    var serverUrl = app.serverUrl;
    var size = _this.data.size;
    var videoDesc = _this.data.videoDesc;
    wx.showLoading({
      title: '加载中...',
    });
    wx.request({
      url: serverUrl + '/video/queryAll?page=' + page +
        '&size=' + size +
        '&isSaveRecord=' + isSaveRecord +
        '&videoDesc=' + videoDesc,
      method: 'POST',
      header: {
        "content-type": "application/json"
      },
      success: function(res) {
        wx.hideLoading();
        wx.hideNavigationBarLoading(); // 停止导航栏加载动画
        wx.stopPullDownRefresh(); //停止下拉后的三个丶
        console.log(res.data);
        if (res.data.status == 200) {
          // 判断是否为第一页，如果是第一页，设置videos为空
          if (page === 1) {
            _this.setData({
              videosList: []
            })
          }
          // 保存新视频List与旧视频List，进行拼接
          var oldVideosList = _this.data.videosList;
          var newVideosList = res.data.data.rows;

          _this.setData({
            videosList: oldVideosList.concat(newVideosList),
            page: res.data.data.page,
            totalPage: res.data.data.total,
            serverUrl: serverUrl
          })
        } else {
          wx.showToast({
            title: '查询出错，联系管理员',
            icon: 'none',
            duration: 3000
          })
        }
      },
      fail: function(res){
        console.log(res);
        wx.hideLoading();
        wx.showToast({
          title: '系统异常，联系管理员',
          icon: 'none',
          duration: 3000
        })
      }
    })
  },

  showVideoInfo : function(e){
    var _this = this;
    console.log(e);
    var index = e.target.dataset.arrindex;
    var videoInfo = JSON.stringify(_this.data.videosList[index]);

    wx.navigateTo({
      url: '../videoinfo/videoinfo?videoInfo=' + videoInfo,
    })
  }

})