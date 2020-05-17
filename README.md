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

- [Map](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Client/blob/master/src/components/map/Map.js) controls the most important component of the application: the map. It handles the current position of the user and stores the chosen filters if the user wants to see just some specific location types.
- [MapService](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Client/blob/master/src/components/map/MapService.js) is called by Map and renders the map itself by accessing the Google Maps API and our MongoDB database to get coordinates and informations about our locations which it shows to the user with the help of Map Markers and Popups after clicking on such a Marker.
- [AppRouter](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Client/blob/master/src/components/shared/routers/AppRouter.js) controls which components are rendered for each specific URL path. Like that it handles a crucial part of the logic of the application. For example it calls [MapRouter](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Client/blob/master/src/components/shared/routers/MapRouter.js) if the map should be displayed. MapRouter then controls which components are rendered for each specific URL path in connection with the map.
- [App](https://github.com/SOPRAFS20Group19/SOPRA_FS20_Group19_Client/blob/master/src/App.js) is the main class of the file, rendering AppRouter. 

## Launch & Deployment

As a new developer, for your local development environment you'll need Node.js >= 8.10. You can download it [here](https://nodejs.org). All other dependencies including React get installed with:

### `npm install`

This has to be done before starting the application for the first time (only once).

### `npm run dev`

Runs the app in the development mode.<br>
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br>
You will also see any lint errors in the console. It is recommended to use Google Chrome.

### `npm run build`

Builds the app for production to the `build` folder.<br>
It correctly bundles React in production mode and optimizes the build for the best performance.
The build is minified and the filenames include the hashes.<br>

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

# SoPra RESTful Service Template FS20

## Getting started with Spring Boot

-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: http://spring.io/guides/tutorials/bookmarks/

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
