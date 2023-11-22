package com.example.project_album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;


public class MyAlbumFragment extends Fragment {
    public GridViewAlbumAdapter gridViewAlbumAdapter;
    private TextView tvEdit;
    private TextView tvBack;

    private GridView gridViewMyAlbum;
    private MainActivity main;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private TextView tv_cancel;
    private boolean isaddtoalbum = false;
    private ShowImageInAlbumFragment showImageInAlbumFragment;
    public ArrayList<Album> albums = new ArrayList<>();
    public MyAlbumFragment(){
        this.isaddtoalbum = false;
    }
    public MyAlbumFragment(ShowImageInAlbumFragment showImageInAlbumFragment){
        this.isaddtoalbum =true;
        this.showImageInAlbumFragment = showImageInAlbumFragment;
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
                closeFragment();
            }
        });

        //albums = null;

        if (isaddtoalbum){
            for(int i = 1;i<AlbumLayout.albums.size();i++){
                albums.add(AlbumLayout.albums.get(i));
            }
            linear1.setVisibility(View.INVISIBLE);
            linear2.setVisibility(View.VISIBLE);
            main.bottom_navigation_album.setVisibility(View.INVISIBLE);
        }
        else{
            albums = AlbumLayout.albums;
            linear2.setVisibility(View.INVISIBLE);
            linear1.setVisibility(View.VISIBLE);
        }
        gridViewAlbumAdapter = new GridViewAlbumAdapter(main,R.layout.item_album,albums);
        gridViewMyAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isaddtoalbum) {
                    FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fragmentmanager.beginTransaction();
                    Fragment fragment = new ShowImageInAlbumFragment(AlbumLayout.albums.get(i));
                    ft.add(R.id.replace_fragment_layout, fragment);
                    ft.addToBackStack(fragment.getClass().getSimpleName());
                    ft.commit();
                }
                else{
                    showImageInAlbumFragment.AddToNewAlbum(albums.get(i).getName());
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
    public void DataChange(Album a){
        albums.add(a);
        gridViewAlbumAdapter.notifyDataSetChanged();
    }
    public void update(){
        albums = AlbumLayout.albums;
        gridViewAlbumAdapter.notifyDataSetChanged();
    }
}