package mnz.creatori.converter.network;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mnz.creatori.converter.Entity.Valute;
import mnz.creatori.converter.MainActivity;
import mnz.creatori.converter.databases.Database;
import mnz.creatori.converter.parse.Parser;


//осуществляет работу с сетью, загрузку данных
public class NetworkHelper {

    private final String SOURCE_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final String TAG = "MyLogs";
    private String content;
    private ArrayList<Valute> valutes;
    private ArrayList<String> valNames;
    private Context context;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    public ArrayList<Valute> getValutes() {
        //Метод должен гарантированно возвращать не null!

        DataLoader dataLoader = new DataLoader();
        dataLoader.execute(SOURCE_URL);
        try {
            content = dataLoader.get();
//            Log.d(TAG, content);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        valutes = new Parser().parse(content);

//        new Database(context).update(valutes);


        return valutes;
    }

    public ArrayList<String> getValuteNames() {
        valNames = new ArrayList<>();
//        valutes = new Parser().parse(content);
        valutes = getValutes();

        for (int i = 0; i < valutes.size(); i++) {
            valNames.add(valutes.get(i).getName());
        }

//        valNames.add("none");

        return valNames;
    }


    //Осуществляет загрузку данных с сайта
    private class DataLoader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String content;
            try {
                content = getContent(strings[0]);
            } catch (IOException ex) {
                content = ex.getMessage();
            }

            return content;
        }

        @Override
        protected void onPostExecute(String s) {


        }

        private String getContent(String path) throws IOException {
            BufferedReader reader = null;
            try {
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Accept-Charset", "UTF-8");
                c.setReadTimeout(10000);
                c.connect();


                reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "windows-1251"));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }

                String answer = buf.toString();


                Log.d(TAG, "String get: " + answer);
                return (answer);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

    }


}
