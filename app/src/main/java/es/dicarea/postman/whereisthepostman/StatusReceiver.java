package es.dicarea.postman.whereisthepostman;

import android.content.Context;
import android.content.Intent;

public class StatusReceiver extends android.content.BroadcastReceiver {

    public static final String ACTION_ALARM_RECEIVER = "action_alarm_receiver";
    private static final String CODE = "PQ4F6P0703142520133205G";
    private static final String URL = "http://aplicacionesweb.correos.es/localizadorenvios/track.asp?accion=LocalizaUno&numero=";

    @Override
    public void onReceive(Context context, Intent intent) {
        new StatusAsyncTask(System.currentTimeMillis()).execute(URL + CODE);
    }

}
