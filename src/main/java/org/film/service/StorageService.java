package org.film.service;

import io.minio.errors.MinioException;

public interface StorageService {

     void uploadFile(String bucketName, String fileName, String data);
     String downloadFile(String bucketName, String fileName);
     void removeFile(String bucketName, String fileName) throws MinioException;
}
