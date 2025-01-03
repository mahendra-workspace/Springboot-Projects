# Spring Boot-Projects
## Spring Boot REST API project
1. How to create
   -  Create a Maven Project
2. **JPA Repository**
   - JpaRepository interface provides several built-in methods
   - ```findById()```: Find an entity by its ID.
   - ```findAll()```: Fetch all records from the table.
   - ```save()```: Save a new entity or update an existing one.
     - When the ```save()``` method is called with an entity that already exists (i.e., the primary key is provided), it updates the existing record in the database.
   - ```deleteById()```: Delete an entity by its ID. 
