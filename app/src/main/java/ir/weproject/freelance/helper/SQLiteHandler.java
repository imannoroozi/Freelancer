package ir.weproject.freelance.helper;

/**
 * Created by Iman on 10/22/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ir.weproject.freelance.ir.weproject.poem.objects.Skill;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    //my skills table name
    private static final String TABLE_MY_SKILLS = "my_skills";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_CREATED_AT = "created_at";

    //my skills table
    private static final String KEY_PARENT_ID = "parent_id";
    private static final String KEY_IS_CATEGORY = "is_category";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        //create login table
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        //create my skills table
        String CREATE_MY_SKILLS_TABLE = "CREATE TABLE " + TABLE_MY_SKILLS + "("
                + KEY_ID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_PARENT_ID + " INTEGER,"
                + KEY_IS_CATEGORY + " TEXT" + ")";
        db.execSQL(CREATE_MY_SKILLS_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_SKILLS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addSkill(int id, String name, int parentID, String isCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PARENT_ID, parentID); // parent ID
        values.put(KEY_IS_CATEGORY, isCategory); // is category?

        // Inserting Row
        db.insert(TABLE_MY_SKILLS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New skill inserted into sqlite: " + id);
    }

    public void updateUser( String name, String email, String uid, String imageURL ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_IMAGE_URL, imageURL);

        // Inserting Row
        db.update(TABLE_USER, values, KEY_UID + "=" + uid, null);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public ArrayList<Skill> getMySkills() {
        ArrayList<Skill> retVal = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MY_SKILLS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Skill skill = new Skill();
                skill.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                skill.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                skill.setParentID(cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID)));
                skill.setCategory(cursor.getString(cursor.getColumnIndex(KEY_IS_CATEGORY)).equalsIgnoreCase("Y") ? true : false);

                retVal.add(skill);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return retVal;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteSkills() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_MY_SKILLS, null, null);
        db.close();
    }
}
