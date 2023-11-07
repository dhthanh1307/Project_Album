package com.example.project_album;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpFragment extends Fragment {

    LoginActivity main;
    Context context = null;

    EditText editUserName, editPhone,editPass,editPass2, editEmail;

    Button btnSignUp;
    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance(String param1) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("strArg1", param1);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        editEmail=view.findViewById(R.id.editEmail);
        editPass=view.findViewById(R.id.editPassword);
        editPass2=view.findViewById(R.id.editPassword2  );
        editUserName=view.findViewById(R.id.editUserName);
        editPhone=view.findViewById(R.id.editPhone);
        btnSignUp=view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editPass.getText().toString().compareTo(editPass2.getText().toString())!=0){
                    Toast.makeText(main, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(MainActivity.dataResource.checkSignUp(editUserName.getText().toString())){
                        User user= new User(editUserName.getText().toString(),editPass.getText().toString(),
                                editEmail.getText().toString(),editPhone.getText().toString());
                        long i=MainActivity.dataResource.InsertUser(user);
                        Toast.makeText(main, "Tài khoản đã được tạo thành công", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.login_container, new LoginFragment());
                        ft.commit();
                    }
                    else {
                        Toast.makeText(main, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        return view;
    }
}