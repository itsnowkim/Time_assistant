package com.example.now.time_assistant;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE;

public class Make_room extends AppCompatActivity {

    String time,kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_room);
        //calender id 가져오기
        materialCalendarView = findViewById(R.id.customized_calender);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019, 1, 1))
                .setMaximumDate(CalendarDay.from(2025, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //꾸미기 함수 호출
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);




        materialCalendarView.setSelectionMode(SELECTION_MODE_RANGE);





    }
}
