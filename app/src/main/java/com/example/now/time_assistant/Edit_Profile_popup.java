package com.example.now.time_assistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Edit_Profile_popup extends AppCompatActivity {

    TextView camera;
    TextView gallery;
    TextView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile_popup);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_edit__profile_popup);


        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);
        delete = findViewById(R.id.delete);

        //camera에서 가져오기
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //gallery에서 가져오기
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


    }
        //닫기 버튼 클릭
        public void mOnClose(View v){
            //액티비티(팝업) 닫기
            finish();
        }


        //바깥레이어 클릭시 안닫히게
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                return false;
            }
            return true;
        }



}

