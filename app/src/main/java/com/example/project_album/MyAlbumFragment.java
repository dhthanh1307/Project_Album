package com.example.project_album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.color.utilities.TonalPalette;


public class MyAlbumFragment extends Fragment {
    public static GridViewAlbumAdapter gridViewAlbumAdapter;
    private TextView tvEdit;
    private TextView tvBack;

    private GridView gridViewMyAlbum;
    public MyAlbumFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_album, container, false);
        tvBack = view.findViewById(R.id.tv_back);
        tvEdit = view.findViewById(R.id.tv_edit);
        gridViewMyAlbum = (GridView)view.findViewById(R.id.grid_view_my_album);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                ft.replace(R.id.replace_fragment_layout,AlbumLayout.newInstance("album"));
                ft.commit();
            }
        });
        gridViewAlbumAdapter = new GridViewAlbumAdapter(getActivity(),R.layout.item_album,AlbumLayout.albums);
        gridViewMyAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fragmentmanager.beginTransaction();
//                ft.replace(R.id.replace_fragment_layout,new ShowImageFragment(AlbumFragment.albums.get(i).getImages()));
//                ft.commit();
            }
        });
        gridViewMyAlbum.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenDialogEdit();
                return true;
            }
        });
        gridViewMyAlbum.setAdapter(gridViewAlbumAdapter);
        return view;
    }

    private void OpenDialogEdit() {

    }

    private void talk(){
        Toast.makeText(getContext(),"click", Toast.LENGTH_SHORT).show();
    }
}