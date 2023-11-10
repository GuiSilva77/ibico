package br.com.ibico.api.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void uploadFile(String fileName, MultipartFile file, String bucketName);

    void deleteFile(String fileName, String bucketName);

    String getFileUrl(String fileName, String bucketName);
}
