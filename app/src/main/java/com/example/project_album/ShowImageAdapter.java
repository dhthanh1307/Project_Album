package com.example.project_album;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowImageAdapter extends RecyclerView.Adapter<ShowImageAdapter.ViewHolder> {
    private ArrayList<Image> images;
    private int idLayout;
    private MainActivity main;
    private ArrayList<Boolean> mSelectedArray = new ArrayList<>();
    private ArrayList<Image> chosenArrayImages = new ArrayList<>();
    private boolean chooseSelection;
    public int count = 0;
    private Button btnDeleteChosenImage;
    private Button btnResotreChosenImage;

    private Button btnUnFavorite;
    private Button btnShare;

    private Fragment myFragment;

    ShowImageAdapter(MainActivity activity, int idLayout, ArrayList<Image> images, Fragment myFragment) {
        this.main = activity;
        this.idLayout = idLayout;
        this.images = images;
        this.myFragment = myFragment;
        for (int i = 0; i < images.size(); i++) {
            mSelectedArray.add(false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = main.getLayoutInflater().inflate(idLayout, parent, false);
        if(main.getIDItemBottomNavigationView() == R.id.action_bin){
            btnDeleteChosenImage = main.findViewById(R.id.btn_delete_chosen_image);
            btnResotreChosenImage = main.findViewById(R.id.btn_restore_chosen_image);
        }else if(main.getIDItemBottomNavigationView() == R.id.action_favorite){
            btnUnFavorite = main.findViewById(R.id.btn_no_favorite);
            btnShare = main.findViewById(R.id.btn_share_in_favorite_layout);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageBitmap(images.get(position).getImgBitmap());
        debug(String.valueOf(position));
        if (main.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            debug("ok");
            RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams
                    (MainActivity.Width / 3 - 40, MainActivity.Width / 3 - 40);
            layout.setMargins(0, 0, 0, 10);
            holder.img.setLayoutParams(layout);

        } else {
            RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                    MainActivity.Height / 5 - 30, MainActivity.Height / 5 - 30);
            layout.setMargins(0, 0, 0, 10);
            holder.img.setLayoutParams(layout);
        }

        if (!mSelectedArray.get(position)) {

            holder.cb.setChecked(false);
            holder.cb.setVisibility(View.INVISIBLE);
        } else {
            holder.cb.setChecked(true);
            holder.cb.setVisibility(View.VISIBLE);
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main.getIDItemBottomNavigationView() == R.id.action_bin) {
                    Log.e("DEBUG","Helllo");
                    if (chooseSelection) {
                        if (mSelectedArray.get(position)) {
                            mSelectedArray.set(position, false);
                            holder.cb.setChecked(false);
                            holder.cb.setVisibility(View.INVISIBLE);
                            count--;
                            chosenArrayImages.remove(images.get(position));
                        } else {
                            chosenArrayImages.add(images.get(position));
                            mSelectedArray.set(position, true);
                            holder.cb.setChecked(true);
                            holder.cb.setVisibility(View.VISIBLE);
                            count++;
                        }
                        if (count > 0) {
                            btnDeleteChosenImage.setText("Xóa " + String.valueOf(count) + " ảnh");
                            btnResotreChosenImage.setText("Khôi phục " + String.valueOf(count) + " ảnh");
                        } else {
                            btnDeleteChosenImage.setText("Xóa tất cả");
                            btnResotreChosenImage.setText("Khôi phục tất cả");
                        }
                    } else {

                        FragmentManager fragmentmanager = main.getSupportFragmentManager();
                        FragmentTransaction ft = fragmentmanager.beginTransaction();
                        Fragment fragment = new ViewPagerTrashCanFragment(images, position);
                        ft.replace(R.id.replace_fragment_layout, fragment);
                        ft.addToBackStack(getClass().getSimpleName());
                        ft.commit();
                    }
                }
                else if(main.getIDItemBottomNavigationView() == R.id.action_favorite){
                    if (chooseSelection) {
                        if (mSelectedArray.get(position)) {
                            mSelectedArray.set(position, false);
                            holder.cb.setChecked(false);
                            holder.cb.setVisibility(View.INVISIBLE);
                            count--;
                            chosenArrayImages.remove(images.get(position));
                        } else {
                            chosenArrayImages.add(images.get(position));
                            mSelectedArray.set(position, true);
                            holder.cb.setChecked(true);
                            holder.cb.setVisibility(View.VISIBLE);
                            count++;
                        }
                        if (count > 0) {
                            btnUnFavorite.setText("Bỏ thích " + String.valueOf(count) + " ảnh");
                            btnShare.setText("Chia sẻ " + String.valueOf(count) + " ảnh");
                        } else {
                            btnUnFavorite.setText("Bỏ thích tất cả");
                            btnShare.setText("Chia sẻ tất cả");
                        }
                    } else {
                        FragmentManager fragmentmanager = main.getSupportFragmentManager();
                        FragmentTransaction ft = fragmentmanager.beginTransaction();
                        Fragment fragment = new ViewPagerAllLayoutFragment(images, position);
                        ft.replace(R.id.replace_fragment_layout, fragment);
                        ft.addToBackStack(getClass().getSimpleName());
                        ft.commit();
                    }
                }

            }
        });
        holder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (main.getIDItemBottomNavigationView() == R.id.action_bin){
                    ((TrashCanLayout) myFragment).doBtnChooseWhenIsChoose();
                }else if(main.getIDItemBottomNavigationView() == R.id.action_favorite){
                    ((FavoriteLayout) myFragment).doBtnChooseWhenIsChoose();
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private CheckBox cb;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            cb = itemView.findViewById(R.id.check_box);
            cb.setVisibility(View.INVISIBLE);
        }
    }

    private void debug(String s) {
        Log.e("Show image in album adapter: ", s);
    }

    public void setChooseSelection(boolean check) {
        chooseSelection = check;
    }

    public void setmSelectedArray() {
        mSelectedArray.clear();
        for (int i = 0; i < images.size(); i++) {
            mSelectedArray.add(false);
        }
    }

    public void setChosenArrayImages() {
        chosenArrayImages.clear();
    }

    public ArrayList<Image> getChosenArrayImages() {
        return chosenArrayImages;
    }

    public ArrayList<Boolean> getmSelectedArray() {
        return mSelectedArray;
    }

    public void resetCount() {
        this.count = 0;
    }
}
