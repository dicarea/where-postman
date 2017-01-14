package es.dicarea.postman.whereisthepostman;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static es.dicarea.postman.whereisthepostman.StatusEnum.ADMITIDO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.ENTREGADO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.EN_ENTREGA;
import static es.dicarea.postman.whereisthepostman.StatusEnum.NO_DEFINIDO;
import static es.dicarea.postman.whereisthepostman.StatusEnum.PRE_REGISTRADO;

public class GetURLAsyncTask extends AsyncTask<String, Void, StatusEnum> {

    @Override
    protected StatusEnum doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String serverResponse = readStream(urlConnection.getInputStream());
                return getStatus(serverResponse);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return NO_DEFINIDO;
    }

    private StatusEnum getStatus(String fullHtml) {

        if (fullHtml.contains(ENTREGADO.getName())) {
            return ENTREGADO;
        } else if (fullHtml.contains(EN_ENTREGA.getName())) {
            return EN_ENTREGA;
        } else if (fullHtml.contains(ADMITIDO.getName())) {
            return ADMITIDO;
        } else if (fullHtml.contains(PRE_REGISTRADO.getName())) {
            return PRE_REGISTRADO;
        }

        return NO_DEFINIDO;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}