
var exec = require('cordova/exec');

var PLUGIN_NAME = 'GooglePay';

var GooglePay = {
  saveToGooglePay: function(successCallback, errorCallback, args) {
    exec(successCallback, errorCallback, PLUGIN_NAME, 'saveToGooglePay', [args]);
  },
};

module.exports = GooglePay;
