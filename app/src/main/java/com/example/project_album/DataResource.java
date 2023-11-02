package com.example.project_album;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DataResource {
    private SQLiteDatabase database,database1;
    private DatabaseHelper helper;
    private String[] allColumns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_USER, DatabaseHelper.COLUMN_IMAGE
            , DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_SIZE, DatabaseHelper.COLUMN_DATE,
            DatabaseHelper.COLUMN_TYPE, DatabaseHelper.COLUMN_DESCRIBE, DatabaseHelper.COLUMN_IS_DELETE};

    //    private String[] allColumns = {DatabaseHelper.COLUMN_ID,DatabaseHelper.COLUMN_IMAGE
//        ,DatabaseHelper.COLUMN_DATE};
    public DataResource(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
        database1=helper.getReadableDatabase();

    }

    public void cloe() {
        helper.close();
    }

    public long InsertImage(Image image, int USER) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER, USER);
            values.put(DatabaseHelper.COLUMN_IMAGE, image.getImgView());
            values.put(DatabaseHelper.COLUMN_NAME, image.getName());
            values.put(DatabaseHelper.COLUMN_SIZE, image.getSize());

            SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
            values.put(DatabaseHelper.COLUMN_DATE, ft.format(image.getDate()));
            values.put(DatabaseHelper.COLUMN_TYPE, image.getType());
            values.put(DatabaseHelper.COLUMN_DESCRIBE, image.getDescribe());
            values.put(DatabaseHelper.COLUMN_IS_DELETE, image.getDeleted());

            long insertId = database.insert(DatabaseHelper.TABLE_PICTURE, null, values);
//            Cursor cursor =database.query(DatabaseHelper.TABLE_PICTURE,allColumns,DatabaseHelper.COLUMN_ID
//                    +" = "+ insertId,null,null,null,null);
//            cursor.moveToFirst();
//            MyPerson newPerson = cursorToPerson(cursor);
//            cursor.close();
            return insertId;
        } catch (Exception ex) {
            return -1;
        }
    }
    public boolean deleteImage(Image image){
        long id = image.getId();
        Log.e("SQLite","Person entry delete with id: "+id);
        try {
            database.delete(DatabaseHelper.TABLE_PICTURE, DatabaseHelper.COLUMN_ID + " = " + id,
                    null);
            return true;
        }
        catch (Exception ex){
            return false;
        }

    }

    public boolean deleteArrayImage(ArrayList<Long> arrayIdDelete) {
        //Log.e("SQLite","Person entry delete with id: "+id);
        try {
            for (int i = 0; i < arrayIdDelete.size(); i++) {
                database.delete(DatabaseHelper.TABLE_PICTURE, DatabaseHelper.COLUMN_ID + " = " + arrayIdDelete.get(i),
                        null);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }

    }
    public ArrayList<Image> getImageInDbHasIsDeletedIsTrue(){
        Log.e("Err","Loi o day1");
        ArrayList<Image> list = new ArrayList<Image>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_PICTURE, allColumns, DatabaseHelper.COLUMN_IS_DELETE + "='T'",
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToImage(cursor));
            cursor.moveToNext();
        }
        Log.e("Err","Loi o day3");
        return list;
    }
    public ArrayList<Image> getAllImage() {
        ArrayList<Image> list = new ArrayList<Image>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_PICTURE, allColumns, null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToImage(cursor));
            cursor.moveToNext();
        }
        return list;
    }

    private Image cursorToImage(Cursor cursor) {
        ;
        Image image = new Image();
        image.setId(cursor.getLong(0));
        image.setName(cursor.getString(3));
        image.setSize(cursor.getFloat(4));
        image.setImgView(cursor.getBlob(2));
        image.setType(cursor.getString(6));
        image.setDescribe(cursor.getString(7));
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            image.setDate(df.parse(cursor.getString(5)));
        } catch (Exception ex) {
            Log.e("DataResource", "oke");
        }
        return image;
    }

    public int getCount() {
        Cursor cursor = database.rawQuery("select* from " + DatabaseHelper.TABLE_PICTURE, null);
        return cursor.getCount();
    }

    private void debug(String str) {
        Log.e("DataResource", str);
    }
}
