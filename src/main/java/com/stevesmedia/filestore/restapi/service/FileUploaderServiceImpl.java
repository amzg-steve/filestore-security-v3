package com.stevesmedia.filestore.restapi.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stevesmedia.filestore.restapi.dao.FileUploaderServiceDao;
import com.stevesmedia.filestore.restapi.domainmodel.FileDocMetaData;
import com.stevesmedia.filestore.restapi.domainmodel.FileDocument;
import com.stevesmedia.filestore.restapi.domainmodel.Response;

/**
 * The service implementation to save, find and get documents through a filesystem based storage. 
 * 
 * @author steves
 */
@Service("fileUploaderService")
public class FileUploaderServiceImpl implements FileUploaderService, Serializable {

	private static final long serialVersionUID = 1L;
	
    private  FileUploaderServiceDao docDao;
    
    @Autowired
	public FileUploaderServiceImpl(FileUploaderServiceDao docDao) {
		this.docDao = docDao;
	}

	@Override
	public FileDocMetaData save(FileDocument document) {
		docDao.insert(document); 
        return document.getMetadata();
    }

	@Override
	public List<FileDocMetaData> findDocuments() {
        return docDao.findFiles();
	}

	@Override
	public FileDocument getDocumentFile(String id) {
		FileDocument document = docDao.load(id);
        if(document!=null) {
            return document;
        } else {
            return null;
        }
	}
	
	@Override
	public Response<String> deleteAll() {
		
		boolean deletionStatus =  false;
		deletionStatus = docDao.deleteAllFiles();
		if (!deletionStatus) {
			throw new RuntimeException("File deletion action failed", null);
		}
		return new Response<String>(Collections.singletonList("All files deleted"), true, new Date(), HttpStatus.OK);
	}

}