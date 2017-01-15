package es.dicarea.postman.whereisthepostman;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import es.dicarea.postman.whereisthepostman.db.DataSource;
import es.dicarea.postman.whereisthepostman.db.Log;

public class MainActivity extends AppCompatActivity {

    private static final long INTERVAL = 5 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkAlarm()) {
            startAlarm();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopAlarm();
    }

    private Activity getActivity() {
        return this;
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), MyReceiver.class);
        intent.setAction(MyReceiver.ACTION_ALARM_RECEIVER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1001, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pendingIntent);
    }

    private void stopAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), MyReceiver.class);
        intent.setAction(MyReceiver.ACTION_ALARM_RECEIVER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1001, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private boolean checkAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), MyReceiver.class);
        intent.setAction(MyReceiver.ACTION_ALARM_RECEIVER);
        boolean isWorking = (PendingIntent.getBroadcast(getActivity(), 1001, intent, PendingIntent.FLAG_NO_CREATE) != null);
        return isWorking;
    }

    @Override
    protected void onResume() {
        super.onResume();

        DataSource ds = DataSource.getInstance();
        List<Log> listLogs = ds.getLogs();

        TextView textView = (TextView) this.findViewById(R.id.textInfo);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (Log log : listLogs) {
            String dateStr = sdf.format(log.getDate());
            String statusName = StatusEnum.getStatusName(log.getStatus()).getName();
            sb.append(dateStr + " -> " + statusName + "\n");
        }

        textView.setText(sb.toString());
    }

}
