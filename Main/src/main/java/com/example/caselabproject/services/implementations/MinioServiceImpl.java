package com.example.caselabproject.services.implementations;


import com.example.caselabproject.exceptions.file.CantSaveFileException;
import com.example.caselabproject.services.MinioService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;
    @Override
    public InputStream getFile(String bucket, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String saveFile(String bucket, MultipartFile file) {
        try {
            // Генерируем уникальное имя файла
            String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());

            // Открываем поток для загрузки файла в MinIO
            InputStream fileStream = file.getInputStream();

            // Сохраняем файл в MinIO
            try {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(uniqueFileName)
                                .stream(fileStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }catch (Exception e){
                throw new CantSaveFileException();
            }

            // Закрываем поток
            fileStream.close();

            // Возвращаем уникальное имя файла
            return uniqueFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateUniqueFileName(String name) {
        String currentDate = getCurrentDateTime();
        // Генерируем UUID и добавляем оригинальное расширение файла
        return currentDate + name;
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            String res = fileName.substring(lastDotIndex);
            return res;
        }
        return "";
    }

    private static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy-HH-mm");
        return dateFormat.format(new Date());
    }
}
