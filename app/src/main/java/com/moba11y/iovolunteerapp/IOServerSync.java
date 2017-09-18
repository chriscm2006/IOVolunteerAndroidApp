package com.moba11y.iovolunteerapp;

import android.os.AsyncTask;
import android.util.Log;

import com.chriscm.clog.CLog;
import com.moba11y.ioserver.GsonSerializable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by chrismcmeeking on 9/18/17.
 */

public class IOServerSync {

    private static ArrayList<AsyncTask> asyncTasks = new ArrayList<>();

    //TODO: THis is how you get your emulator to connect to localhost
    private static final String IOSERVER_URL = "http://10.0.2.2:8080/";

    public static void post(final GsonSerializable value) {

        final AsyncTask task = new AsyncTask<Object, Object, Object>() {

            @Override
            protected String doInBackground(Object... strings) {

                final String url = IOSERVER_URL + value.getClass().getSimpleName().toLowerCase();
                final String body = value.toJson();

                CLog.e("Request: " + url + " " + body);

                HttpPost post = new HttpPost(url);

                try {
                    post.setEntity(new ByteArrayEntity(body.getBytes("UTF8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                post.setHeader("content-type", "application/json; charset=UTF-8");

                HttpClient client = new DefaultHttpClient();

                //TODO: Do things when it fails/succeeds. Best done through a delegate object of some kind cuz this is async.
                try {
                    CLog.e("Attempgint http request");
                    HttpResponse response = client.execute(post);
                    CLog.e(response.getStatusLine().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return "";
            }
        };

        asyncTasks.add(task);

        task.execute("");
    }
}
