package com.example.quiterss.mainPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.quiterss.MySQLiteOpenHelper;
import com.example.quiterss.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private ExpandableListView elv;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String label) {
        Bundle args = new Bundle();
        args.putString("label", label);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getActivity());
        elv = getView().findViewById(R.id.main_elv);
        String label = getArguments().getString("label");
        ExpandedAdapter adapter;
        List<String> group = new ArrayList<>();
        List<List<String>> child = new ArrayList<>();

        if(label.equals("默认")){
            group = mySQLiteOpenHelper.QueryFolderByUserOrSys("0");
            for(int i = 0; i < group.size(); i++){
                child.add(mySQLiteOpenHelper.QueryItemByFolder(group.get(i)));
            }
        }else if (label.equals("我的")){
            group = mySQLiteOpenHelper.QueryFolderByUserOrSys("1");
            for(int i = 0; i < group.size(); i++){
                child.add(mySQLiteOpenHelper.QueryItemByFolder(group.get(i)));
            }
        }
        adapter = new ExpandedAdapter(getActivity(), group, child);



        elv.setAdapter(adapter);
    }
}