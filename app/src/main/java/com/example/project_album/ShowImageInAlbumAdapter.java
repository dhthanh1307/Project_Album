package com.example.project_album;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowImageInAlbumAdapter extends RecyclerView.Adapter<ShowImageInAlbumAdapter.ViewHolder> {
    private ArrayList<Image> images;
    private int idLayout;
    private Activity activity;
    private ArrayList<Boolean> ischoose = new ArrayList<>();
    private boolean choose_selection;

    ShowImageInAlbumAdapter(Activity activity,int idLayout,ArrayList<Image>images){
        this.activity = activity;
        this.idLayout = idLayout;
        this.images = images;
        for(int i = 0;i<images.size();i++){
            ischoose.add(false);
        }
    }
    @NonNull
    @Override
    public ShowImageInAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(idLayout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowImageInAlbumAdapter.ViewHolder holder, int position) {
        holder.img.setImageBitmap(images.get(position).getImgBitmap());
        debug(String.valueOf(position));
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            debug("ok");
            RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams
                    (MainActivity.Width / 3 - 40, MainActivity.Width / 3 - 40);
            layout.setMargins(0,0,0,10);
            holder.img.setLayoutParams(layout);

        }
        else {
            RelativeLayout.LayoutParams layout =new RelativeLayout.LayoutParams(
                    MainActivity.Height / 5 - 30, MainActivity.Height / 5 - 30);
            layout.setMargins(0,0,0,10);
            holder.img.setLayoutParams(layout);
        }

        if(!ischoose.get(position)){
            holder.cb.setChecked(false);
            holder.cb.setVisibility(View.INVISIBLE);
        }
        else{
            holder.cb.setChecked(true);
            holder.cb.setVisibility(View.VISIBLE);
        }
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(choose_selection){
                    if(ischoose.get(position)){
                        ischoose.set(position,false);
                        holder.cb.setChecked(false);
                        holder.cb.setVisibility(View.INVISIBLE);
                    }
                    else{
                        ischoose.set(position,true);
                        holder.cb.setChecked(true);
                        holder.cb.setVisibility(View.VISIBLE);
                    }
                }
                else{

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private CheckBox cb;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            cb = itemView.findViewById(R.id.check_box);
            cb.setVisibility(View.INVISIBLE);
        }
    }

    private void debug(String s){
        Log.e("Show image in album adapter: ",s);
    }
    public void setChooseSelection(boolean check){
        choose_selection = check;
    }
    public void resetChooseSelection(){
        for(int i=0;i<images.size();i++){
            ischoose.set(i,false);
        }
    }
}
