package com.momenton.agltest;

import com.momenton.agltest.model.Pet;
import com.momenton.agltest.service.HttpClient;
import com.momenton.agltest.service.PersonService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by misha on 13/08/2017.
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final String PROP_FILE = "app.properties";
    private static final String PROPERTY_NAME_SERVICE_URL = "serviceUrl";
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
        System.out.println("List of pets by owner gender:\n" + personService.printMap(pets));
    }


    public static void main(String[] args) {
        String petType = args != null && args.length > 0 ? args[0] : DEFAULT_PET_TYPE;
        try {
            App app = new App();
            app.printPets(petType);
        } catch(Exception e) {
            System.out.println("Error initialising application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

