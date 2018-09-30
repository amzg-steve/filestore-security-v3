package com.stevesmedia.filestore.restapi.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.stevesmedia.filestore.restapi.dao.FileUploaderServiceDao;
import com.stevesmedia.filestore.restapi.domainmodel.FileDocMetaData;
import com.stevesmedia.filestore.restapi.domainmodel.FileDocument;

/**
 * The service implementation to save, find and get documents through a filesystem based storage. 
 * 
 * @author steves
 */
@Service("fileUploaderService")
public class FileUploaderServiceImpl implements FileUploaderService, Serializable {

	private static final long serialVersionUID = 1L;
	
    @Autowired
    private  FileUploaderServiceDao docDao;
    
	@Override
	public FileDocMetaData save(FileDocument document) {
		docDao.insert(document); 
        return document.getMetadata();	}

	@Override
	public List<FileDocMetaData> findDocuments() {
        return docDao.findFiles();
	}

	@Override
	public byte[] getDocumentFile(String id) {
		FileDocument document = docDao.load(id);
        if(document!=null) {
            return document.getFileData();
        } else {
            return null;
        }
	}
	
	@Override
	public HttpEntity<String> deleteAll() {
		return docDao.deleteAllFiles();
	}

}