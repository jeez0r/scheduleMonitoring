package com.example.schedulemonitoring.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schedulemonitoring.Database.DatabaseHelper;
import com.example.schedulemonitoring.Database.DatabaseHelperSettings;
import com.example.schedulemonitoring.Model.ModelOTSched;
import com.example.schedulemonitoring.Model.ModelSettings;
import com.example.schedulemonitoring.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragSettings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "FragSettings";

    ModelSettings modelSettings;

    DatabaseHelper databaseHelper;
    DatabaseHelperSettings databaseHelperSettings;

    TextView tv_hourrate, tv_nightdiffrate, tv_officehour;
    Button btn_save, btn_selecthourrate, btn_delete;
    SwitchMaterial sw_remainder;
    int t1Hour, t1Minute;
    String xtimeout;

    public static int KEY_SETTINGID = 0;
    public static String KEY_OFFICE_HOUR = "17:00";
    public static String KEY_TEMP_OFFICE_HOUR = "17:00";
    public static int KEY_HOUR_RATE = 0;
    public static int KEY_NIGHTDIFF_RATE = 0;
    public static String KEY_REMAINDERS = "ON";
    public  static int KEY_DATABASECOUNT = 0;

    public FragSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static FragSettings newInstance(String param1, String param2) {
        FragSettings fragment = new FragSettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_frag_settings, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        databaseHelperSettings = new DatabaseHelperSettings(getContext());

        tv_hourrate = (TextView) view.findViewById(R.id.tv_settings_hourrate);
        tv_nightdiffrate = (TextView) view.findViewById(R.id.tv_settings_nightdiffrate);
        tv_officehour = (TextView) view.findViewById(R.id.tv_settings_Officehour);

        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_selecthourrate = (Button) view.findViewById(R.id.btn_setting_setofficehour);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);

        sw_remainder = view.findViewById(R.id.sw_remainder);
        getSetting();


        btn_selecthourrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Getting the TIME OUT INPUT
                                t1Hour = hourOfDay;
                                t1Minute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, t1Hour, t1Minute);
                                KEY_OFFICE_HOUR = (String) DateFormat.format("HH:mm", calendar);
                                tv_officehour.setText(DateFormat.format("HH:mm", calendar));

                                Log.d(TAG, "onTimeSet: " + xtimeout);
                                btn_selecthourrate.setError(null);
                            }
                        }, 24, 0, true
                );
                timePickerDialog.setTitle("Time Out");
                timePickerDialog.updateTime(t1Hour, t1Minute);

                timePickerDialog.show();

                //  Toast.makeText(getActivity(), "Do not forget to set the Office Hour and Hour rate , in settings tab", Toast.LENGTH_LONG).show();
            }
        });

        tv_hourrate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(tv_hourrate.length() == 0)){
                    try {
                        KEY_HOUR_RATE = Integer.parseInt(tv_hourrate.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        tv_nightdiffrate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if(!(tv_nightdiffrate.getText().length() == 0)){
                   KEY_NIGHTDIFF_RATE = Integer.parseInt(tv_nightdiffrate.getText().toString());
               }

            }
        });

        sw_remainder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    KEY_REMAINDERS = "ON";
                } else {
                    KEY_REMAINDERS = "OFF";
                }
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getSetting();
                databaseHelper.deleteAll();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelSettings = new ModelSettings(null, KEY_OFFICE_HOUR,KEY_REMAINDERS,KEY_HOUR_RATE,KEY_NIGHTDIFF_RATE);
                if(KEY_DATABASECOUNT > 1){
                    boolean success = databaseHelperSettings.updateSettings(modelSettings);
                    if(success){
                        Toast.makeText(getActivity(), "Settings successfully saved", Toast.LENGTH_SHORT).show();
                        getSetting();
                    }
                }else {
                    boolean success = databaseHelperSettings.addOne(modelSettings);
                    if(success){
                        Toast.makeText(getActivity(), "Settings successfully saved", Toast.LENGTH_SHORT).show();
                    }
                }






            }
        });

        return view;
    }

    private void SetSetting(){



    }

    private void getSetting(){
        databaseHelperSettings = new DatabaseHelperSettings(getActivity());
        List<ModelSettings> modelSettings = databaseHelperSettings.selectSettings();
        if (!(modelSettings.size() == 0)){

        try {
//            Getting current date
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int xmonth = cal.get(Calendar.MONTH);
            int xyear = cal.get(Calendar.YEAR);
            int xdayofmonth = cal.get(Calendar.DAY_OF_MONTH);
            xmonth += 1;
            KEY_TEMP_OFFICE_HOUR = xmonth + "/" + xdayofmonth + "/" + xyear +" "+ modelSettings.get(0).getxOfficeHour();
            KEY_OFFICE_HOUR = modelSettings.get(0).getxOfficeHour();
            KEY_HOUR_RATE = modelSettings.get(0).getxHourRate();
            KEY_NIGHTDIFF_RATE= modelSettings.get(0).getxNightDiff();
            KEY_REMAINDERS = modelSettings.get(0).getxRemainders();

            KEY_SETTINGID =  modelSettings.get(0).getxID();

            Log.d(TAG, "getSetting: " + "KEY_OFFICE_HOUR" + KEY_OFFICE_HOUR +"KEY_HOUR_RATE" + KEY_HOUR_RATE + "KEY_NIGHTDIFF_RATE" + KEY_NIGHTDIFF_RATE + "KEY_REMAINDERS");




        } catch (Exception e) {
            e.printStackTrace();
        }
        }

        tv_nightdiffrate.setText(String.valueOf(KEY_NIGHTDIFF_RATE));
        tv_hourrate.setText(String.valueOf(KEY_HOUR_RATE));
        tv_officehour.setText(KEY_TEMP_OFFICE_HOUR);
        if(KEY_REMAINDERS.equals("ON")){
            sw_remainder.setChecked(true);
        }else{
            sw_remainder.setChecked(false);
        }


    }

}