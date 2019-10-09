
var exec = require('cordova/exec');

var PLUGIN_NAME = 'GooglePay';

var GooglePay = {
  saveToGooglePay: function(successCallback, errorCallback, issuerId, loyaltyClassId, loyaltyObjectId, accountId, accountName, issuerName, programName, isProduction) {
    var args = {};
    args.issuerId = issuerId;
    args.loyaltyClassId = loyaltyClassId;
    args.loyaltyObjectId = loyaltyObjectId;
    args.accountId = accountId;
    args.accountName = accountName;
    args.issuerName = issuerName;
    args.programName = programName;
    if (isProduction == 1){
      args.isProduction = true;
    } else {
      args.isProduction = false;
    }

    exec(successCallback, errorCallback, PLUGIN_NAME, 'saveToGooglePay', [args]);
  },
};

module.exports = GooglePay;
