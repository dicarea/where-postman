package es.dicarea.postman.whereisthepostman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import es.dicarea.postman.whereisthepostman.db.DataSource;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Intent intent = getIntent();
        Integer trackingId = intent.getIntExtra("TRACKING_ID", 0);
        String trackingCode = intent.getStringExtra("TRACKING_CODE");

        this.setTitle(trackingCode);

        String text = getTextViewContent(trackingId);
        TextView textView = (TextView) this.findViewById(R.id.textInfo);
        textView.setText(text);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //***********************************

    private String getTextViewContent(Integer trackingId) {
        DataSource ds = DataSource.getInstance();
        List<BeanRepository.StatusItem> statusList = ds.getStatusList(trackingId);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (BeanRepository.StatusItem statusItem : statusList) {
            String dateStr = sdf.format(statusItem.getTime());
            String statusName = statusItem.getStatus().getName();
            sb.append(dateStr + " - " + statusName + "\n");
        }

        return sb.toString();
    }

}
