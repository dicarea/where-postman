package es.dicarea.postman.whereisthepostman;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        updateData();
    }

    public void updateData() {
        this.setTitle(tracking.getCode());

        textView = (TextView) this.findViewById(R.id.textInfo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        textView.setText(getTextViewContent(tracking.getId()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private AlertDialog createDialog() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Warning !!")
                .setMessage("Delete tracking ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ds.deleteStatusByTracking(tracking.getId());
                        ds.deleteTracking(tracking);
                        dialog.dismiss();
                        LogActivity.this.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_log_delete:
                AlertDialog diaBox = createDialog();
                diaBox.show();
                return true;
            case R.id.menu_log_clear:
                ds.deleteStatusByTracking(tracking.getId());
                textView.setText(getTextViewContent(tracking.getId()));
                return true;
            case R.id.menu_log_edit:
                final Dialog commentDialog = new Dialog(this);
                commentDialog.setContentView(R.layout.modal_edit);
                commentDialog.setTitle("New Tracking");
                Button okBtn = (Button) commentDialog.findViewById(R.id.ok);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataSource ds = DataSource.getInstance();
                        EditText code = (EditText) commentDialog.findViewById(R.id.modal_tracking);
                        EditText desc = (EditText) commentDialog.findViewById(R.id.modal_desc);
                        tracking.setCode(code.getText().toString());
                        tracking.setDesc(desc.getText().toString());
                        ds.updateTracking(tracking);
                        updateData();
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
                EditText code = (EditText) commentDialog.findViewById(R.id.modal_tracking);
                EditText desc = (EditText) commentDialog.findViewById(R.id.modal_desc);
                code.setText(tracking.getCode());
                desc.setText(tracking.getDesc());
                commentDialog.show();
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
