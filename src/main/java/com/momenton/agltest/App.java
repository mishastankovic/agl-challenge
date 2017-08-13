package com.momenton.agltest;

import com.momenton.agltest.model.Pet;
import com.momenton.agltest.service.HttpClient;
import com.momenton.agltest.service.PersonService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by misha on 13/08/2017.
 */
public class App {
    private static final String PROP_FILE = "app.properties";
    private static final String PROPERTY_NAME_SERVICE_URL = "seerviceUrl";
    private static final String DEFAULT_PET_TYPE = "Cat";
    private String serviceUrl;


    public App() throws IOException {
        String filename = PROP_FILE;
        InputStream input = App.class.getClassLoader().getResourceAsStream(filename);
        Properties prop = new Properties();
        prop.load(input);
        serviceUrl = prop.getProperty(PROPERTY_NAME_SERVICE_URL);
    }

    /**
     *
     * @param petType
     */
    public void printPets(String petType) {
        PersonService personService = new PersonService(new HttpClient(), serviceUrl);
        Map<String, List<Pet>> pets = personService.getPets(petType);
        personService.printMap(pets);
    }


    public void main(String[] args) {
        try {
            App app = new App();
        } catch(Exception e) {
            System.out.println("Error initialising application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

