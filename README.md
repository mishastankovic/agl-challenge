AGL Programming Challenge
========================================
- - -

This project contains an implementation of the AGL Programming Challenge as described [here](http://agl-developer-test.azurewebsites.net/).



## Setting up the project

* Install JDK 8
* Install Gradle version 3.0 or above
* Checkout the project from GitHub
    * ```` git clone <repo url> ````
* Build the project
    * ````gradle clean build````
* Run tests
    * ```` gradle test ````
* Run application
    * ```` gradle run ````
    * To run with pet type argument: ``gradle run -PappArgs="['Dog']"``


##Implementation overview


### Libraries
This implementation uses Java 1.8 and some third party libraries as follows:
* Java 8 streams for filtering, sorting, grouping and mapping
* JDK HttpUrlConnection for HTTP GET request
* Jackson for JSON to Java object mapping
* JUnit and Mockito for unit testing
* JDK Logger for logging

In a production ready implementation I would use a framework like Spring as it provides dependency injection, Rest Templates, built JSON to Java mapping etc.


###Code overview
This implementation consists of the following modules:
* service
    * App.java - contains main class. It reads application parameters from a property file and creas the service.
    * PersonService.java - Calls REST service using HttpClient and performs filterting, grouping, sorting and printing.
    * HttpClient - A basic HTTP GET implementation using HttpUrlConnection.
* model
    * Person
    * Pet
* unit tests
    * HttpClientUnitTest
    * PersonServiceUnitTest
* integration tests
    * HttpClientIntegrationTest
    * PersonServiceIntegrationTest

