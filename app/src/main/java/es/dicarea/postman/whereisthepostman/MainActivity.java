package es.dicarea.postman.whereisthepostman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
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
                showNewDialog();
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

    //******************************

    private void showNewDialog() {

        final View updateLayout = getLayoutInflater().inflate(R.layout.modal_new, null);
        final EditText codeDialog = (EditText) updateLayout.findViewById(R.id.modal_tracking);
        final EditText descDialog = (EditText) updateLayout.findViewById(R.id.modal_desc);

        AlertDialog editDialog = new AlertDialog.Builder(this)
                .setView(updateLayout)
                .setTitle(R.string.new_tracking)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TrackingItem tracking = new TrackingItem();
                        tracking.setCode(codeDialog.getText().toString());
                        tracking.setDesc(descDialog.getText().toString());
                        DataSource.getInstance().addTracking(tracking);
                        refreshList();
                    }
                }).setNegativeButton(R.string.cancel, null)
                .create();

        editDialog.show();
    }

}
