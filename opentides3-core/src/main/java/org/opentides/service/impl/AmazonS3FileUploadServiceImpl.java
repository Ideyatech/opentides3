package org.opentides.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.opentides.bean.FileInfo;
import org.opentides.service.FileUploadService;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.eaio.uuid.UUID;

/**
 * Service that will upload files to Amazon AWS
 * 
 * @author AJ
 *
 */
@Service(value="amazonS3FileUploadService")
public class AmazonS3FileUploadServiceImpl implements FileUploadService {
	
	@Value("#{applicationSettings['amazon.s3.bucketName']}")
	private String bucketName;
	
	@Value("#{applicationSettings['amazon.s3.amazonURL']}")
	private String amazonURL;
	
	@Value("#{applicationSettings['amazon.s3.accessKey']}")
	private String accessKey;
	
	@Value("#{applicationSettings['amazon.s3.secretKey']}")
	private String secretKey;
	
	/**
	 * Generated filePath format will be as follows.
	 * 
	 * {bucketName}/{folderName}/{uuid}_{originalFileName}.{originalFileExtension}
	 * 
	 */
	@Override
	public FileInfo upload(MultipartFile multipartFile, String folderName, boolean retainFilename) {
		
		AWSCredentials myCredentials = new BasicAWSCredentials(accessKey, secretKey);
		
		TransferManager tm = new TransferManager(myCredentials);

		String filePath = "";
		
		if(!StringUtil.isEmpty(folderName)) {
			filePath +=	folderName + "/";
		}
		
		if(retainFilename) {
			filePath += new UUID();
			filePath +=	"_";
		}
		filePath +=	multipartFile.getOriginalFilename();
		
		Upload upload = null;
		
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(multipartFile.getBytes());
			upload = tm.upload(bucketName, filePath, bis, new ObjectMetadata());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			upload.waitForCompletion();
		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} catch (AmazonClientException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//set the newly uploaded file to public
		tm.getAmazonS3Client().setObjectAcl(bucketName, filePath, CannedAccessControlList.PublicRead);
		
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFilename(multipartFile.getOriginalFilename());
		fileInfo.setFileSize(Long.valueOf(multipartFile.getSize()));
		fileInfo.setOriginalFileName(multipartFile.getOriginalFilename());
		
		fileInfo.setFullPath("http://" + bucketName + amazonURL.replace("http://", ".") + "/" + filePath);
		
		return fileInfo;
		
	}
	
	/**
	 * Generated filePath format will be as follows.
	 * 
	 * {bucketName}/{folderName}/{uuid}_{originalFileName}.{originalFileExtension}
	 * 
	 */
	@Override
	public FileInfo upload(File file, String folderName, boolean retainFilename) {
		
		AWSCredentials myCredentials = new BasicAWSCredentials(accessKey, secretKey);
		
		TransferManager tm = new TransferManager(myCredentials);

		String filePath = "";
		
		if(!StringUtil.isEmpty(folderName)) {
			filePath +=	folderName + "/";
		}
		
		if(retainFilename) {
			filePath += new UUID();
			filePath +=	"_";
		}
		filePath +=	file.getName();
		
		Upload upload = null;
		
		upload = tm.upload(bucketName, filePath, file);
		
		try {
			upload.waitForCompletion();
		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} catch (AmazonClientException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//set the newly uploaded file to public
		tm.getAmazonS3Client().setObjectAcl(bucketName, filePath, CannedAccessControlList.PublicRead);
		
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFilename(file.getName());
		fileInfo.setFileSize(Long.valueOf(file.length()));
		fileInfo.setOriginalFileName(file.getName());
		
		fileInfo.setFullPath("http://" + bucketName + amazonURL.replace("http://", ".") + "/" + filePath);
		
		return fileInfo;
		
	}
	
	@Override
	public FileInfo upload(MultipartFile multipartFile, String folderName) {
		return upload(multipartFile, folderName, false);
	}

	@Override
	public FileInfo upload(MultipartFile multipartFile) {
		return upload(multipartFile, "misc");
	}
	
	@Override
	public FileInfo upload(File file, String folderName) {
		return upload(file, folderName, false);
	}

	@Override
	public FileInfo upload(File file) {
		return upload(file, "misc");
	}

}
