package com.example.project_album;

import android.content.Context;
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

import java.util.ArrayList;


public class TrashCanLayout extends Fragment {
    ImageAdapter mGridAdapter;
    public static ArrayList<Image> images = new ArrayList<Image>();
    MainActivity main;
    Context context = null;
    TextView txtTotal;
    TextView txtDeleteRecently;
    GridView mGridView;
    Button btnChoose;
    Button btnDeleteChosenImages;
    Button btnRestoreChosenImages;

    Bundle myOriginalMemoryBundle;
    LinearLayout lastLinear;

    int count = 0;
    private ArrayList<Boolean> mSelectedItems;

    public TrashCanLayout() {
        Log.e("TrashCanLayout","constructor");
        // Required empty public constructor
    }

    public static TrashCanLayout newInstance(String strArg) {
//        for(int i = 0;i<5;i++){
//            images.add(new Image(R.drawable.img));
//            images.add(new Image(R.drawable.img_1));
//            images.add(new Image(R.drawable.img_3));
//            images.add(new Image(R.drawable.img_4));
//
//        }
        TrashCanLayout fragment = new TrashCanLayout();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myOriginalMemoryBundle = savedInstanceState;

        try {
            context = getActivity();
            main = (MainActivity) getActivity();
<<<<<<< Updated upstream
            if (images.size() == 0) {
                for (int i = 0; i < 54; i++) {
                    images.add(AllLayout.images.get(i));
                }
                mSelectedItems = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    mSelectedItems.add(false);
                }
            }
=======
//            if (images.size() == 0) {// cần if chỗ này để xử lí ấn từ all->trash->all->trash
//                // Đọc dữ liệu thì đã có bên Allayout đọc rồi
//                //Không cần đọc lại, chỉ cần lấy ra những biến là "T" thôi
//
////                for (int i = 0; i < AllLayout.images.size(); i++) {
////                    if (AllLayout.images.get(i).getDeleted().equals("T")) {
////                        images.add(AllLayout.images.get(i));
////                        Log.e("TrashCanLayout", String.valueOf(i));
////                    }
////                }
//            } else {
//                Log.e("Day la loi", "-------------ELSE------------");
//                for (int i = 0; i < images.size(); i++) {
//                    Log.e("Day la loi", "ID Anh=" + String.valueOf(images.get(i).getId()));
//                }
//
//            }
>>>>>>> Stashed changes
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_trash_can_layout, container, false);

        mGridView = mView.findViewById(R.id.grid_view_trashcan);
        txtTotal = mView.findViewById(R.id.txt_display_total_picture_deleted);
        mGridAdapter = new ImageAdapter(main, R.layout.item_image, images);
        mGridView.setAdapter(mGridAdapter);
        btnChoose = mView.findViewById(R.id.btn_choose);
        btnDeleteChosenImages = mView.findViewById(R.id.btn_delete_chosen_image);
        btnRestoreChosenImages = mView.findViewById(R.id.btn_restore_chosen_image);
        lastLinear = mView.findViewById(R.id.last_linear_in_trashcan);
        txtDeleteRecently = mView.findViewById(R.id.txt_delete_recently);

        mGridView = mView.findViewById(R.id.grid_view_trashcan);
        mGridAdapter = new ImageAdapter(main, R.layout.item_image, images);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setSelection(images.size() - 1);
        //Toast.makeText(main, "DAY LA CREATEVIEW", Toast.LENGTH_SHORT).show();
        //xoay màn hình
        doSthWithOrientation(getResources().getConfiguration().orientation);

        doBtnChooseWhenIsCancel();


        //Khi Click vào choose ảnh để xóa
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnChoose.getText().toString().equals("Choose")) {
                    doBtnChooseWhenIsChoose();

                } else {
                    doBtnChooseWhenIsCancel();
                }

            }
        });


