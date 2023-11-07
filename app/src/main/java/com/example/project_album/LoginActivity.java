package com.example.project_album;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MainActivity.dataResource = new DataResource(this);
        MainActivity.dataResource.open();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, new LoginFragment())
                .commit();
    }
}