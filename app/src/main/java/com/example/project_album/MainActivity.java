package com.example.project_album;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends FragmentActivity {
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


//
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.NavigationView, menuLayout);
//        ft.commit();
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
}
