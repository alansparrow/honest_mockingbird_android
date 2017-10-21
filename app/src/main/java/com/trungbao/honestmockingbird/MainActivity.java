package com.trungbao.honestmockingbird;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by baotrungtn on 10/22/17.
 */

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {
        // Register firebase notification service (news topic)
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        SharedInfo.setSharedPrefs(getSharedPreferences("com.trungbao.honestmockingbird", MODE_PRIVATE));

        // Only run once
        if (SharedInfo.getUserToken() == null) {
            SharedInfo.setupUserToken();
        } else {
            Log.i(TAG, "retrieve user token: " + SharedInfo.getUserToken());
        }
    }
}
