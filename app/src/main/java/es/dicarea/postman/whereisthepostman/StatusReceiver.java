package es.dicarea.postman.whereisthepostman;

import android.content.Context;
import android.content.Intent;

public class StatusReceiver extends android.content.BroadcastReceiver {

    public static final String ACTION_ALARM_RECEIVER = "action_alarm_receiver";
    private static final String[] CODES = {"PQ4F6P0703142520133205G", "UX492F0400139660133205V"};

    @Override
    public void onReceive(Context context, Intent intent) {
        new StatusAsyncTask(System.currentTimeMillis()).execute(CODES);
    }

}
