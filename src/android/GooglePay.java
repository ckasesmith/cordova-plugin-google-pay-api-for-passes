package org.apache.cordova.plugin;

import android.content.Intent;

import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolvableVoidResult;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.CreateWalletObjectsRequest;
import com.google.android.gms.wallet.LoyaltyWalletObject;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.WalletObjectsClient;
import com.google.android.gms.wallet.wobs.WalletObjectsConstants;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GooglePay extends CordovaPlugin {

    private static final int SAVE_TO_ANDROID = 888;
    public static final Scope WOB =
            new Scope("https://www.googleapis.com/auth/wallet_object.issuer");

    private String mIssuerId;
    private String mLoyaltyClassId;
    private String mLoyaltyObjectId;

    private String mAccountId = "";
    private String mAccountName = "";
    private String mIssuerName = "";
    private String mProgramName = "";
    private boolean mIsProduction;
    private String mBarcode = "";

    private static CallbackContext mCallbackContext;

    private volatile static GooglePay uniqueInstance;

    public GooglePay() {
    }

    public static GooglePay getInstance() {
        if (uniqueInstance == null) {
            synchronized (GooglePay.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new GooglePay();
                }
            }
        }
        return uniqueInstance;
    }

    public CallbackContext getCallbackContext() {
        return mCallbackContext;
    }

    private LoyaltyWalletObject generateLoyaltyWalletObject() {
        LoyaltyWalletObject wob = LoyaltyWalletObject.newBuilder()
                .setClassId(mIssuerId + "." + mLoyaltyClassId)
                .setId(mIssuerId + "." + mLoyaltyObjectId)
                .setState(WalletObjectsConstants.State.ACTIVE)
                .setAccountId(mAccountId)
                .setAccountName(mAccountName)
                .setBarcodeType("qrCode")
                .setBarcodeValue(mBarcode)
                .setBarcodeAlternateText(mBarcode)
                .setIssuerName(mIssuerName)
                .setProgramName(mProgramName)
                .build();
        return wob;
    }

    public void setSaveToAndroid(CordovaActivity activity, JSONArray args) throws JSONException {
        JSONObject object = args.getJSONObject(0);

        mIssuerId = object.getString("issuerId");
        mLoyaltyClassId = object.optString("loyaltyClassId");
        mLoyaltyObjectId = object.optString("loyaltyObjectId");

        mAccountId = object.optString("accountId");
        mAccountName = object.optString("accountName");
        mIssuerName = object.optString("issuerName");
        mProgramName = object.optString("programName");
        mIsProduction = object.optBoolean("isProduction");
        mBarcode = object.optString("barcode");

        LoyaltyWalletObject wob = generateLoyaltyWalletObject();
        CreateWalletObjectsRequest request = CreateWalletObjectsRequest
                .newBuilder()
                .setLoyaltyWalletObject(wob)
                .build(); //new CreateWalletObjectsRequest(wob);
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setTheme(WalletConstants.THEME_LIGHT)
                .setEnvironment(mIsProduction ? WalletConstants.ENVIRONMENT_PRODUCTION : WalletConstants.ENVIRONMENT_TEST)
                .build();
        WalletObjectsClient walletObjectsClient = Wallet.getWalletObjectsClient(activity, walletOptions);
        Task<AutoResolvableVoidResult> task = walletObjectsClient.createWalletObjects(request);
        AutoResolveHelper.resolveTask(task, activity, SAVE_TO_ANDROID);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("saveToGooglePay".equals(action)) {
            mCallbackContext = callbackContext;
            Intent intent = new Intent(cordova.getActivity(), GooglePayActivity.class);
            intent.putExtra(GooglePayActivity.class.getName(),args.toString());
            cordova.getActivity().startActivity(intent);
            return true;
        }else if("saveJWT".equals(action)){
            JSONObject object = args.getJSONObject(0);
            String url = ""+object.getString("jwt");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse( url ));
            cordova.getActivity().startActivity( i );
	    callbackContext.success();
            return true;
        }
        return false;
    }
}
