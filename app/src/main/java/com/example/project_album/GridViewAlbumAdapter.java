package com.example.project_album;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAlbumAdapter extends BaseAdapter {
    private Activity activity;
    private int idLayout;
    private ArrayList<Album> albums;
    public GridViewAlbumAdapter(Activity activity, int idLayout, ArrayList<Album> albums ){
        this.activity = activity;
        this.idLayout = idLayout;
        this.albums = albums;
    }
    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int i) {
        return albums.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =activity.getLayoutInflater().inflate(idLayout,null);
        ImageView img = view.findViewById(R.id.image);
        TextView tv_name = view.findViewById(R.id.tv_album_name);
        TextView tv_length = view.findViewById(R.id.tv_album_length);
        if(albums.get(i).length() != 0 ){
            img.setImageResource(albums.get(i).get_image(albums.get(i).length() -1 ));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else{
            img.setScaleType(ImageView.ScaleType.CENTER);

        }
        tv_name.setText(albums.get(i).getName());
        tv_length.setText(String.valueOf(albums.get(i).length()));
        return view;
    }
}
