package com.example.project_album;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TrashCanLayout extends Fragment {
    public static ShowImageAdapter mGridAdapter;
    public static ArrayList<Image> images = new ArrayList<Image>();
    MainActivity main;
    Context context = null;
    TextView txtTotal;
    TextView txtDeleteRecently;
    RecyclerView mGridView;
    Button btnChoose;
    Button btnDeleteChosenImages;
    Button btnRestoreChosenImages;

    Bundle myOriginalMemoryBundle;
    LinearLayout lastLinear;

    public TrashCanLayout() {
        Log.e("TrashCanLayout", "constructor");
        // Required empty public constructor
    }

    public static TrashCanLayout newInstance(String strArg) {
        TrashCanLayout fragment = new TrashCanLayout();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("DEBUG", "onCreate of TrashCan");
        myOriginalMemoryBundle = savedInstanceState;

        try {
            context = getActivity();
            main = (MainActivity) getActivity();
            if (images.size() == 0) {// cần if chỗ này để xử lí ấn từ all->trash->all->trash
                // Đọc dữ liệu thì đã có bên Allayout đọc rồi
                //Không cần đọc lại, chỉ cần lấy ra những biến là "T" thôi

                for (int i = 0; i < AllLayout.images.size(); i++) {
                    if (AllLayout.images.get(i).getDeleted().equals("T")) {
                        images.add(AllLayout.images.get(i));
                        Log.e("TrashCanLayout", String.valueOf(i));
                    }
                }
            } else {
                Log.e("Day la loi", "-------------ELSE------------");
                for (int i = 0; i < images.size(); i++) {
                    Log.e("Day la loi", "ID Anh=" + String.valueOf(images.get(i).getId()));
                }

            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("DEBUG", "onCreateView of TrashCan");
        View mView = inflater.inflate(R.layout.fragment_trash_can_layout, container, false);
        mGridView = mView.findViewById(R.id.grid_view_trashcan);
        txtTotal = mView.findViewById(R.id.txt_display_total_picture_deleted);
        btnChoose = mView.findViewById(R.id.btn_choose);
        btnDeleteChosenImages = mView.findViewById(R.id.btn_delete_chosen_image);
        btnRestoreChosenImages = mView.findViewById(R.id.btn_restore_chosen_image);
        lastLinear = mView.findViewById(R.id.last_linear_in_trashcan);
        txtDeleteRecently = mView.findViewById(R.id.txt_delete_recently);
        DoSthWithOrientation(getResources().getConfiguration().orientation);
        mGridView = mView.findViewById(R.id.grid_view_trashcan);
        mGridAdapter = new ShowImageAdapter(main, R.layout.item_image, images, this);
        mGridView.setAdapter(mGridAdapter);
//        Toast.makeText(main, "helllo", Toast.LENGTH_SHORT).show();
        txtTotal.setText("Total: " + String.valueOf(images.size()));
        txtTotal.setVisibility(View.VISIBLE);
        //set hinhf anh ở cuối
        mGridView.scrollToPosition(mGridAdapter.getItemCount() - 1);
        //Toast.makeText(main, "DAY LA CREATEVIEW", Toast.LENGTH_SHORT).show();


        doBtnChooseWhenIsCancel();

        //Khi Click vào choose ảnh để xóa
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnChoose.getText().toString().equals("Chọn")) {
                    doBtnChooseWhenIsChoose();
                } else {
                    doBtnChooseWhenIsCancel();
                }
            }
        });
        return mView;
    }

    public void doBtnChooseWhenIsChoose() {

        txtDeleteRecently.setText("Chọn ảnh");
        lastLinear.setVisibility(View.VISIBLE);
        btnDeleteChosenImages.setVisibility(View.VISIBLE);
        btnRestoreChosenImages.setVisibility(View.VISIBLE);
        mGridAdapter.setChooseSelection(true);
        btnChoose.setText("Hủy");
        btnDeleteChosenImages.setText("Xóa tất cả");
        btnRestoreChosenImages.setText("Khôi phục tất cả");
        btnChoose.setBackgroundColor(getResources().getColor(R.color.green, context.getTheme()));
        main.mBottomNavigationView.setVisibility(View.GONE);
        //Chú ý chỗ này
        mGridAdapter.resetCount();
        btnDeleteChosenImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Hiển thị Dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(main);
                alertDialogBuilder.setMessage("Bạn đã chắc chắn muốn xóa ảnh?");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (btnDeleteChosenImages.getText().toString().equals("Xóa tất cả")) {
                            images.clear();
                            mGridAdapter.setmSelectedArray();
                            mGridAdapter.setChosenArrayImages();
                            mGridAdapter.notifyDataSetChanged();
                            // chỗ này còn xóa ở database nữa.
                            for (int i = 0; i < images.size(); i++) {
                                MainActivity.dataResource.deleteImage(images.get(i));
                            }
                            doBtnChooseWhenIsCancel();
                        }
                        ArrayList<Image> arrayIdDelete = mGridAdapter.getChosenArrayImages();
                        //Xóa dòng hình ảnh đó đó ở dataResource
                        for (int i = 0; i < arrayIdDelete.size(); i++) {
                            MainActivity.dataResource.deleteImage(arrayIdDelete.get(i));
                        }

                        Log.e("Err", "ffff" + String.valueOf(MainActivity.dataResource.getCount()));
                        //xóa ảnh ở images hiện hành
                        int size = images.size();
                        ArrayList<Boolean> mSelectedItems = mGridAdapter.getmSelectedArray();
                        for (int i = 0; i < size; i++) {
                            if (mSelectedItems.get(i)) {
                                size--;
                                images.remove(i);
                                mSelectedItems.remove(i);
                                i--;
                            }
                        }
                        mGridAdapter.setmSelectedArray();
                        mGridAdapter.setChosenArrayImages();
                        mGridAdapter.notifyDataSetChanged();
                        doBtnChooseWhenIsCancel();
                    }
                });

                alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi chọn Cancel
                        dialog.dismiss(); // Đóng AlertDialog mà không làm gì cả
                    }
                });


                // Hiển thị AlertDialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        txtTotal.setText("Tổng " + String.valueOf(images.size()));
    }

    public void doBtnChooseWhenIsCancel() {
        txtDeleteRecently.setText("Xóa gần đây");
        lastLinear.setVisibility(View.GONE);
        btnDeleteChosenImages.setVisibility(View.GONE);
        btnRestoreChosenImages.setVisibility(View.GONE);
        btnChoose.setText("Chọn");
        main.mBottomNavigationView.setVisibility(View.VISIBLE);
        mGridAdapter.setChooseSelection(false);
        if (images.size() == 0) {
            btnChoose.setVisibility(View.INVISIBLE);
        } else {
            btnChoose.setVisibility(View.VISIBLE);
        }
        btnChoose.setBackgroundColor(getResources().getColor(R.color.blue_press, context.getTheme()));
        mGridAdapter.setmSelectedArray();
        mGridAdapter.setChosenArrayImages();
        mGridAdapter.notifyDataSetChanged();
        txtTotal.setText("Tổng: " + String.valueOf(images.size()));
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of TrashCan");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of TrashCan");
        super.onPause();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        DoSthWithOrientation(newOrientation);
        mGridAdapter.notifyDataSetChanged();
    }

    private void DoSthWithOrientation(int newOrientation) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setLayoutManager(new GridLayoutManager(getContext(),5));
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridView.setLayoutManager(new GridLayoutManager(getContext(),3));
        }
    }


}