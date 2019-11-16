//app.js
App({
  serverUrl: "http://192.168.0.104:8008",
  //serverUrl: "http://localhost:8008",

  usrInfo: null,

  setGlobalUserInfo: function(user){
    wx.setStorageSync("userInfo", user);
  },

  getGlobalUserInfo: function () {
    return wx.getStorageSync("userInfo");
  }
})