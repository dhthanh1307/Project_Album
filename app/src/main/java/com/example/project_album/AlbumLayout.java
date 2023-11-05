package com.example.project_album;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AlbumLayout extends Fragment {

    public static ArrayList<Album> albums = new ArrayList<Album>();
    private Bundle saveInstanceState;
    private GridView girdViewAlbum;
    private GridViewAlbumAdapter adapter_album;
    private ViewGroup view_album_top;
    private ViewGroup view_album_bottom;
    private TextView tv_album_small;
    private TextView tv_album_big;
    private TextView tv_all_my_album;
    private View mainView;
    private ViewGroup container;
    private static AlbumLayout instance;
    private Button btnAddAlbum;
    private ScrollView sv;
    private RelativeLayout layout_icon;

    private AlbumLayout(){

    }
    public static AlbumLayout newInstance(String strArg){
        if (instance == null){
            instance =  new AlbumLayout();
            Bundle args = new Bundle();
            args.putString("strArg1", strArg);
            instance.setArguments(args);
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitAlbums();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_album_layout, container, false);
        this.container = container;
        this.saveInstanceState = saveInstanceState;

        view_album_top = (ViewGroup)mainView.findViewById(R.id.ln_image_view_top);
        view_album_bottom = (ViewGroup)mainView.findViewById(R.id.ln_image_view_bottom);

        tv_album_big = mainView.findViewById(R.id.tv_album_big);
        tv_album_small = mainView.findViewById(R.id.tv_album_small);
        tv_all_my_album = mainView.findViewById(R.id.tv_all_my_album);
        btnAddAlbum = mainView.findViewById(R.id.btn_add_album);
        layout_icon = (RelativeLayout)mainView.findViewById(R.id.layout_icon);
        sv = (ScrollView)mainView.findViewById(R.id.sv_parent);
        sv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view,int i, int i1, int i2, int i3) {
                if(tv_album_big.getGlobalVisibleRect(new Rect()) == false){
                    tv_album_small.setTextSize(20);
                    layout_icon.setBackground(getContext().getDrawable(R.color.black_1));
                }
                else{
                    tv_album_small.setTextSize(0);
                    layout_icon.setBackground(getContext().getDrawable(R.color.black));
                }
            }
        });

        btnAddAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAlbumForm();
            }
        });

        UpdateConfiguration(getResources().getConfiguration());

        tv_all_my_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllMyAlbumClick();
            }
        });

        return mainView;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UpdateConfiguration(newConfig);
    }

    private void UpdateConfiguration(Configuration newConfig){
        int newOrientation = newConfig.orientation;

        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv_album_big.setTextSize(0);
            tv_album_small.setTextSize(30);
            setViewAlbumOnLandscapeOritation();
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            tv_album_big.setTextSize(40);
            tv_album_small.setTextSize(0);
            setViewAlbumOnPortraitOritation();
        }
    }
    private void setViewAlbumOnPortraitOritation() {
        view_album_top.removeAllViews();
        view_album_bottom.removeAllViews();
        for(int i = 0;i<albums.size();i+=2){
            View viewchild = createViewChildForAlbum(i);
            //viewchild.setPadding(0,0,130,0);
            view_album_top.addView(viewchild);
        }
        for(int i = 1;i<albums.size();i+=2){
            View viewchild = createViewChildForAlbum(i);
            view_album_bottom.addView(viewchild);

        }
    }

    private void setViewAlbumOnLandscapeOritation(){
        view_album_top.removeAllViews();
        view_album_bottom.removeAllViews();
        for(int i = 0;i<albums.size();i++){
            View viewchild = createViewChildForAlbum(i);
            ImageView img = viewchild.findViewById(R.id.image);
            //img.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.Width/3 - 50,MainActivity.Width/3 -50));
            view_album_top.addView(viewchild);
        }
    }

    private View createViewChildForAlbum(int position){
        View viewchild = getActivity().getLayoutInflater().inflate(R.layout.item_album,null);
        viewchild.setId(position);
        ImageView img = viewchild.findViewById(R.id.image);
        TextView tvname = viewchild.findViewById(R.id.tv_album_name);
        TextView tvlength = viewchild.findViewById(R.id.tv_album_length);
        if(albums.get(position).length() != 0 ){
            img.setImageBitmap(albums.get(position).get_image(
                    albums.get(position).length() -1 ).getImgBitmap());
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else{
            img.setScaleType(ImageView.ScaleType.CENTER);
        }
        CardView cv = (CardView) viewchild.findViewById(R.id.cv_album);
        cv.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.Width/2-150,MainActivity.Width/2-150));
        tvname.setText(albums.get(position).getName());
        tvlength.setText(String.valueOf(albums.get(position).length()));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                ft.replace(R.id.replace_fragment_layout,new
                        ShowImageInAlbumFragment(albums.get(position).getImages()));
                ft.commit();
                //talk();
            }
        });
        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.custtom_edit_album, null);
                ImageView img = v.findViewById(R.id.image);
                try{
                    img.setImageBitmap(albums.get(position).get_image(
                            albums.get(position).length() - 1).getImgBitmap());
                } catch(Exception e){
                    img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }


                TextView tv_name = v.findViewById(R.id.tv_album_name);
                tv_name.setText(albums.get(position).getName());
                TextView tv_length = v.findViewById(R.id.tv_album_length);
                tv_length.setText(String.valueOf(albums.get(position).length()));
