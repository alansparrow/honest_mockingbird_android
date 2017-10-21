package com.trungbao.honestmockingbird.model;

import android.content.Context;

import com.trungbao.honestmockingbird.helper.NetworkRequester;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by baotrungtn on 10/22/17.
 */

public class HotNewsLab {
    private static final String TAG = "HotNewsLab";

    private static HotNewsLab sNewsLab;
    private Context mContext;

    private List<News> mNewsList;

    public static HotNewsLab get(Context context) {
        if (sNewsLab == null) {
            sNewsLab = new HotNewsLab(context);
        }
        return sNewsLab;
    }

    public static void refresh() {
        sNewsLab = null;
    }

    private HotNewsLab(Context context) {
        mContext = context.getApplicationContext();
        mNewsList = new ArrayList<>();
    }

    public void addNews(News n) {
        mNewsList.add(n);
    }

    public List<News> getNewsList() {
        return mNewsList;
    }

    public News getNews(UUID id) {
        for (News n : mNewsList) {
            if (n.getId().equals(id)) {
                return n;
            }
        }

        return null;
    }

    public void fetchHotNews(double score, int newsCount) {
        NetworkRequester myNetworkRequester = new NetworkRequester();
        myNetworkRequester.fetchHotNewsItems(mNewsList, score, newsCount);
    }
}
