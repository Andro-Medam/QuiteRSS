package com.example.quiterss.mainPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiterss.MySQLiteOpenHelper;
import com.example.quiterss.R;
import com.example.quiterss.bean.Item;
import com.example.quiterss.searchPage.RecyclerviewAdapter;

public class SearchActivity extends Activity {

    private RecyclerView recyclerView;

    MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_recyview);

        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);

        recyclerView = findViewById(R.id.search_item_rv);

        Intent intent = getIntent();
        String toSearch = intent.getStringExtra("toSearch");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerviewAdapter(this, toSearch));
    }
}
