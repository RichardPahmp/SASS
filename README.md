# Scientific Article Snowballing System (SASS)

## How to run
The program can be run either by using the provided run.bat or from the command line using Maven:
```
mvn compile javafx:run
```

Credentials for connecting to the Neo4j database can be configured in properties.txt

A Cypher script for populating a new Neo4j database with example articles can be found in the file populate-database.Cypher

### Requirements
Java 11 or later is required to build and run the project.

Maven is required to build the project and is included in the apache-maven-3.6.3 folder.


# Using Scientific Article Snowballing System (SASS)

SASS does not distinguish between different roles of a user at the moment.

A user can add an article by clicking on the new article window and add references to it.
A user can add or remove an article.


![](https://drive.google.com/file/d/16L0_AFUY9UUSNBJVjaY0_MP6nW0odIcn/view?usp=sharing)


A user have three option for filtering results,

* Incoming references 
* Outgoing references
* Shared references

Incoming and outgoing references can show connected articles in steps from the chosen article. At the moment five steps only. One article can simulateneously be one or several steps away from the chosen article which means that an article can be seen in several steps
