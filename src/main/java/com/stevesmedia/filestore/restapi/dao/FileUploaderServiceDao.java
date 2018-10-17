package com.stevesmedia.filestore.restapi.dao;

import java.util.List;

import org.springframework.http.HttpEntity;

import com.stevesmedia.filestore.restapi.domainmodel.FileDocMetaData;
import com.stevesmedia.filestore.restapi.domainmodel.FileDocument;


/**
 * Data access object to insert, find and load {@link Document}s
 * @author steves
 */
public interface FileUploaderServiceDao {

	/**
	 * Inserts a document in the data store.
	 * 
	 * @param document A Document
	 */
	void insert(FileDocument document);

	/**
	 * @return A list of document meta data
	 */
	List<FileDocMetaData> findFiles();

	/**
	 * @param uuid The id of the document
	 * @return A document incl. file and meta data
	 */
	FileDocument load(String uuid);
	
	Boolean deleteAllFiles();

}

