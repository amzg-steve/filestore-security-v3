# filestore-security-v3
Secured File upload and retrieval Spring Boot Rest based Service
=====================

A file system based archive with REST interfaces. An angularJS based web client is also included to test the service.

The available Restful APIs as follows:

* **Upload a file operation(max 2MB size):**
*https://localhost:8443/fileUploaderApi/uploadfile?file={filename.xxx} POST*

* **Retrive a file from filesystem by file id operation:**
*https://localhost:8443/fileUploaderApi/files/{uuid} GET*

* **Retrieve all files from store operation:**
*https://localhost:8443/fileUploaderApi/files GET*

* **Delete all files from filesystem operation:**
*https://localhost:8443/fileUploaderApi/files/deleteAll DELETE*

* **Swagger ui:**
*https://localhost:8443/swagger-ui.html*

* **Swagger default:**
*https://localhost:8443/v2/api-docs*

How to Build and run ?
-------------

```bash
git clone httpss://github.com/amzg-steve/File-Store-Project.git
#After extracting the files
cd File-Store-Project-master/spring-boot-angJs-fileuploader-rest
```
To start this Tomcat based application from pwd
```bash
./mvnw spring-boot:run
```
Or

```bash
mvn package
java -jar target/spring-boot-angJs-fileuploader-rest-0.0.1-SNAPSHOT.jar
```

How to invoke the client application ?
-------------
**https://localhost:8443/index.html**

Directory name where the uploaded files will be stored:
-------------
**spring-boot-angJs-fileuploader-rest/fileStore**

Each file will be stored within individual child folders with a metadata.properties file.

The file metadata displayed on the frontend if you click the **Show Files** button will come from this .properties file.


****Disclaimer: Application has been tested and verified on macOS, using Chrome and Firefox browsers.**


