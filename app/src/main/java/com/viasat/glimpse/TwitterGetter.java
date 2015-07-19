package com.viasat.glimpse;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import static com.viasat.glimpse.R.string.*;

public class TwitterGetter extends Thread {

    private static final String STREAM_URI = "https://stream.twitter.com/1.1/statuses/filter.json";
    private TwitterMapActivity twitterMap;
    private Resources res;

    public TwitterGetter(TwitterMapActivity twitterMap) {
        this.twitterMap = twitterMap;
        res = twitterMap.getResources();
    }

    public void run() {
        try {
            System.out.println("Starting Twitter public stream consumer thread.");

            // Enter your consumer key and secret below
            OAuthService service = new ServiceBuilder()
                    .provider(TwitterApi.class)
                    .apiKey(res.getString(R.string.api_key))
                    .apiSecret(res.getString(R.string.api_secret))
                    .build();

            // Set your access token
            Token accessToken = new Token(
                    res.getString(R.string.token),
                    res.getString(R.string.token_secret)
            );

            // Let's generate the request
            System.out.println("Connecting to Twitter Public Stream");
            OAuthRequest request = new OAuthRequest(Verb.POST, STREAM_URI);
            request.addHeader("oauth_version", "1.0");
            request.setConnectionKeepAlive(true);
            request.addHeader("oauth_signature_method", "HMAC-SHA1");
            request.addHeader("oauth_signature", "gSYxqy86%2BJXGsjY0%2FIHL8SQ6JxA%3D");
            //request.addBodyParameter("locations", calcLocationRad(lat, lng));
            request.addBodyParameter("track", "the");
            service.signRequest(accessToken, request);
            Response response = request.send();

            System.out.println(response.isSuccessful());
            System.out.println(response.getCode() + ": " + response.getMessage());

            // Create a reader to read Twitter's stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));

            System.out.println("Starting output");
            String line;
            JSONObject jObj;
            String id = "NULL";
            int rt = -1;
            while ((line = reader.readLine()) != null) {
                try {
                    jObj = new JSONObject(line);
                    id = jObj.getString("id_str");
                    rt = jObj.getInt("retweet_count");
                } catch (JSONException e) {
                    System.out.println("ERROR: " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
                System.out.println("ID: " + id + " has been retweeted " + rt + " times.");
            }
            System.out.println("Finished output");

            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String calcLocationRad(double lat, double lng) {
        String boxCoords;

        float dist = 1.60934f;

        float latDelta = dist/110.54f;
        float longDelta = dist/(111.320f*(float)Math.cos((double)lng));

        float latA = (float)lat - latDelta;
        float longA = (float)lng - longDelta;
        float latB = (float)lat + latDelta;
        float longB = (float)lng + longDelta;


        boxCoords = String.valueOf(longA) + ",";
        boxCoords += String.valueOf(latA) + ',';
        boxCoords += String.valueOf(longB) + ',';
        boxCoords += String.valueOf(latB);

        return boxCoords;
    }
}