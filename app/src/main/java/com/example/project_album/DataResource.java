package com.example.project_album;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DataResource {
    private SQLiteDatabase database,database1;
    private DatabaseHelper helper;
    private Context context;
    private String[] allColumns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_USER, DatabaseHelper.COLUMN_IMAGE
            , DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_SIZE, DatabaseHelper.COLUMN_DATE,
            DatabaseHelper.COLUMN_TYPE, DatabaseHelper.COLUMN_DESCRIBE,
            DatabaseHelper.COLUMN_IS_DELETE,DatabaseHelper.COLUMN_IS_FAVORITE,DatabaseHelper.COLUMN_IS_HIDE};

    //    private String[] allColumns = {DatabaseHelper.COLUMN_ID,DatabaseHelper.COLUMN_IMAGE
//        ,DatabaseHelper.COLUMN_DATE};
    public DataResource(Context context) {
        this.context  = context;
        helper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
        database1=helper.getReadableDatabase();

    }

    public void close() {
        helper.close();
    }

    //--------------------------- ALBUM SPACE----------------------------

    public long InsertAlbum(String name,int idUser){
        SQLiteDatabase db = database;
        db.beginTransaction();
        long id = -1;
        try{
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME_ALBUM,name);
            values.put(DatabaseHelper.COLUMN_USER,idUser);
            id = database.insert(DatabaseHelper.TABLE_ALBUM,
                    null,values);
            db.setTransactionSuccessful();
            debug("insert sucessfull: "+name);

        }
        catch (Exception ex){
            debug("Error while insert Album");
        }
        finally {
            db.endTransaction();
        }
        return id;
    }

    public long InsertAlbumImage(long idAlbum,long idImage){
        SQLiteDatabase db = database;
        db.beginTransaction();
        long id = -1;
        try{
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_ID_IMAGE, idImage);
            values.put(DatabaseHelper.COLUMN_ID_ALBUM,idAlbum);
            id = database.insert(DatabaseHelper.TABLE_ALBUM_IMAGE,
                    null,values);
            db.setTransactionSuccessful();
            debug("insert sucessfull: "+String.valueOf(idImage));
        }
        catch (Exception ex){
            debug("Error while insert AlbumImage");
        }
        finally {
            db.endTransaction();
        }
        return id;
    }

    public ArrayList<Album> getAllAlbum(int idUser){
        ArrayList<Album> albums = new ArrayList<>();
        String columnAlbum[] = {DatabaseHelper.COLUMN_NAME_ALBUM,DatabaseHelper.COLUMN_ID_ALBUM};
        Cursor cursor = database.query(DatabaseHelper.TABLE_ALBUM,columnAlbum,
                DatabaseHelper.COLUMN_USER+" = "+String.valueOf(idUser),
                null, null, null, null);
        debug(String.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            albums.add(cursorToAlbum(cursor));
            cursor.moveToNext();
            debug("Album length: " +String.valueOf(albums.size()));
        }
        return albums;
    }

    private Album cursorToAlbum(Cursor cursor){
        return getAlbum(cursor.getLong(1),cursor.getString(0));
    }

    public Album getAlbum(long id,String name){
        debug(name);
        ArrayList<Image> images = new ArrayList<>();
        String columnImage_id[] = {DatabaseHelper.COLUMN_ID_IMAGE};
        Cursor cursor = database.rawQuery("select "+DatabaseHelper.COLUMN_ID_IMAGE+
                " from "+DatabaseHelper.TABLE_ALBUM_IMAGE+" where "+
                DatabaseHelper.COLUMN_ID_ALBUM +" = " + String.valueOf(id), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Image i= new Image();
            i.setId(cursor.getLong(0));
            images.add(i);
            debug(String.valueOf(i.getId()));
            cursor.moveToNext();

        }
        return new Album(id,name,images);
    }

    public boolean deleteImageInAlbum(Image image,long idAlbum){
        long id = image.getId();
        Log.e("SQLite","Person entry delete with id: "+id);
        try {
            database.delete(DatabaseHelper.TABLE_ALBUM_IMAGE,
                    DatabaseHelper.COLUMN_ID_IMAGE + " = " + String.valueOf(id) + " and "+
                            DatabaseHelper.COLUMN_ID_ALBUM +" = " +String.valueOf(idAlbum),
                    null);
            debug("Remove Successfull: "+String.valueOf(image.getId()));
            return true;
        }
        catch (Exception ex){
            debug("Exception while delete");
            return false;
        }

    }

    public boolean deleteAlbum(long idAlbum){
        database.beginTransaction();
        try {
            database.delete(DatabaseHelper.TABLE_ALBUM,
                    DatabaseHelper.COLUMN_ID_ALBUM
                            + " = " + String.valueOf(idAlbum),
                    null);
            database.delete(DatabaseHelper.TABLE_ALBUM_IMAGE,
                    DatabaseHelper.COLUMN_ID_ALBUM
                            + " = " + String.valueOf(idAlbum),
                    null);
            database.setTransactionSuccessful();
            return true;
        }
        catch (Exception ex){
            debug("Exception with album "+String.valueOf(idAlbum));
            return false;
        }
        finally {
            database.endTransaction();
        }

    }
    public void updateName(String oldName,String newName,int idUser){
        ContentValues key = new ContentValues();
        key.put(DatabaseHelper.COLUMN_NAME_ALBUM,newName);
        database.update(DatabaseHelper.TABLE_ALBUM,key,
                DatabaseHelper.COLUMN_NAME_ALBUM +" = '"+oldName+"'"
                        +" and "+ DatabaseHelper.COLUMN_USER +" = "+String.valueOf(idUser),
                null);
    }
    //-------------------------------finish album space------------------------------------

    public long InsertUser(User user) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
            values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPass());
            values.put(DatabaseHelper.COLUMN_PHONE, user.getPhone());
            values.put(DatabaseHelper.COLUMN_EMAIL,user.getEmail());
            values.put(DatabaseHelper.COLUMN_NICKNAME,user.getUsername());
            long insertId = database.insert(DatabaseHelper.TABLE_USERS,
                    null, values);
            return insertId;
        } catch (Exception ex) {
            return -1;
        }
    }
    public long InsertImage(Image image, int USER) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER, USER);
            values.put(DatabaseHelper.COLUMN_IMAGE, helper.PATH+"/"+image.getName());
            values.put(DatabaseHelper.COLUMN_NAME, image.getName());
            values.put(DatabaseHelper.COLUMN_SIZE, image.getSize());

            SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
            values.put(DatabaseHelper.COLUMN_DATE, ft.format(image.getDate()));
            values.put(DatabaseHelper.COLUMN_TYPE, image.getType());
            values.put(DatabaseHelper.COLUMN_DESCRIBE, image.getDescribe());
            values.put(DatabaseHelper.COLUMN_IS_DELETE, image.getDeleted());
            values.put(DatabaseHelper.COLUMN_IS_FAVORITE, image.getFavorite());
            values.put(DatabaseHelper.COLUMN_IS_HIDE, image.getHide());

            long insertId = database.insert(DatabaseHelper.TABLE_PICTURE, null, values);

            //luu image vao internal storage
            ContextWrapper cw = new ContextWrapper(context);
            File file = new File(helper.directory, image.getName());
            if (!file.exists()) {
                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    image.getImgBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

            return insertId;
        } catch (Exception ex) {
            return -1;
        }
    }
    public void unlikeImage(long id){
        String que= "UPDATE " +DatabaseHelper.TABLE_PICTURE +" SET "
                +DatabaseHelper.COLUMN_IS_FAVORITE + " = 'F'" +
                " WHERE " +DatabaseHelper.COLUMN_ID +" = "+String.valueOf(id);
        database.execSQL(que);
    }
    public void likeImage(long id){
        String que= "UPDATE " +DatabaseHelper.TABLE_PICTURE +" SET "
                +DatabaseHelper.COLUMN_IS_FAVORITE + " = 'T'" +
                " WHERE " +DatabaseHelper.COLUMN_ID +" = "+String.valueOf(id);
        database.execSQL(que);
    }
    // này là update trạng thái trong database khi chuyển từ all sang Hide bên album thôi
    public void updateStateImageHideIsTrue(long id){
        String que= "UPDATE " +DatabaseHelper.TABLE_PICTURE +" SET "
                +DatabaseHelper.COLUMN_IS_HIDE+ " = 'T'" +
                " WHERE " +DatabaseHelper.COLUMN_ID +" = "+String.valueOf(id);
        database.execSQL(que);
    }
    public void updateStateImageHideIsFalse(long id){
        String que= "UPDATE " +DatabaseHelper.TABLE_PICTURE +" SET "
                +DatabaseHelper.COLUMN_IS_HIDE+ " = 'F'" +
                " WHERE " +DatabaseHelper.COLUMN_ID +" = "+String.valueOf(id);
        database.execSQL(que);
    }
    ////////////////////
    // này là update trạng thái trong database khi chuyển từ all sang trash thôi
    public void updateStateImageDeletedIsTrue(long id){
        String que= "UPDATE " +DatabaseHelper.TABLE_PICTURE +" SET "
                +DatabaseHelper.COLUMN_IS_DELETE+ " = 'T'" +
                " WHERE " +DatabaseHelper.COLUMN_ID +" = "+String.valueOf(id);
        database.execSQL(que);
    }
    public void updateStateImageDeletedIsFalse(long id){
        String que= "UPDATE " +DatabaseHelper.TABLE_PICTURE +" SET "
                +DatabaseHelper.COLUMN_IS_DELETE+ " = 'F'" +
                " WHERE " +DatabaseHelper.COLUMN_ID +" = "+String.valueOf(id);
        database.execSQL(que);
    }
    ////////////////////
    //Này là delete ở database, xóa hẳn luôn.
    public boolean deleteImage(Image image){
        long id = image.getId();
        Log.e("SQLite","Person entry delete with id: "+id);
        try {
            File file = new File(image.getPath());
            file.delete();
            database.delete(DatabaseHelper.TABLE_PICTURE, DatabaseHelper.COLUMN_ID + " = " + id,
                    null);
            return true;
        }
        catch (Exception ex){
            return false;
        }

    }
    public ArrayList<Image> getAllImage(int userID) {
        ArrayList<Image> list = new ArrayList<Image>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_PICTURE, allColumns,
                DatabaseHelper.COLUMN_USER+" = "+String.valueOf(userID),
                null, null, null, null);
