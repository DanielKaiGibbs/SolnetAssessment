# Database Specifications

The table structure is provided below:

Table name - tasks

Table columns:

id int not null generated always as identity,
title varchar(256) not null,
description varchar(1024),
due_date date,
status varchar(10),
creation_date date not null,
primary key (id)

# API Specifications

### Important Development Assumptions:

- This API enforces JSON formatting for task data.
- The 'id' attribute is used when specifying tasks in the database as it is the primary key.
- This API separates reading('/get', '/getOverdue'), writing('/add', '/update') and deleting('/delete') logic into 
  separate Servlet classes. This is to allow future development to be better catered to each permissions set.
- Date parameters are required as Strings, formatted as 'YY-MM-DD'

### /get

Returns a JSON array of all the task data entries in the database.
```
localhost:8080/get
```
Example Output:
```json
[
  {
     "id" : 1,
     "title" : "Breakfast",
     "description" : "A nutritious meal",
     "status" : "complete",
     "due_date" : "2020-03-07",
     "creation_date" : "2020-01-01"
  }, 
  {
    "id" : 2, 
    "title" : "Dinner", 
    "description" : "Cook up something yum", 
    "status" : "to do", 
    "due_date" : "2020-03-07", 
    "creation_date" : "2020-01-01"
  }
]
```
Users may also specify the optional *'id'* parameter to specify an entry to return. If there is no task with that id then an empty JSON array will be returned.

```
localhost:8080/get?id=1
```
Example Output:
```json
[
  {
    "id" : 1,
    "title" : "Breakfast",
    "description" : "A nutritious meal",
    "status" : "complete",
    "due_date" : "2020-03-07",
    "creation_date" : "2020-01-01"
  } 
]
```
### /getOverdue

Returns a JSON array of all the task data entries in the database that have a 'due_date' earlier the current date.

```
localhost:8080/getOverdue
```
Example Output:
```json
[
  {
    "id" : 3,
    "title" : "Y2K",
    "description" : "Celebrate the new Millennium",
    "status" : "complete",
    "due_date" : "2000-01-01",
    "creation_date" : "1999-01-01"
  } 
]
```

### /update

Write changes to a specific task in the database. Requires the 'payload' parameter to define the new JSON object to 
write in place of the existing data (identified with the 'id' primary key). 

**The JSON payload must contain the required attributes:**
- id : int
- title : string
- description : string 
- status : string
- due_date : string (formatted 'YY-MM-DD')
- creation_date : string (formatted 'YY-MM-DD')

```
//Will replace the task with an id equal to 1 with this new paylaod data
localhost:8080/update?payload={"id" : 1, "title" : "NEW_TITLE", "description" : "NEW_DESCRIPTION", "status" : "NEW_STATUS", "due_date" : "NEW_DUE_DATE", "creation_date" : "NEW_CREATION_DATE"}
```

### /add

Creates a new task in the database using the provided data. Requires the 'payload' parameter to define the new JSON object to
create.
**The JSON payload must contain the required attributes:**
- title : string
- description : string
- status : string
- due_date : string (formatted 'YY-MM-DD')
- creation_date : string (formatted 'YY-MM-DD')

```
//Create a new task
localhost:8080/add?payload={"title" : "NEW_TITLE", "description" : "NEW_DESCRIPTION", "status" : "NEW_STATUS", "due_date" : "NEW_DUE_DATE", "creation_date" : "NEW_CREATION_DATE"}
```

### /delete

Creates a new task in the database using the provided data. Requires the 'id' parameter to specify the task to delete.
```
//Deletes the second
localhost:8080/delete?id=2
```

## Run the application Locally
This project uses Jetty as an embedded container to host the web application.  
Goto the base folder of the application and execute the following command to launch the application.  
`mvn jetty:run`

The application will be available at [http://localhost:8080](http://localhost:8080)

You can replace the jetty plugin with something you feel comfortable using as well but make sure we have clear instructions to run your application. 
