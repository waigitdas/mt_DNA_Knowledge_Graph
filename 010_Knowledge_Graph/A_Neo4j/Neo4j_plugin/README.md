# Ne4j Plugin
 
Neo4j functionality is expanded using java coded plugins. These folders contain the code used to poulation and analyze the knowledge graph. The jar file is produced in Maven and theninserted into the Neo4j plugin folder. The code uses two Neo4j plugin: APOCand Graph Data Sceience (GDS). The Neo4jconfiguration file may need modification to optimize the use of RAM. 

The database was created using Neo4j Enterprise v 5.26.1. The server connect is manage by code in the conn folder. It is presently configure to readan external file so multiple projects can be more easily managed. You will need to set the parameters in thes files to assure compatabilty with your environment. The code is designed to work within the Windows file structure. It would need modifications for Mac and Linux.
