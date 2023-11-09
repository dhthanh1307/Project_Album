package com.example.project_album;

import java.util.ArrayList;

public class Album {
    private String name;
    private ArrayList<Image> images;
    public Album(String name,ArrayList<Image> images){
        this.name= name;
        this.images = images;

    }

    public String getName(){
        return name;
    }
    public ArrayList<Image> getImages(){
        return images;
    }
    public Image get_image(int position){
        return images.get(position);
    }
    public void setName(String name){
        this.name = name;
    }
    public void removeImage(int index){
        images.remove(index);
    }
    public void addImage(int index,Image image){
        images.add(index,image);
    }
    public void clear(){
        images.clear();
    }
    public int length(){
        return images.size();
    }
}