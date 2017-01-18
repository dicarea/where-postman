package es.dicarea.postman.whereisthepostman;


import android.content.Context;
import android.content.Intent;

import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.db.DataSource;

public class StatusReceiver extends android.content.BroadcastReceiver {

    public static final String ACTION_ALARM_RECEIVER = "action_alarm_receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Long timeNow = System.currentTimeMillis();
        new StatusAsyncTask(timeNow).execute(getCodes());
    }

    private TrackingItem[] getCodes() {
        DataSource ds = DataSource.getInstance();
        List<TrackingItem> trackingList = ds.getTrackingList();
        return trackingList.toArray(new TrackingItem[trackingList.size()]);
    }

}
