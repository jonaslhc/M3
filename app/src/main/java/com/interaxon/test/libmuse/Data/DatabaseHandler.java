package com.interaxon.test.libmuse.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


public class DatabaseHandler extends SQLiteOpenHelper {

    String TAG = "Database Handler";
    private static DatabaseHandler mDatabaseHandler = null;
    private static Context mContext;

    private static String DIR_PATH;
    private static String CURR_FILE;

    private ProfileData mCurrUser;

    private static final String DATABASE_NAME = "profiles.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATA_TABLE = "data_table";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_FIRST = "first";

    private static final String COLUMN_STROOP_INDEX = "stroop_index";
    private static final String COLUMN_STROOP_COUNT = "stroop_count";
    private static final String COLUMN_ACCURACY = "accuracy";
    private static final String COLUMN_REACTION_TIME = "reaction_time";

    private static final String COLUMN_MEDITATION_SESSION_NUM = "meditation_session_num";
    private static final String COLUMN_MEDITATION_INDEX = "meditation_index";
    private static final String COLUMN_MEDITATION_COUNT = "meditation_count";

    private static final String COLUMN_MEDITATION = "meditation";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + DATA_TABLE + " (" +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_AGE + " INT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_FIRST + " BOOLEAN, " +
                    COLUMN_STROOP_INDEX + " INT, " +
                    COLUMN_STROOP_COUNT + " INT, " +
                    COLUMN_MEDITATION_SESSION_NUM + " INT, " +
                    COLUMN_MEDITATION_INDEX + " INT, " +
                    COLUMN_MEDITATION_COUNT + " INT, " +
                    COLUMN_ACCURACY + " REAL, " +
                    COLUMN_REACTION_TIME + " REAL, " +
                    COLUMN_MEDITATION + " TEXT " +
            ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public DatabaseHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // initialize handler and use it
    public static DatabaseHandler initHandler (Context context) {

        mContext = context;

        if (mDatabaseHandler == null) {
            mDatabaseHandler = new DatabaseHandler(mContext);
        }

        SQLiteDatabase db = mDatabaseHandler.getReadableDatabase();
        DIR_PATH = mContext.getFilesDir().getPath();
        CURR_FILE = db.getPath();

        Log.d("Database Initialized", CURR_FILE);
        db.close();

        return mDatabaseHandler;
    }

