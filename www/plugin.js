
var exec = require('cordova/exec');

var PLUGIN_NAME = 'GooglePay';

var GooglePay = {
  saveToGooglePay: function(successCallback, errorCallback, issuerId, loyaltyClassId, loyaltyObjectId, accountId, accountName, issuerName, programName, isProduction, barcode, memberName, idNumber, policyNumber, base64Image) {
    var args = {};
    args.issuerId = issuerId;
    args.loyaltyClassId = loyaltyClassId;
    args.loyaltyObjectId = loyaltyObjectId;
    args.accountId = accountId;
    args.accountName = accountName;
    args.issuerName = issuerName;
    args.programName = programName;
    args.isProduction = isProduction;
    args.barcode = barcode;
	args.memberName = memberName;
	args.idNumber = idNumber;
	args.policyNumber = policyNumber;
	args.base64Image = base64Image;

    exec(successCallback, errorCallback, PLUGIN_NAME, 'saveToGooglePay', [args]);
  },
  saveJWT: function(successCallback, errorCallback, jwt) {
    var args = {};
    args.jwt = jwt;

    exec(successCallback, errorCallback, PLUGIN_NAME, 'saveJWT', [args]);
  }
};

module.exports = GooglePay;
