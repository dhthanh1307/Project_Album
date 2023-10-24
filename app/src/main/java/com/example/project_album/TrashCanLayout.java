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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrashCanLayout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrashCanLayout extends Fragment {
    ImageAdapter mGridAdapter;
    public static ArrayList<Image> images = new ArrayList<Image>();
    MainActivity main;
    Context context = null;
    TextView txtTotal;
    GridView mGridView;
    Button btnChoose;

    Bundle myOriginalMemoryBundle;
    boolean isChooseBtnChoose=true;

    public TrashCanLayout() {
        // Required empty public constructor
    }

    public static TrashCanLayout newInstance(String strArg) {
        for(int i = 0;i<5;i++){
            images.add(new Image(R.drawable.img));
            images.add(new Image(R.drawable.img_1));
            images.add(new Image(R.drawable.img_3));
            images.add(new Image(R.drawable.img_4));

        }
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
            if(images.size() ==0){
                for(int i = 20;i<40;i++){
                    images.add(AllLayout.images.get(i));
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
        View mView = inflater.inflate(R.layout.fragment_trash_can_layout, container, false);

        mGridView=mView.findViewById(R.id.grid_view_trashcan);
        txtTotal=mView.findViewById(R.id.txt_display_total_picture_deleted);
        mGridAdapter=new ImageAdapter(main,R.layout.item_image,images);
        mGridView.setAdapter(mGridAdapter);
        btnChoose=mView.findViewById(R.id.btn_choose);

        //xoay màn hình
        doSthWithOrientation(getResources().getConfiguration().orientation);

        //Khi click vào ảnh bất kì
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showBigScreen(i);
            }
        });

        //Khi mà click giữ lâu vào item để chuyeenr sang chế độ giống như nút choose
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        //Khi Click vào choose ảnh để xóa
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChooseBtnChoose) {
                    isChooseBtnChoose = false;
                    btnChoose.setText("Cancel");
                    btnChoose.setBackgroundColor(getResources().getColor(R.color.green, context.getTheme()));
                    mGridAdapter.setIsCheckBoxVisible(true);
                    mGridAdapter.notifyDataSetChanged();
                } else {
                    isChooseBtnChoose = true;
                    btnChoose.setText("Choose");
                    btnChoose.setBackgroundColor(getResources().getColor(R.color.blue_press, context.getTheme()));
                    mGridAdapter.setIsCheckBoxVisible(false);
                    mGridAdapter.notifyDataSetChanged();
                }

            }
        });




//Khi scroll thì cái text cuối hiển thị
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
                    txtTotal.setText("Total: "+String.valueOf(images.size()));
                    txtTotal.setVisibility(View.VISIBLE);
                } else {
                    // Hide the end message if not at the end
                    txtTotal.setVisibility(View.GONE);
                }
            }
        });

        mGridView=mView.findViewById(R.id.gridView);
        mGridAdapter=new ImageAdapter(main,R.layout.item_image,images);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setSelection(images.size()-1) ;
        return mView;
    }
    @Override
    public void onResume() {
        super.onResume();
        mGridView.setSelection(mGridAdapter.getCount() - 1);
        txtTotal.setText("Total: "+String.valueOf(images.size()));
        txtTotal.setVisibility(View.VISIBLE);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        doSthWithOrientation(newOrientation);
        mGridAdapter.notifyDataSetChanged();
    }

    private void doSthWithOrientation(int newOrientation ) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setNumColumns(5);
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridView.setNumColumns(3);
        }
    }

    private void showBigScreen(int position){
        FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentmanager.beginTransaction();
        ft.replace(R.id.replace_fragment_layout,new LargeImageFragment(images.get(position)));
        // ở đây còn khúc bên larg image xóa r gởi dề nữa
        ft.commit();

}
}