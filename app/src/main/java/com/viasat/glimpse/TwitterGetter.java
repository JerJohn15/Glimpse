package com.viasat.glimpse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public abstract class TwitterGetter extends Thread {
    private static String consumerKey = "CDM7gJDH0fovtrlUYCJ6YjtGn";
    private static String consumerSecret = "jKhDc4IpqhKCbDdmXIpnb20YnZN0gy5n2oj7tPafP0jW0V7iTp";
    private static String token = "3283700119-9Y8zzYK7wCr6dXWLyHNKvRSLISsbJCs058gJwgB";
    private static String tokenSecret = "V41JnB6a6tbQBCGFI9DMjK9aVfqNXMeYIPhCdwEslXKEC";


}
