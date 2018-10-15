# filestore-security-v3
Secured File upload and retrieval Springboot Rest API Service
===============================================================

A file system based archive with REST interfaces. An angularJS based web client is also included to test the service APIs.(The security components for UI are currently work in progress. You can test the apis independently).

Two users are currently loaded through an embedded H2 database.

* username: admin pass: admin

* username: user pass: user


In order to access the service APIs you need to first make a request for a JWT token from the authorization server with the request body format like {"username": "admin","password": "admin"}. The authorization server and resources server are configured through the same Springboot application. The expiration of token is currently set at 10 minutes. After a new token is generated, it will be maintained in a cache on the auth server for a specific user. This will be re-used next time if the same user tries to re-authorize within the token expiry period or from a different device.

* POST **https://localhost:8443/authorize**

Token revocation capability is also available. Admin authority required for this service.
 
* DELETE **https://localhost:8443/revokeToken/user/{username}** 

The following service APIs are available. While invoking an api please include the token obtained also throught a request header in the following format.
**Authorization : Bearer** &#60;token&#62;

* **Upload a file operation(max 2MB size):**
* POST https://localhost:8443/fileUploader/api/files?file={filename.xxx}

* **Retrive a file from filesystem by file id operation:**
* GET https://localhost:8443/fileUploader/api/files/{uuid}

* **Retrieve all files from store operation:**
* GET https://localhost:8443/fileUploader/api/files

* **Delete all files from filesystem operation (Requires 'admin' credentials):**
* DELETE https://localhost:8443/fileUploader/api/files/deleteAll

* **Swagger ui:**
* https://localhost:8443/swagger-ui.html

* **Swagger default:**
* https://localhost:8443/v2/api-docs

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
**filestore-security-v3/fileStore**

Each file will be stored within individual child folders with a metadata.properties file.

The file metadata displayed on the frontend if you click the **Show Files** button will come from this .properties file.


****Disclaimer: Application has been tested and verified on macOS, using Chrome and Firefox browsers.**


