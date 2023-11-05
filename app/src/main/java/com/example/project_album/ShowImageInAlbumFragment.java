package com.example.project_album;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowImageInAlbumFragment extends Fragment implements View.OnClickListener {
    private ArrayList<Image> images;
    private ShowImageInAlbumAdapter image_adapter;
    private RecyclerView recyclerView;
    private TextView tv_back_to_album,tv_choose;
    Button btn_extend;
    public ShowImageInAlbumFragment(ArrayList<Image> images){
        this.images = images;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_in_album, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_image_in_album);
        DoSthWithOrientation(getResources().getConfiguration().orientation);
        tv_back_to_album = view.findViewById(R.id.tv_back);
        tv_choose = view.findViewById(R.id.tv_choose);
        btn_extend = view.findViewById(R.id.btn_many);
        image_adapter = new ShowImageInAlbumAdapter(getActivity(), R.layout.item_image, images);

        tv_back_to_album.setOnClickListener(this);
        tv_choose.setOnClickListener(this);
        recyclerView.setAdapter(image_adapter);

        return view;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int newOrientation = newConfig.orientation;
        DoSthWithOrientation(newOrientation);
        image_adapter.notifyDataSetChanged();
    }

    private void DoSthWithOrientation(int newOrientation) {
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == tv_back_to_album.getId()){
            FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentmanager.beginTransaction();
            ft.replace(R.id.replace_fragment_layout,AlbumLayout.newInstance("album"));
            ft.commit();
        }
        if(view.getId() == tv_choose.getId()){
            if(tv_choose.getText().equals("Chọn")){
                image_adapter.setChooseSelection(true);
                tv_choose.setText("Hủy");
            }
            else{
                image_adapter.setChooseSelection(false);
                image_adapter.resetChooseSelection();
                tv_choose.setText("Chọn");
                image_adapter.notifyDataSetChanged();
            }
        }

    }
}
