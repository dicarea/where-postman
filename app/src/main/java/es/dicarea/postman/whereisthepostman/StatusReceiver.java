package es.dicarea.postman.whereisthepostman;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.db.DataSource;

public class StatusReceiver extends android.content.BroadcastReceiver {

    public static final String ACTION_ALARM_RECEIVER = "action_alarm_receiver";
    private static final String[] CODES = {"PQ4F6P0703142520133205G", "UX492F0400139660133205V"};

    @Override
    public void onReceive(Context context, Intent intent) {
        String[] codes = getCodes();
        Long timeNow = System.currentTimeMillis();
        new StatusAsyncTask(timeNow).execute(codes);
    }

    private String[] getCodes() {
        DataSource ds = DataSource.getInstance();
        List<BeanRepository.TrackingItem> trackingList = ds.getTrackingList();
        List<String> codesList = new ArrayList<>(trackingList.size());
        for (BeanRepository.TrackingItem trackingItem : trackingList) {
            codesList.add(trackingItem.getCode());
        }
        return codesList.toArray(new String[codesList.size()]);
    }

}
