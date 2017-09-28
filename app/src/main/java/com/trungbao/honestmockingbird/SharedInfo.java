package com.trungbao.honestmockingbird;

import android.content.SharedPreferences;

import com.trungbao.honestmockingbird.model.News;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by baotrungtn on 9/27/17.
 */

public class SharedInfo {
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String USER_VOTE = "USER_VOTE";
    public static final String HOLD_VOTE = "HOLD";
    public static final String BUY_VOTE = "BUY";
    public static final String SELL_VOTE = "SELL";
    public static final String NEUTRAL_VOTE = "NEUTRAL";

    private static SharedPreferences mSharedPrefs = null;
    private static String mUserToken = null;

    public static SharedPreferences getSharedPrefs() {
        return mSharedPrefs;
    }

    public static void setSharedPrefs(SharedPreferences mSharedPrefs) {
        SharedInfo.mSharedPrefs = mSharedPrefs;
    }

    public static void setUserVote(String newsId, String userVote) {
        mSharedPrefs.edit().putString(USER_VOTE + newsId, userVote).commit();
    }

    public static String getUserVote(News news, boolean canBeNull) {
        if (canBeNull == true) {
            return mSharedPrefs.getString(USER_VOTE + news.getId(), null);
        } else {
            return mSharedPrefs.getString(USER_VOTE + news.getId(), NEUTRAL_VOTE);
        }
    }

    public static String getUserToken() {
        String result = null;

        if (mSharedPrefs != null) {
            result = mSharedPrefs.getString(USER_TOKEN, null);
        }

        return result;
    }

    public static void setUserToken(String mUserToken) {
        if (mSharedPrefs != null) {
            mSharedPrefs.edit().putString(USER_TOKEN, mUserToken).commit();
        }
    }
}
