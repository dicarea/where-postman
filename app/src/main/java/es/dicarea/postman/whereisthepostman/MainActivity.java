package es.dicarea.postman.whereisthepostman;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String CODE = "PQ4F6P0703142520133205G";
    private static final String URL = "http://aplicacionesweb.correos.es/localizadorenvios/track.asp?accion=LocalizaUno&numero=";
    private static final int MINUTES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Timer timer = new Timer();
        timer.schedule(new SayHello(), 0, MINUTES * 60 * 1000);
    }

    class SayHello extends TimerTask {
        public void run() {
            new GetURLAsyncTask().execute(URL + CODE);
        }
    }

}
