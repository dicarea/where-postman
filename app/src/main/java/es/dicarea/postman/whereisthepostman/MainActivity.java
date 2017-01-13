package es.dicarea.postman.whereisthepostman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MainHelper mainHelper;
    private StatusHelper mStatusHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mStatusHelper = StatusHelper.getInstance();
        mainHelper = MainHelper.getInstance();

        TextView textView = (TextView) this.findViewById(R.id.textInfo);
        textView.setText(mStatusHelper.getHistory());

        mainHelper.setActivity(this);
        mainHelper.startTimer();
    }
}
