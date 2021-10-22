package com.example.schedulemonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.schedulemonitoring.Adapter.AdapterOTList;
import com.example.schedulemonitoring.Database.DatabaseHelper;
import com.example.schedulemonitoring.Model.ModelOTSched;
import com.example.schedulemonitoring.fragments.FragOTSched;
import com.example.schedulemonitoring.fragments.FragOTList;
import com.example.schedulemonitoring.fragments.FragSettings;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.schedulemonitoring.fragments.FragOTList.KEY_DATEFORM;
import static com.example.schedulemonitoring.fragments.FragOTList.KEY_DATETO;

public class MainMenu extends AppCompatActivity {
    private static final String TAG = "MainMenu";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);












        setDate();
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {

        AdapterViewPage adapterViewPage = new AdapterViewPage(getSupportFragmentManager());
        adapterViewPage.addFragment(new FragOTSched(), "OT Sched");
        adapterViewPage.addFragment(new FragOTList(), "OT List");
        adapterViewPage.addFragment(new FragSettings(), "Settings");
        viewPager.setAdapter(adapterViewPage);






    }

    private void setDate(){
        Calendar cal = Calendar.getInstance();
        int res = cal.getActualMaximum(Calendar.DATE);
        Log.d(TAG, "getActualMaximum: " + res);
        try {
            LocalDate todaydate = LocalDate.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            if(todaydate.getDayOfMonth() < 15) {
                KEY_DATEFORM = dtf.format(todaydate.withDayOfMonth(1));
                KEY_DATETO = dtf.format(todaydate.withDayOfMonth(15));

            }else{
                KEY_DATEFORM = dtf.format(todaydate.withDayOfMonth(16));
                KEY_DATETO = dtf.format(todaydate.withDayOfMonth(res));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }



    }


}