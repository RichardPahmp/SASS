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
