package mnz.creatori.converter.SharPref;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;

import mnz.creatori.converter.R;


public class SharPrefHelper {

    private Date currentDate;
    private long time;
    private SharedPreferences sPref;
    private Context context;

    public SharPrefHelper(Context context) {
        this.context = context;
        sPref = context.getSharedPreferences("settings.xml", Context.MODE_PRIVATE);

    }




    public void setUpdateDate() {

        currentDate = new Date(System.currentTimeMillis());
        time = System.currentTimeMillis();
        SharedPreferences.Editor editor = sPref.edit();
        editor.putLong("last_update", time);
        editor.apply();

    }

    public String  getUpdateInfo() {

        time = sPref.getLong("last_update", 0L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        String result = context.getString(R.string.data_updated) + "\n" +dateFormat.format(new java.util.Date(time));
        return result;
    }
}
