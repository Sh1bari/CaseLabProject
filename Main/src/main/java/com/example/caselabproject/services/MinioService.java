package com.example.caselabproject.services;


import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
public interface MinioService {
    InputStream getFile(String bucket, String objectName);

    String saveFile(String bucket, MultipartFile file);
}
