package es.dicarea.postman.whereisthepostman;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

public class MyReceiver extends android.content.BroadcastReceiver {

    private static final String STATUS_FILENAME = "laststatus.info";
    private static final String LOG_FILENAME = "log.info";
    private static final String CODE = "PQ4F6P0703142520133205G";
    private static final String URL = "http://aplicacionesweb.correos.es/localizadorenvios/track.asp?accion=LocalizaUno&numero=";

    @Override
    public void onReceive(Context context, Intent intent) {

        GetURLAsyncTask getURLAsyncTask = new GetURLAsyncTask();
        GetURLAsyncTask asyncTask = (GetURLAsyncTask) getURLAsyncTask.execute(URL + CODE);

        StatusEnum status = null;
        try {
            status = asyncTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            checkChange(context, status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    private void saveStatus(Context context, StatusEnum status) throws IOException {

        FileOutputStream fos = context.openFileOutput(STATUS_FILENAME, Context.MODE_PRIVATE);
        fos.write(String.valueOf(status.getOrder()).getBytes());
        fos.close();
    }

    private int readStatus(Context context) throws IOException {

        FileInputStream in = context.openFileInput(STATUS_FILENAME);
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        bufferedReader.close();
        inputStreamReader.close();
        in.close();

        return Integer.parseInt(line);
    }

    public void checkChange(Context context, StatusEnum status) throws IOException {

        if (!fileExists(context, STATUS_FILENAME)) {
            saveStatus(context, status);
            sendNotification(context, status);
        } else {
            int lastStatus = readStatus(context);
            if (lastStatus < status.getOrder()) {
                saveStatus(context, status);
                sendNotification(context, status);
            }
        }
    }

    public void sendNotification(Context context, StatusEnum status) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String now = dateFormat.format(System.currentTimeMillis());
        String line = now + " " + status.getName();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context).setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("Correos").setContentText(line);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }

}
