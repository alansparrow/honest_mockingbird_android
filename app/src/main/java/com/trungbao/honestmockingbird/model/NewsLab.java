package com.trungbao.honestmockingbird.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.trungbao.honestmockingbird.SharedInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by baotrungtn on 9/17/17.
 */

public class NewsLab {
    private static final String TAG = "NewsLab";

    private static NewsLab sNewsLab;
    private Context mContext;

    private List<News> mNewsList;

    public static NewsLab get(Context context) {
        if (sNewsLab == null) {
            sNewsLab = new NewsLab(context);
        }
        return sNewsLab;
    }

    public static void refresh() {
        sNewsLab = null;
    }

    private NewsLab(Context context) {
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

    public void fetchNews(String beforeDateStr, int newsCount) {
        DataRequester myDataRequester = new DataRequester();
        myDataRequester.fetchItems(mNewsList, beforeDateStr, newsCount);
    }

    private class DataRequester {
        private byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                connection.connect();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
                }

                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                return out.toByteArray();
            } finally {
                connection.disconnect();
            }
        }

        private String getUrlString(String urlSpec) throws IOException {
            return new String(getUrlBytes(urlSpec));
        }

        public void fetchItems(List<News> newsList, String beforeDateStr, int newsCount) {
            try {
                String url = Uri.parse("http://192.241.147.78:8000/news/get")
                        .buildUpon()
                        .appendQueryParameter("pub_date", beforeDateStr)
                        .appendQueryParameter("news_count", String.valueOf(newsCount))
                        .build().toString();
                String jsonString = getUrlString(url);
                JSONObject jsonBody = new JSONObject(jsonString);
                parseItems(newsList, jsonBody);
//            Log.i(TAG, "Received JSON: " + jsonString);
            } catch (Exception e) {
                Log.e(TAG, "Failed to fetch items", e);
            }
        }

        private void parseItems(List<News> items, JSONObject jsonBody) throws IOException, JSONException {
            JSONArray itemJsonArray = jsonBody.getJSONArray("items");

            for (int i = 0; i < itemJsonArray.length(); i++) {
                JSONObject itemJsonObject = itemJsonArray.getJSONObject(i);

                News news = new News();
                news.setId(itemJsonObject.getString("id"));
                news.setTitle(itemJsonObject.getString("title"));
                news.setPubSource(itemJsonObject.getString("pub_source"));
                news.setUrl(itemJsonObject.getString("url"));
                news.setFingerprint(itemJsonObject.getString("fingerprint"));
                news.setBuyVoteCount(itemJsonObject.getInt("buy_vote_count"));
                news.setSellVoteCount(itemJsonObject.getInt("sell_vote_count"));
                news.setHoldVoteCount(itemJsonObject.getInt("hold_vote_count"));
                news.setPubDate(parseToLocalTime(itemJsonObject.getString("pub_date")));
                news.setTradeVote(SharedInfo.getUserTradeVoteRef(news, true));
                news.setFactVoteCount(itemJsonObject.getInt("fact_vote_count"));
                news.setOpinionVoteCount(itemJsonObject.getInt("opinion_vote_count"));
                news.setFactOpinionVote(SharedInfo.getUserFactopinionVoteRef(news, true));

                items.add(news);
            }
        }

        private Date parseToLocalTime(String str) {
            Date pubTime = new Date();
            String pubTimeStr = str.substring(0, 19);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                pubTime = simpleDateFormat.parse(pubTimeStr);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            return pubTime;
        }
    }


}
