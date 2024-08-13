# **Project Forum**
Simple forum created using Spring Boot and Thymeleaf.
Example of simple CRUD web application. 

## Technologies used
- Spring Boot
- Spring Security
- Spring JPA & Hibernate
- Thymeleaf
- Bootstrap 5
- Sass (SCSS)
- Lombok
- Docker
- Docker Compose
- MySQL
- Node.js (gulp)



## Features demonstrated
### Spring Boot
- custom HandlerMethodArgumentResolver - PathTopicArgumentResolver and @PathTopic that looks for `idTopic` in the path and resolves it into Topic object that is injected into controller's methods
- custom data validation annotations - @UniqueEmail or @UniqueUsername
- resource handler with multiple paths - path `/avatars/**` (e.g. `/avatars/person.png`)
- uploading file & data using multipart form - section image
- saving file to storage

### Thymeleaf
- Thymeleaf templates - table template for more complex example of data binding
- Thymeleaf and Sass

### MySQL
- changing default collation to utf8mb4_unicode_ci - docker-compose.yml > db > command 



## Current user's functions
### For everyone:
- Creating a user account
- Logging to a user account using email and password
- Displaying sections, recent topics and recent posts at home page
- Displaying section and topics created in that section
- Displaying topic and posts created in that topic

### Authorized user (has role USER):
- Creating new topic in a section
- Creating new post in a topic
- ❌Editing & deleting topic
- ❌Editing & deleting post
- ❌Displaying of user profiles 
- ❌Displaying your own profile at
- ❌Editing your profile at 
- ❌Removal user profile with password confirmation

### Moderator (has role MOD): 
- Creating new sections
- Displaying list of all sections
- ❌Displaying list of all users



## Configuration
Example application properties are in the `application.example.properties` file.
Before build Forum or run tests you have to create `application.properties` files.



## Development Setup

1. run mysql database - you can use docker compose with file `docker-compose.yml`
1. in directory `scr/main/resources` copy file `application.example.properties` to `application.properties` - change settings if you need
1. run `npm i `
1. run `npm run build`
1. run spring application with your IDE or maven
1. run `npm run watch`
1. application should be running on `localhost:3000`