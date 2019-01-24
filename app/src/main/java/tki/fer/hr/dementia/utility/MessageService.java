package tki.fer.hr.dementia.utility;

import android.telephony.SmsManager;


/**
 * Created by lucija on 26.02.18..
 */

public class MessageService {
    private final static SmsManager smsManager = SmsManager.getDefault();

    public static void sendSMS(String phoneNo, String msg) {
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
    }
}
