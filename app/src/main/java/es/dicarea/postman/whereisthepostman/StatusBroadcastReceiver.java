package es.dicarea.postman.whereisthepostman;


import android.content.Context;
import android.content.Intent;

public class StatusBroadcastReceiver extends android.content.BroadcastReceiver {

    public static final String ACTION_ALARM_RECEIVER = "action_alarm_receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        new StatusReqHelper().executeRequest();
    }

}
