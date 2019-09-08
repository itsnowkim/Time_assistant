package com.example.now.time_assistant;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE;

public class Room extends AppCompatActivity {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;
    Button timeplus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        init();

    }

    public void init(){
        //calender id 가져오기
        materialCalendarView = findViewById(R.id.calender_after_created);

        Intent intent = getIntent();

        final Integer FirstDay_Y  = intent.getExtras().getInt("FirstDay_Year");
        final Integer FirstDay_M  = intent.getExtras().getInt("FirstDay_Month");
        final Integer FirstDay_D = intent.getExtras().getInt("FirstDay_Date");
        final Integer LastDay_Y = intent.getExtras().getInt("LastDay_Year");
        final Integer LastDay_M  = intent.getExtras().getInt("LastDay_Month");
        final Integer LastDay_D  = intent.getExtras().getInt("LastDay_Date");

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(FirstDay_Y, FirstDay_M, FirstDay_D))
                .setMaximumDate(CalendarDay.from(LastDay_Y, LastDay_M, LastDay_D))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //꾸미기 함수 호출
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        timeplus = findViewById(R.id.time_plus);
        timeplus.setClickable(false);
        timeplus.setEnabled(false);
        timeplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Room.this, SetPossibleTime.class);
                //putextra
                startActivityForResult(intent, 1253);
            }
        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                timeplus.setClickable(true);
                timeplus.setEnabled(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1253){
                //성공!
            }
        }
    }
}
