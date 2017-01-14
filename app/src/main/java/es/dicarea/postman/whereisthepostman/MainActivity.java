package es.dicarea.postman.whereisthepostman;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MainHelper mainHelper;
    private StatusHelper mStatusHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mStatusHelper = StatusHelper.getInstance();
//        mainHelper = MainHelper.getInstance();
//
//        TextView textView = (TextView) this.findViewById(R.id.textInfo);
//        textView.setText(mStatusHelper.getHistory());

        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000, pIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, 1000 * 5, pIntent);
    }

}
