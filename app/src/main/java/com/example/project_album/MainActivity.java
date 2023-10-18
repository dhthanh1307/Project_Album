package com.example.project_album;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = getLayoutInflater().inflate(R.layout.fragment_menu, null);

        accountLayout = AccountLayout.newInstance("account");
        allLayout = AllLayout.newInstance("all");
        albumLayout = AlbumLayout.newInstance("album");
        trashCanLayout = TrashCanLayout.newInstance("trashcan");
        favoriteLayout = FavoriteLayout.newInstance("favorite");
        menuLayout = MenuLayout.newInstance("menu");

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flFragment, accountLayout);
        ft.commit();

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.NavigationView, menuLayout);
        ft.commit();
    }

    public void clickAllLayout(View view) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flFragment, allLayout);
        ft.commit();
    }

    public void clickAlbumLayout(View view) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flFragment, albumLayout);
        ft.commit();
    }

    public void clickTrashCanLayout(View view) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flFragment, trashCanLayout);
        ft.commit();
    }

    public void clickFavoriteLayout(View view) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flFragment, favoriteLayout);
        ft.commit();
    }

    public void clickAccountLayout(View view) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flFragment, accountLayout);
        ft.commit();
    }
}
