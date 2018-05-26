package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
* Created by MY-HOMEDESKTOP on 5/9/2017.
*/
class ServerManager extends Handler {

    @Override
    public void handleMessage(Message msg) {
        Log.e("error", msg.getData().getString("CONNECTION"));


        super.handleMessage(msg);
    }
}
