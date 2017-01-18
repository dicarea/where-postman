package es.dicarea.postman.whereisthepostman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String code = (String) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                intent.putExtra("TRACKING_ID", code);
                startActivity(intent);
            }
        });

        DataSource ds = DataSource.getInstance();
        List<BeanRepository.TrackingItem> trackingList = ds.getTrackingList();
        List<String> listCodes = new ArrayList<>(trackingList.size());
        for (BeanRepository.TrackingItem trackingItem : trackingList) {
            listCodes.add(trackingItem.getCode());
        }
        String[] codes = listCodes.toArray(new String[listCodes.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
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

}
