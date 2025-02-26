# Vite_React_Spring_Java_Template
A fullstack application with the following technologies:
- Frontend: Vite, ReactJS, Tailwindcss
- Backend: Maven, Springboot, Java, Lombook
- Database: MySQL
- Container: Docker


## Getting Started

**1.**  Make sure you have a fresh version of Node.js, NPM installed and Java.<br>
The current Long Term Support (LTS) release is an ideal starting point: https://nodejs.org/en<br>
You can download the java JDK here: https://www.oracle.com/java/technologies/downloads/

**2.**  Clone this repository to your computer:
```
git clone https://github.com/BillySwaggyKidy/Vite_React_Spring_Java_Template.git
```

**3.** From the project's client directory, go to the main/client folder and install the required packages (dependencies):
```
npm install
```

**4.** To start developping, you need first to go launch the spring boot server by either:<br>
* running the following command: `mvn spring-boot:run -Pdev`
* or if you are using visual studio code, using the Spring boot dashboard to start the server by cliking in the APPS section

This will start a spring boot server at the following address: http://localhost:8080/
<br>In dev mode, the spring app will run a H2 database, in order to access the h2 console, go to the following address: http://localhost:8080/h2-console
Then type the password inside the form in order to access the database.

**5.** Then open a new terminal and start the dev server for the front-end:<br>
Go to the src/main/client folder where the vite architecture is then start the following command:
```
cd src/main/client
npm run dev
```
It will start a local server at the following address: http://localhost:5173/

**6.** If you want to launch the app to production, first you need to have docker running then type the following command:
```
docker-compose up
./mvnw package
```
It will compile the code and launch to production

**7.** Finaly execute the Jar file by typing the following command:
```
java -jar target/template-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```


## Commands

**Java backend (root):**

You can find the following commands:
* `mvn spring-boot:run -Pdev`: start the spring boot server
* `./mvnw compile`: build the application with the frontend assets ready
* `./mvnw test`: runs the unit tests in your project
* `./mvnw package`: compiles the source code, runs the tests, and then packages the compiled code into a distributable format, such as a JAR or WAR file
* `./mvnw install`: performs everything that package does, and then installs the built package (e.g., JAR/WAR) into the local Maven repository

**Vite frontend (src/main/client):**

You can find the following commands:
* `npm run dev`: start the frontend server in development mode with HMR enabled
* `npm run build`: build the frontend assets
* `npm run preview`: start a local web server that serves the built solution from ./dist for previewing
* `npm run lint`: execute eslint to analyse the code for problems