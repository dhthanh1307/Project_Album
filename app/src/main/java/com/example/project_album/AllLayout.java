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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import android.hardware.Camera;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AllLayout extends Fragment {

    // TODO: Rename and change types of parameters
    MainActivity main;
    Context context = null;
    public static ArrayList<Image> images= new ArrayList<Image>();
    ImageAdapter adapter;
    GridView gridView;
    Spinner spinner;
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    FloatingActionButton btnAddCamera;
    public AllLayout() {
        debug("constructor");
        images = MainActivity.dataResource.getAllImage();
        debug(String.valueOf(images.size()));
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
        adapter=new ImageAdapter(main,R.layout.item_image,images);
        gridView = view.findViewById(R.id.gridView);
        DoSthWithOrientation(getResources().getConfiguration().orientation);
        gridView.setAdapter(adapter);
        gridView.setSelection(images.size() - 1);
        spinner = view.findViewById(R.id.spinner);
        ArrayList<String> data = new ArrayList<>();
        data.add("Date");
        data.add("ID");
        data.add("Default");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(main, android.R.layout.simple_spinner_item, data);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = data.get(position);
                if (selectedItem.equals("Date")) {
                    // Xử lý sự kiện khi chọn mục "Date"
                    ArrayList<Image> copiedImages = new ArrayList<>();
                    copiedImages.addAll(images);
                    for (int i = 0; i < copiedImages.size() - 1; i++)
                        for (int j = i + 1; j < copiedImages.size(); j++) {
                            if (copiedImages.get(i).getDate().compareTo(copiedImages.get(j).getDate()) < 0) {
                                Image temp = copiedImages.get(i);
                                copiedImages.set(i, copiedImages.get(j));
                                copiedImages.set(j, temp);
                            }
                        }
                    adapter = new ImageAdapter(main, R.layout.item_image, copiedImages);
                    gridView.setAdapter(adapter);
                } else if (selectedItem.equals("ID")) {
                    // Xử lý sự kiện khi chọn mục "ID"
                    ArrayList<Image> copiedImages = new ArrayList<>();
                    copiedImages.addAll(images);
                    for (int i = 0; i < copiedImages.size() - 1; i++)
                        for (int j = i + 1; j < copiedImages.size(); j++) {
                            if (copiedImages.get(i).getId() < copiedImages.get(j).getId()) {
                                Image temp = copiedImages.get(i);
                                copiedImages.set(i, copiedImages.get(j));
                                copiedImages.set(j, temp);
                            }
                        }
                    adapter = new ImageAdapter(main, R.layout.item_image, copiedImages);
                    gridView.setAdapter(adapter);
                }else if(selectedItem.equals("Default")){
                    adapter = new ImageAdapter(main, R.layout.item_image, images);
                    gridView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý sự kiện khi không có mục nào được chọn
            }
        });
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
            for(int j =0;j<9;j++){
                for(int i = 0; i<6;i++) {
//                    Bitmap image = BitmapFactory.decodeResource(getResources(), img[i]);
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte imageInByte[] = stream.toByteArray();
                    images.add(new Image(main.ChangeImageToByte(img[i])));
                    debug(String.valueOf(j*6+i));
                    images.get(j*6+i).setId(MainActivity.dataResource.
                            InsertImage(images.get(j*6+i), 1));
                    debug(String.valueOf(j*6+i));
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
    private void debug(String str){
        Log.e("AllLayout",str);
    }
}