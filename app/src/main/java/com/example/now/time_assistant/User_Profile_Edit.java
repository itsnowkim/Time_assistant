package com.example.now.time_assistant;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class User_Profile_Edit extends Activity {

    ImageView backward;
    ImageView ok_sign;
    int flag = 0;

    ImageView userProfile;
    ImageView userBackground;

    EditText user_name;
    EditText user_nickname;

    TextView user_email;
    TextView user_phone;

    int mMode = AppConstants.MODE_INSERT;       //프로필을 변경했던 것이 있으면 그걸로 뿌려줘야 하므로 여부 결정해야됨
    ProfileData item;
    Bitmap resultPhotoBitBack;
    Bitmap resultPhotoBitProfile;

    Boolean is_Profile_changed = false;
    Boolean is_Back_changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile__edit);

        initUI();

        //데이터베이스 가져오기
        loadProfileData();

        //check
        check_diff();

        //초기 값 설정
        applyItem();
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    private Bitmap sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        bitmap = rotate(bitmap, exifDegree);//이미지 뷰에 비트맵 넣기

        return bitmap;
    }


    private void initUI(){

        //상단 bar의 id
        backward = findViewById(R.id.go_backwards);
        ok_sign = findViewById(R.id.ok_submit);
        ok_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveProfile();
            }
        });

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

        user_email = findViewById(R.id.user_email);
        user_phone = findViewById(R.id.user_phone);

        /**리스너 - 건들지마**/
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
                flag = AppConstants.BACKGROUND_IMAGE_CLICK;
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //profile 사진 변경 하려고 누름
                openGallery();
                flag = AppConstants.PROFILE_IMAGE_CLICK;
            }
        });
        /****/

    }



    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
