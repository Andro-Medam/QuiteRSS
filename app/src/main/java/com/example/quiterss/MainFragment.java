package com.example.quiterss;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.quiterss.bean.Channel;
import com.example.quiterss.bean.Folder;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private ExpandableListView elv;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getActivity());

        elv = view.findViewById(R.id.main_elv);
        ExpandedAdapter adapter = new ExpandedAdapter(getActivity());

        elv.setAdapter(adapter);
        return view;
    }
}