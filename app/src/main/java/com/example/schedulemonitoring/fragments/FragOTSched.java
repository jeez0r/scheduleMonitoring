package com.example.schedulemonitoring.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schedulemonitoring.Database.DatabaseHelper;
import com.example.schedulemonitoring.Database.DatabaseHelperSettings;
import com.example.schedulemonitoring.MainActivity;
import com.example.schedulemonitoring.MainMenu;
import com.example.schedulemonitoring.Model.ModelOTSched;
import com.example.schedulemonitoring.Model.ModelSettings;
import com.example.schedulemonitoring.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.provider.Contacts.Settings.getSetting;
import static com.example.schedulemonitoring.fragments.FragSettings.KEY_OFFICE_HOUR;
import static com.example.schedulemonitoring.fragments.FragSettings.KEY_REMAINDERS;
import static com.example.schedulemonitoring.fragments.FragSettings.KEY_TEMP_OFFICE_HOUR;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragOTSched#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragOTSched extends Fragment {
    private static final String TAG = "FragOTSched";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters


    private String mParam1;
    private String mParam2;
    CalendarView cv_otdate;
    Button btn_settimeout, btn_save, btn_cancel;
    SwitchCompat sc_nightdif;
    int xnightDiff = 0;
    int t1Hour, t1Minute;
    String xtimeout, xremarks, xdatenow, xtemptimeout;
    TextInputEditText tv_remarks, tv_NightDiffHours;
    TextInputLayout til_NightDiffHours;
    String xOTDate;
    int xOTMonth;
    int xOTYear;
    int xOTDayofmonth;

    ModelOTSched modelOTSched;

    DatabaseHelper databaseHelper;
    DatabaseHelperSettings databaseHelperSettings;
    long xtotalMilliSec;

    public FragOTSched() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragOTSched.
     */
    // TODO: Rename and change types and number of parameters
    public static FragOTSched newInstance(String param1, String param2) {
        FragOTSched fragment = new FragOTSched();
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
        View rootview1 = inflater.inflate(R.layout.fragment_frag_o_t_sched, container, false);
        btn_settimeout = (Button) rootview1.findViewById(R.id.btn_settimeout);
        btn_save = (Button) rootview1.findViewById(R.id.btn_save);
        btn_cancel = (Button) rootview1.findViewById(R.id.btn_cancel);
        sc_nightdif = (SwitchCompat) rootview1.findViewById(R.id.sc_nightdif);
        tv_remarks = (TextInputEditText) rootview1.findViewById(R.id.tv_remarks);
        til_NightDiffHours = (TextInputLayout) rootview1.findViewById(R.id.til_NightDiffHours);
        tv_NightDiffHours = (TextInputEditText) rootview1.findViewById(R.id.tv_NightDiffHours);
        cv_otdate = (CalendarView) rootview1.findViewById(R.id.cv_otdate);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
//        xOTDate = dtf.format(now);
        xdatenow = dtf.format(now);


        SetDate();

        til_NightDiffHours.setVisibility(View.GONE);


        cv_otdate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month+=1;
                xOTDate = month + "/" + dayOfMonth + "/" + year;
                Log.d(TAG, "xOTDate: " + xOTDate);
                xOTMonth = month;
                xOTYear = year;
                xOTDayofmonth = dayOfMonth;

            }
        });
        btn_settimeout.setOnClickListener(new View.OnClickListener() {
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

//                                 GET THE TIME ONLY
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(xOTYear, xOTMonth, xOTDayofmonth, t1Hour, t1Minute);
                                xtemptimeout = (String) DateFormat.format("MM/dd/yyyy HH:mm", calendar1);
//                                GET THE TIME WITH DATE
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, t1Hour, t1Minute);

                                xtimeout = (String) DateFormat.format("HH:mm", calendar);
                                btn_settimeout.setText(DateFormat.format("HH:mm", calendar));
                                Log.d(TAG, "onTimeSet: " + xtimeout);
                                btn_settimeout.setError(null);
                            }
                        }, 24, 0, true
                );
                timePickerDialog.setTitle("Time Out");
                timePickerDialog.updateTime(t1Hour, t1Minute);

                timePickerDialog.show();
        if(KEY_REMAINDERS.equals("ON")){
            Toast.makeText(getActivity(), "Do not forget to set the Office Hour and Hour rate , in settings tab", Toast.LENGTH_LONG).show();
        }

            }
        });

        sc_nightdif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    til_NightDiffHours.setVisibility(View.VISIBLE);
                    if(KEY_REMAINDERS.equals("ON")){
                        Toast.makeText(getActivity(), "Do not forget to set the Night differential rate, in settings tab", Toast.LENGTH_LONG).show();
                    }

                } else {

                    til_NightDiffHours.setVisibility(View.GONE);
                }

                Log.d(TAG, "onCheckedChanged: " + xnightDiff);
            }


        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                try {

                    if (TextUtils.isEmpty(xtimeout)) {
                        btn_settimeout.setError("Choose Time out");
                        Toast.makeText(getActivity(), "Set Time out", Toast.LENGTH_SHORT).show();

                    } else {
                        xremarks = Objects.requireNonNull(tv_remarks.getText()).toString();
                        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                        String time1 = "16:00:00";
                        String time2 = "17:00:00";

                        //GETTING TOTAL MILISECONDS OF OVER TIME
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                        Date date1 = format.parse(xtemptimeout);
                        Date date2 = format.parse(KEY_TEMP_OFFICE_HOUR);
                        xtotalMilliSec = date1.getTime() - date2.getTime();

                        if (xtotalMilliSec > 1861200000){

                        }
                        long temphours =  xtotalMilliSec / (60 * 60 * 1000) % 24;

                        Log.d(TAG, "date1: " + xtemptimeout);
                        Log.d(TAG, "date2: " + KEY_TEMP_OFFICE_HOUR);
                        Log.d(TAG, "TOTALMILLISEC: " + xtotalMilliSec);


                        modelOTSched = new ModelOTSched(xremarks, xOTDate, xtimeout, xtotalMilliSec, xnightDiff, null);
//
//                        Log.d(TAG, "xremarks: " + xremarks);
//                        Log.d(TAG, "xOTDate: " + xOTDate);
//                        Log.d(TAG, "xtimeout: " + xtimeout);
//                        Log.d(TAG, "xtotalMilliSec: " + xtotalMilliSec);
//                        Log.d(TAG, "xnightDiff: " + xnightDiff);


                        boolean success = databaseHelper.addOne(modelOTSched);

                        if (success) {
                            Toast.makeText(getActivity(), "Overtime Successfully saved", Toast.LENGTH_SHORT).show();
                            xremarks = "";
                            SetDate();
                            xtimeout = "";
                            xtemptimeout = "";
                            xnightDiff = 0;
                            xtotalMilliSec = 0;

                            tv_remarks.clearFocus();
                            tv_remarks.setText("");
                            sc_nightdif.setChecked(false);
                            btn_settimeout.setText("SET TIME OUT");
                            tv_NightDiffHours.setText("");

                            cv_otdate.setDate(Objects.requireNonNull(new SimpleDateFormat("MM/dd/yyyy").parse(xdatenow)).getTime(), true, true);

                        } else {
                            Toast.makeText(getActivity(), "Overtime not saved. Please try Again.", Toast.LENGTH_SHORT).show();
                        }

                    }
//                    Log.d(TAG, "difference: " + difference);
//                    long diffHours = difference / (60 * 60 * 1000) % 24;
//                    long diffMinutes = difference / (60 * 1000) % 60;
//                    Log.d(TAG, "diffHours: " + diffHours);
//                    Log.d(TAG, "diffMinutes: " + diffMinutes);
//                    String xtotalHours = diffHours + "." + diffMinutes;
//                    Log.d(TAG, "xtotalHours: " + xtotalHours);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                try {
                    xremarks = "";
                    SetDate();
                    xtimeout = "";
                    xnightDiff = 0;
                    xtotalMilliSec = 0;
                    tv_NightDiffHours.setText("");
                    btn_settimeout.setError(null);
                    tv_remarks.clearFocus();
                    tv_remarks.setText("");
                    sc_nightdif.setChecked(false);
                    xtemptimeout = "";
                    btn_settimeout.setText("SET TIME OUT");

                    cv_otdate.setDate(Objects.requireNonNull(new SimpleDateFormat("MM/dd/yyyy").parse(xdatenow)).getTime(), true, true);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        tv_NightDiffHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(tv_NightDiffHours.length() == 0)) {
                    xnightDiff = Integer.parseInt(tv_NightDiffHours.getText().toString());
                } else {
                    xnightDiff = 0;
                }
            }
        });

        return rootview1;


    }


    private void SetDate() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int xmonth = cal.get(Calendar.MONTH);
        int xyear = cal.get(Calendar.YEAR);
        int xdayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        xmonth+=1;

        xOTMonth = xmonth;
        xOTYear = xyear;
        xOTDayofmonth = xdayofmonth;

        xOTDate = xmonth + "/" + xdayofmonth + "/" + xyear;
    }


}