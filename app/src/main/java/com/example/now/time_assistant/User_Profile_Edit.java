package com.example.now.time_assistant;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

public class User_Profile_Edit extends Activity {

    ImageView backward;
    ImageView ok_sign;

    ImageView userProfile;
    ImageView userBackground;

    EditText user_name;
    EditText user_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile__edit);


        Intent intent = new Intent(this.getIntent());
        String input = intent.getStringExtra("user_name");
        Log.d("username : ","username : "+input);

        //상단 bar의 id
        backward = findViewById(R.id.go_backwards);
        ok_sign = findViewById(R.id.ok_submit);

        //내용물의 id
        userProfile = findViewById(R.id.user_profile_image_edit);
        userBackground = findViewById(R.id.user_background_image_edit);
        //위의 두 개는 edit 가능한 사용자의 프로필이다.

        user_name = findViewById(R.id.user_profile_name_edit);
        user_nickname = findViewById(R.id.user_profile_nickname_edit);


        //받은 거 set 하는 과정
        user_name.setText(input);

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //배경 변경 위해 누름.
                Intent intent1 = new Intent(User_Profile_Edit.this,Edit_Profile_popup.class);
                startActivity(intent1);
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //profile 사진 변경 하려고 누름
                Intent intent2 = new Intent(User_Profile_Edit.this,Edit_Profile_popup.class);
                startActivity(intent2);
            }
        });

    }

}