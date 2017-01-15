package es.dicarea.postman.whereisthepostman;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import es.dicarea.postman.whereisthepostman.db.DataSource;
import es.dicarea.postman.whereisthepostman.db.Log;

import static es.dicarea.postman.whereisthepostman.StatusEnum.ADMITIDO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.ENTREGADO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.EN_ENTREGA;
import static es.dicarea.postman.whereisthepostman.StatusEnum.NO_DEFINIDO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.PRE_REGISTRADO;

public class GetURLAsyncTask extends AsyncTask<String, Void, StatusEnum> {

    private long lastTime;

    public GetURLAsyncTask(long time) {
        lastTime = time;
    }

    @Override
    protected StatusEnum doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String serverResponse = readStream(urlConnection.getInputStream());
                return getStatus(serverResponse);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return NO_DEFINIDO;
    }

    @Override
    protected void onPostExecute(StatusEnum statusEnum) {
        super.onPostExecute(statusEnum);

        /* Check if notification necessary. */
        try {
            checkChange(MyApp.getContext(), statusEnum);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Store the new request. */
        DataSource ds = DataSource.getInstance();
        Log log = new Log();
        log.setDate(lastTime);
        log.setStatus(statusEnum.getOrder());
        ds.addLog(log);
    }

    public void checkChange(Context context, StatusEnum status) throws IOException {

        DataSource ds = DataSource.getInstance();
        Integer lastStatus = ds.getLastStatus();

        if (lastStatus == null || lastStatus < status.getOrder()) {
            sendNotification(context, status);
        }

    }

    public void sendNotification(Context context, StatusEnum status) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String now = dateFormat.format(System.currentTimeMillis());
        String line = now + " " + status.getName();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context).setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("Correos").setContentText(line).setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }

    private StatusEnum getStatus(String fullHtml) {

        if (fullHtml.contains(ENTREGADO.getName())) {
            return ENTREGADO;
        } else if (fullHtml.contains(EN_ENTREGA.getName())) {
            return EN_ENTREGA;
        } else if (fullHtml.contains(ADMITIDO.getName())) {
            return ADMITIDO;
        } else if (fullHtml.contains(PRE_REGISTRADO.getName())) {
            return PRE_REGISTRADO;
        }

        return NO_DEFINIDO;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}