//                LinearLayout ln = (LinearLayout) v.findViewById(R.id.ln_parent);
//                ln.setBackgroundColor(getResources().getColor(R.color.black_n));

                TextView tv_rename = v.findViewById(R.id.tv_rename);
                TextView tv_delete = v.findViewById(R.id.tv_delete);

                tv_rename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });


                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(v);
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams attribute = window.getAttributes();
                attribute.gravity = Gravity.CENTER;
//                dialog.registerForContextMenu(getLayoutInflater().inflate(R.layout.item_image,null));
//                dialog.openOptionsMenu();
                dialog.show();
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        albums.remove(position);
                        dialog.cancel();
                        UpdateConfiguration(getResources().getConfiguration());
                    }
                });
                return true;
            }
        });
        return viewchild;
    }
    private void InitAlbums() {
        ArrayList<Image> images = new ArrayList<Image>();
        for(int i =0;i<4;i++){
            images.add(AllLayout.images.get(i));
        }
        Album a3 = new Album("Airplane",images);

        ArrayList<Image> images1 = new ArrayList<Image>();
        images1.addAll(images);
        Album a4 = new Album("Fruit",images1);

        ArrayList<Image> images2 = new ArrayList<Image>();
        images2.addAll(images);
        Album a5 = new Album("Holiday",images2);

        ArrayList<Image> images3 = new ArrayList<Image>();
        Album a2 = new Album("Mục yêu thích",images3);

        ArrayList<Image> images4 = new ArrayList<Image>();
        images4.addAll(AllLayout.images);
        Album a1 = new Album("Tất cả",images4);

        albums.add(a1);
        albums.add(a2);
        albums.add(a3);
        albums.add(a4);
        albums.add(a5);
    }
    private void AllMyAlbumClick(){
        FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentmanager.beginTransaction();
        ft.replace(R.id.replace_fragment_layout,new MyAlbumFragment());
        //ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.commit();
    }

    private void AddAlbumForm(){
        FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentmanager.beginTransaction();
        ft.replace(R.id.replace_fragment_layout,new MyAlbumFragment());
        ft.commit();

        Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);


        View view  = getActivity().getLayoutInflater().inflate(R.layout.custom_add_album, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams attribute = window.getAttributes();
        attribute.gravity =Gravity.CENTER;

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.custom_dialog));
        EditText txtTitle = view.findViewById(R.id.txt_title);
        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txtTitle.length() != 0){
                    btnSave.setTextColor(Color.BLUE);
                    btnSave.setBackgroundResource(R.drawable.custtom_button);
                    btnSave.setClickable(true);

                }
                else{
                    btnSave.setTextColor(Color.GRAY);
                    btnSave.setBackgroundResource(R.color.black);
                    btnSave.setClickable(false);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Image> image = new ArrayList<Image>();
                Album album = new Album(txtTitle.getText().toString(),image);
                albums.add(album);
                dialog.cancel();
                MyAlbumFragment.gridViewAlbumAdapter.notifyDataSetChanged();
            }
        });

        dialog.show();
    }

    private void talk(){
        Toast.makeText(getActivity(), "click",Toast.LENGTH_SHORT).show();
    }
}