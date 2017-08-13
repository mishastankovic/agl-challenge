package com.momenton.agltest;

import com.momenton.agltest.service.HttpClient;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test harness for com.momenton.agltest.HttpClient
 */
public class HttpClientIntegrationTest {

    public static final Logger logger = Logger.getLogger(HttpClientIntegrationTest.class.getName());

    private static final String VALID_URL = "http://agl-developer-test.azurewebsites.net/people.json";
    private static final String INVALID_URL = "http://some.invalid.url";

    private HttpClient httpClient;

    @Before
    public void setup() {
        httpClient = new HttpClient();
    }

    @Test
    public void testCallSuccess() throws Exception {
        String response = httpClient.get(new URL(VALID_URL));
        assertNotNull(response);
        assertTrue(response.length() > 0);
    }


    @Test
    public void testInvalidUrl() throws Exception {
        try {
            httpClient.get( new URL(INVALID_URL));
            fail("Expected exception for invalid url");
        } catch( UnknownHostException e) {
        }
    }

}
