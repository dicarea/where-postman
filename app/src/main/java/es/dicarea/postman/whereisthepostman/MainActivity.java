package es.dicarea.postman.whereisthepostman;

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
        List<Log> listLogs = ds.getLogs();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (Log log : listLogs) {
            String dateStr = sdf.format(log.getDate());
            String statusName = StatusEnum.getStatus(log.getStatus()).getName();
            sb.append(dateStr + " -> " + statusName + "\n");
        }

        return sb.toString();
    }

}