    // get handler
    public static DatabaseHandler getHandler (){
        return mDatabaseHandler;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE);
        onCreate(db);
    }

    public void updateCurrUser (String username) {
        mCurrUser = getData(username);
        Log.d(TAG, username);
    }

    public ProfileData getCurrUser () {
        return mCurrUser;
    }

    public void load(String file_name) {

        // close the data base
        mDatabaseHandler.close();

        // checks if destination folder exists and create if not
        File createOutFile = new File(DIR_PATH);
        if (!createOutFile.exists()){
            createOutFile.mkdir();
        }

        File currFile = new File(CURR_FILE);
        File newFile = new File(DIR_PATH, file_name);

        FileInputStream inStream;
        FileOutputStream outStream;

        // copy file over
        try {
            inStream = new FileInputStream(newFile);
            outStream = new FileOutputStream(currFile);

            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();

            inChannel.transferTo(0, inChannel.size(), outChannel);

            inStream.close();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // add new data to the table
    public void addData(ProfileData newUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, newUser.getUsername());
        values.put(COLUMN_PASSWORD, newUser.getPassword());
        values.put(COLUMN_NAME, newUser.getName());
        values.put(COLUMN_ACCURACY, newUser.getAccuracy());
        values.put(COLUMN_REACTION_TIME, newUser.getReaction_time());
        values.put(COLUMN_MEDITATION, newUser.getMeditation());

        db.insert(DATA_TABLE, null, values);
        db.close();
    }

    // delete one data then username becomes available again
    public void deleteData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATA_TABLE, COLUMN_USERNAME + "=?",
                new String[]{username});
        db.close();
    }

    // delete database
    public void deleteDatabase () {
        mContext.deleteDatabase(DATABASE_NAME);
        mDatabaseHandler = null;
        mDatabaseHandler = initHandler(mContext);
    }

    static public String getDirectory (){
        Log.d("dir path", DIR_PATH);
        return DIR_PATH;
    }

    // get all the values
    public ArrayList<ProfileData> getDataList() {
        ArrayList<ProfileData> DataList = new ArrayList<ProfileData>();

        SQLiteDatabase db = this.getWritableDatabase();

        // use cursor to move through the database
        Cursor cursor = db.query(DATA_TABLE, new String[] {
                COLUMN_USERNAME,
                COLUMN_PASSWORD,
                COLUMN_NAME,
                COLUMN_ACCURACY,
                COLUMN_REACTION_TIME,
                COLUMN_MEDITATION
        } , null, null, null, null, null, null);

        // go through the database and add to the array
        if (cursor.moveToFirst()) {
            do {
                ProfileData CurrUser = new ProfileData(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5)
                );
                DataList.add(CurrUser);
            } while (cursor.moveToNext());
        }

        // return the array list
        return DataList;
    }



    // get one value
    public ProfileData getData(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        // use cursor to move through the database
        Cursor cursor = db.query(DATA_TABLE, new String[]{
                COLUMN_USERNAME,
                COLUMN_PASSWORD,
                COLUMN_NAME,
                COLUMN_ACCURACY,
                COLUMN_REACTION_TIME,
                COLUMN_MEDITATION
        }, COLUMN_USERNAME + "=?", new String[]{username
        }, null, null, null, null);


        ProfileData data = null;

        // go through the database and add to the array
        if (cursor.moveToFirst()) {
            data = new ProfileData(
                    cursor.getString(0), //username
                    cursor.getString(1), //password
                    cursor.getString(2),  //name
                    cursor.getDouble(3),  //accuracy
                    cursor.getDouble(4),   // reaction time
                    cursor.getString(5) //meditation
            );
        }

        cursor.close();
        db.close();

        // return the data
        return data;
    }

    // check if name is in database
    public boolean checkUser(ProfileData user){

        boolean itExists = false;
        SQLiteDatabase db = this.getWritableDatabase();

        if (user.getPassword() == null) return itExists;

        Cursor cursor = db.query(DATA_TABLE, new String[]{
                COLUMN_USERNAME,
                COLUMN_PASSWORD,
                COLUMN_NAME,
                COLUMN_ACCURACY,
                COLUMN_REACTION_TIME,
                COLUMN_MEDITATION
        } , COLUMN_USERNAME + "=?", new String[]{ user.getUsername()
        } , null, null, null, null);

        if (cursor.moveToFirst()) {
            Log.d("check user", cursor.getString(1));

            if (cursor.getString(1).equals(user.getPassword())) {
                itExists = true;
            }
        }

        db.close();
        cursor.close();

        return itExists;
    }



    public void updateAccuracy(double value, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ACCURACY, value);
        db.update(DATA_TABLE, cv, COLUMN_USERNAME + "=?", new String[]{username});
    }
    public void updateReactionTime(double value, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_REACTION_TIME, value);
        db.update(DATA_TABLE, cv, COLUMN_USERNAME + "=?", new String[]{username});
    }


    public void updateMeditation(ArrayList<Double> meditation){

        Log.d(TAG+"update", meditation.get(0).toString());
        Log.d(TAG + "update", String.valueOf(meditation.size()));

        Gson gson = new Gson();
        String inputMeditation = gson.toJson(meditation);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEDITATION, inputMeditation);

        Log.d(TAG + "update", inputMeditation);

        db.update(DATA_TABLE, cv, COLUMN_USERNAME + "=?", new String[]{mCurrUser.getUsername()});
        updateCurrUser(mCurrUser.getUsername());
    }

    public void addMeditation(ArrayList<Double> meditation){

        SQLiteDatabase db = this.getWritableDatabase();

        Gson gson = new Gson();
        String inputMeditation = gson.toJson(meditation);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEDITATION, inputMeditation);
        cv.put(COLUMN_USERNAME, mCurrUser.getUsername());

        db.insert(DATA_TABLE, null, cv);

        //db.update(DATA_TABLE, cv, COLUMN_USERNAME + "=?", new String[]{mCurrUser.getUsername()});
        updateCurrUser(mCurrUser.getUsername());

        mCurrUser.incrMeditation();


    }

    public ArrayList<Double> getMeditation(){

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Double>>() {}.getType();

        ArrayList<Double> outputMeditation = gson.fromJson(mCurrUser.getMeditation(), type);

        Log.d(TAG + "get", outputMeditation.get(0).toString());
        Log.d(TAG + "get", String.valueOf(outputMeditation.size()));


        return outputMeditation;
    }
    public ArrayList<ProfileData> getMeditationList() {
        ArrayList<ProfileData> DataList = new ArrayList<ProfileData>();

        String username = getCurrUser().getUsername();

        Log.d("getmeditationlist", username);

        SQLiteDatabase db = this.getWritableDatabase();

        // use cursor to move through the database
        Cursor cursor = db.query(DATA_TABLE, new String[] {
                COLUMN_USERNAME,
                COLUMN_PASSWORD,
                COLUMN_NAME,
                COLUMN_AGE,
                COLUMN_EMAIL,
                COLUMN_DATE,
                COLUMN_FIRST,
                COLUMN_STROOP_INDEX,
                COLUMN_STROOP_COUNT,
                COLUMN_MEDITATION_SESSION_NUM,
                COLUMN_MEDITATION_INDEX,
                COLUMN_MEDITATION_COUNT,
                COLUMN_ACCURACY,
                COLUMN_REACTION_TIME,
                COLUMN_MEDITATION
        } , null, null, null, null, null, null);

        // do not add first element since it does not contain med info
        boolean firstpass = false;

        // go through the database and add to the array
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).matches(username)){
                    if (firstpass == false) firstpass = true;
                    else {
                        ProfileData CurrUser = new ProfileData(
                                cursor.getString(0), // username
                                cursor.getString(1), // password
                                cursor.getString(2), // name
                                cursor.getInt(3), // age
                                cursor.getString(4), // email
                                cursor.getString(5), // date
                                cursor.getInt(6), // first
                                cursor.getInt(7), // stroop index
                                cursor.getInt(8), // stroop count
                                cursor.getInt(9), // meditation session #
                                cursor.getInt(10), // meditation index
                                cursor.getInt(11), // meditation count
                                cursor.getInt(12), // accuracy
                                cursor.getInt(13), // reaction time
                                cursor.getString(14) // meditation
                        );
                        DataList.add(CurrUser);
                    }
                }
            } while (cursor.moveToNext());
        }

        // return the array list
        return DataList;
    }

}