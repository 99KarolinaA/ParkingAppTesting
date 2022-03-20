# ParkingAppTesting


The ***ParkingServiceTest*** and ***UserServiceTest*** tests test the **business logic** of the **Parking application** (the service implementation - *ParkingServiceImpl* and *UserServiceImpl*). 
These tests are not intended to check the full application functionality including inserting data into database.
For this purpose, calls to some of the methods that access the database are simulated using the **Mockito framework**.

Some of the tests test the **full functionality** of the application including the components for accessing the database 
(**integration tests**). H2 database is used as a database which is an empty in memory database that is filled with the help of DataHolder.
These are the following tests:

- ***ControllersTest*** - testing the presentation logic (Spring MVC). To perform HTTP requests, you need the Spring MVC Test framework, known as **MockMvc**. 
After performing them, the received HTTP status, the returned view and the presence of certain attributes of the model are reviewed.
- ***ParkingServiceIntegrationTest*** and ***UserServiceIntegrationTest*** - test the full functionality of the *ParkingServiceImpl* and *UserServiceImpl* service implementation


The ***LoginAndRegisterTestSelenium*** and ***ParkingTestSelenium*** tests test the **clicks** of the application, ie the human behavior on the interface. 
Clicking on the application and checking the accuracy of the results obtained is enabled through the **Selenium framework**.
