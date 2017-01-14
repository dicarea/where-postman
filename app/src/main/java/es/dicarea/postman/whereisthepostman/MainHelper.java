package es.dicarea.postman.whereisthepostman;

import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

public class MainHelper {

    private static final String CODE = "PQ4F6P0703142520133205G";
    private static final String URL = "http://aplicacionesweb.correos.es/localizadorenvios/track.asp?accion=LocalizaUno&numero=";
    private static final int MINUTES = 5;

    private boolean hasStarted = false;
    private static MainHelper instance;
    private Activity activity;

    private MainHelper() {
    }

    public static MainHelper getInstance() {
        if (instance == null) {
            instance = new MainHelper();
        }
        return instance;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void startTimer() {
        if (!hasStarted) {
            Timer timer = new Timer();
            MyTimerTask timerTask = new MyTimerTask();
            timer.schedule(timerTask, 1000, MINUTES * 60 * 1000);
            hasStarted = true;
        }
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            new GetURLAsyncTask().execute(URL + CODE);
        }
    }

}
