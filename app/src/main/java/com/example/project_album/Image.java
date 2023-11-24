package com.example.project_album;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Image {
    private long id;
    private String path;
    private String name;
    private float size;
    private Date date;
    private String type;
    private String describe;
    private String is_deleted;
    private String is_favorite;
    private Bitmap imgBitmap;
    public Image(){
        imgBitmap = null;
    }

    public Image(Bitmap imgbitmap,String name) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imgbitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        this.size= imageInByte.length / 1024;
        Log.e("Image",String.valueOf(size));
        this.type = "";
        this.name=name;
        this.id=0;
        this.date = new Date();
        this.describe = "";
        this.imgBitmap = imgbitmap;
        this.is_deleted="F";
        this.is_favorite="F";
    }


    public Bitmap getImgBitmap(){return imgBitmap;}
    public String getPath(){
        return path;
    }
    public String getName(){
        return name;
    }
    public long getId(){
        return id;
    }
    public float getSize(){
        return size;
    }
    public Date getDate(){
        return date;
    }
    public String getType(){
        return type;
    }
    public String getDescribe(){
        return describe;
    }
    public String getDeleted() {
        return is_deleted;
    }
    public void setPath(String path){
        this.path = path;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setSize(float size){
        this.size = size;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setDescribe(String describe){
        this.describe = describe;
    }
    public void setId(long id){
        this.id = id;
    }
    void setImgBitmap(Bitmap img){
        imgBitmap = img;
    }

    public void setDeleted(String deleted) {
        this.is_deleted = deleted;
    }

    public void setFavorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getFavorite() {
        return is_favorite;
    }

    private Bitmap ChangeByteToBitmap(byte[] outImage ) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        return theImage;
    }
    public String GenerateName(){
        return System.currentTimeMillis() + ".jpeg";
    }
}