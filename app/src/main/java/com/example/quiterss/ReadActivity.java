package com.example.quiterss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.quiterss.bean.Item;

public class ReadActivity extends Activity {

    TextView titleTv, descTv;

    MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_item);

        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);

        titleTv = findViewById(R.id.title_tv);
        descTv = findViewById(R.id.desc_tv);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        Item item = mySQLiteOpenHelper.QueryItem(title);
        titleTv.setText(item.getTitle());
        descTv.setText(item.getDescription());
    }
}
