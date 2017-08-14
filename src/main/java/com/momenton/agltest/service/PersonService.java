package com.momenton.agltest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momenton.agltest.model.Person;
import com.momenton.agltest.model.Pet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Client service that retrieves list of people from the remote REST service and performs the required processing.
 * It uses {{@link com.momenton.agltest.service.HttpClient}} to make HTTP GET call to the remote service.
 * It uses Jackson to convert JSON response to Java objects, then performs filtering, grouping and sorting using
 * Java streams.
 *
 */
public class PersonService {
    private static final Logger logger = Logger.getLogger(PersonService.class.getName());

    /**
     * In a Spring based application this field would be injectd e.g. through @Autowire
     */
    private HttpClient httpClient;
    private ObjectMapper mapper;

    private String url;


    public PersonService(HttpClient httpClient, String url) {
        this.httpClient = httpClient;
        this.url = url;
        mapper = new ObjectMapper();
    }


    /**
     * Returns alphabetically sorted list of pets against their owner gender.
     *
     * @param petType - e.g. "Cat", "Dog"
     * @return Map with owner gender as key and list of pets as value.
     */
    public Map<String, List<Pet>> getPets(String petType) {
        List<Person> people = getPeople();
        return groupAndSort(people, petType);
    }


    /**
     * Groups pets by owner gender then sorts by pet name alphabetically.
     *
     * @param people - List of Person records - pet owners
     * @param petType - Pet type for filtering
     * @return Map with gender as key and sorted list of pets as value
     */
    private Map<String, List<Pet>> groupAndSort(List<Person> people, String petType) {

        Map<String, List<Pet>> result = null;
        if (people != null && people.size() > 0) {
            result = people.stream()
                    .filter(p -> p.getPets() != null)
                    .collect(Collectors.groupingBy(Person::getGender,
                            Collectors.mapping(p -> p.getPets().stream()
                                    .filter(pet -> pet.getType().equals(petType))
                                    .collect(Collectors.toList()), Collectors.toList())))
                    .entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey(),
                            entry -> entry.getValue().stream().flatMap(List::stream)
                                    .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                                    .collect(Collectors.toList())));
        }
        return result;
    }

    /**
     * Retrieves list of people from the server as a JSON and this list to List<Person>.
     *
     * @return List<Person>
     */
    private List<Person> getPeople() {
        List<Person> people = null;
        try {
            String response = httpClient.get(new URL(url));
            if (response != null) {
                people = Arrays.asList(mapper.readValue(response, Person[].class));
            }
        } catch (Exception e) {
            // In a real application error handling would be done
            logger.log(Level.SEVERE, "Error in service call to " + url, e);
        }
        return people;
    }

    /**
     * Prints map of pets by owner gender into a String.
     *
     * @param map - Map with gender as key and list of pets as value
     * @return String containing printed list of pets
     */
    public String printMap(Map<String, List<Pet>> map) {
        StringBuffer result = new StringBuffer();
        if(map != null) {
            map.entrySet().stream().forEach(entry -> {
                result.append(entry.getKey()).append("\n");
                entry.getValue().stream().forEach(p -> result.append("   ").append(p.getName()).append("\n"));
            });
        }
        return result.toString();
    }

}
