package com.example.project_album;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteLayout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteLayout extends Fragment {

    MainActivity main;
    Context context = null;
    public static Image []images={new Image(R.drawable.img),new Image(R.drawable.img),new Image(R.drawable.img),new Image(R.drawable.img)};
    ImageAdapter adapter;
    public FavoriteLayout() {
        // Required empty public constructor
    }

    public static FavoriteLayout newInstance(String strArg) {
        FavoriteLayout fragment = new FavoriteLayout();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (MainActivity) getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view;
        view=inflater.inflate(R.layout.fragment_all_layout, container, false);
        adapter=new ImageAdapter(requireContext(),images);
        GridView gridView = view.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        return view;
    }

}