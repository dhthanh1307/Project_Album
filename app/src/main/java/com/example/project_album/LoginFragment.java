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

import android.os.Trace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
    String password;
    int userID;
    boolean isadmin = false;

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
            int count = MainActivity.dataResource.countUser();
            if(count == 0){
                User user= new User("admin","admin",
                        "THASH@gmail.com","035555557");
                long i=MainActivity.dataResource.InsertUser(user);
            }
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
                if (inputUsername.getText().toString().length() == 0) {
                    Toast.makeText(main, "Tên đăng nhập không thể để trống", Toast.LENGTH_SHORT).show();
                }
                else if (inputPassword.getText().toString().length() == 0) {
                    Toast.makeText(main, "Mật khẩu không thể để trống", Toast.LENGTH_SHORT).show();
                }
                else {
                    username = inputUsername.getText().toString();
                    password = inputPassword.getText().toString();
                    userID = MainActivity.dataResource.checkLogin(username, password);
                    if (userID != -1) {
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("username", username);
//                        startActivity(intent);
                        //doc database
                        MainActivity.images = MainActivity.dataResource.getAllImage(userID);
                        if(username.equals("admin") && password.equals("admin")) {
                            isadmin = true;

                        }
                        initDataResource();
                    }
                    else {
                        Toast.makeText(main, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }
    private void initDataResource() {

        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.show();
        if(isadmin){
            dialog.setTitle("Đang tạo thông tin admin");
        }
        else{
            dialog.setTitle("Vui lòng đợi");
        }
        Thread my = new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.images.size() == 0 && isadmin) {
                    Log.e("LoginFragment","init All images");
                    ArrayList<String> links = Link.AllLinks();
                    ArrayList<Bitmap> bitmaps = new ArrayList<>();
                    for (int i = 0; i < links.size(); i++) {
                        //bitmaps.add(BitmapFactory.decodeResource(getResources(), img[i]));
                        try {
                            URL url = new URL(links.get(i));
                            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            bitmaps.add(image);
                            Log.e("LoginFragment",String.valueOf(i));
                        } catch(IOException e) {
                            Log.e("Loginfragment",e.toString());
                        }
                    }
                    for (int j = 0; j < 3; j++) {
                        for (int i = 0; i < bitmaps.size(); i++) {
                            MainActivity.images.add(new Image(bitmaps.get(i), GenerateName()));
                            MainActivity.images.get(i).setId(MainActivity.dataResource.
                                    InsertImage(MainActivity.images.get(i), userID));
                        }
                    }
                    getImageForLayouts();
                }
                else{

                    getImageForLayouts();
                    //Change path to bitmap
                    for (int i = AllLayout.images.size() - 1; i >= AllLayout.images.size()
                            - 15 && i >=0; i--) {
                        if(AllLayout.images.get(i).getImgBitmap() == null)
                            AllLayout.images.get(i).setImgBitmap(getImageFromPath
                                    (AllLayout.images.get(i).getPath()));
                    }
                }
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("username",username);
                intent.putExtra("password",password);

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
    private void getImageForLayouts(){
        for (int i = 0;i<MainActivity.images.size();i++){
            if(MainActivity.images.get(i).getDeleted().equals("F")){
                AllLayout.images.add(MainActivity.images.get(i));
                if(MainActivity.images.get(i).getFavorite().equals("T")){
                    FavoriteLayout.images.add(MainActivity.images.get(i));
                }
            }
            else{
                TrashCanLayout.images.add(MainActivity.images.get(i));
            }
        }
        Log.e("LoginFragment","AllLayout: "+String.valueOf(AllLayout
                .images.size())
        +", FavoriteLayout: "+String.valueOf(FavoriteLayout.images.size())
        +", TrashCanLayout: "+String.valueOf(TrashCanLayout.images.size()));
    }
}