package com.momenton.agltest.service;

import com.momenton.agltest.model.Pet;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PersonServiceUnitTest {

    private static final String SERVICE_URL = "http://some.host/somefile.json";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private HttpClient httpClient;

    private PersonService service;

    @Before
    public void setup() {
        service  = new PersonService(httpClient, SERVICE_URL);
    }

    @Test
    public void testSuccess() throws Exception {

        // Prepare test context
        String response = this.getTestResponse("people.json");
        when(httpClient.get(any(URL.class))).thenReturn(response);

        Map<String, List<Pet>> pets = service.getPets("Cat");

        // Check result
        List<Pet> maleOwnerPets = pets.get("Male");
        List<Pet> femaleOwnerPets = pets.get("Female");
        assertEquals(maleOwnerPets.size(), 4);
        assertEquals(femaleOwnerPets.size(), 3);
        checkListOrder(maleOwnerPets);
        checkListOrder(femaleOwnerPets);
    }



    @Test
    public void testNoCats() throws Exception {

        // Prepare test context
        String response = "[{\"name\": \"Bob\", \"gender\": \"Male\", \"age\": 23, \"pets\": [{ \"name\": \"Fido\", \"type\": \"Dog\"}]}]";
        when(httpClient.get(any(URL.class))).thenReturn(response);

        Map<String, List<Pet>> pets = service.getPets("Cat");

        // Check result
        assertNotNull(pets);
        assertEquals(pets.size(), 1);
        List<Pet> maleOwnerPets = pets.get("Male");
        List<Pet> femaleOwnerPets = pets.get("Female");
        assertEquals(maleOwnerPets.size(), 0);
        assertNull(femaleOwnerPets);
    }


    @Test
    public void testEmptyResponse() throws Exception {

        // Prepare test context
        String response = "[]";
        when(httpClient.get(any(URL.class))).thenReturn(response);

        Map<String, List<Pet>> pets = service.getPets("Cat");

        // Check result
        assertNull(pets);
    }

    @Test
    public void testNullResponse() throws Exception {
        // Prepare test context
        when(httpClient.get(any(URL.class))).thenReturn(null);

        Map<String, List<Pet>> pets = service.getPets("Cat");

        // Check result
        assertNull(pets);
    }

    @Test
    public void testException() throws Exception {
        // Prepare test context
        when(httpClient.get(any(URL.class))).thenThrow(new IOException());

        Map<String, List<Pet>> pets = service.getPets("Cat");

        // Check result
        assertNull(pets);
    }

    @Test
    public void testInvalidUrl() throws Exception {
        // Prepare test context
        when(httpClient.get(any(URL.class))).thenThrow(new MalformedURLException());

        Map<String, List<Pet>> pets = service.getPets("Cat");

        // Check result
        assertNull(pets);
    }


    private void checkListOrder(List<Pet> pets) {
        for(int i = 0; i < pets.size() - 1; i++) {
            assertTrue(pets.get(i).getName().compareTo(pets.get(i+1).getName()) < 0);
        }

    }

    private String getTestResponse(String fileName) throws Exception {
        File jsonFile = new File(this.getClass().getClassLoader().getResource(fileName).toURI());
        return FileUtils.readFileToString(jsonFile, "UTF-8");
    }


}
