package com.viasat.glimpse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class TwitterStreamConsumer extends Thread {

    private static final String STREAM_URI = "https://stream.twitter.com/1.1/statuses/filter.json";

    public void run() {
        try {
            System.out.println("Starting Twitter public stream consumer thread.");

            // Enter your consumer key and secret below
            OAuthService service = new ServiceBuilder()
                    .provider(TwitterApi.class)
                    .apiKey("CDM7gJDH0fovtrlUYCJ6YjtGn")
                    .apiSecret("jKhDc4IpqhKCbDdmXIpnb20YnZN0gy5n2oj7tPafP0jW0V7iTp")
                    .build();

            // Set your access token
            Token accessToken = new Token(
                    "3283700119-9Y8zzYK7wCr6dXWLyHNKvRSLISsbJCs058gJwgB",
                    "V41JnB6a6tbQBCGFI9DMjK9aVfqNXMeYIPhCdwEslXKEC"
            );

            // Let's generate the request
            System.out.println("Connecting to Twitter Public Stream");
            OAuthRequest request = new OAuthRequest(Verb.POST, STREAM_URI);
            request.addHeader("oauth_version", "1.0");
            request.addHeader("oauth_timestamp", "1437286037");
            request.setConnectionKeepAlive(true);
            request.addHeader("oauth_signature_method", "HMAC-SHA1");
            request.addHeader("oauth_signature", "gSYxqy86%2BJXGsjY0%2FIHL8SQ6JxA%3D");
            request.addBodyParameter("track", "ViaSatInterns");
            service.signRequest(accessToken, request);
            Response response = request.send();

            System.out.println(response.isSuccessful());
            System.out.println(response.getMessage());
            System.out.println(response.getCode());

            // Create a reader to read Twitter's stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));

            System.out.println("Starting output");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("Finished output");

            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}