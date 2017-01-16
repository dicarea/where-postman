package es.dicarea.postman.whereisthepostman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import es.dicarea.postman.whereisthepostman.db.DataSource;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        String text = getTextViewContent();
        TextView textView = (TextView) this.findViewById(R.id.textInfo);
        textView.setText(text);
    }

    private String getTextViewContent() {
        DataSource ds = DataSource.getInstance();
        List<StatusItem> statusList = ds.getStatusList();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (StatusItem statusItem : statusList) {
            String dateStr = sdf.format(statusItem.getTime());
            String statusName = statusItem.getStatus().getName();
            sb.append(statusItem.getCode() + "\n\t\t" + dateStr + " - " + statusName + "\n");
        }

        return sb.toString();
    }

}
