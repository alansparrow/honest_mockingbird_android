package com.trungbao.honestmockingbird.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by baotrungtn on 9/17/17.
 */

public class NewsLab {
    private static NewsLab sNewsLab;
    private Context mContext;

    private List<News> mNewsList;

    public static NewsLab get(Context context) {
        if (sNewsLab == null) {
            sNewsLab = new NewsLab(context);
        }
        return sNewsLab;
    }

    private NewsLab(Context context) {
        mContext = context.getApplicationContext();
        mNewsList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            News n = new News();
            mNewsList.add(n);
        }
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


}
