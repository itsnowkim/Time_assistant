package com.example.now.time_assistant;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class SetPossibleTime extends AppCompatActivity {

    GridView gridView;
    SingerAdapter adapter;
    Button time_set_ok;
    TimePicker startTime;
    TimePicker endTime;

    private int TIME_PICKER_INTERVAL = 30;
    NumberPicker minutePicker;
    List<String> displayedValues;

    /**
     * 해야하는 것 :
     * 1. load data
     * 그 주에 대한 미리 생성한 시간표가 있다면 뿌리기
     *
     * 2. add time
     * 어느 특정 날짜를 누른 뒤에 가능한 시간을 추가하고,
     * firebase에 그 값을 저장한다.
     *
     * 3.저장 버튼을 누르고 나갈 때 특정 날짜에 시간을 추가했다, 뭐 그런 걸 주기 -> startactivityforresult했으니까
     * 이거 부른 데에다가 추가하면 될듯.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_possible_time);

        init();

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TimeCell item = (TimeCell)adapter.getItem(position);
//                //Toast.makeText(getApplicationContext(), "선택 : "+  position, Toast.LENGTH_LONG).show();
//
//            }
//        });


    }

    public void init(){
        gridView = findViewById(R.id.gridView);

        adapter = new SingerAdapter();

        for(int i = 0;i<7;i++){
            for(int j=0;j<8;j++){
                adapter.addItem(new TimeCell(0,0));
            }
        }
        gridView.setAdapter(adapter);

        //ok버튼
        time_set_ok = findViewById(R.id.time_setted_ok);

        //start
        startTime = findViewById(R.id.start_time);
        //end
        endTime = findViewById(R.id.end_time);

        //TimePicker
        startTime.setIs24HourView(true);
        startTime.setCurrentHour(0);
        startTime.setCurrentMinute(0);

        setTimePickerInterval(startTime);

        endTime.setIs24HourView(true);
        endTime.setCurrentHour(0);
        endTime.setCurrentMinute(0);

        setTimePickerInterval(endTime);

    }
    @SuppressLint("NewApi")
    private void setTimePickerInterval(TimePicker timePicker) {
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");

            Field field = classForid.getField("minute");
            minutePicker =  timePicker
                    .findViewById(field.getInt(null));

            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(1);
            displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            //  for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
            //      displayedValues.add(String.format("%02d", i));
            //  }
            minutePicker.setDisplayedValues(displayedValues
                    .toArray(new String[0]));
            minutePicker.setWrapSelectorWheel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class SingerAdapter extends BaseAdapter {
        ArrayList<TimeCell> items = new ArrayList();

        @Override
        public int getCount(){
            return items.size();
        }

        public void addItem(TimeCell item){
            items.add(item);
        }

        @Override
        public Object getItem(int position){
            return items.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup){
            TimeCellView view = new TimeCellView(getApplicationContext());
            TimeCell item = items.get(position);

            return view;
        }
    }
}
