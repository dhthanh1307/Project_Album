package com.example.project_album;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class ViewPagerAllLayoutFragment extends Fragment {

    ArrayList<Image> images = new ArrayList<>();
    ViewPager2 mViewPager;
    TextView txtTimeCapture;
    TextView txtInformation;

    TextView txtDeleteIntoTrashCan;

    TextView txtFavorite;

    TextView txtShare;

    MainActivity main;
    Context context = null;
    ViewPagerInTrashCanAdapter mAdapter;
    int index = 0;//Vị trí được chọn hiện tại

    public ViewPagerAllLayoutFragment(ArrayList<Image> imgs, int index) {
        this.images = imgs;
        this.index = index;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.fragment_view_pager_all_layout, container, false);

        txtTimeCapture=view.findViewById(R.id.edt_time_capture);
        txtDeleteIntoTrashCan=view.findViewById(R.id.txt_delete_into_trashcan);
        txtFavorite=view.findViewById(R.id.txt_favorite);
        txtInformation=view.findViewById(R.id.txt_information);
        txtShare=view.findViewById(R.id.txt_share);


        mViewPager = view.findViewById(R.id.viewpager_in_all_layout);
        // chỗ nay tan dung lai Adapter ben TrashCan khoi can tao moi
        mAdapter = new ViewPagerInTrashCanAdapter(main.getSupportFragmentManager(), main.getLifecycle(), images);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(index,false);
        txtTimeCapture.setText(String.valueOf(index + 1) + "/" + String.valueOf(images.size()));

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                index = position;
                txtTimeCapture.setText(String.valueOf(index + 1) + "/" + String.valueOf(images.size()));
            }
        });
        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main, "Share", Toast.LENGTH_SHORT).show();
            }
        });
        txtDeleteIntoTrashCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main, "Delete", Toast.LENGTH_SHORT).show();
            }
        });
        txtFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main, "Favorite", Toast.LENGTH_SHORT).show();
            }
        });

        txtInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main, "Information", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        int newOrientation=newConfig.orientation;
        doSthWithOrientation(newOrientation);
    }

    private void doSthWithOrientation(int newOrientation){
        if(newOrientation==Configuration.ORIENTATION_LANDSCAPE){

        }else if(newOrientation==Configuration.ORIENTATION_PORTRAIT){

        }
    }
}