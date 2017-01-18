package es.dicarea.postman.whereisthepostman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.dicarea.postman.whereisthepostman.BeanRepository.StatusItem;
import es.dicarea.postman.whereisthepostman.db.AndroidDatabaseManager;
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

        Button clickButton = (Button) findViewById(R.id.code_button);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText codeText = (EditText) findViewById(R.id.code);
                DataSource ds = DataSource.getInstance();
                BeanRepository.TrackingItem trackingItem = new BeanRepository.TrackingItem();
                trackingItem.setCode(codeText.getText().toString());
                ds.addTracking(trackingItem);
                codeText.getText().clear();
            }
        });

        ListView listView = (ListView) findViewById(R.id.tracking_list);
        DataSource ds = DataSource.getInstance();
        List<BeanRepository.TrackingItem> trackingList = ds.getTrackingList();
        List<String> listCodes = new ArrayList<>(trackingList.size());
        for (BeanRepository.TrackingItem trackingItem : trackingList) {
            listCodes.add(trackingItem.getCode());
        }
        String[] codes = listCodes.toArray(new String[listCodes.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listCodes);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        String text = getTextViewContent();
//        TextView textView = (TextView) this.findViewById(R.id.textInfo);
//        textView.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_tracking:
                startActivity(new Intent(MainActivity.this, AndroidDatabaseManager.class));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //***********************************

//    private String getTextViewContent() {
//        DataSource ds = DataSource.getInstance();
//        List<StatusItem> statusList = ds.getStatusList();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//
//        StringBuilder sb = new StringBuilder();
//        for (StatusItem statusItem : statusList) {
//            String dateStr = sdf.format(statusItem.getTime());
//            String statusName = statusItem.getStatus().getName();
//            sb.append(statusItem.getCode() + "\n\t\t" + dateStr + " - " + statusName + "\n");
//        }
//
//        return sb.toString();
//    }

}
