package com.example.now.time_assistant;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ImageView pencil;
    TextView user_name;
    ImageView user_img;
    ImageView user_back;

    private List<AppointmentData> list_apointment;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    int list_apointment_index = 5;
    public static ProfileDatabase mDatabase = null;

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
        user_back = findViewById(R.id.user_background_image);


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
        FloatingActionButton fab = findViewById(R.id.fab);
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
                startActivityForResult(intent,1000);
            }
        });

        openDatabase();
        loadDatabase();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1000){
                loadDatabase();
        }
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

    public void setPicture(String picturePath, int sampleSize,int flag) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        switch (flag){
            case AppConstants.BACKGROUND_IMAGE_CLICK:
                try{
                    Bitmap bm = BitmapFactory.decodeFile(picturePath,options);
                    user_back.setImageBitmap(bm);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case AppConstants.PROFILE_IMAGE_CLICK:
                try{
                    Bitmap bm = BitmapFactory.decodeFile(picturePath,options);
                    user_img.setImageBitmap(bm);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void loadDatabase(){
        String sql = "select USER_NAME, USER_NICKNAME, PICTURE_PROFILE, PICTURE_BACK, USER_EMAIL, USER_PHONENUM from " + ProfileDatabase.TABLE_PROFILE;

        int recordCount = -1;
        ProfileDatabase database = ProfileDatabase.getInstance(this);

        if (database != null) {
            Cursor outCursor = database.rawQuery(sql);
            recordCount = outCursor.getCount();

            //처음 실행시켰을 경우에만 default 값 넣어줌.
            if(recordCount == 0){
                database.execSQL("insert into " + ProfileDatabase.TABLE_PROFILE
                        + "(USER_NAME,USER_NICKNAME,PICTURE_PROFILE,PICTURE_BACK,USER_EMAIL,USER_PHONENUM)"
                        + " values "
                        + "('이름','닉네임','','','useremail@eail.com','010-1234-5678')");
                outCursor = database.rawQuery(sql);
                recordCount++;
            }

            ArrayList<ProfileData> items = new ArrayList();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();

                String user_name = outCursor.getString(0);
                String user_nickname = outCursor.getString(1);
                String picture_profile = outCursor.getString(2);
                String picture_back = outCursor.getString(3);
                String email = outCursor.getString(4);
                String phone_num = outCursor.getString(5);

                items.add(new ProfileData(user_name, user_nickname, picture_profile, picture_back, email, phone_num));
            }

            outCursor.close();

            /****/
            user_name.setText(items.get(0).user_name);

            if(items.get(0).user_profile_img.equals(AppConstants.DEFAULT_IMG)) {
                user_img.setImageResource(R.drawable.default_user_icon_11);
            }else{
                setPicture(items.get(0).user_profile_img, 1, AppConstants.PROFILE_IMAGE_CLICK);
            }

            if(items.get(0).user_profile_backimg.equals(AppConstants.DEFAULT_BACK)){
                user_back.setImageResource(R.drawable.mintcolor);
            }else{
                setPicture(items.get(0).user_profile_backimg,1,AppConstants.BACKGROUND_IMAGE_CLICK);
            }

            /****/
        }
    }


}
