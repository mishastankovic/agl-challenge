package com.momenton.agltest.service;

import java.net.MalformedURLException;
import java.net.URLConnection;

/**
 * This is a simple wrapper around java.net.URL that allows
 * URL to be mocked.
 * java.net.URL is a final class that cannot be mocked by Mockito.
 */
public class URL {

    private java.net.URL targetUrl;



    public URL(String url) throws MalformedURLException {

        targetUrl = new java.net.URL(url);
    }

    public URLConnection openConnection() throws java.io.IOException {
        return targetUrl.openConnection();
    }

    public String getPath() {
        return targetUrl.getPath();
    }



}
