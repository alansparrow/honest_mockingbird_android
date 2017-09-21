package com.trungbao.honestmockingbird;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.trungbao.honestmockingbird.model.News;
import com.trungbao.honestmockingbird.model.NewsLab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by baotrungtn on 9/19/17.
 */

public class DataRequestThread extends Thread {
    private static final String TAG = "DataRequestThread";
    Context mContext;
    Date mPubDate;

    public DataRequestThread(Context context, Date pubDate) {
        mContext = context;
        mPubDate = pubDate;
    }

    public void run() {
        fetchItems();
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
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

    private void fetchItems() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String pubDateStr = df.format(mPubDate);

            String url = Uri.parse("http://192.241.147.78:8000/news/get")
                    .buildUpon()
                    .appendQueryParameter("pub_date", pubDateStr)
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(NewsLab.get(mContext).getNewsList(), jsonBody);
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

//            Log.i(TAG, news.toString());
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
