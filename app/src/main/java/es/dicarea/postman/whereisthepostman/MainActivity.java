package es.dicarea.postman.whereisthepostman;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 60 * 1000, pIntent);
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
