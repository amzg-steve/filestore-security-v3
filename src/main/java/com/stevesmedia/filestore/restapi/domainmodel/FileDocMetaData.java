package com.stevesmedia.filestore.restapi.domainmodel;

import java.io.Serializable;
import java.util.Properties;

import com.datastax.driver.core.utils.UUIDs;
import com.stevesmedia.filestore.restapi.service.FileUploaderService;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * Meta data of a document from an archive managed by {@link FileUploaderService}.
 * @author steves
 */
@Data
@Log4j2
public class FileDocMetaData implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String PROP_UUID = "id";
	private static final String PROP_FILE_NAME = "file-name";
	private static final String PROP_FILE_SIZE = "file-size";
	private static final String PROP_DOCUMENT_DATE = "file-timestamp";
	private static final String PROP_FILE_TYPE = "file-type";

	private  String uuid;
	private  String fileName;
	private  String timeStamp;
	private  String fileSize;
	private  String fileType;

	public FileDocMetaData() {
		super();
	}

	public FileDocMetaData(String fileName, String timeStamp, String fileSize, String fileType) {
		this(UUIDs.timeBased().toString(), fileName, timeStamp, fileSize, fileType);
	}

	public FileDocMetaData(String uuid, String fileName, String timeStamp, String fileSize, String fileType) {
		super();
		this.uuid = uuid;
		this.fileName = fileName;
		this.timeStamp = timeStamp;
		this.fileSize = fileSize;
		this.fileType = fileType;
	}

	public FileDocMetaData(Properties properties) {
		this(properties.getProperty(PROP_UUID),
				properties.getProperty(PROP_FILE_NAME),
				properties.getProperty(PROP_DOCUMENT_DATE),
				properties.getProperty(PROP_FILE_SIZE),
				properties.getProperty(PROP_FILE_TYPE));
	}

	public Properties createProperties() {
		Properties props = new Properties();
		props.setProperty(PROP_UUID, this.uuid);
		props.setProperty(PROP_FILE_NAME, this.fileName);
		props.setProperty(PROP_FILE_SIZE, this.fileSize);
		props.setProperty(PROP_DOCUMENT_DATE, this.timeStamp);
		props.setProperty(PROP_FILE_TYPE, this.fileType);
		return props;
	}

}
