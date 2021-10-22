package com.example.schedulemonitoring.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemonitoring.Database.DatabaseHelper;
import com.example.schedulemonitoring.Model.ModelOTSched;
import com.example.schedulemonitoring.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterOTList extends ArrayAdapter<ModelOTSched> {
    private List<ModelOTSched> modelOTSchedList;
    private Context context;
    DatabaseHelper databaseHelper;
    public static long xTotalMiliSec;
    long otMiliSec, xtotalhours, xtotalmin, xOverallTotalHours, xOverallTotalMin;
    Integer nightdiffhour;
    String xtotalOT;


    public AdapterOTList(Context context, List<ModelOTSched> modelOTSchedList) {
        super(context, R.layout.layout_o_t_list, modelOTSchedList);
        this.context = context;
        this.modelOTSchedList = modelOTSchedList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_o_t_list, null, true);
        Log.d(TAG, "modelOTSchedList.size(): " + modelOTSchedList.size());
        otMiliSec = modelOTSchedList.get(position).getXtotalOTMilisec();

        if (!(otMiliSec < 0)) {
            xTotalMiliSec += otMiliSec;
        }

        xtotalhours = otMiliSec / (60 * 60 * 1000) % 24;
        xtotalmin = otMiliSec / (60 * 1000) % 60;

        if (xtotalhours < 2){
            if (xtotalmin <= 1){
                xtotalOT = xtotalhours + " Hour";
            }else{
                xtotalOT = xtotalhours + " Hour " + xtotalmin + " Minutes";
            }
        }else{
            if (xtotalmin <= 1){
                xtotalOT = xtotalhours + " Hours ";
            }else{
                xtotalOT = xtotalhours + " Hours " + xtotalmin + " Minutes";
            }
        }

        nightdiffhour = modelOTSchedList.get(position).getXnightdiff();

        TextView tv_otlist_timeout = (TextView) view.findViewById(R.id.tv_otlist_timeout);
        TextView tv_otlist_date = (TextView) view.findViewById(R.id.tv_otlist_date);
        TextView tv_otlist_totOTHrs = (TextView) view.findViewById(R.id.tv_otlist_totOTHrs);
        TextView tv_otlist_remarks = (TextView) view.findViewById(R.id.tv_otlist_remarks);
        TextView tv_otlist_nightdiff = (TextView) view.findViewById(R.id.tv_otlist_nightdiff);
        databaseHelper = new DatabaseHelper(context);

        tv_otlist_timeout.setText(modelOTSchedList.get(position).getXtimeout());
        tv_otlist_date.setText(modelOTSchedList.get(position).getXdateIn());



        if(nightdiffhour < 2){
            if(nightdiffhour <= 0){
                tv_otlist_nightdiff.setText("--");
            }else {
                tv_otlist_nightdiff.setText(String.valueOf(nightdiffhour) + " Hour");
            }

        }else {
            tv_otlist_nightdiff.setText(String.valueOf(nightdiffhour) + " Hours");
        }

        if (xtotalhours < 0 ) {
            tv_otlist_totOTHrs.setText("0");
        } else {

            tv_otlist_totOTHrs.setText(xtotalOT);
        }

        tv_otlist_remarks.setText(modelOTSchedList.get(position).getXremarks());


        if (position == modelOTSchedList.size() - 1) {
            xOverallTotalHours = 0;
            xOverallTotalMin = 0;
            xOverallTotalHours = xTotalMiliSec / (60 * 60 * 1000) % 24;
            xOverallTotalMin = xTotalMiliSec / (60 * 1000) % 60;




        }


        Log.d(TAG, "xTotalMiliSec: " + position + "/" + xOverallTotalHours + " Hours" + xOverallTotalMin + " Min" + xTotalMiliSec + "/" );
        return view;
    }
}
