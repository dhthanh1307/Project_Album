package com.example.project_album;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AccountLayout extends Fragment {
    MainActivity main;
    Context context = null;
    EditText editNickname;
    EditText editPassword;
    EditText editNewPassword;
    EditText editRetypeNewPassword;
    Button btnSaveChange;
    Button btnLogout;
    ArrayList<String> accountInfo;
    public AccountLayout() {
    }

    public static AccountLayout newInstance(String strArg) {
        AccountLayout fragment = new AccountLayout();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_layout, container, false);
        editNickname = view.findViewById(R.id.editAccountNickname);
        editPassword = view.findViewById(R.id.editAccountPassword);
        editNewPassword = view.findViewById(R.id.editNewPassword);
        editRetypeNewPassword = view.findViewById(R.id.editRetypeNewPassword);
        btnSaveChange = view.findViewById(R.id.btnEditAccount);
        btnLogout = view.findViewById(R.id.btnLogout);

        accountInfo = MainActivity.dataResource.getAccountInfo(MainActivity.userID);

        editNickname.setText(accountInfo.get(0));
        editPassword.setText("");
        editNewPassword.setText("");
        editRetypeNewPassword.setText("");

        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPassword.getText().toString().compareTo(accountInfo.get(1))!=0) {
                    Toast.makeText(main, "Wrong password!", Toast.LENGTH_SHORT).show();
                }
                else if (editNewPassword.getText().toString().compareTo(editRetypeNewPassword.getText().toString())!=0) {
                    Toast.makeText(main, "New password and retyped new password are not matched!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(main);
                    alertDialogBuilder.setMessage("Are you sure you want to save the changes?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newNickname = editNickname.getText().toString();
                            String newPassword = editNewPassword.getText().toString();
                            if (newPassword.length() == 0) {
                                newPassword = accountInfo.get(1);
                            }
                            MainActivity.dataResource.updateAccountInfo(MainActivity.userID, newNickname, newPassword);
                            accountInfo = MainActivity.dataResource.getAccountInfo(MainActivity.userID);
                            editPassword.setText(accountInfo.get(1));
                            editPassword.setText("");
                            editNewPassword.setText("");
                            editRetypeNewPassword.setText("");
                            Toast.makeText(main, "Update successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                main.finish();
            }
        });

        return view;
    }
}