 ![Bildschirmfoto 2020-05-17 um 00 52 52](https://user-images.githubusercontent.com/45396540/82131756-e6417100-97d8-11ea-9aff-df5728be28d9.png)

# KNOW YOUR CITY
Take a look at the current running version of the application [here](https://sopra-fs20-group-19-client.herokuapp.com/map).

This is a SoPra Project from Students at the University of Zurich, realized in FS20.

## Introduction

KNOW YOUR CITY is a complement to ubiquitous map services as Google Maps. It should help people find places of everyday life: 

- Fountains
- Fireplaces
- Toilets
- Recycling Stations 
- Table Tennis Spots
- Beautiful Benches

Our target audience are people who want to spend a nice summer evening in Zurich with BBQ and a round of table tennis. But don't forget about the others: A normal resident could find a new recycling station here. Or what if a stressed businessman has to pee urgently while travelling? KNOW YOUR CITY will show him the nearest toilet for sure.

Furthermore, the application is a remote meeting place for people who want to get to know their city better. You can: 

- rate locations
- leave a comment about a location
- chat with your friends
- take a look at your friends favorite locations
- add a new location if it's missing in the database.

Have fun!

## Technologies

Server:
- SpringBoot: Used to build the REST service with Spring
- MongoDB: Database used to store all location and user data
- Gradle: Used to build the application
- Postman: Used to test the API calls
- SonarQube: Used to analyse code and test quality

Client:
- React: Used to design the User Interface with JavaScript
- Heroku: Used to build, run and deploy the application

## High-level components

- [Application](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/Application.java) hosts the main method of the project. To run the whole server, you have to run this class including the main method.

- [UserController](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/controller/UserController.java) and [LocationController](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/controller/LocationController.java) are crucial because they handle the REST requests from the client or Postman. They then delegate the execution to [UserService](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/service/UserService.java) and [LocationService](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/service/LocationService.java).

- [UserService](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/service/UserService.java) and [LocationService](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/service/LocationService.java) handle the functionality related to user and location respectively. They call the important Database Connector classes.

- These classes like [DatabaseConnectorUser](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/database/DatabaseConnectorUser.java) or [DatabaseConnectorLocation](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Server/blob/master/src/main/java/ch/uzh/ifi/seal/soprafs20/database/DatabaseConnectorLocation.java) access the online MongoDB database and make the necessary changes.

## Launch & Deployment

As a new developer, for your local development environment 

## Setup this Template with your IDE of choice

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)) and make sure Java 13 is installed on your system.

1. File -> Open... -> SoPra Server Template
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

## Building with Gradle

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

-   MAC OS X: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing

### Postman

-   We highly recommend to use [Postman](https://www.getpostman.com) in order to test your API Endpoints.

## Debugging

If something is not working and/or you don't know what is going on. We highly recommend that you use a debugger and step
through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command),
do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug"Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing

Have a look here: https://www.baeldung.com/spring-boot-testing

Every push to the master branch automatically redeploys the heroku application and thus is "going live". If you don't want that you have to push to another branch because every push to the master is automatically a new release.

## Roadmap 

As a new developer you could add the following functionalities: 

- Users could upload pictures of the locations for a better understanding of how this place looks.

- The database could be extended to bigger regions than just the city of Zurich.

- Users could filter for their favorite locations on the map.


## Authors and acknowledgement

KNOW YOUR CITY was built as a SoPra Project at the University of Zurich in FS20.

Idea and realization by group 19: Lena, Luca, Luis, Maximilian, Tim.

Contact: knowyourcity@gmx.ch

We thank the whole SoPra team for their tipps and tricks, especially our tutor Anja.

Thanks to grillstelle.ch for providing us the fireplace database.

Thanks to Stadt Zürich for the free and public data access of fountains, recycling stations and toilets databases.

Thanks to Lucas Pelloni for the template.

## License 

This project is licensed under the Apache 2.0 License - see the [License.md](LICENSE) file for details.

