package com.example.project_album;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class LoginFragment extends Fragment {

    LoginActivity main;
    Context context = null;
    FragmentTransaction ft;
    TextView inputUsername;
    TextView inputPassword;
    Button btnLogin;
    TextView toSignUp;

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
                String username = inputUsername.getText().toString();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
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
            }
        });
        return view;
    }
}