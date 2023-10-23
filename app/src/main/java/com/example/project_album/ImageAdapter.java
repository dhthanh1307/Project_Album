package com.example.project_album;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Image> images;
    private int idLayout;
    private MainActivity activity;
    public ImageAdapter(MainActivity activity,int idLayout,ArrayList<Image> images){
        this.images = images;
        this.activity = activity;
        this.idLayout = idLayout;
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        view = activity.getLayoutInflater().inflate(idLayout,null);
//        ImageView img = view.findViewById(R.id.img);
//        img.setImageBitmap(images.get(i).getBitmapImage());
//        LinearLayout layout= (LinearLayout)view.findViewById(R.id.parent);
//        if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//            img.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.Width/3 - 40,MainActivity.Width/3 -40));
//        else{
//            img.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.Height/5 - 30,MainActivity.Height/5 -30));
//        }
        ImageHolder holder = null;
        if(view == null)
        {
            Log.e("ImageAdapter",String.valueOf(i));
            view = activity.getLayoutInflater().inflate(idLayout,null);
            holder = new ImageHolder();
            holder.img = (ImageView) view.findViewById(R.id.img);
            if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                holder.img.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.Width/3 - 40,MainActivity.Width/3 -40));
            else{
                holder.img.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.Height/5 - 30,MainActivity.Height/5 -30));
            }
            view.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)view.getTag();
        }
        holder.img.setImageBitmap(ChangeByteToBitmap(images.get(i).getImgView()));

        return view;
    }
    static class ImageHolder{
        ImageView img;

    }
    public Bitmap ChangeByteToBitmap(byte[] outImage){
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        return theImage;
    }

}
