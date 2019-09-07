package com.example.now.time_assistant;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    TextView user_email;
    TextView user_phone;

    int mMode = AppConstants.MODE_INSERT;       //프로필을 변경했던 것이 있으면 그걸로 뿌려줘야 하므로 여부 결정해야됨
    ProfileData item;
    Bitmap resultPhotoBitBack;
    Bitmap resultPhotoBitProfile;
    File file;

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
        /****/

    }


    public void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivityForResult(intent, 1);

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

//            //경로를 저장하는 방법
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String filePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            switch(flag) {
//                case BACKGROUND_IMAGE_CLICK :
//                    resultPhotoBitBack = decodeSampledBitmapFromResource(new File(filePath), userBackground.getWidth(), userBackground.getHeight());
//                    userBackground.setImageBitmap(resultPhotoBitBack);
//                    break;
//
//                case PROFILE_IMAGE_CLICK :
//                    resultPhotoBitProfile = decodeSampledBitmapFromResource(new File(filePath), userProfile.getWidth(), userProfile.getHeight());
//                    userBackground.setImageBitmap(resultPhotoBitProfile);
//                    break;
//            }

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

            /****/
            user_name.setText(items.get(0).user_name);
            user_nickname.setText(items.get(0).user_nickname);

            if(items.get(0).user_profile_img.equals("")) {
                userProfile.setImageResource(R.drawable.default_user_icon_11);
            }else{
                setPicture(items.get(0).user_profile_img, 1, PROFILE_IMAGE_CLICK);
            }

            if(items.get(0).user_profile_backimg.equals("")){
                userBackground.setImageResource(R.drawable.mintcolor);
            }else{
                setPicture(items.get(0).user_profile_backimg,1,BACKGROUND_IMAGE_CLICK);

            }

            /****/

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
            case BACKGROUND_IMAGE_CLICK:
                resultPhotoBitBack = BitmapFactory.decodeFile(picturePath);
                userBackground.setImageBitmap(resultPhotoBitBack);
                break;
            case PROFILE_IMAGE_CLICK:
                resultPhotoBitProfile = BitmapFactory.decodeFile(picturePath);
                userProfile.setImageBitmap(resultPhotoBitProfile);

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
                setPicture(item.getUser_profile_img(), 1,PROFILE_IMAGE_CLICK);
            }
            //background image
            String picturePath_back = item.getUser_profile_backimg();
            if (picturePath_back == null || picturePath_back.equals("")) {
                userBackground.setImageResource(R.drawable.mintcolor);
            } else {
                setPicture(item.getUser_profile_backimg(), 1,BACKGROUND_IMAGE_CLICK);
            }

        }else{//item은 존재하지 않고 처음 수정해서 저장만 하면 될 때.
            mMode = AppConstants.MODE_INSERT;

            userProfile.setImageResource(R.drawable.default_user_icon_11);
            userBackground.setImageResource(R.drawable.mintcolor);
        }

    }


    /**다른 액티비티로부터 응답 처리*/

    public static Bitmap decodeSampledBitmapFromResource(File res, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res.getAbsolutePath(),options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(res.getAbsolutePath(),options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height;
            final int halfWidth = width;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private String createFilename() {
        String curDateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return curDateStr;
    }

    private String savePicture(int flag) {

        String picturePath = null;

        switch (flag){
            case BACKGROUND_IMAGE_CLICK :
                File pfb = new File(AppConstants.FOLDER_PHOTO_PROFILE);

                if(!pfb.isDirectory()) {
                    pfb.mkdirs();
                }
                String photoFileBack = createFilename();
                picturePath = pfb + File.separator + photoFileBack;

                try{
                    FileOutputStream outstream = new FileOutputStream(picturePath);
                    resultPhotoBitBack.compress(Bitmap.CompressFormat.PNG, 100, outstream);
                    outstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case PROFILE_IMAGE_CLICK :
                File photoFolder = new File(AppConstants.FOLDER_PHOTO_PROFILE);

                if(!photoFolder.isDirectory()) {
                    photoFolder.mkdirs();
                }

                String photoFilename = createFilename();
                picturePath = photoFolder + File.separator + photoFilename;

                try {
                    FileOutputStream outstream = new FileOutputStream(picturePath);
                    resultPhotoBitProfile.compress(Bitmap.CompressFormat.PNG, 100, outstream);
                    outstream.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return picturePath;
    }


    /**
     * 데이터베이스 레코드 수정
     */

    private void saveProfile() {
        String u_name = user_name.getText().toString();
        String u_nick = user_nickname.getText().toString();

        String picture_profile_Path = savePicture(PROFILE_IMAGE_CLICK);
        String picture_back_Path = savePicture(BACKGROUND_IMAGE_CLICK);



        String sql = "update " + ProfileDatabase.TABLE_PROFILE +
                " set " +
                "USER_NAME = '" + u_name + "'" +
                ",USER_NICKNAME = '" + u_nick +"'" +
                ",PICTURE_PROFILE = '" + picture_profile_Path + "'" +
                ",PICTURE_BACK = '" + picture_back_Path + "'";


        ProfileDatabase database = ProfileDatabase.getInstance(this);
        database.execSQL(sql);

        //저장 후 밖으로 나감
        onBackPressed();
    }


}