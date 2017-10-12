package com.trungbao.honestmockingbird;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.trungbao.honestmockingbird.model.News;

/**
 * Created by baotrungtn on 10/12/17.
 */

public class NewsPageActivity extends SingleFragmentActivity {
    private static final String NEWS = "com.trungbao.honestmockingbird.NEWS";

    public static Intent newIntent(Context context, News news) {
        Intent i = new Intent(context, NewsPageActivity.class);
        i.putExtra(NEWS, news);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        return NewsPageFragment.newInstance((News) getIntent().getSerializableExtra(NEWS));
    }

    @Override
    public void onBackPressed() {
        NewsPageFragment newsPageFragment = (NewsPageFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (newsPageFragment != null && newsPageFragment.webViewGoBack()) {
            return;
        }
        super.onBackPressed();
    }
}
