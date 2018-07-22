package com.smartpoint.fingerRecog;

import android.os.Handler;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.smartpoint.marquee.activity.FingerprintsRecogActivity;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class MyAuthCallback extends FingerprintManagerCompat.AuthenticationCallback {

    private Handler handler = null;

    public MyAuthCallback(Handler handler) {
        super();

        this.handler = handler;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);

        if (handler != null) {
            handler.obtainMessage(FingerprintsRecogActivity.MSG_AUTH_ERROR, errMsgId, 0).sendToTarget();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);

        if (handler != null) {
            handler.obtainMessage(FingerprintsRecogActivity.MSG_AUTH_HELP, helpMsgId, 0).sendToTarget();
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        try {
            result.getCryptoObject().getCipher().doFinal();

            if (handler != null) {
                handler.obtainMessage(FingerprintsRecogActivity.MSG_AUTH_SUCCESS).sendToTarget();
            }
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();

        if (handler != null) {
            handler.obtainMessage(FingerprintsRecogActivity.MSG_AUTH_FAILED).sendToTarget();
        }
    }
}
