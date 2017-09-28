package com.trungbao.honestmockingbird.helper;

import android.net.Uri;
import android.util.Log;

import com.trungbao.honestmockingbird.SharedInfo;
import com.trungbao.honestmockingbird.model.News;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by baotrungtn on 9/27/17.
 */

public class NetworkRequester {
    private static final String TAG = "NetworkRequester";

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
}
