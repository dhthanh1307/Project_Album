package com.example.project_album;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    public static DataResource dataResource;
    public static int Width;
    public static int Height;
    NavigationView navigationView;
    FragmentTransaction ft;
    AllLayout allLayout;
    AlbumLayout albumLayout;
    FavoriteLayout favoriteLayout;
    TrashCanLayout trashCanLayout;
    AccountLayout accountLayout;
    MenuLayout menuLayout;
    View menu;
    BottomNavigationView mBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver2);
        dataResource = new DataResource(this);
        dataResource.open();
        getSizeWindow();
        initUI();


//
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.NavigationView, menuLayout);
//        ft.commit();
    }
    @Override
    protected void onResume(){
        dataResource.open();
        super.onResume();
    }
    @Override
    protected void onPause(){
        dataResource.cloe();
        super.onPause();
    }

    private void initUI() {
        menu = getLayoutInflater().inflate(R.layout.fragment_menu, null);

        accountLayout = AccountLayout.newInstance("account");
        allLayout = AllLayout.newInstance("all");
        albumLayout = AlbumLayout.newInstance("album");
        trashCanLayout = TrashCanLayout.newInstance("trashcan");
        favoriteLayout = FavoriteLayout.newInstance("favorite");
        menuLayout = MenuLayout.newInstance("menu");


        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.replace_fragment_layout, allLayout);
        ft.commit();
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_all_picture) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.replace_fragment_layout, allLayout);
                    ft.commit();
                } else if (item.getItemId() == R.id.action_album) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.replace_fragment_layout, albumLayout);
                    ft.commit();

                } else if (item.getItemId() == R.id.action_favorite) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.replace_fragment_layout, favoriteLayout);
                    ft.commit();

                } else if (item.getItemId() == R.id.action_bin) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.replace_fragment_layout, trashCanLayout);
                    ft.commit();
                } else if (item.getItemId() == R.id.action_account) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.replace_fragment_layout, accountLayout);
                    ft.commit();
                }
                return true;
            }
        });
    }

    //Đoạn code dưới này là của VH
//    public void clickAllLayout(View view) {
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flFragment, allLayout);
//        ft.commit();
//    }
//
//    public void clickAlbumLayout(View view) {
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flFragment, albumLayout);
//        ft.commit();
//    }
//
//    public void clickTrashCanLayout(View view) {
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flFragment, trashCanLayout);
//        ft.commit();
//    }
//
//    public void clickFavoriteLayout(View view) {
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flFragment, favoriteLayout);
//        ft.commit();
//    }
//
//    public void clickAccountLayout(View view) {
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flFragment, accountLayout);
//        ft.commit();
//    }
    private void getSizeWindow(){
        DisplayMetrics dis = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dis);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Width = dis.widthPixels;
            Height = dis.heightPixels;
        }
        else{
            Height = dis.widthPixels;
            Width = dis.heightPixels;
        }
    }
    public byte[] ChangeImageToByte(int idImage){
        Bitmap image = BitmapFactory.decodeResource(getResources(), idImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 40 , stream);
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }
    public Bitmap ChangeByteToBitmap(byte[] outImage){
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        return theImage;
    }
    public Bitmap ChangeImageToBitmap(int idImage){
        Bitmap image = BitmapFactory.decodeResource(getResources(), idImage);
        return image;
    }
    public byte[] ChangeBitmapToByte(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }
}
