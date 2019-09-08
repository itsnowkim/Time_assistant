package com.example.now.time_assistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.util.Calendar;
import java.util.List;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE;

public class Make_room extends AppCompatActivity {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;

    //버튼 활성화를 위한 멤버 추가
    Button ok_btn, back_btn;
    String First = "";
    String Last = "";

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

        //Ok 버튼 활성화 기능 설정
        ok_btn = findViewById(R.id.ok_btn);
        ok_btn.setClickable(false);
        ok_btn.setEnabled(false);

        //Back 버튼
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        materialCalendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                ok_btn.setClickable(false);
                ok_btn.setEnabled(false);

                final String FirstDay = String.valueOf(dates.get(0));
                final String LastDay = String.valueOf(dates.get(dates.size()-1));

                First = FirstDay;
                Last = LastDay;

                if(!First.equals("")&& !Last.equals("")){
                    ok_btn.setClickable(true);
                    ok_btn.setEnabled(true);
                }

                final Integer FirstDay_Y = Integer.valueOf(dates.get(0).getYear());
                final Integer FirstDay_M = Integer.valueOf(dates.get(0).getMonth());
                final Integer FirstDay_D = Integer.valueOf(dates.get(0).getDay());
                final Integer LastDay_Y = Integer.valueOf(dates.get(dates.size()-1).getYear());
                final Integer LastDay_M = Integer.valueOf(dates.get(dates.size()-1).getMonth());
                final Integer LastDay_D = Integer.valueOf(dates.get(dates.size()-1).getDay());

                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Make_room.this, MainActivity.class);
                        intent.putExtra("FirstDay", FirstDay);
                        intent.putExtra("LastDay", LastDay);
                        intent.putExtra("FirstDay_Year", FirstDay_Y);
                        intent.putExtra("FirstDay_Month", FirstDay_M);
                        intent.putExtra("FirstDay_Date", FirstDay_D);
                        intent.putExtra("LastDay_Year", LastDay_Y);
                        intent.putExtra("LastDay_Month", LastDay_M);
                        intent.putExtra("LastDay_Date", LastDay_D);

                        setResult(RESULT_OK, intent);

                        AlertDialog.Builder builder = new AlertDialog.Builder(Make_room.this);
                        builder.setTitle("도움말").setMessage("메인 화면에 추가된 약속 카테고리 안에서 사용자를 추가하고," +
                                " 가능한 시간을 설정하세요.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                    }
                });
            }
        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected){
                final String temp = String.valueOf(date);
                check(temp);
            }
        });
    }

    public void check(String temp){
        if( !temp.equals(First) || !temp.equals(Last)){
            First = "";
            Last = "";
            ok_btn.setClickable(false);
            ok_btn.setEnabled(false);
        }
    }

}
