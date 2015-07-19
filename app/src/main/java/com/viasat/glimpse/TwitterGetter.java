package com.viasat.glimpse;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import com.viasat.glimpse.TwitterMapActivity.*;

public class TwitterGetter extends Thread {

    private static final String STREAM_URI =
            "https://api.twitter.com/1.1/search/tweets.json?";
    private static final double myLat = 33.12743;
    private static final double myLong = -117.2654932;
    private TwitterMapActivity twitterMap;
    private Resources res;
    private Handler handler = new Handler();
    private static final String geoCode = "geocode=33.12743%2C-117.2654932%2C30mi";
    private static final String sinceID = "&since_id=";
    private static int lastIDNum = -1;
    private static String twitURL = null;

    public TwitterGetter(TwitterMapActivity twitterMap) {
        this.twitterMap = twitterMap;
        res = twitterMap.getResources();
    }

    public void run() {
        try {
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
            if(lastIDNum == -1) {
                 twitURL = STREAM_URI + geoCode;
            }
            else{
                 twitURL = STREAM_URI + geoCode + sinceID + lastIDNum;
            }
            OAuthRequest request = new OAuthRequest(Verb.GET, twitURL);
            request.setConnectionKeepAlive(true);
            service.signRequest(accessToken, request);
            Response response = request.send();

//            System.out.println(response.isSuccessful());
//            System.out.println(response.getCode() + ": " + response.getMessage());

            // Create a reader to read Twitter's stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));

            String line;
            JSONObject jObj = null;
            JSONObject jObj2 = null;
            JSONObject jObj3 = null;
            JSONObject geoObj = null;
            JSONArray jArr = null;
            String sn = null;
            String msg = null;
            LatLng coords = null;
            double lng = 0;
            double lat = 0;

            if((line = reader.readLine()) != null) {
                try {
                    System.out.println(line);
                    jObj = new JSONObject(line);
                    jArr = jObj.getJSONArray("statuses");
                } catch (JSONException e) {
                    System.out.println("ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if(jArr != null){
                for(int i = 0; i < jArr.length(); i++) {
                    try {
                        jObj2 = jArr.getJSONObject(i);
                        jObj3 = jObj2.getJSONObject("user");

                        msg = jObj2.getString("text");
                        sn = jObj3.getString("screen_name");

                        geoObj = jObj2.getJSONObject("geo");
                        lat = (geoObj.getJSONArray("coordinates")).getDouble(0);
                        lng = (geoObj.getJSONArray("coordinates")).getDouble(1);

                        System.out.println("LAT: " + lat);
                        System.out.println("LNG: " + lng);
                        coords = new LatLng(lat, lng);

                        twitterMap.addTweetToMap(sn, msg, coords, 120);
                    } catch (JSONException e) {
                        //e.printStackTrace();
                    }
                }
            }
            reader.close();
            //handler.postDelayed(this, 1500);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}