package com.example.now.time_assistant;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class Edit_Profile_popup extends AppCompatActivity{

    private static final int PICK_FROM_ALBUM = 1;

    Bitmap imgBitmap;
    TextView camera;
    TextView gallery;
    TextView delete;
    Intent intent;
    int flag;
    View parentView;
    ImageView imageView;
    //위의 인텐트는 배경인지 프로필인지 뭘 눌렀는지 알기 위해 만들었다.

    //닫기 버튼 클릭 함수
    public void mOnClose(View v){
        //액티비티(팝업) 닫기
        finish();
    }

    //바깥레이어 클릭시 안닫히게 하는 함수
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    //권한 요청 시 화면에 띄울 문구 보여주는 함수
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))   //values - strings에 정의해놓음
                .setDeniedMessage(getResources().getString(R.string.permission_1))      //가서 확인해보길 - 바꿀 수도 있음.
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile_popup);
        intent = getIntent();
        flag = intent.getIntExtra("Check",0);
        //안받아졌다 -> 0 근데 그럴 일은 없을 것 같다.
        //flag == -1 -> background image 변경할거야
        //flag == -2 -> profile image 변경할거야


        //권한 요청시 띄울 문구 pop
        //tedPermission();


        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);
        delete = findViewById(R.id.delete);

        //gallery에서 가져오기
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(resultCode == RESULT_OK) {
                Uri fileUri = data.getData();

                ContentResolver resolver = getContentResolver();

                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    imgBitmap = BitmapFactory.decodeStream(instream);

                    //imageView.setImageBitmap(imgBitmap);


                    instream.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            Intent toparent = new Intent();
            //toparent.putExtra("data",imgBitmap);
            toparent.putExtra("data","aaaaa");

            setResult(flag,toparent);
            finish();

    }


}

