package com.stevesmedia.filestore.restapi.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stevesmedia.filestore.restapi.domainmodel.FileDocMetaData;
import com.stevesmedia.filestore.restapi.domainmodel.FileDocument;
import com.stevesmedia.filestore.restapi.exceptions.ResourceNotFound;
import com.stevesmedia.filestore.restapi.service.FileUploaderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST web service for file uploading service.
 * All service calls are delegated to instances of {@link FileUploaderService}
 * 
 * /fileUploader/api/uploadfile?file={file}  				   
 * Uplolad file by POST
 * file: A file posted in a multipart request 
 * @author steves
 */
@RestController
@RequestMapping(value = "/fileUploader/api")
@Api(value = "document")
//@Log4j2
public class MainFileOperationsController {
	
	@Autowired
	FileUploaderService fileUploaderService;

	/**
	 * Adds a document to the file store.
	 * 
	 * Url: /fileUploader/api/files?file={filename} [POST]
	 * 
	 * @param file A file posted in a multipart request
	 * @return The meta data of the added document
	 * @throws Exception 
	 */
	@PostMapping(value = "/files")
	@ApiOperation(value = "Operation to upload new files to file repository")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file ) throws Exception {

		try {
			if(file == null || file.isEmpty()) {
				return ResponseEntity.badRequest().body("No valid file found in Request");
			}
			//get the local timestamp
			LocalDateTime timeStamp = LocalDateTime.now();
			//get the uploaded filesize
			String fileSize = FileUtils.byteCountToDisplaySize(file.getSize());
			String fileType = file.getContentType();
			FileDocument document = new FileDocument(file.getBytes(), file.getOriginalFilename(), timeStamp.toString(), fileSize, fileType);
			FileDocMetaData fileAdded = fileUploaderService.save(document);
			URI newFileUri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(fileAdded.getUuid())
					.toUri();

			return ResponseEntity.created(newFileUri).body(newFileUri.toString());

		} catch (Exception e) {
			throw new Exception("File Upload Failed. Please ensure file size < 2MB");
		}
	}

	/**
	 * Finds document in the archive.
	 * 
	 * Url: /fileUploader/api/files [GET]
	 * 
	 * @return A list of document meta data
	 */
	@GetMapping(value="/files",  produces= {"application/json"})
	@ApiOperation(value = "Operation to retrieve all files from file repo")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public HttpEntity<?> retrieveDocuments() {
		
		List<FileDocMetaData> docs = fileUploaderService.findDocuments();
		if (docs.isEmpty()) {
			throw new ResourceNotFound("No files found at repo", null);

		}
		return new ResponseEntity<List<FileDocMetaData>>(fileUploaderService.findDocuments(),
				new HttpHeaders(), HttpStatus.FOUND);
	}

	/**
	 * Returns the document file from the store with the given UUID.
	 * 
	 * Url: /fileUploader/api/files/{uuid} [GET]
	 * 
	 * @param id The UUID of a document
	 * @return The document file
	 */
	@GetMapping(value = "/files/{uuid}")
	@ApiOperation(value = "operation to retrieve file based on id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public HttpEntity<byte[]> getDocument(@PathVariable String uuid) {         

		HttpHeaders httpHeaders = new HttpHeaders();
		FileDocument fileDoc = fileUploaderService.getDocumentFile(uuid);
		if (fileDoc == null) {
			throw new ResourceNotFound(uuid +" file not found", null);
		}
		String typeStr = fileDoc.getFileType();
        httpHeaders.setContentType(MediaType.valueOf(typeStr));
		return new ResponseEntity<byte[]>(fileDoc.getFileData(), httpHeaders, HttpStatus.OK);
		
	}
	
	/**
	 * Deletes all files from repo
	 * 
	 * Url: /fileUploader/api/files/deleteAll [DELETE]
	 */
	@DeleteMapping(value="/files/deleteAll")
	@ApiOperation(value = "Operation to delete all files from repository")
    @PreAuthorize("hasRole('ADMIN')")
	public HttpEntity<String> deleteAllFiles() {
	
		return fileUploaderService.deleteAll();
	}

}