//Khi scroll thì cái textTotal cuối hiển thị
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Do nothing here
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Để hiển thị cái txtTotal khi mà kéo xuống tới cuối.
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    // Show the end message when the last item is visible
                    txtTotal.setText("Total: " + String.valueOf(images.size()));
                    txtTotal.setVisibility(View.VISIBLE);
                } else {
                    // Hide the end message if not at the end
                    txtTotal.setVisibility(View.GONE);
                }
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mGridView.setSelection(mGridAdapter.getCount() - 1);
        txtTotal.setText("Total: " + String.valueOf(images.size()));
        txtTotal.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        doSthWithOrientation(newOrientation);
        mGridAdapter.notifyDataSetChanged();
    }

    private void doSthWithOrientation(int newOrientation) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setNumColumns(5);
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridView.setNumColumns(3);
        }
    }
    public void update(){
        try {
            mGridAdapter.notifyDataSetChanged();
        }
        catch(Exception e){

<<<<<<< Updated upstream
    private void showBigScreen(int position) {
        FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentmanager.beginTransaction();
        ft.replace(R.id.replace_fragment_layout, new LargeImageFragment(images.get(position)));
        // ở đây còn khúc bên larg image xóa r gởi dề nữa
        ft.commit();
    }

    private void doBtnChooseWhenIsChoose() {
        //Huy viec lang nghe truoc do
        mGridView.setOnItemClickListener(null);
        mGridView.setOnItemLongClickListener(null);


        count = 0;
        txtDeleteRecently.setText("You choose: ");
        lastLinear.setVisibility(View.VISIBLE);
        btnDeleteChosenImages.setVisibility(View.VISIBLE);
        btnRestoreChosenImages.setVisibility(View.VISIBLE);
        btnChoose.setText("Cancel");
        btnDeleteChosenImages.setText("Delete All");
        btnRestoreChosenImages.setText("Restore All");
        btnChoose.setBackgroundColor(getResources().getColor(R.color.green, context.getTheme()));
        mGridAdapter.setIsCheckBoxVisible(true);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedItems.set(i, !mSelectedItems.get(i));
                mGridAdapter.setItemSelection(i, mSelectedItems.get(i));
                //Log.e("Loi1", String.valueOf(mSelectedItems.get(i))+String.valueOf(i));
                CheckBox checkboxChoose = view.findViewById(R.id.check_box);
                if (mSelectedItems.get(i)) {
                    count++;
                    txtDeleteRecently.setText("You choose: " + String.valueOf(count));
                    checkboxChoose.setVisibility(View.VISIBLE);
                    checkboxChoose.setChecked(true);
                } else {
                    count--;
                    txtDeleteRecently.setText("You choose: " + String.valueOf(count));
                    checkboxChoose.setVisibility(View.GONE);
                    checkboxChoose.setChecked(false);
                }
                if (count>0){
                    btnDeleteChosenImages.setText("Delete "+String.valueOf(count)+" images");
                    btnRestoreChosenImages.setText("Restore "+String.valueOf(count)+" images");
                }else{
                    btnDeleteChosenImages.setText("Delete All");
                    btnRestoreChosenImages.setText("Restore All");
                }
            }
        });
        btnDeleteChosenImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Long> arrayIdDelete=new ArrayList<>();
                for (int i=0;i<images.size();i++){
                    if(mSelectedItems.get(i)){
                        arrayIdDelete.add(images.get(i).getId());
                    }
                }
//                Log.e("Err","kj"+String.valueOf(arrayIdDelete.size()));
//                Log.e("Err","kj"+String.valueOf(mSelectedItems.size()));
//                for (int i=0;i<arrayIdDelete.size();i++){
//                    Log.e("Err","kjkdkd"+String.valueOf(arrayIdDelete.get(i)));
//                }
                for (int i=0;i<mSelectedItems.size();i++){
                    if (mSelectedItems.get(i)){
                        Log.e("Err","Dung "+String.valueOf(i));

                    }
                }

//                Log.e("Err","ffff"+String.valueOf(MainActivity.dataResource.getCount()));

                //Xóa dòng hình ảnh đó đó ở dataResource
                MainActivity.dataResource.deleteArrayImage(arrayIdDelete);
                Log.e("Err","ffff"+String.valueOf(MainActivity.dataResource.getCount()));

                mSelectedItems.clear();
                images.clear();

                //Lấy lại hình ảnh có is_delete="T" và set lại cho adapter
                images=MainActivity.dataResource.getImageInDbHasIsDeletedIsTrue();

//                Log.e("Err","ffff"+String.valueOf(MainActivity.dataResource.getCount()));
                for (int i=0;i<images.size();i++){
                    mSelectedItems.add(false);
                }
                //VERY IMPORTANT
                //Này là thay đổi selection,image ở bên adapter ở adapter thôi
                mGridAdapter.resetItemSelectionArray(images);
                doBtnChooseWhenIsCancel();
            }
        });
    }

    private void doBtnChooseWhenIsCancel() {
        // Hủy việc lắng nghe trước đó để tạo 1 lắng nghe mới.
        mGridView.setOnItemClickListener(null);
        mGridView.setOnItemLongClickListener(null);
        mGridView.setSelection(images.size() - 1);
        txtDeleteRecently.setText("Delete Recently");
        lastLinear.setVisibility(View.GONE);
        btnDeleteChosenImages.setVisibility(View.GONE);
        btnRestoreChosenImages.setVisibility(View.GONE);
        btnChoose.setText("Choose");
        btnChoose.setBackgroundColor(getResources().getColor(R.color.blue_press, context.getTheme()));
        mGridAdapter.setIsCheckBoxVisible(false);
        for (int i = 0; i < images.size(); i++) {
            mSelectedItems.set(i, false);
            mGridAdapter.setItemSelection(i, false);
        }
        //cần set lại chỗ này để cho nó xét cái select bên adapter để mà nó k hiển thị
        //mấy cái checkbox nữa
        mGridAdapter.notifyDataSetChanged();

        //Khi click vào ảnh bất kì
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showBigScreen(i);
            }
        });
        //Khi mà click giữ lâu vào item để chuyển sang chế độ giống như nút choose
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                doBtnChooseWhenIsChoose();
                return false;
            }
        });
=======
        }
>>>>>>> Stashed changes
    }

}