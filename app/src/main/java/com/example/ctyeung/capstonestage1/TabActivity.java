package com.example.ctyeung.capstonestage1;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.example.ctyeung.capstonestage1.TabFragment1;

/*
 * Reference:
 *
 * Tab pages
 * https://www.truiton.com/2015/06/android-tabs-example-fragments-viewpager/
 * http://www.gadgetsaint.com/android/create-viewpager-tabs-android/#.W13LpC2ZPOQ
 * http://www.gadgetsaint.com/android/create-viewpager-tabs-android/#.W2Yc2S2ZPOR
 */
public class TabActivity extends AppCompatActivity {

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        Fragment fragment1 = new TabFragment1();
        Fragment fragment2 = new TabFragment2();
        Fragment fragment3 = new TabFragment3();
        Fragment fragment4 = new TabFragment4();

        String share = this.getResources().getString(R.string.share);
        String msg = this.getResources().getString(R.string.msg);
        String shape = this.getResources().getString(R.string.shape);
        String preview = this.getResources().getString(R.string.preview);

        adapter.addFragment(fragment1, share);
        adapter.addFragment(fragment2, msg);
        adapter.addFragment(fragment3, shape);
        adapter.addFragment(fragment4, preview);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (shouldAskPermissions())
            askPermissions();
    }

    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
