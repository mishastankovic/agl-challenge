package com.momenton.agltest.service;

import com.momenton.agltest.model.Pet;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Integration tests for PersonService are executed against the real web service.
 * For PersonService unit tests refer to {{@link com.momenton.agltest.service.PersonServiceUnitTest}}
 */
public class PersonServiceIntegrationTest {

    private static final String SERVICE_URL = "http://agl-developer-test.azurewebsites.net/people.json";

    private PersonService service;


    @Before
    public void setup() throws Exception {
        service = new PersonService(new HttpClient(), SERVICE_URL);
    }

    @Test
    public void testGetPets_Success() throws Exception {
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

    @Test
    public void testGetPets_InvalidUrl() throws Exception {
        service = new PersonService(new HttpClient(), "http://invalid.host/invalidpath.json");
        Map<String, List<Pet>> pets = service.getPets("Cat");
        assertNull(pets);
    }

    private void checkListOrder(List<Pet> pets) {
        for(int i = 0; i < pets.size() - 1; i++) {
            assertTrue(pets.get(i).getName().compareTo(pets.get(i+1).getName()) < 0);
        }
    }

}
