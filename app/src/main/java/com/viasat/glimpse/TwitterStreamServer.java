package com.viasat.glimpse;

public class TwitterStreamServer {
    public static void main(String[] args){
        TwitterStreamConsumer streamConsumer = new TwitterStreamConsumer(); // final because we will later pull the latest Tweet
        streamConsumer.start();
    }
}