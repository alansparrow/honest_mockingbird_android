package com.trungbao.honestmockingbird.helper;

import android.net.Uri;
import android.util.Log;

import com.trungbao.honestmockingbird.SharedInfo;
import com.trungbao.honestmockingbird.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by baotrungtn on 9/27/17.
 */

public class NetworkRequester {
    private static final String TAG = "NetworkRequester";

    public void voteFact(News news) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/vote/fact")
                    .buildUpon()
                    .build().toString();
            String data = "user_token=" + SharedInfo.getUserToken() + "&news_id=" + news.getId();

            String jsonString = getUrlString(url, "POST", data);
            Log.i(TAG, "Result: " + jsonString);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public void voteOpinion(News news) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/vote/opinion")
                    .buildUpon()
                    .build().toString();
            String data = "user_token=" + SharedInfo.getUserToken() + "&news_id=" + news.getId();

            String jsonString = getUrlString(url, "POST", data);
            Log.i(TAG, "Result: " + jsonString);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public void voteHold(News news) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/vote/hold")
                    .buildUpon()
                    .build().toString();
            String data = "user_token=" + SharedInfo.getUserToken() + "&news_id=" + news.getId();

            String jsonString = getUrlString(url, "POST", data);
            Log.i(TAG, "Result: " + jsonString);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public void voteBuy(News news) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/vote/buy")
                    .buildUpon()
                    .build().toString();
            String data = "user_token=" + SharedInfo.getUserToken() + "&news_id=" + news.getId();

            String jsonString = getUrlString(url, "POST", data);
            Log.i(TAG, "Result: " + jsonString);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public void voteSell(News news) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/vote/sell")
                    .buildUpon()
                    .build().toString();
            String data = "user_token=" + SharedInfo.getUserToken() + "&news_id=" + news.getId();

            String jsonString = getUrlString(url, "POST", data);
            Log.i(TAG, "Result: " + jsonString);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public void voteUp(News news) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/vote/up")
                    .buildUpon()
                    .build().toString();
            String data = "user_token=" + SharedInfo.getUserToken() + "&news_id=" + news.getId();

            String jsonString = getUrlString(url, "POST", data);
            Log.i(TAG, "Result: " + jsonString);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public void voteDown(News news) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/vote/down")
                    .buildUpon()
                    .build().toString();
            String data = "user_token=" + SharedInfo.getUserToken() + "&news_id=" + news.getId();

            String jsonString = getUrlString(url, "POST", data);
            Log.i(TAG, "Result: " + jsonString);
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public String getNewUserToken() {
        String result = null;

        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/user/create")
                    .buildUpon()
                    .build().toString();
            String jsonString = getUrlString(url, "POST", null);
            JSONObject jsonBody = new JSONObject(jsonString);
            JSONObject newUser = jsonBody.getJSONObject("new_user");
            if (newUser != null) {
                result = newUser.getString("token");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch items", e);
        }

        return result;
    }

    private byte[] getUrlBytes(String urlSpec, String requestMethod, String data) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        connection.setRequestMethod(requestMethod);
        if (data != null) {
            //set the sending type and receiving type to json
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/x-www-form-urlencoded");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();

        }


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

    private String getUrlString(String urlSpec, String requestMethod, String data) throws IOException {
        return new String(getUrlBytes(urlSpec, requestMethod, data));
    }

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

    public void fetchNewNewsItems(List<News> newsList, String beforeDateStr, int newsCount) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/get/new")
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

    public void fetchHotNewsItems(List<News> newsList, double score, int newsCount) {
        try {
            String url = Uri.parse("http://192.241.147.78:8000/news/get/hot")
                    .buildUpon()
                    .appendQueryParameter("score", String.valueOf(score))
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
            if (isInList(items, news))
                continue;

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
            news.setUpVoteCount(itemJsonObject.getInt("up_vote_count"));
            news.setDownVoteCount(itemJsonObject.getInt("down_vote_count"));
            news.setUpDownVote(SharedInfo.getUserUpDownVoteRef(news, true));
            news.setScore(itemJsonObject.getDouble("score"));

            items.add(news);

        }
    }

    private boolean isInList(List<News> newsList, News news) {
        for (News n : newsList) {
            if (news.getId().equals(n.getId()))
                return true;
        }

        return false;
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
