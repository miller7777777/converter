package mnz.creatori.converter.databases;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import mnz.creatori.converter.Entity.Valute;
import mnz.creatori.converter.MainActivity;

import static android.R.attr.id;
import static android.R.attr.version;
import static android.R.string.no;
import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

public class Database {

    private DBHelper dbHelper;
    private Context context;
    private ArrayList<Valute> valutes;
    private ArrayList<String> valNames;

    public Database(Context context) {
        this.context = context;
    }

    public ArrayList<String> getValuteNames() {

        //Метод должен гарантированно возвращать не null!


        valNames = new ArrayList<>();

        valutes = getValutes();

        for (int i = 0; i < valutes.size(); i++) {
            valNames.add(valutes.get(i).getCharCode());
        }

        return valNames;
    }

    public void update(ArrayList<Valute> valuteNames) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        for (int i = 0; i < valuteNames.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put("id", valuteNames.get(i).getId());
            cv.put("numCode", valuteNames.get(i).getNumCode());
            cv.put("charCode", valuteNames.get(i).getCharCode());
            cv.put("nominal", valuteNames.get(i).getNominal());
            cv.put("name", valuteNames.get(i).getName());
            cv.put("value", valuteNames.get(i).getValue());

//            long rowID = db.update("valutes", cv, null, null);

            int isUpdated = db.update("valutes",
                    cv,
                    "charCode = ?",
                    new String[] {valuteNames.get(i).getCharCode()});

            Log.d("dbLogs", "isUpdated = " + isUpdated);






            if(isUpdated <= 0){


                long isInserted = db.insertWithOnConflict("valutes", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                Log.d("dbLogs", "isInserted = " + isInserted);

            }

        }

        db.setTransactionSuccessful();
        db.endTransaction();

        db.close();
        dbHelper.close();

    }

    public ArrayList<Valute> getValutes() {

        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        valutes = new ArrayList<Valute>();


        Cursor cursor = db.query("valutes", null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int idColNumCode = cursor.getColumnIndex("numCode");
            int idColCharCode = cursor.getColumnIndex("charCode");
            int idColNominal = cursor.getColumnIndex("nominal");
            int idColName = cursor.getColumnIndex("name");
            int idColValue = cursor.getColumnIndex("value");

            do{

                String id = cursor.getString(idColIndex);
                String numCode = cursor.getString(idColNumCode);
                String charCode = cursor.getString(idColCharCode);
                String nominal = cursor.getString(idColNominal);
                String name = cursor.getString(idColName);
                String value = cursor.getString(idColValue);

                Valute valute = new Valute(id, numCode, charCode, nominal, name, value);
                valutes.add(valute);

                Log.d("db_query", "Valute: " + id + " " + numCode + " " + charCode + " " + nominal + " " + name + " " + value);

            }while (cursor.moveToNext());
        }else {
            Log.d("db_query", "error reading from database");
        }

        Log.d("db_query", "Считано из базы: " + valutes.size());


        cursor.close();

        return valutes;
    }

    class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {
            super(context, "converterDB", null, 1);
        }



        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table valutes ("
                    + "id text,"
                    + "numCode text,"
                    + "charCode text,"
                    + "nominal text,"
                    + "name text,"
                    + "value text"
                    + ");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        }
    }
}
