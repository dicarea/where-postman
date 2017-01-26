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
import java.text.ParseException;
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
            Document doc = null;
            try {
                doc = Jsoup.connect(URL + tracking.getCode()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            StatusCorreosEnum status = findStatus(doc);
            Long timeLast = findTime(doc);
            if (checkNotifyRequired(status, tracking.getId())) {
                StatusItem statusItem = new StatusItem();
                statusItem.setTracking(tracking);
                statusItem.setTime(timeNow);
                statusItem.setStatus(status);
                statusItem.setTimeLastStatus(timeLast);
                notifyList.add(statusItem);
            }
            storeStatus(status, tracking.getId(), timeLast);
        }

        return notifyList;
    }

    @Override
    protected void onPostExecute(List<StatusItem> result) {
        for (StatusItem statusItem : result) {
            sendNotification(statusItem);
        }
    }

    private void storeStatus(StatusCorreosEnum statusEnum, Integer trackingId, Long timeStatus) {
        DataSource dataSource = DataSource.getInstance();
        StatusItem statusItem = new StatusItem();
        statusItem.setTime(timeNow);
        statusItem.setStatus(statusEnum);
        statusItem.setTimeLastStatus(timeStatus);
        TrackingItem tracking = new TrackingItem();
        tracking.setId(trackingId);
        statusItem.setTracking(tracking);
        dataSource.addStatus(statusItem);
    }

    public boolean checkNotifyRequired(StatusCorreosEnum status, Integer trackingId) {
        StatusItem statusItem = DataSource.getInstance().getLastValidStatus(trackingId);
        /* Only notification if status changes to a higher one. */
        return statusItem == null || statusItem.getStatus().getOrder() < status.getOrder();
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

    private StatusCorreosEnum findStatus(Document doc) {
        Elements elements = doc.select("span.txtNormal");
        if (elements != null && elements.size() > 0) {
            String statusStr = elements.get(elements.size() - 1).text().trim();
            return StatusCorreosEnum.getStatus(statusStr);
        }
        return StatusCorreosEnum.NO_DEFINIDO;
    }

    private Long findTime(Document doc) {
        Elements elements = doc.select("td.txtDescripcionTabla");
        if (elements != null && elements.size() > 0) {
            try {
                String value = elements.get(elements.size() - 1).text().trim();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return Long.valueOf(sdf.parse(value).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}