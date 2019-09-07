package com.example.now.time_assistant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProfileDatabase {

        private static final String TAG = "ProfileDatabase";

        /**
         * 싱글톤 인스턴스
         */
        private static ProfileDatabase database;

        /**
         * table name for MEMO
         */
        public static String TABLE_PROFILE = "PROFILE";

        /**
         * version
         */
        public static int DATABASE_VERSION = 1;


        /**
         * Helper class defined
         */
        private DatabaseHelper dbHelper;

        /**
         * SQLiteDatabase 인스턴스
         */
        private SQLiteDatabase db;

        /**
         * 컨텍스트 객체
         */
        private Context context;

        /**
         * 생성자
         */
        private ProfileDatabase(Context context) {
            this.context = context;
        }

        /**
         * 인스턴스 가져오기
         */
        public static ProfileDatabase getInstance(Context context) {
            if (database == null) {
                database = new ProfileDatabase(context);
            }

            return database;
        }

        /**
         * 데이터베이스 열기
         */
        public boolean open() {

            dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            return true;
        }

        /**
         * 데이터베이스 닫기
         */
        public void close() {
            db.close();

            database = null;
        }

        /**
         * execute raw query using the input SQL
         * close the cursor after fetching any result
         *
         * @param SQL
         * @return
         */
        public Cursor rawQuery(String SQL) {

            Cursor c1 = null;
            try {
                c1 = db.rawQuery(SQL, null);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in executeQuery", ex);
            }

            return c1;
        }

        public boolean execSQL(String SQL) {

            try {
                Log.d(TAG, "SQL : " + SQL);
                db.execSQL(SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in executeQuery", ex);
                return false;
            }

            return true;
        }



        /**
         * Database Helper inner class
         */
        private class DatabaseHelper extends SQLiteOpenHelper {

            //들어갈 db의 내용은 일단 고정으로 한다. - 나중에 동적으로 바꾸어주어야 할 듯 하다.
            public DatabaseHelper(Context context) {
                super(context, "profile.db", null, DATABASE_VERSION);
            }

            //앱이 install 되서 첫 실행시만 최초로 딱 한번 호출 - db table을 만들어주는 함수.
            public void onCreate(SQLiteDatabase db) {

                // create table
                String CREATE_SQL = "create table " + TABLE_PROFILE + "("
                        + " USER_NAME TEXT NOT NULL PRIMARY KEY, "
                        + " USER_NICKNAME TEXT,"
                        + " PICTURE_PROFILE TEXT DEFAULT '', "             //default값으로 기본이미지같은거 넣으면 좋을듯 - 마무리 단계에서.
                        + " PICTURE_BACK TEXT DEFAULT '', "
                        + " USER_EMAIL TEXT,"
                        + " USER_PHONENUM TEXT"
                        + ")";
//                try {
                    db.execSQL(CREATE_SQL);
//                } catch(Exception ex) {
//                    Log.e(TAG, "Exception in CREATE_SQL", ex);
//                }

//                // create index
//                String CREATE_INDEX_SQL = "create index " + TABLE_PROFILE + "_IDX ON " + TABLE_PROFILE + "("
//                        + "CREATE_DATE"
//                        + ")";
//                try {
//                    db.execSQL(CREATE_INDEX_SQL);
//                } catch(Exception ex) {
//                    Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
//                }
            }

            public void onOpen(SQLiteDatabase db) {
            }

            //버전 업그레이드시 호출된다.
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                /*if(newVersion > 1){
                    // drop existing table
                    String DROP_SQL = "drop table if exists " + TABLE_PROFILE;
                    try {
                        db.execSQL(DROP_SQL);
                    } catch(Exception ex) {
                        Log.e(TAG, "Exception in DROP_SQL", ex);
                    }
                }
                onCreate(db);

                 */
            }
        }

}

