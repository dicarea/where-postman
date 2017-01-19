package es.dicarea.postman.whereisthepostman;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.db.DataSource;

public class StatusAsyncTask extends AsyncTask<TrackingItem, Void, List<StatusItem>> {

    private static final String URL = "http://aplicacionesweb.correos.es/localizadorenvios/track.asp?accion=LocalizaUno&numero=";
    private long timeNow;


    public StatusAsyncTask(long time) {
        timeNow = time;
    }

    @Override
    protected List<StatusItem> doInBackground(TrackingItem... items) {

        List<StatusItem> notifyList = new ArrayList<>();

        for (TrackingItem tracking : items) {
            StatusEnum status = findStatus(tracking);
            if (checkNotifyRequired(status, tracking.getId())) {
                StatusItem statusItem = new StatusItem();
                statusItem.setTracking(tracking);
                statusItem.setTime(timeNow);
                statusItem.setStatus(status);
                notifyList.add(statusItem);
            }
            storeStatus(status, tracking.getId());
        }

        return notifyList;
    }

    @Override
    protected void onPostExecute(List<StatusItem> result) {
        for (StatusItem statusItem : result) {
            sendNotification(statusItem);
        }
    }

    private void storeStatus(StatusEnum statusEnum, Integer trackingId) {
        DataSource dataSource = DataSource.getInstance();
        StatusItem statusItem = new StatusItem();
        statusItem.setTime(timeNow);
        statusItem.setStatus(statusEnum);
        TrackingItem tracking = new TrackingItem();
        tracking.setId(trackingId);
        statusItem.setTracking(tracking);
        dataSource.addStatus(statusItem);
    }

    public boolean checkNotifyRequired(StatusEnum status, Integer trackingId) {
        DataSource ds = DataSource.getInstance();
        StatusEnum lastStatus = ds.getMaxStatus(trackingId);
        /* Only notification if status changes to a higher one. */
        return lastStatus == null || lastStatus.getOrder() < status.getOrder();
    }

    public void sendNotification(StatusItem statusItem) {

        Context context = CustomApp.getContext();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String now = dateFormat.format(statusItem.getTime());
        String line = now + " -> " + statusItem.getStatus().getName();

        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);

        Notification myNotification = new NotificationCompat.Builder(context)
                .setContentTitle(statusItem.getTracking().getCode())
                .setContentText(line)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(statusItem.getStatus().getOrder(), myNotification);
    }

    private StatusEnum findStatus(BeanRepository.TrackingItem tracking) {
        try {
            Document doc = Jsoup.connect(URL + tracking.getCode()).get();
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