//            Bitmap imgBitmap = null;
//            Uri fileUri = data.getData();
//            ContentResolver resolver = getContentResolver();
//
//            try {
//                InputStream instream = resolver.openInputStream(fileUri);
//                imgBitmap = BitmapFactory.decodeStream(instream);
//                switch (flag) {
//                    case AppConstants.BACKGROUND_IMAGE_CLICK :
//                        userBackground.setImageBitmap(imgBitmap);
//                        resultPhotoBitBack = imgBitmap;
//                        is_Back_changed = true;
//                        break;
//                    case AppConstants.PROFILE_IMAGE_CLICK :
//                        userProfile.setImageBitmap(imgBitmap);
//                        resultPhotoBitProfile = imgBitmap;
//                        is_Profile_changed = true;
//                        break;
//                }
//                instream.close();
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//
            Bitmap imgBitmap = sendPicture(data.getData());
            switch (flag){
                case AppConstants.BACKGROUND_IMAGE_CLICK :
                    userBackground.setImageBitmap(imgBitmap);
                    resultPhotoBitBack = imgBitmap;
                    is_Back_changed = true;
                    break;
                case AppConstants.PROFILE_IMAGE_CLICK :
                    userProfile.setImageBitmap(imgBitmap);
                    resultPhotoBitProfile = imgBitmap;
                    is_Profile_changed = true;
                    break;
            }
        }

    }


    public int loadProfileData(){
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

                this.item = new ProfileData(user_name,user_nickname,picture_profile,picture_back,email,phone_num);
            }

            outCursor.close();

            user_email.setText(items.get(0).user_email);
            user_phone.setText(items.get(0).user_phonenum);

        }
        return recordCount;
    }

    public void setUserName(String data){
        user_name.setText(data);
    }
    public void setUserNickName(String data){
        user_nickname.setText(data);
    }

    public void setPicture(String picturePath, int sampleSize,int flag) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        switch (flag){
            case AppConstants.BACKGROUND_IMAGE_CLICK:
                try{
                    Bitmap bm = BitmapFactory.decodeFile(picturePath);
                    userBackground.setImageBitmap(bm);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case AppConstants.PROFILE_IMAGE_CLICK:
                try{
                    Bitmap bm = BitmapFactory.decodeFile(picturePath);
                    userProfile.setImageBitmap(bm);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void check_diff(){

        String temp;

        temp = item.getUser_nickname();
        if(temp.equals(AppConstants.DEFAULT_NICK)){
            mMode = AppConstants.MODE_INSERT;
        }else{
            mMode = AppConstants.MODE_MODIFY;
        }


    }

    public void applyItem() {

        if (mMode == AppConstants.MODE_MODIFY) {//item이 존재할 때 -> 예전에 한번 프로필 수정이 이루어졌다.
            setUserName(item.getUser_name());
            setUserNickName(item.getUser_nickname());

            //profile
            String picturePath_profile = item.getUser_profile_img();
            if (picturePath_profile == null || picturePath_profile.equals("")) {
                userProfile.setImageResource(R.drawable.default_user_icon_11);
            } else {
                setPicture(item.getUser_profile_img(), 1,AppConstants.PROFILE_IMAGE_CLICK);
            }
            //background image
            String picturePath_back = item.getUser_profile_backimg();
            if (picturePath_back == null || picturePath_back.equals("")) {
                userBackground.setImageResource(R.drawable.mintcolor);
            } else {
                setPicture(item.getUser_profile_backimg(), 1,AppConstants.BACKGROUND_IMAGE_CLICK);
            }

        }else{//item은 존재하지 않고 처음 수정해서 저장만 하면 될 때.
            mMode = AppConstants.MODE_INSERT;

            userProfile.setImageResource(R.drawable.default_user_icon_11);
            userBackground.setImageResource(R.drawable.mintcolor);
        }

    }

    /**다른 액티비티로부터 응답 처리*/

    private String savePicture(int flag) {

        String picturePath = null;

        switch (flag){
            case AppConstants.BACKGROUND_IMAGE_CLICK :
                try{
                    File file = new File("back.png");
                    FileOutputStream fos = openFileOutput("back.png" , 0);
                    resultPhotoBitBack.compress(Bitmap.CompressFormat.PNG, 100 , fos);
                    fos.flush();
                    fos.close();
                }catch(Exception e) {
                    e.printStackTrace();
                }
                picturePath =  "/data/data/com.example.now.time_assistant/files/back.png";
                break;
            case AppConstants.PROFILE_IMAGE_CLICK :
                try{
                    File file = new File("profile.png");
                    FileOutputStream fos = openFileOutput("profile.png" , 0);
                    resultPhotoBitProfile.compress(Bitmap.CompressFormat.PNG, 100 , fos);
                    fos.flush();
                    fos.close();
                }catch(Exception e) {
                    e.printStackTrace();
                }
                picturePath =  "/data/data/com.example.now.time_assistant/files/profile.png";
                break;
        }
        return picturePath;
    }


    /**
     * 데이터베이스 레코드 수정
     */

    //달라진 것만 업데이트 하면 됨.
    private void saveProfile() {
        String u_name = user_name.getText().toString();
        String u_nick = user_nickname.getText().toString();

        String sql = "update " + ProfileDatabase.TABLE_PROFILE +
                " set " +
                "USER_NAME = '" + u_name + "'" +
                ",USER_NICKNAME = '" + u_nick +"'";

        if(is_Profile_changed){
            String picture_profile_Path = savePicture(AppConstants.PROFILE_IMAGE_CLICK);
            sql = sql + ",PICTURE_PROFILE = '" + picture_profile_Path + "'";
        }
        if(is_Back_changed) {
            String picture_back_Path = savePicture(AppConstants.BACKGROUND_IMAGE_CLICK);
            sql = sql + ",PICTURE_BACK = '" + picture_back_Path + "'";

        }

        ProfileDatabase database = ProfileDatabase.getInstance(this);
        database.execSQL(sql);

        //저장 후 밖으로 나감
        finish();
    }

}
