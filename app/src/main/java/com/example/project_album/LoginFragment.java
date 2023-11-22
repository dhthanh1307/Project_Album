package com.example.project_album;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class LoginFragment extends Fragment {

    LoginActivity main;
    Context context = null;
    FragmentTransaction ft;
    TextView inputUsername;
    TextView inputPassword;
    Button btnLogin;
    TextView toSignUp;
    String username;

    public static LoginFragment newInstance(String strArg) {
        LoginFragment fragment = new LoginFragment();
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
            main = (LoginActivity) getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        inputUsername = view.findViewById(R.id.inputUsername);
        inputPassword = view.findViewById(R.id.inputPassword);
        toSignUp = view.findViewById(R.id.btnToSignUp);
        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.login_container, new SignUpFragment());
                ft.commit();

               // getFragmentManager().beginTransaction().replace(R.id.container, new SignUpFragment()).commit();
            }
        });
        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Khi test
                username = inputUsername.getText().toString();
                //Khi sử dụng thật xóa 4 dòng trên, bỏ comment các dòng dưới này
//                if (inputUsername.getText().toString().length() == 0) {
//                    Toast.makeText(main, "Tên đăng nhập không thể để trống", Toast.LENGTH_SHORT).show();
//                }
//                else if (inputPassword.getText().toString().length() == 0) {
//                    Toast.makeText(main, "Mật khẩu không thể để trống", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    String username = inputUsername.getText().toString();
//                    String password = inputPassword.getText().toString();
//                    if (MainActivity.dataResource.checkLogin(username, password) == true) {
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("username", username);
//                        startActivity(intent);
//                    }
//                    else {
//                        Toast.makeText(main, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
//                    }
//                }

                //doc database
                AllLayout.images = MainActivity.dataResource.getAllImage();
                initDataResource();
            }
        });
        return view;
    }
    private void initDataResource() {

        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.show();
        Thread my = new Thread(new Runnable() {
            @Override
            public void run() {
                if (AllLayout.images.size() == 0) {
                    int img[] = {R.drawable.img, R.drawable.img_2,
                            R.drawable.img_3,
                            R.drawable.img_4, R.drawable.img_5, R.drawable.img_6};
                    ArrayList<Bitmap> bitmaps = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        bitmaps.add(BitmapFactory.decodeResource(getResources(), img[i]));
                    }
                    for (int j = 0; j < 5; j++) {
                        for (int i = 0; i < 6; i++) {
                            AllLayout.images.add(new Image(bitmaps.get(i), GenerateName()));
                            AllLayout.images.get(j * 6 + i).setId(MainActivity.dataResource.
                                    InsertImage(AllLayout.images.get(j * 6 + i), 1));
                            //images.get(j*6+i).setImgBitmap(main.ChangeImageToBitmap(img[i]));
                            //debug(String.valueOf(j*6+i));
                        }
                    }
                }
                else{
                    for (int i = AllLayout.images.size() - 1; i >= AllLayout.images.size()
                            - 15 && i >=0; i--) {
                        if(AllLayout.images.get(i).getImgBitmap() == null)
                            AllLayout.images.get(i).setImgBitmap(getImageFromPath
                                    (AllLayout.images.get(i).getPath()));
                    }
                }
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                main.finish();
            }
        });
        my.start();

    }

    public Bitmap ChangeImageToBitmap(int id){
        return BitmapFactory.decodeResource(getResources(), id);
    }
    public String GenerateName(){
        return System.currentTimeMillis() + ".jpeg";
    }
    private Bitmap getImageFromPath(String path) {
        Bitmap b =null;
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