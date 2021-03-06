package mnz.creatori.converter.network;

import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import mnz.creatori.converter.Entity.Valute;
import mnz.creatori.converter.parse.Parser;


//осуществляет работу с сетью, загрузку данных
public class NetworkHelper {

    private final String SOURCE_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private String content;
    private List<Valute> valutes;
    private List<String> valNames;
    private Context context;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    public List<Valute> getValutes() {
        //Метод должен гарантированно возвращать не null!

        DataLoader dataLoader = new DataLoader();
        dataLoader.execute(SOURCE_URL);
        try {
            content = dataLoader.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        valutes = new Parser().parse(content);

        return valutes;
    }

    public List<String> getValuteNames() {
        valNames = new ArrayList<>();
        valutes = getValutes();

        for (int i = 0; i < valutes.size(); i++) {
            valNames.add(valutes.get(i).getName());
        }

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

                return (answer);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

    }
}
