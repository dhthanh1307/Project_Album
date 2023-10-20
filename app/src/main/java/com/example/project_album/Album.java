package com.example.project_album;

import java.util.ArrayList;

public class Album {
    private String name;
    private ArrayList<Integer> images;
    public Album(String name,ArrayList<Integer> images){
        this.name= name;
        this.images = images;

    }

    public String getName(){
        return name;
    }
    public ArrayList<Integer> getImages(){
        return images;
    }
    public int get_image(int position){
         return Integer.parseInt(images.get(position).toString());
    }
    public void setName(String name){
        this.name = name;
    }
    public void removeImage(int index){
        images.remove(index);
    }
    public void addImage(int index,int image){
        images.add(index,image);
    }
    public void clear(){
        images.clear();
    }
    public int length(){
        return images.size();
    }
}