//        Cursor c = database.rawQuery("select ")
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
        image.setPath(cursor.getString(2));
        image.setType(cursor.getString(6));
        image.setDescribe(cursor.getString(7));
        image.setDeleted(cursor.getString(8));
        image.setFavorite(cursor.getString(9));
        image.setHide(cursor.getString(10));
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

    public int checkLogin(String username, String password) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER},
                DatabaseHelper.COLUMN_USERNAME + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);
        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            cursor.moveToFirst();
            int userIDColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER);
            if (userIDColumnIndex != -1) {
                int userID = cursor.getInt(userIDColumnIndex);
                cursor.close();
                return userID;
            }
        }
        cursor.close();
        return -1;
    }

    public boolean checkSignUp(String name) {
        String mySQL = "SELECT username FROM users WHERE username = ?";
        Cursor cursor = database.rawQuery(mySQL, new String[]{name});
        int cursorCount = cursor.getCount();
        cursor.close();
        if (cursorCount == 0) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getAccountInfo(int userID) {
        ArrayList<String> userInfo = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, new String[] { DatabaseHelper.COLUMN_NICKNAME, DatabaseHelper.COLUMN_PASSWORD }, DatabaseHelper.COLUMN_USER + "=?", new String[] { String.valueOf(userID) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            userInfo.add(cursor.getString(0));
            userInfo.add(cursor.getString(1));
            cursor.close();
        }
        return userInfo;
    }

    public void updateAccountInfo(int userID, String newNickname, String newPassword) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NICKNAME, newNickname);
        values.put(DatabaseHelper.COLUMN_PASSWORD, newPassword);
        database1.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_USER + "=?", new String[] { String.valueOf(userID) });
    }
    public int countUser() {
        String query = "SELECT  * FROM " + DatabaseHelper.TABLE_USERS;
        Cursor cursor = database.rawQuery(query, null);
        int count= cursor.getCount();
        cursor.close();
        return count;
    }

    private void debug(String str) {
        Log.e("DataResource", str);
    }
}
