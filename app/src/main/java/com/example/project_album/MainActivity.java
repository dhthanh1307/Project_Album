package com.example.project_album;

import android.app.WallpaperManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MainActivity extends FragmentActivity {
    public static DataResource dataResource;
    private WallpaperManager wallpaperManager;
    private byte[] wallpaperImage;
    public static int Width;
    public static int Height;
    public static int userID;
    NavigationView navigationView;
    FragmentTransaction ft;
    AllLayout allLayout;
    AlbumLayout albumLayout;
    FavoriteLayout favoriteLayout;
    TrashCanLayout trashCanLayout;
    AccountLayout accountLayout;

    BottomNavigationView mBottomNavigationView;
    ConstraintLayout bottom_navigation_album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver2);

        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        userID = bundle.getInt("userID");
        Log.e("userID", Integer.toString(userID));

//        dataResource = new DataResource(this);
//        dataResource.open();
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
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
        bottom_navigation_album = findViewById(R.id.bottom_navigation_album);
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

    public byte[] ChangeImageToByte(int img){
        Bitmap image = BitmapFactory.decodeResource(getResources(), img);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }

    public Bitmap ChangeByteToBitmap(byte[] outImage ){
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        return theImage;
    }

    public Bitmap ChangeImageToBitmap(int id){
        return BitmapFactory.decodeResource(getResources(), id);
    }

    @Override
    protected void onResume(){
        dataResource.open();
        super.onResume();
    }
    @Override
    protected void onPause(){
        dataResource.close();
        super.onPause();
    }
    public void setWallPaper(byte[] img){
        wallpaperImage = img;
        Thread myBackgroundThread = new Thread( wallPaper_backgroundTask);
        myBackgroundThread.start();
    }
    private Runnable wallPaper_backgroundTask = new Runnable() {
        @Override
        public void run() { // busy work goes here...
            try {
                Thread.sleep(1);
                Bitmap image = ChangeByteToBitmap(wallpaperImage);
                Bitmap imageEdit = Bitmap.createBitmap((int)Width, (int)Height, Bitmap.Config.ARGB_8888);

                float originalWidth = image.getWidth();
                float originalHeight = image.getHeight();

                Canvas canvas = new Canvas(imageEdit);

                float scale = Width / originalWidth;

                float xTranslation = 0.0f;
                float yTranslation = (Height - originalHeight * scale) / 2.0f;

                Matrix transformation = new Matrix();
                transformation.postTranslate(xTranslation, yTranslation);
                transformation.preScale(scale, scale);

                Paint paint = new Paint();
                paint.setFilterBitmap(true);

                canvas.drawBitmap(image, transformation, paint);

                try {
                    wallpaperManager.setBitmap(imageEdit);
                }
                catch (Exception e){

                }

            }
            catch (InterruptedException e) { }
        }
    };
    public String GenerateName(){
        return System.currentTimeMillis() + ".jpeg";
    }
}
