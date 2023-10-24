package com.example.project_album;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends FragmentActivity {
    public static int Width;
    public static int Height;
    NavigationView navigationView;
    FragmentTransaction ft;
    AllLayout allLayout;
    AlbumLayout albumLayout;
    FavoriteLayout favoriteLayout;
    TrashCanLayout trashCanLayout;
    AccountLayout accountLayout;

    BottomNavigationView mBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver2);

        getSizeWindow();

        accountLayout = AccountLayout.newInstance("account");
        allLayout = AllLayout.newInstance("all");
        albumLayout = AlbumLayout.newInstance("album");
        trashCanLayout = TrashCanLayout.newInstance("trashcan");
        favoriteLayout = FavoriteLayout.newInstance("favorite");


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
}
