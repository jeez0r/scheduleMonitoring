package com.example.schedulemonitoring.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.schedulemonitoring.Model.ModelOTSched;

import java.util.ArrayList;
import java.util.List;

import static com.example.schedulemonitoring.Adapter.AdapterOTList.KEY_OT_ID;
import static com.example.schedulemonitoring.fragments.FragOTList.KEY_DATEFORM;
import static com.example.schedulemonitoring.fragments.FragOTList.KEY_DATETO;
import static com.example.schedulemonitoring.fragments.FragSettings.KEY_HOUR_RATE;
import static com.example.schedulemonitoring.fragments.FragSettings.KEY_NIGHTDIFF_RATE;

public class DatabaseHelper  extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    public static final String OT_TABLE = "OT_list";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_TimeOut = "TimeOut";
    public static final String COLUMN_NightDiff = "NightDiff";
    public static final String COLUMN_Remarks = "Remarks";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TotalOTMilliSec = "TotalOTMilliSec";
    public static String xoverallTotalOT;
    public static long KEY_TOTAL_EARN;
    long xtotalmilisec , xtotalhours, xtotalmins, xTotalNightdiff;
    long xTotalearnHours , xTotalearnMin, xTotalEarn, xTotalNightdiffEarn;


    public DatabaseHelper(@Nullable Context context) {
        super(context, "DB_OTList.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + OT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_Remarks + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_TimeOut + " TEXT, " + COLUMN_NightDiff + " INT, " + COLUMN_TotalOTMilliSec + " LONG )";
        db.execSQL(createTableStatement);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(ModelOTSched modelOTSched){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_Remarks, modelOTSched.getXremarks());
        cv.put(COLUMN_DATE, modelOTSched.getXdateIn());
        cv.put(COLUMN_TimeOut, modelOTSched.getXtimeout());
        cv.put(COLUMN_TotalOTMilliSec, modelOTSched.getXtotalOTMilisec());
        cv.put(COLUMN_NightDiff, modelOTSched.getXnightdiff());

        long insert = db.insert(OT_TABLE, null, cv);
        return insert != -1;


    }


    public List<ModelOTSched> selectAllProduct(){


        List<ModelOTSched> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + OT_TABLE + " WHERE DATE BETWEEN '" + KEY_DATEFORM +"' AND '" + KEY_DATETO + "' ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            // loop throught the cursor (result set) and create new Overtime object put them into return list

            do{
                int otid = cursor.getInt(0);
                String xremarks = cursor.getString(1);
                String xdateIn = cursor.getString( 2);
                String xtimeout = cursor.getString(3);
                Integer xnightdiff =  cursor.getInt(4);
                long xsinglehours = cursor.getLong(5);

                if (!(xtotalmilisec < 0)) {
              xtotalmilisec += xsinglehours;
                    xtotalhours += xsinglehours / (60 * 60 * 1000) % 24;
                    xtotalmins += xsinglehours / (60 * 1000) % 60;
                }

                if(xnightdiff > 0){
                    xTotalNightdiff += xnightdiff;
                }




                ModelOTSched newOT= new ModelOTSched(xremarks, xdateIn,xtimeout,xsinglehours,xnightdiff,otid);
                Log.d(TAG, "selectAllProduct: " +xremarks +"/"+xdateIn+" "+xtimeout+"/"+xsinglehours+"/"+xnightdiff.toString());
                returnList.add(newOT);

            }while (cursor.moveToNext());

            HoursComputation();

        }else{
            // failure do not add anything to the list

        }
        // close the cursor and database when done.
        cursor.close();
        db.close();

        return returnList;

    }


    public boolean  deleteAll(){
        // find product model in the database. if it found delete it and return true.
        // if its not found , return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryStringDelete ="DELETE FROM " + OT_TABLE;

        Cursor cursor = db.rawQuery(queryStringDelete, null);

        return cursor.moveToFirst();

    }

    public boolean deleteOne(){
        // find product model in the database. if it found delete it and return true.
        // if its not found , return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryStringDelete ="DELETE FROM " + OT_TABLE + " WHERE " + COLUMN_ID + " = " + KEY_OT_ID;

        Cursor cursor = db.rawQuery(queryStringDelete, null);

        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }

    }

    private void HoursComputation(){
//       xtotalhours = xtotalmilisec / (60 * 60 * 1000) % 24;
//        xtotalmins = xtotalmilisec / (60 * 1000) % 60;


        long xtemptotalhours;
        xtemptotalhours = xtotalhours - xTotalNightdiff;

        xTotalNightdiffEarn = xTotalNightdiff * KEY_NIGHTDIFF_RATE;

        xTotalearnHours  = xtemptotalhours * KEY_HOUR_RATE;
        xTotalearnMin  = xtotalmins * KEY_HOUR_RATE / 60;
        KEY_TOTAL_EARN = xTotalearnHours +  xTotalearnMin + xTotalNightdiffEarn;

        if (xtotalhours < 2 ) {

            if (xtotalmins < 1) {
                xoverallTotalOT = xtotalhours + " Hour";
            } else {
                xoverallTotalOT = xtotalhours + " Hour & " + xtotalmins + " Minutes";
            }

        } else {
            if (xtotalmins < 1) {
                xoverallTotalOT = xtotalhours + " Hours";
            } else {
                xoverallTotalOT = xtotalhours + " Hours & " + xtotalmins + " Minutes";
            }
        }



        Log.d(TAG, "xoverallTotalOT: " + xoverallTotalOT + xtotalmilisec);
    }





}
