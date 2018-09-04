package org.apache.cordova.plugin;

import android.app.Activity;
import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.wallet.AutoResolvableVoidResult;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.CreateWalletObjectsRequest;
import com.google.android.gms.wallet.LoyaltyWalletObject;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.WalletObjectsClient;
import com.google.android.gms.wallet.wobs.WalletObjectsConstants;

public class GooglePay extends CordovaPlugin {

    private static final int SAVE_TO_ANDROID = 888;

    private String ISSUER_ID;
    private String LOYALTY_CLASS_ID;
    private String LOYALTY_OBJECT_ID;

    private String mAccountId = "";
    private String mAccountName = "";
    private String mIssuerName = "";
    private String mProgramName = "";

    private CallbackContext mCallbackContext;

    private LoyaltyWalletObject generateLoyaltyWalletObject() {
        LoyaltyWalletObject wob = LoyaltyWalletObject.newBuilder()
                .setClassId(ISSUER_ID + "." + LOYALTY_CLASS_ID)
                .setId(ISSUER_ID + "." + LOYALTY_OBJECT_ID)
                .setState(WalletObjectsConstants.State.ACTIVE)
                .setAccountId(mAccountId)
                .setAccountName(mAccountName)
                .setIssuerName(mIssuerName)
                .setProgramName(mProgramName)
                .build();
        return wob;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("saveToGooglePay".equals(action)) {

            this.mCallbackContext = callbackContext;

            ISSUER_ID = args.optString(0);
            LOYALTY_CLASS_ID = args.optString(1);
            LOYALTY_OBJECT_ID = args.optString(2);

            mAccountId = args.optString(3);
            mAccountName = args.optString(4);
            mIssuerName = args.optString(5);
            mProgramName = args.optString(6);

            LoyaltyWalletObject wob = generateLoyaltyWalletObject();
            CreateWalletObjectsRequest request = new CreateWalletObjectsRequest(wob);
            Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                    .setTheme(WalletConstants.THEME_DARK)
                    .setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION)
                    .build();

            WalletObjectsClient walletObjectsClient = Wallet.getWalletObjectsClient(this, walletOptions);
            Task<AutoResolvableVoidResult> task = walletObjectsClient.createWalletObjects(request);
            AutoResolveHelper.resolveTask(task, this, SAVE_TO_ANDROID);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case SAVE_TO_ANDROID:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mCallbackContext.success("SUCCESS");
                        break;
                    case Activity.RESULT_CANCELED:
                        mCallbackContext.error("CANCELED");
                        break;
                    default:
                        int errorCode =
                                intent.getIntExtra(
                                        WalletConstants.EXTRA_ERROR_CODE, -1);
                        mCallbackContext.error("ERROR CODE: " + errorCode);
                        break;
                }
        }
    }
}
