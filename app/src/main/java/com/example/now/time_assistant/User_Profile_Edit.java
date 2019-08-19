package com.example.now.time_assistant;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.io.InputStream;

public class User_Profile_Edit extends Activity {

    public static final int BACKGROUND_IMAGE_CLICK = -1;
    public static final int PROFILE_IMAGE_CLICK = -2;

    ImageView backward;
    ImageView ok_sign;
    int flag = 0;

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
        if(Build.VERSION.SDK_INT >= 21) {
            userProfile.setBackground(new ShapeDrawable(new OvalShape()));
            userProfile.setClipToOutline(true);
        }

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
                openGallery();
                flag = BACKGROUND_IMAGE_CLICK;
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //profile 사진 변경 하려고 누름
                openGallery();
                flag = PROFILE_IMAGE_CLICK;
            }
        });
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Uri fileUri = data.getData();

            ContentResolver resolver = getContentResolver();

            try {
                InputStream instream = resolver.openInputStream(fileUri);

                Bitmap imgBitmap = BitmapFactory.decodeStream(instream);

                switch (flag) {

                    case BACKGROUND_IMAGE_CLICK :
                        userBackground.setImageBitmap(imgBitmap);
                        break;
                    case PROFILE_IMAGE_CLICK :
                        userProfile.setImageBitmap(imgBitmap);
                        break;
                }

                instream.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}