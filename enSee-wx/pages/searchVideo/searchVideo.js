// 1 导入js文件
var WxSearch = require('../../wxSearchView/wxSearchView.js');

const app = getApp()

Page({
  data: {},

  onLoad: function() {
    // 2 搜索栏初始化
    var that = this;
    var serverUrl = app.serverUrl;
    wx.request({
      url: serverUrl + '/video/queryHotList',
      method: 'POST',
      header: {
        "content-type": "application/json"
      },
      success: function(res) {
        var data = res.data;
        console.log(data);
        var hotList = data.data;
        if (data.status == 200) {
          WxSearch.init(
            that, // 本页面一个引用
            hotList, // 热点搜索推荐，[]表示不使用
            hotList, // 搜索匹配，[]表示不使用
            that.mySearchFunction, // 提供一个搜索回调函数
            that.myGobackFunction //提供一个返回回调函数
          );
        } else {
          wx.showToast({
            title: '系统错误，联系管理员',
            icon: 'none'
          })
        }
      }
    })
  },

  // 3 转发函数，固定部分，直接拷贝即可
  wxSearchInput: WxSearch.wxSearchInput, // 输入变化时的操作
  wxSearchKeyTap: WxSearch.wxSearchKeyTap, // 点击提示或者关键字、历史记录时的操作
  wxSearchDeleteAll: WxSearch.wxSearchDeleteAll, // 删除所有的历史记录
  wxSearchConfirm: WxSearch.wxSearchConfirm, // 搜索函数
  wxSearchClear: WxSearch.wxSearchClear, // 清空函数

  // 4 搜索回调函数  
  mySearchFunction: function(value) {
    // do your job here
    // 示例：跳转
    wx.redirectTo({
      url: '../index/index?isSaveRecord=1&search=' + value
    })
  },

  // 5 返回回调函数
  myGobackFunction: function() {
    // do your job here
    // 示例：返回
    wx.redirectTo({
      url: '../index/index'
    })
  }


})