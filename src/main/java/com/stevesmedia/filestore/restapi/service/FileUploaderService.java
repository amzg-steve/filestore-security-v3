package com.stevesmedia.filestore.restapi.service;

import java.util.List;

import com.stevesmedia.filestore.restapi.domainmodel.FileDocMetaData;
import com.stevesmedia.filestore.restapi.domainmodel.FileDocument;
import com.stevesmedia.filestore.restapi.domainmodel.Response;


/**
 * A service to save, find and get documents from an archive. 
 * @author steves
 */
public interface FileUploaderService {

	/**
	 * Saves a document in the archive.
	 * @param document A document
	 * @return DocumentMetadata The meta data of the saved document
	 */
	FileDocMetaData save(FileDocument document);

	/**
	 * Finds document in the archive matching the given parameter.
	 * @return A list of document meta data
	 */
	List<FileDocMetaData> findDocuments();


	/**
	 * @param id The id of a document
	 * @return A document file
	 */
	FileDocument getDocumentFile(String id);
	
	Response<String> deleteAll();
}