package br.com.ibico.api.services.impl;

import br.com.ibico.api.exceptions.ServerErrorException;
import br.com.ibico.api.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    public final S3Client s3Client;

    Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    public FileStorageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void uploadFile(String fileName, MultipartFile file, String bucketName) {
        InputStream inputStream;
        try {
            inputStream = new ByteArrayInputStream(file.getBytes());
        } catch (IOException e) {
            logger.error("Error while uploading file to S3; {}", e.getMessage());
            throw new ServerErrorException("Error while uploading file to S3" + e.getMessage());
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName).key(fileName).build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
    }

    @Override
    public void deleteFile(String fileName, String bucketName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName).key(fileName).build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    @Override
    public String getFileUrl(String fileName, String bucketName) {
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
    }
}
