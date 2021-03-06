package com.example.timeattack.service;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public interface FileService {
    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);

    String getFileUrl(String fileName);
}
