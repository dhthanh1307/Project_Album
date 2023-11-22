package com.example.project_album;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import android.hardware.Camera;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AllLayout extends Fragment {

    // TODO: Rename and change types of parameters
    //khai báo biến cho dialog
    private Dialog dialog;
    private TextView tv_addtoAlbum;
    private TextView tv_delete;
    private TextView tv_favorite;
    private TextView tv_blind;
    private TextView tv_slider;
    private TextView tv_zip;
    private View v_allInfo;


    //finish

    //view header
    private TextView tv_info,tv_choose;
    private Button btn_extend;
    //finish

    //view footer
    private ImageView img_all_info;
    private TextView tv_numSelect;
    //finish
    private TextView tv_bottom;
    MainActivity main;
    Context context = null;
    public static ArrayList<Image> images= new ArrayList<Image>();
    private ArrayList<Image> copiedImages = new ArrayList<Image>();
//    ImageAdapter adapter;
    //GridView gridView;
    ShowImageInAllAdapter adapter;
    RecyclerView recyclerView;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    FloatingActionButton btnAddCamera;
    public AllLayout() {
        debug("constructor");
        //images = MainActivity.dataResource.getAllImage();
        if(images.get(0).getImgBitmap() == null) {
//            for (int i = images.size() - 1; i >= images.size() - 15 && i >=0; i--) {
//                debug(String.valueOf(i));
//                if(images.get(i).getImgBitmap() == null)
//                    images.get(i).setImgBitmap(getImageFromPath
//                            (images.get(i).getPath()));
//            }
            Thread myBackgroundThread = new Thread(image_bitmap_backgroundTask);
            myBackgroundThread.start();
        }
    }

    public AllLayout(ArrayList<Image> imgs) {
        this.images = imgs;
    }
    public static AllLayout newInstance(String strArg) {
        AllLayout fragment = new AllLayout();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        debug("onCreate");
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (MainActivity) getActivity();
            //initDataResource();

        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
        debug("finish onCreate");
    }
    @Override
    public void onResume(){
        super.onResume();
        debug("onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        debug("onCreateView");

        // view for all info dialog
        v_allInfo = main.getLayoutInflater().inflate(R.layout.all_info_alllayout, null);
        tv_addtoAlbum = v_allInfo.findViewById(R.id.add_to_album);
        tv_delete = v_allInfo.findViewById(R.id.delete);
        tv_favorite = v_allInfo.findViewById(R.id.favorite);
        tv_slider = v_allInfo.findViewById(R.id.slider);
        tv_blind = v_allInfo.findViewById(R.id.blind);
        tv_zip = v_allInfo.findViewById(R.id.zip);
        dialog = new Dialog(main);
        dialog.setContentView(v_allInfo);
        EventViewDialog();
        // finish view for all info dialog

        for (int i1 = 0; i1 < AllLayout.images.size(); i1++) {
            Log.e("fix loi","anh ="+String.valueOf(AllLayout.images.get(i1).getId()));
        }
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all_layout, container, false);

        if (copiedImages.size() == 0) {
            copiedImages.addAll(images);
        }


        //view header
        tv_info = view.findViewById(R.id.tv_info);
        tv_choose = view.findViewById(R.id.tv_choose);
        btn_extend = view.findViewById(R.id.btn_many);
        EventViewHeader();
        //finish

        //view footer
        img_all_info = main.bottom_navigation_album.findViewById(R.id.img_all_info);
        tv_numSelect = main.bottom_navigation_album.findViewById(R.id.tv1);
        EventViewFooter();
        //finish
        debug(String.valueOf(copiedImages.size()));

        tv_bottom = view.findViewById(R.id.tv_bottom);
        tv_bottom.setText(String.valueOf(copiedImages.size()) + " ảnh");
        adapter=new ShowImageInAllAdapter(main,R.layout.item_image,copiedImages);
        recyclerView = view.findViewById(R.id.rv_image_in_all);
        DoSthWithOrientation(getResources().getConfiguration().orientation);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(copiedImages.size() - 1);


        //Khi click vào ảnh bất kì
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                for (int i1 = 0; i1 < AllLayout.images.size(); i1++) {
//                    Log.e("fix loi1","anh ="+String.valueOf(AllLayout.images.get(i1).getId()));
//                }
//                showBigScreen(i);
//            }
//        });
//        SetGridViewItemLongClick();
//        gridView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
////                debug("scroll");
////                adapter.notifyDataSetChanged();
//            }
//        });

        spinner = view.findViewById(R.id.spinner);
        initSpinerView();
        btnAddCamera = view.findViewById(R.id.btn_add_camera);
        btnAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(main, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(main, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
                    return;
                }
                startCamera();
            }
        });


        return view;
    }

    public void setTextInfo(String info){
        tv_info.setText(info);
    }
    //=======================bắt các sự kiện từ dialog ===================
    private void EventViewDialog() {
        tv_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                Fragment fragment = new ViewPagerAllLayoutFragment(adapter.image_chosen);
                ft.add(R.id.replace_fragment_layout,fragment);
                ft.addToBackStack(fragment.getClass().getSimpleName());
                ft.commit();
                dialog.dismiss();
                adapter.resetChooseSelection();
                tv_choose.callOnClick();
            }
        });

    }


    //========================kết thúc bắt các sự kiện từ dialog

    private void EventViewHeader() {
        tv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_choose.getText().equals("Chọn")){
                    tv_choose.setTranslationX(100);
                    btnAddCamera.setVisibility(View.INVISIBLE);
                    tv_choose.setText("Hủy");
                    adapter.setChooseSelection(true);
                    btn_extend.setVisibility(View.INVISIBLE);
                    main.mBottomNavigationView.setVisibility(View.INVISIBLE);
                    main.bottom_navigation_album.setVisibility(View.VISIBLE);
                }
                else{
                    tv_choose.setTranslationX(0);
                    btnAddCamera.setVisibility(View.VISIBLE);
                    adapter.setChooseSelection(false);
                    adapter.resetChooseSelection();
                    adapter.notifyDataSetChanged();
                    main.bottom_navigation_album.setVisibility(View.INVISIBLE);
                    main.mBottomNavigationView.setVisibility(View.VISIBLE);
                    btn_extend.setVisibility(View.VISIBLE);
                    tv_choose.setText("Chọn");

                }
            }
        });
    }
    private void EventViewFooter() {
        img_all_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams attribute = window.getAttributes();
                attribute.y = 280;
                attribute.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                dialog.show();
            }
        });
    }


    private void initSpinerView() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Default");
        data.add("ID");
        data.add("Date");
        spinnerAdapter = new ArrayAdapter<>(main, android.R.layout.simple_spinner_item, data);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = data.get(position);
                if (selectedItem.equals("Date")) {
                    debug("date");
                    // Xử lý sự kiện khi chọn mục "Date"
                    for (int i = 0; i < copiedImages.size() - 1; i++) {
                        for (int j = i + 1; j < copiedImages.size(); j++) {
                            if (copiedImages.get(i).getDate().compareTo(copiedImages.get(j).getDate()) < 0) {
                                Image temp = copiedImages.get(i);
                                copiedImages.set(i, copiedImages.get(j));
                                copiedImages.set(j, temp);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else if (selectedItem.equals("ID")) {
                    debug("12");
                    for (int i = 0; i < copiedImages.size() - 1; i++) {
                        for (int j = i + 1; j < copiedImages.size(); j++) {
                            if (copiedImages.get(i).getId() < copiedImages.get(j).getId()) {
                                Image temp = copiedImages.get(i);
                                copiedImages.set(i, copiedImages.get(j));
                                copiedImages.set(j, temp);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else if(selectedItem.equals("Default")){
                    copiedImages.clear();
                    copiedImages.addAll(images);
                    adapter.notifyDataSetChanged();
                    debug1();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                debug("11");
                // Xử lý sự kiện khi không có mục nào được chọn
            }
        });
    }

    public void startCamera() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Đặt 123 là requestCode cho chụp hình
        startActivityForResult(camera_intent, 123);
        //getActivity().startActivityFromFragment(AllLayout.this, camera_intent, 123);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                //TH1: Chuyển thành byte để lưu vào database
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
//                byte[] byteArray = stream.toByteArray();

                //TH2: Lưu vào gallery
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                String filename = sdf.format(new Date());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                Log.i("Test", path.toString());
                OutputStream fOut = null;
                File file = new File(path, filename + ".jpg");

                try {
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fOut);
                    fOut.flush();
                    fOut.close();

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(Uri.fromFile(file));
                    main.sendBroadcast(mediaScanIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        DoSthWithOrientation(newOrientation);
        adapter.notifyDataSetChanged();
    }

    private void DoSthWithOrientation(int newOrientation ) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        }
        //Toast.makeText(getContext(),"oke",Toast.LENGTH_SHORT).show();
    }

    private Runnable image_bitmap_backgroundTask = new Runnable() {
        @Override
        public void run() { // busy work goes here...
            try {
                for(int i = images.size() -16;i >=0;i--) {
                    Thread.sleep(1);
                    images.get(i).setImgBitmap(
                            getImageFromPath(images.get(i).getPath()));
                    debug(String.valueOf(i));
                }
                //adapter.notifyDataSetChanged();
            }
            catch (InterruptedException e) { }
        }
    };

    @Override
    public void onStart(){
        super.onStart();
    }

    public void debug1(){
        for(int i =0;i<copiedImages.size();i++){
            debug(String.valueOf(copiedImages.get(i).getId()));
        }
    }
    public void showBigScreen(int position){
        FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentmanager.beginTransaction();
        ViewPagerAllLayoutFragment newFragment=new ViewPagerAllLayoutFragment(copiedImages,position);
        ft.replace(R.id.replace_fragment_layout, newFragment);
        ft.commit();

    }
    private void debug(String str){
        Log.e("AllLayout",str);
    }
    private Bitmap getImageFromPath(String path) {
        Bitmap b =null;
        debug(path);
        try {
            File f = new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {

            e.printStackTrace();
        }
        return b;
    }
}