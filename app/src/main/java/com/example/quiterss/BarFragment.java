package com.example.quiterss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.quiterss.bean.Folder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private EditText search_et;
    private ImageView search_iv, more_iv;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarFragment newInstance(String param1, String param2) {
        BarFragment fragment = new BarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar, container, false);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getActivity());
        search_et = view.findViewById(R.id.search_et);
        search_iv = view.findViewById(R.id.search_iv);
        more_iv = view.findViewById(R.id.more_iv);
        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                SearchFragment searchFragment = new SearchFragment();
                Bundle args = new Bundle();
                args.putString("toSearch", search_et.getText().toString());
                searchFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.frag_main_container, searchFragment).addToBackStack(null).commit();
            }
        });
        more_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
                Toast.makeText(getActivity(), "you clicked more!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.more_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                switch (item.getItemId()){
                    case R.id.add_url://添加url
                        View urlView = getLayoutInflater().inflate(R.layout.add_dialog, null);
                        builder.setView(urlView)
                                .setPositiveButton("订阅", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText et = urlView.findViewById(R.id.add_url);
                                        mySQLiteOpenHelper.InsertItemFromURL(et.getText().toString());
                                    }
                                })
                                .show();
                        //mySQLiteOpenHelper.InsertItemFromURL(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         );
                        return true;
                    case R.id.add_folder://添加文件夹
                        View folderView = getLayoutInflater().inflate(R.layout.add_folder, null);
                        builder.setView(folderView)
                                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText et = folderView.findViewById(R.id.insert_file_et);

                                        if(!et.getText().toString().equals(null)){
                                            Folder folder = new Folder();
                                            folder.setName(et.getText().toString());
                                            Log.d("--------------", et.getText().toString());
                                            mySQLiteOpenHelper.InsertFolder(folder);
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .show();
                        return true;
                    case R.id.refresh_url://刷新url
                        return true;
                    default:
                        return false;
                }
            }});
        popupMenu.show();
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.more_menu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.add_url://添加url
//                return true;
//            case R.id.add_folder://添加文件夹
//                return true;
//            case R.id.refresh_url://刷新url
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
}