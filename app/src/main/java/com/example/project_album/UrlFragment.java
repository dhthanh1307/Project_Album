package com.example.project_album;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import java.io.IOException;
import java.net.URL;

public class UrlFragment extends DialogFragment {
    private String url;
    public String getUrl() {
        return url;
    }
    private ImageView URLImage;
    public static String Tag = "URLDialog";
    private EditText edtURL;
    MainActivity main;
    Image img;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder urlDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        AlertDialog.Builder urlDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_url, null);
        edtURL = view.findViewById(R.id.edtURL);
        URLImage = view.findViewById(R.id.urlImage);
        urlDialog.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(img!=null) {
                            img.setId(main.dataResource.InsertImage(img, MainActivity.userID));
                            AllLayout.images.add(img);
                            MainActivity.images.add(img);
                            dismiss();
                        }
                        else{
                            Toast.makeText(main,"Không tìm thấy link",Toast.LENGTH_SHORT).show();
                            edtURL.setText("");
                        }
                    }
                });

        edtURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().startsWith("https://") || s.toString().startsWith("http://")) {

                    url = edtURL.getText().toString();
                    DownloadImgURL(url);
                }
            }
        });

        return urlDialog.create();
    }

    public void DownloadImgURL (String url) {
        boolean network = checkInternetConnection();
        if (!network) {
            return;
        }
        else {
            URLImage.setVisibility(View.VISIBLE);
            try {
                URL Url = new URL(url);
                Log.e("Qdsf","fdsafs");
                Bitmap image = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                Log.e("Qdsf","fdsafs");
                URLImage.setImageBitmap(image);
                Log.e("Qdsf","fdsafs");
                img = new Image(image,main.GenerateName());
            }
            catch(IOException e) {
                Log.e("UrlFragment",e.toString());
            }
        }
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(getContext(), "No network is currently active!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(getContext(), "Network is not connected!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(getContext(), "Network is not available!", Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(getContext(), "Network is OK!", Toast.LENGTH_SHORT).show();
        return true;
    }

}