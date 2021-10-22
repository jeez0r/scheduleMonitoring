package com.example.schedulemonitoring.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.schedulemonitoring.Adapter.AdapterOTList;
import com.example.schedulemonitoring.Database.DatabaseHelper;
import com.example.schedulemonitoring.Model.ModelOTSched;
import com.example.schedulemonitoring.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.schedulemonitoring.Adapter.AdapterOTList.xTotalMiliSec;
import static com.example.schedulemonitoring.Database.DatabaseHelper.xoverallTotalOT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragOTList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragOTList extends Fragment implements DatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public static String KEY_DATEFORM;
    public static String KEY_DATETO;

    private String mParam1;
    private String mParam2;
    DatePicker picker;
    DatePickerDialog datePickerDialog;

    AdapterOTList adapterOTList;
   private List <ModelOTSched> modelOTSched;
    ListView listview;
    Button btn_daterange;
    TextView tv_tv_totalOThrs, tv_daterange;
    DatabaseHelper databaseHelper;


    public FragOTList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragOTList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragOTList newInstance(String param1, String param2) {
        FragOTList fragment = new FragOTList();
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

        View rootview1 = inflater.inflate(R.layout.fragment_frag_o_t_list, container, false);
        final SwipeRefreshLayout pullToRefresh = rootview1.findViewById(R.id.pullToRefresh);
        listview = (ListView) rootview1.findViewById(R.id.recview);
        btn_daterange = (Button) rootview1.findViewById(R.id.btn_daterange);
        tv_tv_totalOThrs = (TextView) rootview1.findViewById(R.id.tv_totalOThrs);
        tv_daterange = (TextView) rootview1.findViewById(R.id.tv_daterange);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initListview();

                pullToRefresh.setRefreshing(false);
            }
        });

      btn_daterange.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              ShowCalendar();
          }
      });


      tv_daterange.setText(KEY_DATEFORM + "  TO  " + KEY_DATETO);
        initListview();
        return rootview1;
    }



private void initListview(){
    xTotalMiliSec = 0;
    databaseHelper = new DatabaseHelper(getActivity());
    List<ModelOTSched>  modelOTSchedList = databaseHelper.selectAllProduct();

    adapterOTList = new AdapterOTList(getContext(), modelOTSchedList);
    listview.setAdapter(adapterOTList);

    tv_tv_totalOThrs.setText(xoverallTotalOT);
}

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month += 1;
        KEY_DATETO = month +"/"+dayOfMonth  +"/"+year;
        Log.d(TAG, KEY_DATETO);
        initListview();
        tv_daterange.setText(KEY_DATEFORM + "  TO  " + KEY_DATETO);
    }

    private void  ShowCalendar(){
         datePickerDialog  = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int month = datePickerDialog.getDatePicker().getMonth() + 1;
                        KEY_DATEFORM =  month + "/"+ datePickerDialog.getDatePicker().getDayOfMonth() +"/" + datePickerDialog.getDatePicker().getYear();
                        Log.d(TAG, "onClick: " +  KEY_DATEFORM);

                        ShowCalendarTO();
                    }
                });


        datePickerDialog.setMessage("From");
        datePickerDialog.show();
    }



    private void ShowCalendarTO(){
         datePickerDialog  = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setMessage("To");
        datePickerDialog.show();
    }
}