package com.example.now.time_assistant;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Appointment> appointments;
    LayoutInflater layoutInflater;
    LinearLayout container;
    ImageView pencil;
    TextView user_name;
    ImageView user_img;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pencil = findViewById(R.id.pencil);
        user_name = findViewById(R.id.user_profile_name);
        user_img = findViewById(R.id.user_profile_image);


        user_img.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            user_img.setClipToOutline(true);
        }
        //list 임의로 추가 시험 - 10개만 해 봄. 이 부분은 listview, addview라고 검색하면 공부할 수 있음. 내 블로그에도 있음
        appointments = new ArrayList<>();
        for(int i=0;i<10;i++) {
            Appointment ap1 = new Appointment("약속 "+ i, "default");

            appointments.add(ap1);
        }
        layoutInflater = LayoutInflater.from(MainActivity.this);
        container = findViewById(R.id.main_list_container);

        for(int i=0;i<appointments.size();i++){
            View view = layoutInflater.inflate(R.layout.main_promise_content,null,false);
            TextView name = view.findViewById(R.id.list_content_name);
            ImageView imageView = view.findViewById(R.id.list_content_image);

            name.setText(appointments.get(i).appointment_name);
            imageView.setImageResource(R.drawable.ic_launcher_foreground);

            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                imageView.setClipToOutline(true);
            }
            container.addView(view);
        }

        //화면 오른쪽 밑에 떠 있는 fab에 대한 내용
        //새로운 방을 파는 activity
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
                //example로 넣어 놓은 것
                intent.putExtra("user_name","나우킴");
                startActivity(intent);
            }
        });


    }



}
