package com.example.quiterss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.quiterss.bean.Channel;
import com.example.quiterss.bean.Folder;
import com.example.quiterss.bean.Folder_item;
import com.example.quiterss.bean.Item;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class BarFragment extends Fragment {

    private EditText search_et;
    private ImageView search_iv, more_iv;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private String addUrlString = null;
    Handler addURLHandler = null;

    public BarFragment() {
        // Required empty public constructor
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
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                switch (item.getItemId()) {
                    case R.id.add_url://添加url
                        View urlView = getLayoutInflater().inflate(R.layout.add_dialog, null);
                        builder.setView(urlView)
                                .setPositiveButton("订阅", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        EditText et = urlView.findViewById(R.id.insert_url_et);
                                        if (et.getText().toString().equals("")){
                                            Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            addUrlString = et.getText().toString();
                                            new Thread(addRSSTask).start();
                                        }
                                    }
                                })
                                .show();
                        return true;
                    case R.id.add_folder://添加文件夹
                        View folderView = getLayoutInflater().inflate(R.layout.add_folder, null);
                        builder.setView(folderView)
                                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText et = folderView.findViewById(R.id.insert_file_et);
                                        if (!et.getText().toString().equals("")) {
                                            Folder folder = new Folder();
                                            folder.setName(et.getText().toString());
                                            mySQLiteOpenHelper.InsertFolder(folder);
                                        } else {
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
            }
        });
        popupMenu.show();
    }

    Runnable addRSSTask = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d("-------*********", "run: " + addUrlString);
                SaxHelper saxHelper = readXmlForSAX(addUrlString);
//                "https://link.springer.com/search.rss?facet-content-type=Article&facet-journal-id=10664&channel-name=Empirical+Software+Engineering"
                Channel channel = saxHelper.getChannel();
                channel.setRSSlink("https://link.springer.com/search.rss?facet-content-type=Article&facet-journal-id=10664&channel-name=Empirical+Software+Engineering");
                Folder folder = saxHelper.getFolder();
                ArrayList<Folder_item> folder_items = saxHelper.getFolder_items();
                ArrayList<Item> items = saxHelper.getItems();
                mySQLiteOpenHelper.InsertFolder(folder);
                mySQLiteOpenHelper.InsertChannel(channel);
                for (Item item : items) {
                    mySQLiteOpenHelper.InsertItem(item);
                }
                for (Folder_item folder_item : folder_items){
                    mySQLiteOpenHelper.InsertFolderItem(folder_item);
                }
                Toast.makeText(getActivity(), "插入数据成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private SaxHelper readXmlForSAX(String link) throws Exception {
//        URL url = new URL("https://link.springer.com/search.rss?facet-content-type=Article&facet-journal-id=10664&channel-name=Empirical+Software+Engineering");
        URL url = new URL(link);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        SaxHelper saxHelper = new SaxHelper();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(inputStream, saxHelper);
        inputStream.close();
        return saxHelper;
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