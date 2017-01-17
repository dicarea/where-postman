package es.dicarea.postman.whereisthepostman;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.StatusItem;
import es.dicarea.postman.whereisthepostman.db.DataSource;

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
            StatusEnum status = findStatus(code);
            if (checkNotifyRequired(status, code)) {
                StatusItem statusItem = new StatusItem();
                statusItem.setCode(code);
                statusItem.setTime(timeNow);
                statusItem.setStatus(status);
                notifyList.add(statusItem);
            }
            storeStatus(status, code);
        }

        return notifyList;
    }

    @Override
    protected void onPostExecute(List<StatusItem> result) {
        for (StatusItem statusItem : result) {
            sendNotification(statusItem);
        }
    }

    private void storeStatus(StatusEnum statusEnum, String code) {
        DataSource dataSource = DataSource.getInstance();
        StatusItem statusItem = new StatusItem();
        statusItem.setTime(timeNow);
        statusItem.setStatus(statusEnum);
        statusItem.setCode(code);
        dataSource.addStatus(statusItem);
    }

    public boolean checkNotifyRequired(StatusEnum status, String code) {
        DataSource ds = DataSource.getInstance();
        StatusEnum lastStatus = ds.getMaxStatus(code);
        /* Only notification if status changes to a higher one. */
        return lastStatus == null || lastStatus.getOrder() < status.getOrder();
    }

    public void sendNotification(StatusItem statusItem) {

        Context context = CustomApp.getContext();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String now = dateFormat.format(statusItem.getTime());
        String line = now + " -> " + statusItem.getStatus().getName();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context).setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle(statusItem.getCode()).setContentText(line).setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(statusItem.getStatus().getOrder(), mBuilder.build());
    }

    private StatusEnum findStatus(String code) {
        try {
            Document doc = Jsoup.connect(URL + code).get();
            Elements elements = doc.select("span.txtNormal");
            if (elements != null && elements.size() > 0) {
                String statusStr = elements.get(elements.size() - 1).text().trim();
                return StatusEnum.getStatus(statusStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StatusEnum.NO_DEFINIDO;
    }

}