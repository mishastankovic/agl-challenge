package com.momenton.agltest;

import com.momenton.agltest.model.Pet;
import com.momenton.agltest.service.HttpClient;
import com.momenton.agltest.service.PersonService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PersonServiceIntegrationTest {

    private static final String SERVICE_URL = "http://agl-developer-test.azurewebsites.net/people.json";

    private PersonService service;


    @Before
    public void setup() throws Exception {
        service = new PersonService(new HttpClient(), SERVICE_URL);
    }

    @Test
    public void testGetPets() throws Exception {
        Map<String, List<Pet>> pets = service.getPets("Cat");
        assertNotNull(pets);
        assertTrue(pets.size() == 2);
        List<Pet> maleOwnerPets = pets.get("Male");
        List<Pet> femaleOwnerPets = pets.get("Female");
        System.out.println(service.printMap(pets));
        assertEquals(maleOwnerPets.size(), 4);
        assertEquals(femaleOwnerPets.size(), 3);
        checkListOrder(maleOwnerPets);
        checkListOrder(femaleOwnerPets);
    }

    private void checkListOrder(List<Pet> pets) {
        for(int i = 0; i < pets.size() - 1; i++) {
            assertTrue(pets.get(i).getName().compareTo(pets.get(i+1).getName()) < 0);
        }

    }

}
