package com.example.project_album;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import android.hardware.Camera;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class AllLayout extends Fragment {

    // TODO: Rename and change types of parameters
    MainActivity main;
    Context context = null;
    public static ArrayList<Image> images= new ArrayList<Image>();
    private ArrayList<Image> copiedImages = new ArrayList<Image>();
    ImageAdapter adapter;
    GridView gridView;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    FloatingActionButton btnAddCamera;
    public AllLayout() {
        debug("constructor");
        images = MainActivity.dataResource.getAllImage();
        if(images.size()!= 0) {
            for (int i = images.size() - 1; i >= images.size() - 15; i--) {
                debug(String.valueOf(i));
                images.get(i).setImgBitmap(getImageFromPath(images.get(i).getPath()));
            }
            Thread myBackgroundThread = new Thread(image_bitmap_backgroundTask);
            myBackgroundThread.start();
        }
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
            initDataResource();

        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
        debug("finish onCreate");
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        debug("onCreateView");
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all_layout, container, false);

        if (copiedImages.size() == 0) {
            copiedImages.addAll(images);
        }
        debug(String.valueOf(copiedImages.size()));

        adapter=new ImageAdapter(main,R.layout.item_image,copiedImages);
        gridView = view.findViewById(R.id.gridView);
        DoSthWithOrientation(getResources().getConfiguration().orientation);
        gridView.setAdapter(adapter);
        gridView.setSelection(images.size() - 1);
        // Chỗ này set ảnh màn hình của huy chưa thêm vô, để thêm vô sau.
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(),"set Wallpaper successfull!"
//                        ,Toast.LENGTH_SHORT).show();
//                //main.setWallPaper(images.get(i).getImgView());
//            }
//        });

        //Khi click vào ảnh bất kì
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showBigScreen(i);
            }
        });
        SetGridViewItemLongClick();
        gridView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                debug("scroll");
//                adapter.notifyDataSetChanged();
            }
        });

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
//                int count = 0;
//                int temp1 = 0;
//                int temp2 = 0;
//                for (int i = images.size() - 1; i >= images.size() - 15; i--) {
//                    temp1++;
//                    if (images.get(i).getDeleted().equals("T")) {
//                        continue;
//                    }
//                    for (int j = i - 1; j >= images.size() - 15; j--) {
//                        temp2++;
//                        if (images.get(j).getDeleted().equals("T")) {
//                            continue;
//                        }
//                        if (compareBitmap(images.get(i).getImgBitmap(),images.get(j).getImgBitmap())) {
//                            images.get(j).setDeleted("T");
//                            count++;
//                        }
//                    }
//                    Log.e("Temp 2", Integer.toString(temp2));
//                }
//                Log.e("Temp 1", Integer.toString(temp1));
//                Toast.makeText(main, "Found " + count + " duplicate image(s)", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private boolean compareBitmap(Bitmap b1, Bitmap b2) {
        if (b1.getWidth() == b2.getWidth() && b1.getHeight() == b2.getHeight()) {
            int[] pixels1 = new int[b1.getWidth() * b1.getHeight()];
            int[] pixels2 = new int[b2.getWidth() * b2.getHeight()];

            b1.getPixels(pixels1, 0, b1.getWidth(), 0, 0, b1.getWidth(), b1.getHeight());
            b2.getPixels(pixels2, 0, b2.getWidth(), 0, 0, b2.getWidth(), b2.getHeight());

            return Arrays.equals(pixels1, pixels2);
        } else {
            return false;
        }
    }

    private void SetGridViewItemLongClick() {
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                debug("scroll");
                adapter.notifyDataSetChanged();
                return true;
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

    private void initDataResource() {
        if(images.size() == 0){
            int img[] = {R.drawable.img,R.drawable.img_2,
                    R.drawable.img_3,
                    R.drawable.img_4,R.drawable.img_5,R.drawable.img_6};
            for(int j =0;j<5;j++){
                for(int i = 0; i<6;i++) {
                    images.add(new Image(main.ChangeImageToBitmap(img[i]),main.GenerateName()));
                    images.get(j*6+i).setId(MainActivity.dataResource.
                            InsertImage(images.get(j*6+i), 1));
                    //images.get(j*6+i).setImgBitmap(main.ChangeImageToBitmap(img[i]));
                    //debug(String.valueOf(j*6+i));
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
            gridView.setNumColumns(5);
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            gridView.setNumColumns(3);
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