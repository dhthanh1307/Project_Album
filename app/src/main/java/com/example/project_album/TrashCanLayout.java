package com.example.project_album;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrashCanLayout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrashCanLayout extends Fragment {
    ImageAdapter mGridAdapter;
    public static ArrayList<Image> images = new ArrayList<Image>();
    MainActivity main;
    Context context = null;
    TextView txtTrash;
    GridView mGridView;
    public TrashCanLayout() {
        // Required empty public constructor
    }

    public static TrashCanLayout newInstance(String strArg) {
        TrashCanLayout fragment = new TrashCanLayout();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for(int i = 0;i<20;i++){
            images.add(new Image(R.drawable.img));
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
        View mView = inflater.inflate(R.layout.fragment_trash_can_layout, container, false);
        txtTrash = mView.findViewById(R.id.txt_bin);
        mGridView=mView.findViewById(R.id.grid_view);
        mGridAdapter=new ImageAdapter(main,R.layout.item_image,images);
        mGridView.setAdapter(mGridAdapter);
        return mView;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        DoSthWithOrientation(newOrientation);
        mGridAdapter.notifyDataSetChanged();
    }

    private void DoSthWithOrientation(int newOrientation ) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setNumColumns(5);
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridView.setNumColumns(3);
        }
        //Toast.makeText(getContext(),"oke",Toast.LENGTH_SHORT).show();
    }
}