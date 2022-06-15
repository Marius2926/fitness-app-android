package eu.unibuc.ro.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager extends AsyncTask<String, Void, String> {

    private HttpURLConnection conn;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder rez = new StringBuilder();

        try {
            URL url = new URL(strings[0]);
            conn = (HttpURLConnection) url.openConnection();
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                rez.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.disconnect();
        }


        return rez.toString();
    }
}



