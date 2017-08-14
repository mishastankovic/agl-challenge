package com.momenton.agltest.service;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.logging.Logger;


public class HttpClient {

    private final Logger logger = Logger.getLogger(HttpClient.class.getName());
    private static final String GET_METHOD = "GET";
    private static final int READ_TIMEOUT = 120; // in seconds


    /**
     * A simple implementaiton of the HTTP GET request.
     *
     * @param url - remote service url
     * @return String containing server response
     * @throws Exception
     */
    public String get(URL url) throws Exception {

        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setReadTimeout(READ_TIMEOUT * 1000);
        con.setRequestMethod(GET_METHOD);

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {

            InputStream is = con.getInputStream();
            if(is != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                for (String inputLine; (inputLine = in.readLine()) != null; ) {
                    response.append(inputLine);
                }
                in.close();
            }
        } else {
            logger.warning("Http request to url " + url.getPath() + " failed with response code " + responseCode);
        }
        return response.toString();

    }
}
