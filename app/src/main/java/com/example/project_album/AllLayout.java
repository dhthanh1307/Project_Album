package com.example.project_album;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class AllLayout extends Fragment {

    // TODO: Rename and change types of parameters
    MainActivity main;
    Context context = null;
    public static ArrayList<Image> images= new ArrayList<Image>();
    ImageAdapter adapter;
    GridView gridView;
    public AllLayout() {
        debug("constructor");
        images = MainActivity.dataResource.getAllImage();
        debug(String.valueOf(images.size()));
    }

    public static AllLayout newInstance(String strArg) {
        AllLayout fragment = new AllLayout();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        debug("onCreate");
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (MainActivity) getActivity();
            initDataResource();

        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        debug("onCreateView");
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all_layout, container, false);
        adapter=new ImageAdapter(main,R.layout.item_image,images);
        gridView = view.findViewById(R.id.gridView);
        DoSthWithOrientation(getResources().getConfiguration().orientation);
        gridView.setAdapter(adapter);
        gridView.setSelection(images.size() - 1);

        return view;
    }
    private void initDataResource() {
        if(images.size() == 0){
            int img[] = {R.drawable.img,R.drawable.img_2,
                    R.drawable.img_3,
                    R.drawable.img_4,R.drawable.img_5,R.drawable.img_6};
            for(int j =0;j<9;j++){
                for(int i = 0; i<6;i++) {
//                    Bitmap image = BitmapFactory.decodeResource(getResources(), img[i]);
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte imageInByte[] = stream.toByteArray();
                    images.add(new Image(main.ChangeImageToByte(img[i])));
                    debug(String.valueOf(j*6+i));
                    images.get(j*6+i).setId(MainActivity.dataResource.
                            InsertImage(images.get(j*6+i), 1));
                    debug(String.valueOf(j*6+i));
                }
            }
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        DoSthWithOrientation(newOrientation);
        adapter.notifyDataSetChanged();
    }

    private void DoSthWithOrientation(int newOrientation ) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.setNumColumns(5);
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            gridView.setNumColumns(3);
        }
        //Toast.makeText(getContext(),"oke",Toast.LENGTH_SHORT).show();
    }
    private void debug(String str){
        Log.e("AllLayout",str);
    }
}