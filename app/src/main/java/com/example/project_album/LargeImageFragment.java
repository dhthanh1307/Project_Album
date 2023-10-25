package com.example.project_album;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class LargeImageFragment extends Fragment {
    //Iamge này là truyền vào để delete
    Image myImage;
    TextView txtTimeDelete;

    ImageView imgDelete;
    Button btnDeleteImmediately,btnRestore;
    MainActivity main;
    Context context = null;
    public LargeImageFragment(Image myImage) {
        this.myImage=myImage;
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_layout_solo_picture_in_trashcan, container, false);
        txtTimeDelete=view.findViewById(R.id.edt_time_delete);
        imgDelete=view.findViewById(R.id.img_delete);
        btnDeleteImmediately=view.findViewById(R.id.btn_delete_immediately);
        btnRestore=view.findViewById(R.id.btn_restore);
        imgDelete.setImageBitmap(main.ChangeByteToBitmap(myImage.getImgView()));

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main, "Restore", Toast.LENGTH_SHORT).show();
            }
        });
        btnDeleteImmediately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        doSthWithOrientation(newOrientation);
    }

    private void doSthWithOrientation(int newOrientation ) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {

        }
        //Toast.makeText(getContext(),"oke",Toast.LENGTH_SHORT).show();
    }
    
}
