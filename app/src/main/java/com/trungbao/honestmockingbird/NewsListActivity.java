package com.trungbao.honestmockingbird;

import android.support.v4.app.Fragment;

/**
 * Created by baotrungtn on 9/17/17.
 */

public class NewsListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new NewsListFragment();
    }
}
