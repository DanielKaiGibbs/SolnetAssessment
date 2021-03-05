# Java Coding Challenge - Task Management API Suite

This is a maven project which uses an embedded derby database to store tasks in a table.

The table structure is provided below:

**Table name** - *tasks*

**Table columns:**
- *id* int not null generated always as identity,
- *title* varchar(256) not null,
- *description* varchar(1024),
- *due_date* date,
- *status* varchar(10),
- *creation_date* date not null,
- *primary key (id)*

Your challenge is to build a production grade API suite that uses HTTP to allow users to manage their task data. 

## Requirements

You will need to provide APIs for the following actions:  
 
1. Fetch all tasks.
1. Fetch all overdue tasks.
1. Fetch data for a single task.
1. Add a new task.
1. Modify a task.
1. Delete a task.


## Pre-requisites
1. Java needs to be installed on the system and environment variable JAVA_HOME should be set correctly to the JDK path.  
   Check by running below command in command prompt  
   `java -version`  
2. Maven needs to be installed on the system.  
   Check by running below command in command prompt  
   `mvn -v`  

## Run the application
This project uses Jetty as an embedded container to host the web application.  
Goto the base folder of the application and execute the following command to launch the application.  
`mvn jetty:run`  

The application will be available at [http://localhost:8080](http://localhost:8080)  
 
You can replace the jetty plugin with something you feel comfortable using as well but make sure we have clear instructions to run your application. 
