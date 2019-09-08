package com.example.now.time_assistant;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout container;
    ImageView pencil;
    TextView user_name;
    ImageView user_img;

    private List<AppointmentData> list_apointment;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    int list_apointment_index = 5;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                final String firstday = data.getExtras().getString("FirstDay");
                final String lastday = data.getExtras().getString("LastDay");
                Toast.makeText(MainActivity.this, "Day:" + firstday + " and " + lastday, Toast.LENGTH_SHORT).show();


                AppointmentData appointData = new AppointmentData();
                appointData.setAppointment_date_created("2019/08/22");
                appointData.setAppointment_img("for test");
                appointData.setAppointment_name("약속 "+ list_apointment_index);
                list_apointment_index++;
                appointData.setAppointment_time("00:00~15:00");
                appointData.setDate_start_end(firstday + "," + lastday);

                list_apointment.add(appointData);

                mAdapter = new AppointmentAdapter(list_apointment, MainActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj = v.getTag();
                        if(obj != null){
                            int position = (int)obj;
                            ((AppointmentAdapter)mAdapter).getAppointment(position);


                            /**FirstDay & LastDay를 intent로 넘겨주어야함**/
                            Intent intent = new Intent(MainActivity.this,Room.class);

                            intent.putExtra("FirstDay", firstday);
                            intent.putExtra("LastDay", lastday);
                            intent.putExtra("FirstDay_Year", data.getExtras().getInt("FirstDay_Year"));
                            intent.putExtra("FirstDay_Month", data.getExtras().getInt("FirstDay_Month"));
                            intent.putExtra("FirstDay_Date", data.getExtras().getInt("FirstDay_Date"));
                            intent.putExtra("LastDay_Year", data.getExtras().getInt("LastDay_Year"));
                            intent.putExtra("LastDay_Month", data.getExtras().getInt("LastDay_Month"));
                            intent.putExtra("LastDay_Date", data.getExtras().getInt("LastDay_Date"));

                            // intent.setPackage("com.android.chrome");   // 브라우저가 여러개 인 경우 콕 찍어서 크롬을 지정할 경우
                            startActivity(intent);
                        }else{          }
                    }
                });
                recyclerView.setAdapter(mAdapter);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pencil = findViewById(R.id.pencil);
        user_name = findViewById(R.id.user_profile_name);
        user_img = findViewById(R.id.user_profile_image);


        /**프로필 이미지 둥글게
         * */
        user_img.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            user_img.setClipToOutline(true);
            user_img.setBackground(new ShapeDrawable(new OvalShape()));
            //user_img.setBackgroundColor(Color.rgb(65, 246, 197));
        }

        /**밑의 리스트 추가하는 과정
         **/
        recyclerView = findViewById(R.id.main_list_container);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getAppointments();
        /**
         **/

        //화면 오른쪽 밑에 떠 있는 fab에 대한 내용
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "새로운 약속(방 파는거) 만들어야 함 - 노트 2번 페이지", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, Make_room.class);
                startActivityForResult(intent, 1000);

            }
        });


        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,User_Profile_Edit.class);
                //example로 넣어 놓은 것
                intent.putExtra("user_name","나우킴");
                startActivity(intent);
            }
        });
    }

    //밑의 리스트 데이터 받고,
    //set해주는 함수
    public void getAppointments(){
        list_apointment = new ArrayList<>();

        //list_appointment에 사용자가 방 만들면 그 정보를 넣어줘야 함
        //원래는 get~해서 어디선가 데이터베이스에서 가져와서 넣어야겠지

        /**sample data**/

        for(int i=0;i<5;i++){
            AppointmentData appointData = new AppointmentData();
            appointData.setAppointment_date_created("2019/08/22");
            appointData.setAppointment_img("for test");
            appointData.setAppointment_name("약속 "+ i);
            appointData.setAppointment_time("00:00~15:00");
            appointData.setDate_start_end("2019/08/22,2019/08/31");

            list_apointment.add(appointData);
        }


        /****/

        mAdapter = new AppointmentAdapter(list_apointment, MainActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){
                    int position = (int)obj;
                    ((AppointmentAdapter)mAdapter).getAppointment(position);

                    Intent intent = new Intent(MainActivity.this,Room.class);
                    // intent.setPackage("com.android.chrome");   // 브라우저가 여러개 인 경우 콕 찍어서 크롬을 지정할 경우
                    startActivity(intent);
                }else{          }
            }
        });
        recyclerView.setAdapter(mAdapter);

    }


}
