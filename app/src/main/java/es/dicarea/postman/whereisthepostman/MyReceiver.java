package es.dicarea.postman.whereisthepostman;

import android.content.Context;
import android.content.Intent;

public class MyReceiver extends android.content.BroadcastReceiver {

    private static final String STATUS_FILENAME = "laststatus.info";
    private static final String LOG_FILENAME = "log.info";
    private static final String CODE = "PQ4F6P0703142520133205G";
    private static final String URL = "http://aplicacionesweb.correos.es/localizadorenvios/track.asp?accion=LocalizaUno&numero=";

    @Override
    public void onReceive(Context context, Intent intent) {

        GetURLAsyncTask getURLAsyncTask = new GetURLAsyncTask(System.currentTimeMillis());
        GetURLAsyncTask asyncTask = (GetURLAsyncTask) getURLAsyncTask.execute(URL + CODE);
    }


}
