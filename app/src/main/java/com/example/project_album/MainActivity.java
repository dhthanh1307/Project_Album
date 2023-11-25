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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
<<<<<<< Updated upstream

public class MainActivity extends FragmentActivity {
    public static DataResource dataResource;
=======
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.annotation.Target;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    public static DataResource dataResource;
    public static ArrayList<Image> images = new ArrayList<>();
    private WallpaperManager wallpaperManager;
    private byte[] wallpaperImage;
>>>>>>> Stashed changes
    public static int Width;
    public static int Height;
    private WallpaperManager wallpaperManager;
    private byte[] wallpaperImage;
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
<<<<<<< Updated upstream
        Log.e("MainActivity:","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver2);

        dataResource = new DataResource(this);
        dataResource.open();
=======
        Log.e("MainActivity","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver2);

        if(images.get(0).getImgBitmap() == null) {
            Thread myBackgroundThread = new Thread(image_bitmap_backgroundTask);
            myBackgroundThread.start();
        }
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        userID = bundle.getInt("username");

//        dataResource = new DataResource(this);
//        dataResource.open();
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
    private Runnable image_bitmap_backgroundTask = new Runnable() {
        @Override
        public void run() { // busy work goes here...
            try {
                for(int i = images.size() -16;i >=0;i--) {
                    Thread.sleep(1);
                    images.get(i).setImgBitmap(
                            getImageFromPath(images.get(i).getPath()));
                    debug(String.valueOf(i));
//                    allLayout.update();
//                    favoriteLayout.update();
//                    trashCanLayout.update();
                }
                //adapter.notifyDataSetChanged();
            }
            catch (InterruptedException e) { }
        }
    };

>>>>>>> Stashed changes
}
