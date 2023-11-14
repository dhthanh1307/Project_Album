package com.example.project_album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.color.utilities.TonalPalette;

import java.util.ArrayList;


public class MyAlbumFragment extends Fragment {
    public static GridViewAlbumAdapter gridViewAlbumAdapter;
    private TextView tvEdit;
    private TextView tvBack;

    private static GridView gridViewMyAlbum;
    private MainActivity main;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private TextView tv_cancel;
    private boolean isaddtoalbum = false;
    private static ArrayList<Album> albums = new ArrayList<>();
    public MyAlbumFragment(boolean isaddtoalbum){
        this.isaddtoalbum = isaddtoalbum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity)getActivity() ;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_album, container, false);
        tvBack = view.findViewById(R.id.tv_back);
        tvEdit = view.findViewById(R.id.tv_edit);
        gridViewMyAlbum = (GridView)view.findViewById(R.id.grid_view_my_album);
        linear1 = view.findViewById(R.id.linear1);
        linear2 = view.findViewById(R.id.linear2);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.bottom_navigation_album.setVisibility(View.VISIBLE);
                closeFragment();
            }
        });


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentmanager.beginTransaction();
                ft.replace(R.id.replace_fragment_layout,AlbumLayout.newInstance("album"));
                ft.commit();
            }
        });

        albums.addAll(AlbumLayout.albums);
        if (isaddtoalbum){
            linear1.setVisibility(View.INVISIBLE);
            linear2.setVisibility(View.VISIBLE);
            main.bottom_navigation_album.setVisibility(View.INVISIBLE);
            albums.remove(0);
        }
        else{
            linear2.setVisibility(View.INVISIBLE);
            linear1.setVisibility(View.VISIBLE);
        }
        gridViewAlbumAdapter = new GridViewAlbumAdapter(getActivity(),R.layout.item_album,albums);
        gridViewMyAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isaddtoalbum) {
                    FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fragmentmanager.beginTransaction();
                    ft.replace(R.id.replace_fragment_layout, new ShowImageInAlbumFragment(AlbumLayout.albums.get(i)));
                    ft.commit();
                }
                else{
                    ShowImageInAlbumFragment.AddToNewAlbum(albums.get(i).getName());
                    closeFragment();
                }
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
    private void closeFragment() {
        FragmentTransaction transaction = main.getSupportFragmentManager().beginTransaction();
        main.getSupportFragmentManager().popBackStack();
        transaction.remove(this);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
    }
    public static void DataChange(Album a){
        albums.add(a);
        gridViewAlbumAdapter.notifyDataSetChanged();
    }
}