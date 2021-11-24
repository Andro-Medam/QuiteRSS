package com.example.quiterss;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private BarFragment barFragment;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barFragment = new BarFragment();
        mainFragment = new MainFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.frag_bar_container, barFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_main_container, mainFragment).commit();
    }
}