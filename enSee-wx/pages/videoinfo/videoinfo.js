var vedioUtil = require("../../utils/videoutil.js");

const app = getApp()

Page({
  data: {
    cover: "cover",
    src: "",
    videoInfo: {},
    serverUrl: "",
    userLikeVideo: false,

    commentsPage: 1,
    commentsSize : 7777, // 暂时不做分页
    commentsTotalPage: 1,
    commentsSum: 0,
    commentsList: [],

    contentValue: "",
    talkViewClass:"talks-layer",
    talkHidden: false,


    placeholder: "说点什么..."
  },

  videoContent: {},

  onLoad: function(params) {
    // 评论弹出层动画创建
    this.animation = wx.createAnimation({
      duration: 400,
      timingFunction: "ease",
      delay: 0
    })
    this.animation.bottom("0rpx").height("100%").step();
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
    if (user != undefined || user != null || user != '') {
      userId = user.id;
    }

    // 请求后端，获取用户是否点赞
    wx.request({
      url: serverUrl + '/user/queryIsLiked?userId=' + userId +
        '&videoId=' + videoInfo.id,
      method: 'POST',
      header: {
        "content-type": "application/json"
      },
      success: function(res) {
        // 成功，设置userLikeVideo为返回结果，true or false
        console.log(res);
        if (res.data.status == 200) {
          _this.setData({
            userLikeVideo: res.data.data
          })
        }
      }
    });

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
  upload: function() {
    var user = app.getGlobalUserInfo();
    var info = JSON.stringify(this.data.videoInfo);
    if (user.id == undefined || user.id == '' || user.id == null) {
      wx.navigateTo({
        url: '../userLogin/login?from=upload&info=' + info,
      })
    } else {
      vedioUtil.uploadVideo();
    }
  },

  // 查看视频作者信息
  showPublisher: function() {
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
  showIndex: function() {
    wx.redirectTo({
      url: '../index/index',
    })
  },

  // 去個人中心
  showMine: function() {
    var user = app.getGlobalUserInfo();
    if (user.id == undefined || user.id == '' || user.id == null) {
      wx.navigateTo({
        url: '../userLogin/login',
      })
    } else {
      wx.navigateTo({
        url: '../mine/mine',
      })
    }
  },

  // 用户点赞视频
  likeVideoOrNot: function() {
    var _this = this;
    var videoInfo = _this.data.videoInfo;
    var serverUrl = app.serverUrl;
    var user = app.getGlobalUserInfo();
    var info = JSON.stringify(this.data.videoInfo);
    if (user.id == undefined || user.id == '' || user.id == null) {
      wx.navigateTo({
        url: '../userLogin/login?from=upload&info=' + info,
      })
      return;
    }

    // 拼接url地址
    var url = serverUrl + "/video/userLikeVideo?userId=" + user.id +
      "&videoId=" + videoInfo.id +
      "&videoCreateId=" + videoInfo.userId;
    if (_this.data.userLikeVideo) {
      // 如果已经喜欢了视频，需要改为取消点赞
      var url = serverUrl + "/video/userUnLikeVideo?userId=" + user.id +
        "&videoId=" + videoInfo.id +
        "&videoCreateId=" + videoInfo.userId;
    }

    wx.request({
      url: url,
      method: 'POST',
      header: {
        "content-type": "application/json"
      },
      success: function(res) {
        // 成功将点赞状态修改
        _this.setData({
          userLikeVideo: !_this.data.userLikeVideo
        })
      }
    })
  },

  // 用户点击分享按钮
  shareMe: function() {
    var _this = this;
    var videoInfo = _this.data.videoInfo;
    wx.showActionSheet({
      itemList: ['下载视频', '举报视频'],
      success(res) {
        console.log(res.tapIndex);
        if (res.tapIndex == 0) { // 下载视频
          wx.showLoading({
            title: '下载中...',
          })
          wx.downloadFile({
            url: app.serverUrl + videoInfo.videoPath, //仅为示例，并非真实的资源
            success(res) {
              // 只要服务器有响应数据，就会把响应内容写入文件并进入 success 回调，业务需要自行判断是否下载到了想要的内容
              if (res.statusCode === 200) {
                console.log(res.tempFilePath);
                // 保存视频到相册
                wx.saveVideoToPhotosAlbum({
                  filePath: res.tempFilePath,
                  success: function(res) {
                    console.log(res);
                    debugger
                    if (res.status == 200) {
                      wx.showToast({
                        title: '下载成功',
                        duration: 2000
                      })
                    }
                  }
                })
              }
            }
          })
        } else if (res.tapIndex == 1) { // 举报视频
          // 转跳举报页面，传递视频id与创建者id
          var videoId = videoInfo.id;
          var publishId = videoInfo.userId;
          wx.navigateTo({
            url: '../report/report?videoId=' + videoId +
              '&publishId=' + publishId,
          })
        }
      },
      fail(res) {
        console.log(res.errMsg)
      }
    })
  },

  // 转发
  onShareAppMessage: function(res) {
    var videoInfo = JSON.stringify(this.data.videoInfo);
    return {
      title: '嗯看视频',
      path: '/pages/videinfo/videoinfo?videoInfo=' + videoInfo
    }
  },

  // 点击评论按钮，显示留言框
  leaveComment : function(){
    this.onHide();
    this.setData({
      talkViewClass: "talks-layer-show",
      talkHidden : true
    })
    // 加载留言
    this.queryComments(1);
  },
  // 关闭评论，清空留言list并设置为第一页
  hideTalks: function () {
    this.onShow();
    this.setData({
      talkViewClass: "talks-layer",
      talkHidden: false,
      commentsList: [],
      commentsPage: 1
    })
  },

  // 保存留言
  saveComment : function(e){
    console.log(e);
    var comment = e.detail.value;
    if(comment){
      // 判断是否登录
      var _this = this;
      var videoInfo = _this.data.videoInfo;
      var serverUrl = app.serverUrl;
      var user = app.getGlobalUserInfo();
      var info = JSON.stringify(this.data.videoInfo);
      if (user.id == undefined || user.id == '' || user.id == null) {
        wx.navigateTo({
          url: '../userLogin/login?from=upload&info=' + info,
        })
      } else {
        wx.request({
          url: serverUrl + '/comment/save?',
          method: 'POST',
          data: {
            videoId: videoInfo.id,
            fromUserId: user.id,
            comment: comment
          },
          header: {
            "content-type": "application/json",
            "userId": user.id,
            "userToken": user.userToken
          },
          success: function (res) {
            console.log(res);
            if (res.data.status == 200) {
              wx.showToast({
                title: '评论成功',
              });
              // 刷新评论并且设置input框为空
              _this.setData({
                contentValue: "",
                commentsList: []
              });
              _this.queryComments(1);
            } else if (res.data.status == 502) {
              wx.showToast({
                title: res.data.msg,
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
                title: '评论失败',
                icon: 'none'
              })
            }
          }

        })
      }
    }else{
      wx.showToast({
        title: '无字天书？',
        icon:'none'
      })
    }
    
  },

  // 查看留言
  queryComments: function(page){
    var _this = this;
    var videoInfo = _this.data.videoInfo;
    var serverUrl = app.serverUrl;
    // var page = _this.data.commentsPage;
    var size = _this.data.commentsSize;
    wx.request({
      url: serverUrl + '/comment/query?videoId=' + videoInfo.id
          + '&page=' + page
          + '&size=' + size,
      method: 'POST',
      header: {
        "content-type": "application/json"
      },
      success: function(res){
        console.log(res);
        
        var data = res.data;
        if(data.status == 200){
          var commentsList = data.data.rows;
          if(page != 1){
            var oldCommentsList = _this.data.commentsList; // 旧的集合，将新的拼接起来
            commentsList = oldCommentsList.concat(commentsList);
          }
          var commentsSum = commentsList.length;
          _this.setData({
            commentsList: commentsList,
            commentsPage: page,
            commentsSum: commentsSum,
            commentsTotalPage: res.data.data.total
          });
        } 
      }
    })
  },

  // 上拉分页加载留言
  onScrollLoad: function(res){
    var _this = this;
    var currentPage = _this.data.commentsPage;
    var totalPage = _this.data.commentsTotalPage;
    // 如果是最后一页，不做任何操作
    if (currentPage === totalPage) {
      return;
    }
    var page = currentPage + 1;
    _this.queryComments(page);
  }

})