package mnz.creatori.converter.network;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import mnz.creatori.converter.Entity.Valute;

import static android.R.attr.path;
import static android.content.ContentValues.TAG;


//осуществляет работу с сетью, загрузку данных
public class NetworkHelper{

    private final String SOURCE_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final String TAG = "MyLogs";
    private String content;





    public ArrayList<String> getValuteNames() {
        //Метод должен гарантированно возвращать не null!

        DataLoader dataLoader = new DataLoader();
        dataLoader.execute(SOURCE_URL);
        try {
            content = dataLoader.get();
            Log.d(TAG, content);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        ArrayList<String> valNames = new ArrayList<>();
        valNames.add("none");
        return valNames;
    }








    //Осуществляет загрузку данных с сайта
    private class DataLoader extends AsyncTask<String, Void, String>{

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


//                String temp = c.getContentEncoding();
//                Log.d(TAG, "Encoding: " + temp);


//                String contentType = c.getHeaderField("Content-Type");
//                Log.d(TAG, "Content type: " + contentType);


                reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "windows-1251"));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }

                String answer = buf.toString();


//                Log.d(TAG, "String get: " + answer);
                return (answer);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

    }

    //осуществляет парсинг XML
    public class XMLHelper{
    }
}
