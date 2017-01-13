package es.dicarea.postman.whereisthepostman;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatusHelper {

    private static StatusHelper instance;

    private List<String> history;

    private StatusHelper() {
        history = new ArrayList<String>();
    }

    public static StatusHelper getInstance() {
        if (instance == null) {
            instance = new StatusHelper();
        }
        return instance;
    }

    public void add(StatusEnum status) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        String now = dateFormat.format(System.currentTimeMillis());
        String line = now + " -> " + status.getName();
        history.add(line);
    }

    public String getHistory() {
        StringBuilder sb = new StringBuilder();
        for (String s : history) {
            sb.insert(0, s + "\n");
        }
        return sb.toString();
    }

}
