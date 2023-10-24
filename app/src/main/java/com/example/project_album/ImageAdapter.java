package com.example.project_album;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Image> images;
    private int idLayout;
    private Activity activity;
    private CheckBox checkBoxChoose;

    boolean isCheckBoxVisible = false;
    private ArrayList<Boolean> mSelectedItems;

    public ImageAdapter(Activity activity, int idLayout, ArrayList<Image> images) {
        this.images = images;
        this.activity = activity;
        this.idLayout = idLayout;

        mSelectedItems = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            mSelectedItems.add(false);
        }
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = activity.getLayoutInflater().inflate(idLayout, null);
        ImageView img = view.findViewById(R.id.img);
        img.setImageResource(images.get(i).imgView);
        img.setTag(i);
        final int position = i;
        checkBoxChoose = view.findViewById(R.id.check_box);
        //Vì mỗi lần cuộn là GridView sẽ bị như notify ấy, nên là cần setCheck lại
       checkBoxChoose.setChecked(mSelectedItems.get(i));

        if (isCheckBoxVisible == true) {
            checkBoxChoose.setVisibility(View.VISIBLE);
            //Toast.makeText(activity, "day la", Toast.LENGTH_SHORT).show();

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Đảo ngược trạng thái của checkbox khi click vào hình ảnh
                    boolean newState = !mSelectedItems.get(position);
                    setItemSelection(position, newState);
                    checkBoxChoose.setChecked(newState);
                    //notifyDataSetChanged();
                }
            });
            checkBoxChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.e("Loi", "Helllllll"+String.valueOf(position));
                    setItemSelection(position, b);
                    //notifyDataSetChanged();
                    talk();
                }
            });
        } else {
            checkBoxChoose.setVisibility(View.GONE);
        }
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.parent);
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            img.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.Width / 3 - 40, MainActivity.Width / 3 - 40));
        else {
            img.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.Height / 5 - 30, MainActivity.Height / 5 - 30));
        }
        return view;
    }

    // Phương thức để thiết lập trạng thái của mục (được chọn hoặc không được chọn)
    public void setItemSelection(int position, boolean isSelected) {
        mSelectedItems.set(position, isSelected);
    }

    public void setIsCheckBoxVisible(boolean check) {
        isCheckBoxVisible = check;
    }

    public void talk() {
        String temp = "";
        for (int i = 0; i < mSelectedItems.size(); i++) {
            if (mSelectedItems.get(i) == true) {
                temp = temp + String.valueOf(i) + "----";
            }
        }
        Log.e("Loi", temp);
    }
}
