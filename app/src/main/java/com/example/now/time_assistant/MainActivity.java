package com.example.now.time_assistant;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ImageView pencil;
    TextView user_name;
    ImageView user_img;

    private List<AppointmentData> list_apointment;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static ProfileDatabase mDatabase = null;



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
                Intent intent = new Intent(MainActivity.this,Make_room.class);
                startActivity(intent);

            }
        });


        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,User_Profile_Edit.class);
                startActivity(intent);
            }
        });


        setPicturePath();

        openDatabase();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    /**
     * 데이터베이스 열기 (데이터베이스가 없을 때는 만들기)
     */
    public void openDatabase() {
        // open database
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = ProfileDatabase.getInstance(this);
        boolean isOpen = mDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }

    public void setPicturePath() {
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        AppConstants.FOLDER_PHOTO_PROFILE = sdcardPath + File.separator + "photo";
    }


}
