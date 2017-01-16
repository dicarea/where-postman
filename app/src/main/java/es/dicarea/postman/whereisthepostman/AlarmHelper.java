package es.dicarea.postman.whereisthepostman;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmHelper {

    private static final long INTERVAL = 5 * 60 * 1000;
    private static final int REQUEST_CODE = 1001;

    private Activity mActivity;
    private AlarmManager mAlarmManager;

    AlarmHelper(Activity activity) {
        mActivity = activity;
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    }

    private Activity getActivity() {
        return mActivity;
    }

    //**********************************

    private Intent createCommonIntent() {
        Intent intent = new Intent(getActivity(), StatusReceiver.class);
        intent.setAction(StatusReceiver.ACTION_ALARM_RECEIVER);
        return intent;
    }

    private PendingIntent crateCommonPendingIntent(Intent intent) {
        return PendingIntent.getBroadcast(getActivity(), REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    //**********************************

    public void startAlarm() {
        Intent intent = createCommonIntent();
        PendingIntent pendingIntent = crateCommonPendingIntent(intent);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pendingIntent);
    }

    public void stopAlarm() {
        Intent intent = createCommonIntent();
        PendingIntent pendingIntent = crateCommonPendingIntent(intent);
        mAlarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public boolean checkAlarm() {
        Intent intent = createCommonIntent();
        boolean isWorking = (PendingIntent.getBroadcast(getActivity(), REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null);
        return isWorking;
    }

}
