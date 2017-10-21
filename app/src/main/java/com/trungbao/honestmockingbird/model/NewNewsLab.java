package com.trungbao.honestmockingbird.model;

import android.content.Context;

import com.trungbao.honestmockingbird.helper.NetworkRequester;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by baotrungtn on 9/17/17.
 */

public class NewNewsLab {
    private static final String TAG = "NewNewsLab";

    private static NewNewsLab sNewsLab;
    private Context mContext;

    private List<News> mNewsList;

    public static NewNewsLab get(Context context) {
        if (sNewsLab == null) {
            sNewsLab = new NewNewsLab(context);
        }
        return sNewsLab;
    }

    public static void refresh() {
        sNewsLab = null;
    }

    private NewNewsLab(Context context) {
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

    public void fetchNewNews(String beforeDateStr, int newsCount) {
        NetworkRequester myNetworkRequester = new NetworkRequester();
        myNetworkRequester.fetchNewNewsItems(mNewsList, beforeDateStr, newsCount);
    }


}
