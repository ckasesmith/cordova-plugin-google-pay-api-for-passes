
var exec = require('cordova/exec');

var PLUGIN_NAME = 'GooglePay';

var GooglePay = {
  saveToGooglePay: function(successCallback, errorCallback, issuerId, loyaltyClassId, loyaltyObjectId, accountId, accountName, issuerName, programName) {
    var args = {};
    args.issuerId = issuerId;
    args.loyaltyClassId = loyaltyClassId;
    args.loyaltyObjectId = loyaltyObjectId;
    args.accountId = accountId;
    args.accountName = accountName;
    args.issuerName = issuerName;
    args.programName = programName;
    
    exec(successCallback, errorCallback, PLUGIN_NAME, 'saveToGooglePay', [args]);
  },
};

module.exports = GooglePay;
