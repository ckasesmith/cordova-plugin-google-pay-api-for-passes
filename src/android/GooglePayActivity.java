package org.apache.cordova.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.wallet.WalletConstants;

import org.apache.cordova.CordovaActivity;
import org.json.JSONException;

public class GooglePayActivity extends CordovaActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            GooglePay.getInstance().setSaveToAndroid(this);
        } catch (JSONException e) {
            GooglePay.getInstance().getCallbackContext().error("ERROR CODE: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 888:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        GooglePay.getInstance().getCallbackContext().success("SUCCESS");
                        finish();
                        break;
                    case Activity.RESULT_CANCELED:
                        GooglePay.getInstance().getCallbackContext().error("CANCELED");
                        finish();
                        break;
                    default:
                        int errorCode =
                                intent.getIntExtra(
                                        WalletConstants.EXTRA_ERROR_CODE, -1);
                        GooglePay.getInstance().getCallbackContext().error("ERROR: " + errorCode);
                        finish();
                        break;
                }
        }
    }
}
