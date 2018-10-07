package com.stevesmedia.filestore.restapi.domainmodel;

import java.io.Serializable;
import java.util.Properties;

/**
 * Document from an archive {@link FileUploaderService}
 * @author steves
 */
public class FileDocument extends FileDocMetaData implements Serializable {

	private static final long serialVersionUID = 1L;
    
    private byte[] fileData;
    
    public FileDocument( byte[] fileData, String fileName, String timeStamp, String fileSize, String fileType) {
        super(fileName, timeStamp, fileSize, fileType);
        this.fileData = fileData;
    }

    public FileDocument(Properties properties) {
        super(properties);
    }
    
    public FileDocument(FileDocMetaData metadata) {
        super(metadata.getUuid(), metadata.getFileName(), metadata.getTimeStamp(), metadata.getFileSize(), metadata.getFileType());
    }

    public byte[] getFileData() {
        return fileData;
    }
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
    
    public FileDocMetaData getMetadata() {
        return new FileDocMetaData(getUuid(), getFileName(), getTimeStamp(), getFileSize(), getFileType());
    }
    
}
