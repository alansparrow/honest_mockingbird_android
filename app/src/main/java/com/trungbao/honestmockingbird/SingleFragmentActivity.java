package com.trungbao.honestmockingbird;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.trungbao.honestmockingbird.helper.NetworkRequester;

/**
 * Created by baotrungtn on 9/17/17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    private static final String TAG = "SingleFragmentActivity";

    protected abstract Fragment createFragment();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedInfo.setSharedPrefs(getSharedPreferences("com.trungbao.honestmockingbird", MODE_PRIVATE));

        // Only run once
        if (SharedInfo.getUserToken() == null) {
            SharedInfo.setupUserToken();
        } else {
            Log.i(TAG, "retrieve user token: " + SharedInfo.getUserToken());
        }

        // Register firebase notification service (news topic)
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        setContentView(R.layout.activity_fragment);



        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
            Log.i(TAG, "SingleFragmentActivity run commit fragment");
        }
    }
}
