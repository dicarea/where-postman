package es.dicarea.postman.whereisthepostman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.db.DataSource;

public class LogActivity extends AppCompatActivity {

    private TrackingItem tracking;
    private final DataSource ds = DataSource.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        tracking = ds.getTrackingById(getIntent().getIntExtra("TRACKING_ID", 0));

        this.setTitle(tracking.getCode());

        String text = getTextViewContent(tracking.getId());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_log_delete:
                ds.deleteStatusByTracking(tracking.getId());
                ds.deleteTracking(tracking);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_menu, menu);
        return true;
    }

    //***********************************

    private String getTextViewContent(Integer trackingId) {
        DataSource ds = DataSource.getInstance();
        List<BeanRepository.StatusItem> statusList = ds.getStatusList(trackingId);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        for (BeanRepository.StatusItem statusItem : statusList) {
            String dateStr = sdf.format(statusItem.getTime());
            String statusName = statusItem.getStatus().getName();
            sb.append(dateStr + " - " + statusName + "\n");
        }

        return sb.toString();
    }

}
