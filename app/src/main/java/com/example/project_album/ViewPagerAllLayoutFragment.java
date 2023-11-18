package com.example.project_album;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ViewPagerAllLayoutFragment extends Fragment {

    ArrayList<Image> images = new ArrayList<>();
    ViewPager2 mViewPager;
    TextView txtTimeCapture;
    ImageView txtInformation;

    ImageView txtDeleteIntoTrashCan;
    ImageView imgBack;
    ImageView txtFavorite;

    ImageView txtShare;

    ImageView txtEdit;

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
        //Chỗ này là imageView mà sa chwua đổi txt tên ở id bên xml á.
        txtTimeCapture=view.findViewById(R.id.edt_time_capture);
        txtDeleteIntoTrashCan=view.findViewById(R.id.txt_delete_into_trashcan);
        txtFavorite=view.findViewById(R.id.txt_favorite);
        txtInformation=view.findViewById(R.id.txt_information);
        txtShare=view.findViewById(R.id.txt_share);
        txtEdit=view.findViewById(R.id.txt_edit);
        imgBack=view.findViewById(R.id.img_back);


        mViewPager = view.findViewById(R.id.viewpager_in_all_layout);
        // chỗ nay tan dung lai Adapter ben TrashCan khoi can tao moi
        mAdapter = new ViewPagerInTrashCanAdapter(main.getSupportFragmentManager(), main.getLifecycle(), images);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(index,false);
        txtTimeCapture.setText(String.valueOf(index + 1) + "/" + String.valueOf(images.size()));
        main.mBottomNavigationView.setVisibility(View.GONE);
        if (images.get(index).getFavorite().equals("T")){
            txtFavorite.setImageResource(R.drawable.icon_fill_favorite_in_all_layout);
        }else{
            txtFavorite.setImageResource(R.drawable.icon_favorite_in_alllayout);
        }
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                index = position;
                txtTimeCapture.setText(String.valueOf(index + 1) + "/" + String.valueOf(images.size()));
                if (images.get(index).getFavorite().equals("T")){
                    txtFavorite.setImageResource(R.drawable.icon_fill_favorite_in_all_layout);
                }else{
                    txtFavorite.setImageResource(R.drawable.icon_favorite_in_alllayout);
                }
            }
        });










        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main, "Share", Toast.LENGTH_SHORT).show();
                Uri UriToShare = saveImage(images.get(index).getImgBitmap());
                shareImageUri(UriToShare);
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
                if (images.get(index).getFavorite().equals("T")){
                    images.get(index).setFavorite("F");
                    txtFavorite.setImageResource(R.drawable.icon_favorite_in_alllayout);
                    MainActivity.dataResource.unlikeImage(images.get(index).getId());
//                    FavoriteLayout.mGridAdapter.setmSelectedArray();
                }else{
                    images.get(index).setFavorite("T");
                    txtFavorite.setImageResource(R.drawable.icon_fill_favorite_in_all_layout);
                    MainActivity.dataResource.likeImage(images.get(index).getId());
//                    FavoriteLayout.mGridAdapter.setmSelectedArray();
                }
            }
        });

        txtInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main, "Information", Toast.LENGTH_SHORT).show();
            }
        });
        //PHAN EDIT
        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.replace_fragment_layout, new EditFragment(images,index));
                ft.commit();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                if (main.getIDItemBottomNavigationView()==R.id.action_favorite){
                    ft.replace(R.id.replace_fragment_layout,new FavoriteLayout(images));
                }else if(main.getIDItemBottomNavigationView()==R.id.action_all_picture){
                    ft.replace(R.id.replace_fragment_layout, new AllLayout(images));
                }
                ft.commit();
                main.mBottomNavigationView.setVisibility(View.VISIBLE);
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

    private Uri saveImage(Bitmap image) {
        File imagesFolder = new File(main.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(main, "com.example.fileprovider", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void shareImageUri(Uri uri){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        main.startActivity(intent);
    }
}