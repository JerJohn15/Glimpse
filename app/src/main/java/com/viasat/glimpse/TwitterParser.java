package com.viasat.glimpse;

import org.json.JSONObject;

public class TwitterParser {

    public static String[] getLocation(JSONObject jObj) {
        return null;
    }

    public static void main(String[] args){
        TwitterGetter streamConsumer = new TwitterGetter(); // final because we will later pull the latest Tweet
        streamConsumer.start();
    }
}
