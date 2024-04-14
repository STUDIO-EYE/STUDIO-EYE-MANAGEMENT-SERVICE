package com.mju.management.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mju.management.global.model.Exception.ExceptionList;
import com.mju.management.global.model.Exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;

    private final AmazonS3 amazonS3;

    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {
        if(file == null){
            throw new NonExistentException(ExceptionList.INVALID_PARAMETER);
        }
        String fileName =  file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))
                + "-" + convertToRandomName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        InputStream inputStream = file.getInputStream();

        String filePath = BUCKET;
        amazonS3.putObject(new PutObjectRequest(filePath, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }
    @Transactional
    public void deleteFile(String s3key) {
        if(s3key == null){
            throw new NonExistentException(ExceptionList.INVALID_PARAMETER);
        }
        amazonS3.deleteObject(new DeleteObjectRequest(BUCKET, s3key));
    }

    public String convertToRandomName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString().concat(fileExtension);
    }

    public String getUrl(String s3key) {
        if(s3key == null){
            throw new NonExistentException(ExceptionList.INVALID_PARAMETER);
        }
        return amazonS3.getUrl(BUCKET, s3key).toString();
    }
}
