package com.example.project_album;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class EditFragment extends Fragment {



    Context context = null;
    MainActivity main;
    ImageView imageView;
    Bitmap originalBitmap ;
    ArrayList<Image> images = new ArrayList<>();
    int index=0;
    EditFragment(ArrayList<Image> imgs, int index){

        this.images=imgs;
        this.index=index;
        this.originalBitmap=images.get(index).getImgBitmap();
    }
    EditFragment(){}




    public static EditFragment newInstance(String strArg) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_edit, container, false);
        imageView=view.findViewById(R.id.imgview);
        imageView.setImageBitmap(images.get(index).getImgBitmap());
        ImageView right=view.findViewById(R.id.txt_right);
        ImageView left=view.findViewById(R.id.txt_left);
        ImageView flip=view.findViewById(R.id.txt_flip);
        ImageView cancel=view.findViewById(R.id.cancel);
        ImageView save=view.findViewById(R.id.save);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rightBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                        originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
                //image.setImgBitmap(rotatedBitmap);
                originalBitmap=rightBitmap;
                imageView.setImageBitmap(rightBitmap);

            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                Bitmap leftBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                        originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
                //image.setImgBitmap(rotatedBitmap);
                originalBitmap=leftBitmap;
                imageView.setImageBitmap(leftBitmap);

            }
        });
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matrix matrix = new Matrix();
                matrix.postScale(-1,1);
                Bitmap flipdBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                        originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
                //image.setImgBitmap(rotatedBitmap);
                originalBitmap=flipdBitmap;
                imageView.setImageBitmap(flipdBitmap);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images.get(index).setImgBitmap(originalBitmap);
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                ViewPagerAllLayoutFragment newFragment=new ViewPagerAllLayoutFragment(images,index);
                ft.replace(R.id.replace_fragment_layout, newFragment);
                ft.commit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                ViewPagerAllLayoutFragment newFragment=new ViewPagerAllLayoutFragment(images,index);
                ft.replace(R.id.replace_fragment_layout, newFragment);
                ft.commit();
            }
        });

        return view;
    }
}