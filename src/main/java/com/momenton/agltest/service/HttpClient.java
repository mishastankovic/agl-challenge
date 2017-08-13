package com.momenton.agltest.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


public class HttpClient {

    private final Logger logger = Logger.getLogger(HttpClient.class.getName());


    /**
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String get(URL url) throws Exception {

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
            for (String inputLine; (inputLine = in.readLine()) != null; ) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            logger.warning("Http request to url " + url.getPath() + " failed with response code " + responseCode);
            return null;
        }

    }
}
