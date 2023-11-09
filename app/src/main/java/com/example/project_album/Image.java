package com.example.project_album;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.util.Date;

public class Image {
    private long id;
    private byte[] imgView;
    private String name;
    private float size;
    private Date date;
    private String type;
    private String describe;
    private String is_deleted;
    private Bitmap imgBitmap;
    public Image(){

    }

    public Image(byte[] imgView) {
        this.imgView = imgView;
        this.size = 0;
        this.type = "";
        this.name="";
        this.id=0;
        this.date = new Date();
        this.describe = "";
        this.imgBitmap = ChangeByteToBitmap(imgView);
        this.is_deleted="T";
    }


    public Bitmap getImgBitmap(){return imgBitmap;}
    public byte[] getImgView(){
        return imgView;
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
    public void setImgView(byte[] imgView){
        this.imgView = imgView;
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
    private Bitmap ChangeByteToBitmap(byte[] outImage ) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        return theImage;
    }

}