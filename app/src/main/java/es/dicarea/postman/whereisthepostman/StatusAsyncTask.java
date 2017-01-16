package es.dicarea.postman.whereisthepostman;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.db.DataSource;
import es.dicarea.postman.whereisthepostman.db.Log;

import static es.dicarea.postman.whereisthepostman.StatusEnum.ADMITIDO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.ENTREGADO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.EN_ENTREGA;
import static es.dicarea.postman.whereisthepostman.StatusEnum.NO_DEFINIDO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.PRE_REGISTRADO;

public class StatusAsyncTask extends AsyncTask<String, Void, List<StatusItem>> {

    private static final String URL = "http://aplicacionesweb.correos.es/localizadorenvios/track.asp?accion=LocalizaUno&numero=";
    private long timeNow;


    public StatusAsyncTask(long time) {
        timeNow = time;
    }

    @Override
    protected List<StatusItem> doInBackground(String... strings) {

        List<StatusItem> notifyList = new ArrayList<>();

        for (String code : strings) {
            StatusEnum status = httpRequest(code);
            storeLog(status);
            if (checkNotifyRequired(status)) {
                StatusItem statusItem = new StatusItem();
                statusItem.setCode(code);
                statusItem.setTime(timeNow);
                statusItem.setStatus(status);
                notifyList.add(statusItem);
            }
        }

        return notifyList;
    }

    @Override
    protected void onPostExecute(List<StatusItem> result) {
        for (StatusItem statusItem : result) {
            sendNotification(statusItem);
        }
    }

    private void storeLog(StatusEnum statusEnum) {
        DataSource dataSource = DataSource.getInstance();
        Log log = new Log(timeNow, statusEnum.getOrder());
        dataSource.addLog(log);
    }

    private StatusEnum httpRequest(String code) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(URL + code);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String serverResponse = readStream(urlConnection.getInputStream());
                return getStatus(serverResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            return NO_DEFINIDO;
        }
    }

    public boolean checkNotifyRequired(StatusEnum status) {
        DataSource ds = DataSource.getInstance();
        Integer lastStatus = ds.getLastStatus();
        return lastStatus == null || lastStatus < status.getOrder();
    }

    public void sendNotification(StatusItem statusItem) {

        Context context = CustomApp.getContext();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String now = dateFormat.format(statusItem.getTime());
        String line = statusItem.getCode() + " " + now + " " + statusItem.getStatus().getName();

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

    private StatusEnum findStatus() {
        Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
        Elements newsHeadlines = doc.select("#mp-itn b a");
        return null;
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