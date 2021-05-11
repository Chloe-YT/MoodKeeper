package com.e.moodkeeper.activity;

import android.content.Intent;
import android.os.Bundle;

import com.e.moodkeeper.R;
import com.e.moodkeeper.ui.fragment.ShowDiaryFragment;
import com.e.moodkeeper.ui.fragment.ShowCalendarFragment;
import com.e.moodkeeper.ui.fragment.ShowChartFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.e.moodkeeper.adapter.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import util.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static MyDatabaseHelper dbHelper;

    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ShowDiaryFragment());
        fragments.add(new ShowChartFragment());
        fragments.add(new ShowCalendarFragment());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        for (int i = 0; i < TAB_TITLES.length; i++) {
            tabs.getTabAt(i).setText(TAB_TITLES[i]);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        Button mineBt = (Button) findViewById(R.id.mine);
        mineBt.setOnClickListener(this);

        dbHelper = new MyDatabaseHelper(this, "MoodKeeper.db", null, 2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine:
                Intent intent1 = new Intent(MainActivity.this, MineActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.fab:
                Intent intent2 = new Intent(MainActivity.this, ActivityChooseMood.class);
                startActivityForResult(intent2,2);
                break;
        }
    }
}