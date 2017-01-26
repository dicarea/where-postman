package es.dicarea.postman.whereisthepostman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.TrackingItem;
import es.dicarea.postman.whereisthepostman.db.DataSource;

public class LogActivity extends AppCompatActivity {

    private final DataSource ds = DataSource.getInstance();

    private TrackingItem tracking;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tracking = ds.getTrackingById(getIntent().getIntExtra("TRACKING_ID", 0));

        textView = (TextView) this.findViewById(R.id.textInfo);

        updateTitle(this, tracking);
    }

    @Override
    protected void onStart() {
        super.onStart();

        refreshLog();
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
                showDeleteDialog();
                return true;
            case R.id.menu_log_clear:
                ds.deleteStatusByTracking(tracking.getId());
                refreshLog();
                return true;
            case R.id.menu_log_edit:
                showEditDialog();
                return true;
            case R.id.menu_log_force_request:
                forceStatusReq();
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

    private void showDeleteDialog() {
        AlertDialog deleteDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(R.string.delete_ask)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ds.deleteStatusByTracking(tracking.getId());
                        ds.deleteTracking(tracking);
                        dialog.dismiss();
                        LogActivity.this.finish();
                    }
                }).setNegativeButton(R.string.cancel, null)
                .create();
        deleteDialog.show();
    }

    private void showEditDialog() {

        final View updateLayout = getLayoutInflater().inflate(R.layout.modal_edit, null);
        final EditText codeDialog = (EditText) updateLayout.findViewById(R.id.modal_tracking);
        codeDialog.setText(tracking.getCode());
        final EditText descDialog = (EditText) updateLayout.findViewById(R.id.modal_desc);
        descDialog.setText(tracking.getDesc());

        AlertDialog editDialog = new AlertDialog.Builder(this)
                .setView(updateLayout)
                .setTitle(R.string.update_tracking)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DataSource ds = DataSource.getInstance();
                        tracking.setCode(codeDialog.getText().toString());
                        tracking.setDesc(descDialog.getText().toString());
                        ds.updateTracking(tracking);
                        updateTitle(LogActivity.this, tracking);
                    }
                }).setNegativeButton(R.string.cancel, null)
                .create();

        editDialog.show();
    }

    private void forceStatusReq() {
        new StatusReqHelper().executeRequestSync(tracking);
        refreshLog();
    }

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

    private void refreshLog() {
        textView.setText(getTextViewContent(tracking.getId()));
    }

    private void updateTitle(Activity activity, TrackingItem tracking) {
        activity.setTitle(tracking.getCode());
    }
}
