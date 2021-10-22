package com.example.schedulemonitoring.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.schedulemonitoring.Model.ModelOTSched;
import com.example.schedulemonitoring.Model.ModelSettings;

import java.util.ArrayList;
import java.util.List;

import static com.example.schedulemonitoring.fragments.FragOTList.KEY_DATEFORM;
import static com.example.schedulemonitoring.fragments.FragOTList.KEY_DATETO;
import static com.example.schedulemonitoring.fragments.FragSettings.KEY_DATABASECOUNT;
import static com.example.schedulemonitoring.fragments.FragSettings.KEY_SETTINGID;

public class DatabaseHelperSettings extends SQLiteOpenHelper {

    public static final String COLUMN_SETTINGID = "SettingsID";
    public static final String SETTINGS_TABLE = "OT_Settings";
    public static final String COLUMN_HOURRATE = "HourRate";
    public static final String COLUMN_NIGHTDIFFRATE = "NightDifferential";
    public static final String COLUMN_REMAINDERS = "Remainders";
    public static final String COLUMN_OFFICEHOUR = "OfficeHour";

    public DatabaseHelperSettings(@Nullable Context context) {
        super(context, "DB_Settings.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + SETTINGS_TABLE + " (" + COLUMN_SETTINGID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_HOURRATE + " FLOAT, " + COLUMN_NIGHTDIFFRATE + " FLOAT, " + COLUMN_REMAINDERS + " TEXT," + COLUMN_OFFICEHOUR + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(ModelSettings modelSettings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HOURRATE, modelSettings.getxHourRate());
        cv.put(COLUMN_NIGHTDIFFRATE, modelSettings.getxNightDiff());
        cv.put(COLUMN_REMAINDERS, modelSettings.getxRemainders());
        cv.put(COLUMN_OFFICEHOUR, modelSettings.getxOfficeHour());

        long insert = db.insert(SETTINGS_TABLE, null, cv);
        if (insert == -1) {
            return false;

        } else {
            return true;
        }


    }

    public boolean updateSettings(ModelSettings modelSettings){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_OFFICEHOUR, modelSettings.getxOfficeHour());
        cv.put(COLUMN_HOURRATE, modelSettings.getxHourRate());
        cv.put(COLUMN_NIGHTDIFFRATE, modelSettings.getxNightDiff());
        cv.put(COLUMN_REMAINDERS, modelSettings.getxRemainders());

        db.update(SETTINGS_TABLE,cv,"SettingsID = ?",new String[]{String.valueOf(KEY_SETTINGID)} );

        return true;
    }

    public List<ModelSettings> selectSettings(){

        try {


            List<ModelSettings> returnList = new ArrayList<>();

            String queryString = "SELECT * FROM " + SETTINGS_TABLE;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(queryString, null);

            if (cursor.moveToFirst()) {
                // loop throught the cursor (result set) and create new Overtime object put them into return list

                do {
                    KEY_DATABASECOUNT += 1;

                    int settingID = cursor.getInt(0);
                    int xhourate = cursor.getInt(1);
                    int xnightdiffrate = cursor.getInt(2);
                    String xremainders = cursor.getString(3);
                    String xofficehour = cursor.getString(4);


                    ModelSettings newSettings = new ModelSettings(settingID, xofficehour, xremainders, xhourate, xnightdiffrate);


                    returnList.add(newSettings);

                } while (cursor.moveToNext());


            } else {
                // failure do not add anything to the list

            }
            // close the cursor and database when done.
            cursor.close();
            db.close();

            return returnList;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

}
