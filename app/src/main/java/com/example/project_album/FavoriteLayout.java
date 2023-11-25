package com.example.project_album;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteLayout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteLayout extends Fragment {

    MainActivity main;
    Context context = null;
    public static ArrayList<Image> images = new ArrayList<Image>();
    ImageAdapter adapter;
    private ImageAdapter image_adapter;
    private GridView gridviewImage;
    public FavoriteLayout() {
        debug("Constructor");
        // Required empty public constructor
    }

    public static FavoriteLayout newInstance(String strArg) {
        FavoriteLayout fragment = new FavoriteLayout();
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
            if(images.size() ==0){
                for(int i = 0;i<20;i++){
                    images.add(AllLayout.images.get(i));
                }
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        debug("onCreateView");
        // Inflate the layout for this fragment
<<<<<<< Updated upstream
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_layout, container, false);
        gridviewImage = (GridView)view.findViewById(R.id.gridView);
        image_adapter = new ImageAdapter(main,R.layout.item_image,images);


=======
        //Khởi tạo lại danh sách ưa thích
//        for (int i = 0; i < AllLayout.images.size(); i++) {
//            if (AllLayout.images.get(i).getFavorite().equals("T")) {
//                Log.e("fix loi","anh ="+String.valueOf(AllLayout.images.get(i).getId()));
//                images.add(AllLayout.images.get(i));
//            }
//        }
        Log.e("DEBUG", "onCreateView of TrashCan");
        View mView = inflater.inflate(R.layout.fragment_favorite_layout, container, false);
        mGridView = mView.findViewById(R.id.grid_view_favorite);
        txtTotal = mView.findViewById(R.id.txt_display_total_picture_favorite);
        btnChoose = mView.findViewById(R.id.btn_choose_in_favorite);
        btnUnFavoriteChosenImages = mView.findViewById(R.id.btn_no_favorite);
        btnShareChosenImages = mView.findViewById(R.id.btn_share_in_favorite_layout);
        lastLinear = mView.findViewById(R.id.last_linear_in_favorite);
        txtFavoriteRecently = mView.findViewById(R.id.txt_favorite_recently);
>>>>>>> Stashed changes
        DoSthWithOrientation(getResources().getConfiguration().orientation);
        gridviewImage.setAdapter(image_adapter);
        gridviewImage.setSelection(images.size()-1) ;

        return view;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        DoSthWithOrientation(newOrientation);
        image_adapter.notifyDataSetChanged();
    }

    private void DoSthWithOrientation(int newOrientation ) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridviewImage.setNumColumns(5);
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            gridviewImage.setNumColumns(3);
        }
        //Toast.makeText(getContext(),"oke",Toast.LENGTH_SHORT).show();
    }
    private void debug(String str){
        Log.e("FavoriteLayout",str);
    }
<<<<<<< Updated upstream
=======
    public void update(){
        try {
            mGridAdapter.notifyDataSetChanged();
        }
        catch(Exception e){

        }
    }
    public void updateFavorite(Image img){
        if (img.getFavorite().equals("F")) {
            images.add(img);
        } else {
            images.remove(img);
        }
    }
    public void updateArrayFavorite(ArrayList<Image> imgs){
        for(int i = 0;i<imgs.size();i++){
            if (imgs.get(i).getFavorite().equals("F")){
                images.add(imgs.get(i));
            }
        }
    }
>>>>>>> Stashed changes
}