package com.example.project_album;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class AllLayout extends Fragment {

    // TODO: Rename and change types of parameters
    MainActivity main;
    Context context = null;
    public static ArrayList<Image> images= new ArrayList<Image>();
    ImageAdapter adapter;
    GridView gridView;
    public AllLayout() {
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
        super.onCreate(savedInstanceState);
        images.add(new Image(R.drawable.img));
        for(int i = 0;i<3;i++) {
            images.add(0,new Image(R.drawable.img));
            images.add(0,new Image(R.drawable.img_1));
            images.add(0,new Image(R.drawable.img_2));
            images.add(0,new Image(R.drawable.img_3));
            images.add(0,new Image(R.drawable.img_4));
            images.add(0,new Image(R.drawable.img_5));
            images.add(0,new Image(R.drawable.img_6));
        }
        try {
            context = getActivity();
            main = (MainActivity) getActivity();

        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all_layout, container, false);
        adapter=new ImageAdapter(main,R.layout.item_image,images);
        gridView = view.findViewById(R.id.gridView);
        DoSthWithOrientation(getResources().getConfiguration().orientation);
        gridView.setAdapter(adapter);
        gridView.setSelection(images.size() - 1);
        return view;
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
}