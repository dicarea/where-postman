package es.dicarea.postman.whereisthepostman;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class AlarmHelper {

    private static final long INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
    private static final int REQUEST_CODE = 1001;

    private Context mContext;
    private AlarmManager mAlarmManager;

    AlarmHelper(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    private Context getContext() {
        return mContext;
    }

    //**********************************

    private Intent createCommonIntent() {
        Intent intent = new Intent(getContext(), StatusReceiver.class);
        intent.setAction(StatusReceiver.ACTION_ALARM_RECEIVER);
        return intent;
    }

    private PendingIntent crateCommonPendingIntent(Intent intent) {
        return PendingIntent.getBroadcast(getContext(), REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    //**********************************

    public void startAlarm() {
        Intent intent = createCommonIntent();
        PendingIntent pendingIntent = crateCommonPendingIntent(intent);
        mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                INTERVAL, pendingIntent);
    }

    public void stopAlarm() {
        Intent intent = createCommonIntent();
        PendingIntent pendingIntent = crateCommonPendingIntent(intent);
        mAlarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public boolean checkAlarm() {
        Intent intent = createCommonIntent();
        return PendingIntent.getBroadcast(getContext(), REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

}
