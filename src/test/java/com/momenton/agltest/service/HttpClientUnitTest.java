package com.momenton.agltest.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Test harness for {{@link com.momenton.agltest.service.HttpClient}}
 */
public class HttpClientUnitTest {

    @Mock
    private URL url;

    @Mock
    private HttpURLConnection httpConnection;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private HttpClient httpClient;

    @Before
    public void setup() {
        httpClient = new HttpClient();
    }

    @Test
    public void testSuccess() throws Exception {
        String sampleResponse = "test";
        InputStream stream = new ByteArrayInputStream(sampleResponse.getBytes(StandardCharsets.UTF_8));
        when(url.openConnection()).thenReturn(httpConnection);
        when(httpConnection.getResponseCode()).thenReturn(200);
        when(httpConnection.getInputStream()).thenReturn(stream);

        String response = httpClient.get(url);
        assertEquals(sampleResponse, response);
    }

    @Test
    public void testEmptyResponse() throws Exception {
        String sampleResponse = "";
        InputStream stream = new ByteArrayInputStream(sampleResponse.getBytes(StandardCharsets.UTF_8));
        when(url.openConnection()).thenReturn(httpConnection);
        when(httpConnection.getResponseCode()).thenReturn(200);
        when(httpConnection.getInputStream()).thenReturn(stream);

        String response = httpClient.get(url);
        assertEquals(response.length(), 0);
    }

    @Test
    public void testNullResponse() throws Exception {
        when(url.openConnection()).thenReturn(httpConnection);
        when(httpConnection.getResponseCode()).thenReturn(200);
        when(httpConnection.getInputStream()).thenReturn(null);

        String response = httpClient.get(url);
        assertNotNull(response);
        assertEquals(response.length(), 0);
    }

    @Test
    public void testHttpError() throws Exception {
        when(url.openConnection()).thenReturn(httpConnection);
        when(httpConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_CLIENT_TIMEOUT);

        String response = httpClient.get(url);
        assertNotNull(response);
        assertEquals(response.length(), 0);
        verify(httpConnection, never()).getInputStream();
    }

    @Test
    public void testHttpConnectionExpcetion() throws Exception {
        String message = "error message";
        when(url.openConnection()).thenThrow(new IOException(message));
        verify(httpConnection, never()).getInputStream();

        try {
            String response = httpClient.get(url);
            fail("Processing after exception");
        } catch(IOException e) {
            assertEquals(e.getMessage(), message);
        }
    }

}
