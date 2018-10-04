# filestore-security-v3
Secured File upload and retrieval Springboot Rest API Service
===============================================================

A file system based archive with REST interfaces. An angularJS based web client is also included to test the service.(The security components for UI are currently work in progress. You can test the apis independenly).

Two users are currently loaded through the embedded H2 DB.

* username: admin pass: admin

* username: user pass: user


In order to access the service APIs you need to first make a request for JWT token from the authorization server with the request body format like {"username": "admin","password": "admin"}
* *POST* **https://localhost:8443/authorize**


The available Service APIs as follows.

* **Upload a file operation(max 2MB size):**
*POST https://localhost:8443/fileUploaderApi/uploadfile?file={filename.xxx}*

* **Retrive a file from filesystem by file id operation:**
* GET https://localhost:8443/fileUploaderApi/files/{uuid}*

* **Retrieve all files from store operation:**
* GET https://localhost:8443/fileUploaderApi/files*

* **Delete all files from filesystem operation (Requires 'admin' credentials):**
* DELETE https://localhost:8443/fileUploaderApi/files/deleteAll*

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


