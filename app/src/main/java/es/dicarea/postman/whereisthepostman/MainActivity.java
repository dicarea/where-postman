package es.dicarea.postman.whereisthepostman;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.db.AndroidDatabaseManager;
import es.dicarea.postman.whereisthepostman.db.DataSource;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView = (ListView) findViewById(R.id.tracking_list);

        refreshList();
    }

    private void refreshList() {
        listView = (ListView) findViewById(R.id.tracking_list);
        DataSource ds = DataSource.getInstance();
        List<TrackingItem> trackingList = ds.getTrackingList();
        TrackingAdapter adapter = new TrackingAdapter(this, trackingList);
        listView.setAdapter(adapter);
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
            case R.id.action_add:
                final Dialog commentDialog = new Dialog(this);
                commentDialog.setContentView(R.layout.modal_new);
                commentDialog.setTitle("New Tracking");
                Button okBtn = (Button) commentDialog.findViewById(R.id.ok);
                okBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        DataSource ds = DataSource.getInstance();
                        TrackingItem trackingItem = new TrackingItem();
                        EditText code = (EditText) commentDialog.findViewById(R.id.modal_tracking);
                        EditText desc = (EditText) commentDialog.findViewById(R.id.modal_desc);
                        trackingItem.setCode(code.getText().toString());
                        trackingItem.setDesc(desc.getText().toString());
                        ds.addTracking(trackingItem);
                        refreshList();
                        commentDialog.dismiss();
                    }
                });
                Button cancelBtn = (Button) commentDialog.findViewById(R.id.cancel);
                cancelBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        commentDialog.dismiss();
                    }
                });
                commentDialog.show();
                return true;
            case R.id.action_tracking:
                startActivity(new Intent(MainActivity.this, AndroidDatabaseManager.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

}
