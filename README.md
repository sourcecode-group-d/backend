# backend


## Starter Guide:
To run the server application,
1. clone down this repo.

2. 'cd' into the cloned repository in your terminal

3. Make sure you have modified the applicaton.properties file with your own environment variables:

 spring.datasource.url=${DATABASE_URL}
 spring.datasource.username=${DATABASE_USERNAME}
 spring.datasource.password=${DATABASE_PASSWORD}
 spring.jpa.hibernate.ddl-auto=update
 spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
 server.port=8080
 
4. Run the command:

 ./gradlew bootrun
