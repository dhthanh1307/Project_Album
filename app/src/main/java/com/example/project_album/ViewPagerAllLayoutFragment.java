package com.example.project_album;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

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
    TextView tv_animate;

    MainActivity main;
    Context context = null;
    ViewPagerInTrashCanAdapter mAdapter;

    private LinearLayout ln_top;
    private LinearLayout ln_bottom;
    private boolean isSlider = false;
    private Handler handler = new Handler();
    int index = 0;//Vị trí được chọn hiện tại



    public ViewPagerAllLayoutFragment(ArrayList<Image> imgs, int index) {
        this.images = imgs;
        this.index = index;
    }
    public ViewPagerAllLayoutFragment(ArrayList<Image> imgs){
        this.images = imgs;
        this.index = 0;
        isSlider = true;
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
    private void onPageChange() {
        Log.e("ViewPaper ",String.valueOf(index));
        txtTimeCapture.setText(String.valueOf(index + 1) + "/" + String.valueOf(images.size()));
        if (images.get(index).getFavorite().equals("T")){
            txtFavorite.setImageResource(R.drawable.icon_fill_favorite_in_all_layout);
        }else{
            txtFavorite.setImageResource(R.drawable.icon_favorite_in_alllayout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager_all_layout, container, false);
        //Chỗ này là imageView mà sa chwua đổi txt tên ở id bên xml á.
        ln_top = view.findViewById(R.id.top);
        ln_bottom = view.findViewById(R.id.bottom);
        mViewPager = view.findViewById(R.id.viewpager_in_all_layout);
        // chỗ nay tan dung lai Adapter ben TrashCan khoi can tao moi
        mAdapter = new ViewPagerInTrashCanAdapter(main.getSupportFragmentManager(), main.getLifecycle(), images);
        mViewPager.setAdapter(mAdapter);
        if(!isSlider ) {
            ln_top.setVisibility(View.VISIBLE);
            ln_bottom.setVisibility(View.VISIBLE);
            mViewPager.setCurrentItem(index,false);
            main.mBottomNavigationView.setVisibility(View.GONE);
            txtTimeCapture = view.findViewById(R.id.edt_time_capture);
            txtDeleteIntoTrashCan = view.findViewById(R.id.txt_delete_into_trashcan);
            txtFavorite = view.findViewById(R.id.txt_favorite);
            txtInformation = view.findViewById(R.id.txt_information);
            txtShare = view.findViewById(R.id.txt_share);
            txtEdit = view.findViewById(R.id.txt_edit);
            imgBack = view.findViewById(R.id.img_back);
            onPageChange();

            EventForAll();
        }
        //view paper dung de tao slide
        else{
            mViewPager.animate().setDuration(500);
            tv_animate = view.findViewById(R.id.tv_animate);
            tv_animate.setTextColor(Color.MAGENTA);
            tv_animate.animate().setDuration(1000);
            tv_animate.setVisibility(View.VISIBLE);
//            mViewPager.setClipToPadding(false);
//            mViewPager.setClipChildren(false);
//            mViewPager.setOffscreenPageLimit(3);
//            mViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    int pageWidth = page.getWidth();
                    int pageHeight = page.getHeight();
                    float MIN_SCALE = 0.85f;
                    float MIN_ALPHA = 0.5f;

                    if ( position < -1 ) { // [ -Infinity,-1 )
                        // This page is way off-screen to the left.
                        page.setAlpha( 0 );
                    }
                    else if ( position <= 1 ) { // [ -1,1 ]
                        // Modify the default slide transition to shrink the page as well
                        float scaleFactor = Math.max( MIN_SCALE, 1 - Math.abs( position ) );
                        float vertMargin = pageHeight * ( 1 - scaleFactor ) / 2;
                        float horzMargin = pageWidth * ( 1 - scaleFactor ) / 2;
                        if ( position < 0 ) {
                            page.setTranslationX( horzMargin - vertMargin / 2 );
                        } else {
                            page.setTranslationX( -horzMargin + vertMargin / 2 );
                        }

                        // Scale the page down ( between MIN_SCALE and 1 )
                        page.setScaleX( scaleFactor );
                        page.setScaleY( scaleFactor );

                        // Fade the page relative to its size.
                        page.setAlpha( MIN_ALPHA +
                                ( scaleFactor - MIN_SCALE ) /
                                        ( 1 - MIN_SCALE ) * ( 1 - MIN_ALPHA ));

                    } else { // ( 1,+Infinity ]
                        // This page is way off-screen to the right.
                        page.setAlpha( 0 );
                    }
                }
            });
            mViewPager.setPageTransformer(compositePageTransformer);

            ln_bottom.setVisibility(View.INVISIBLE);
            ln_top.setVisibility(View.INVISIBLE);
        }

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(!isSlider) {
                    index = position;
                    onPageChange();
                }
                else{
                    handler.removeCallbacks(sliderRunable);
                    handler.postDelayed(sliderRunable,2000);
                }
            }
        });

        return view;
    }
    private Runnable sliderRunable = new Runnable() {
        @Override
        public void run() {
            if(mViewPager.getCurrentItem() + 1 == images.size()){
                closeFragment();
                main.mBottomNavigationView.setVisibility(View.VISIBLE);
            }
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            mViewPager.setBackgroundColor(RandomBackgroundColor());
            tv_animate.setText("Picture "+String.valueOf(mViewPager.getCurrentItem() +1));
            tv_animate.setScaleX(1);
            tv_animate.setScaleY(1);
            tv_animate.setTextSize(50);
            tv_animate.setTranslationX(0);
            tv_animate.setTranslationY(0);
            tv_animate.setRotation(0);
            getAnimateOnTextView();

        }

        private void getAnimateOnTextView() {
            Random random = new Random();
            int type = random.nextInt(4);
            if(type == 0){
                tv_animate.setTextSize(5);
                tv_animate.animate().scaleX(10).withLayer();
                tv_animate.animate().scaleY(10).withLayer();
            }
            else if (type == 1){
                tv_animate.setTranslationX(700);
                tv_animate.animate().translationX(0).withLayer();

            }
            else if ( type == 2){
                tv_animate.setTextSize(5);
                tv_animate.animate().scaleX(10).withLayer();
                tv_animate.animate().scaleY(10).withLayer();
                tv_animate.animate().rotation(360).withLayer();
            }
            else if(type == 3){
                tv_animate.setTranslationY(-500);
                tv_animate.animate().translationY(0).withLayer();
            }
        }

        private int RandomBackgroundColor() {
            int color[] = {Color.BLACK,Color.BLUE,Color.YELLOW,Color.GREEN};
            Random random = new Random();
            int i= Math.abs(random.nextInt()%4);
            Log.e("Viewpaper",String.valueOf(i));
            return color[i];
        }
    };
    private float RandomTranlation(){
        return 1;
    }

    private void closeFragment() {

        FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();
        main.getSupportFragmentManager().popBackStack();
        transaction.remove(this);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
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

    private void EventForAll(){
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
                main.albumLayout.updateFavorite(images.get(index));
                main.favoriteLayout.updateFavorite(images.get(index));
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
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                images.get(mViewPager.getCurrentItem()
                ).getImgBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
                main.setWallPaper(imageInByte);
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
                    ft.replace(R.id.replace_fragment_layout,main.favoriteLayout);
                }else if(main.getIDItemBottomNavigationView()==R.id.action_all_picture){
                    ft.replace(R.id.replace_fragment_layout, main.allLayout);
                }
                ft.commit();
                main.mBottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }
}