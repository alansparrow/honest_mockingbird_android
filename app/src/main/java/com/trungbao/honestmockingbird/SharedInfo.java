package com.trungbao.honestmockingbird;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.trungbao.honestmockingbird.helper.NetworkRequester;
import com.trungbao.honestmockingbird.model.News;

import java.net.URL;

/**
 * Created by baotrungtn on 9/27/17.
 */

public class SharedInfo {
    public static final String TAG = "SharedInfo";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String USER_TRADE_VOTE = "USER_TRADE_VOTE";
    public static final String USER_FACTOPINION_VOTE = "USER_FACTOPINION_VOTE";
    public static final String USER_UPDOWN_VOTE = "USER_UPDOWN_VOTE";
    public static final String HOLD_VOTE = "HOLD";
    public static final String BUY_VOTE = "BUY";
    public static final String SELL_VOTE = "SELL";
    public static final String FACT_VOTE = "FACT";
    public static final String UP_VOTE = "UP";
    public static final String DOWN_VOTE = "DOWN";
    public static final String OPINION_VOTE = "OPINION";
    public static final String NEUTRAL_VOTE = "NEUTRAL";
    public static final int BAD_RESULT = -1;

    private static SharedPreferences mSharedPrefs = null;
    private static String mUserToken = null;

    public static SharedPreferences getSharedPrefs() {
        return mSharedPrefs;
    }

    public static void setSharedPrefs(SharedPreferences mSharedPrefs) {
        SharedInfo.mSharedPrefs = mSharedPrefs;
    }

    public static void setUserFactopinionVoteRef(News news, String userVote) {
        mSharedPrefs.edit().putString(USER_FACTOPINION_VOTE + news.getId(), userVote).commit();
    }

    public static String getUserFactopinionVoteRef(News news, boolean canBeNull) {
        if (canBeNull == true) {
            return mSharedPrefs.getString(USER_FACTOPINION_VOTE + news.getId(), null);
        } else {
            return mSharedPrefs.getString(USER_FACTOPINION_VOTE + news.getId(), NEUTRAL_VOTE);
        }
    }

    public static void setUserTradeVoteRef(News news, String userVote) {
        mSharedPrefs.edit().putString(USER_TRADE_VOTE + news.getId(), userVote).commit();
    }

    public static String getUserTradeVoteRef(News news, boolean canBeNull) {
        if (canBeNull == true) {
            return mSharedPrefs.getString(USER_TRADE_VOTE + news.getId(), null);
        } else {
            return mSharedPrefs.getString(USER_TRADE_VOTE + news.getId(), NEUTRAL_VOTE);
        }
    }

    public static void setUserUpDownVoteRef(News news, String userVote) {
        mSharedPrefs.edit().putString(USER_UPDOWN_VOTE + news.getId(), userVote).commit();
    }

    public static String getUserUpDownVoteRef(News news, boolean canBeNull) {
        if (canBeNull == true) {
            return mSharedPrefs.getString(USER_UPDOWN_VOTE + news.getId(), null);
        } else {
            return mSharedPrefs.getString(USER_UPDOWN_VOTE + news.getId(), NEUTRAL_VOTE);
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

    public static String formatNumberStringWithSuffix(long count) {
        if (count < 1000 && count > -1000) return "" + count;

        if (count <= -1000) {
            count *= -1;
            int exp = (int) (Math.log(count) / Math.log(1000));
            return String.format("%.1f%c",
                    -1 * count / Math.pow(1000, exp),
                    "kMGTPE".charAt(exp-1));
        }

        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }

    public static String getDomainNameFromUrl(String urlStr) {
        String result = "";
        try{
            URL url = new URL(urlStr);
            result = url.getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void setupUserToken() {
        // Only run once
        if (SharedInfo.getUserToken() == null) {
            new UserTokenTask().execute();
        } else {
            Log.i(TAG, "retrieve user token: " + SharedInfo.getUserToken());
        }
    }


    private static class UserTokenTask extends AsyncTask<Void,Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return new NetworkRequester().getNewUserToken();
        }

        @Override
        protected void onPostExecute(String userToken) {
            super.onPostExecute(userToken);
            SharedInfo.setUserToken(userToken);
            Log.i(TAG, "GetNewUserTokenTask: get new user token: " + userToken);
        }
    }
}
