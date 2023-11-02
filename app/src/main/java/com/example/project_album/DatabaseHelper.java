package com.example.project_album;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="manage_account.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_PICTURE="picture";
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DESCRIBE = "describe";
    public static final String COLUMN_IS_DELETE = "is_delete";// chỗ này nếu true thì sẽ k hiển thị ở all layout
    //mà chỉ hiển thị ở trash can, biến này kiểu trong SQL là text



    private static final String DATABASE_CREATE = "create table "+TABLE_PICTURE+"( "+COLUMN_ID
            +" integer primary key autoincrement, "
            +COLUMN_USER
            +" integer, "
            +COLUMN_IMAGE
            +" BLOB, "
            +COLUMN_NAME
            +" text not null, "
            +COLUMN_SIZE
            +" float, "
            +COLUMN_DATE
            +" text not null, "
            +COLUMN_TYPE
            +" text not null, "
            +COLUMN_IS_DELETE
            +" text not null, "
            +COLUMN_DESCRIBE
            +" text);";
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.v(DatabaseHelper.class.getName(),
                "Upgrading database from version " + i +" to "+i1
                        +", which will destroy all data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_PICTURE);
        onCreate(sqLiteDatabase);
    }
}
