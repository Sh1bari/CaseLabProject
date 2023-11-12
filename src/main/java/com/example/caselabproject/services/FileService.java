package com.example.caselabproject.services;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.response.FileDownloadResponseDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Validated
public interface FileService {

    /**
     * Позволяет пользовтелю создавшему документ добавить файл к документу по его ID документа.
     *
     * @param username Никнейм пользователя для привязки файла к его документу.
     * @param file Файл для добавления к документу.
     * @param documentId ID документа для привязки к нему файла.
     * @throws DocumentAccessException если к документу пытается добавить файл не создатель документа.
     * @throws DocumentDoesNotExistException если документ с указанным ID не существует.
     * @throws FileConnectToDocumentException если добавить файл к документу не получилось
     */
    @Transactional
    List<FileResponseDto> addFile(@NotBlank(message = "Username cant be blank.") String username, MultipartFile file,
                                  @Min(value = 1L, message = "Id can't be less than 1") Long documentId);

    /**
     * Позволяет найти получить список файлов прикрепленных к документу по его ID.
     *
     * @param documentId ID документа, файлы которого нужно найти.
     * @param pageable Модель страницы для пагинации.
     * @throws NoFilesPageFoundException если файлов не найдено на странице.
     */
    @Transactional
    List<FileResponseDto> getFiles(@Min(value = 1L, message = "Id can't be less than 1") Long documentId, Pageable pageable);

    /**
     * Позволяет скачать файл по ID файла и по ID документа, к которому он привязан.
     *
     * @param documentId ID документа, к которому привязан файл.
     * @param fileId ID файла, который нужно скачать.
     * @throws FileNotExistException если файл с указанным ID не существует.
     */
    @Transactional
    FileDownloadResponseDto downloadFile(@Min(value = 1L, message = "Id can't be less than 1") Long documentId,
                                         @Min(value = 1L, message = "Id can't be less than 1") Long fileId) throws IOException;

    /**
     * Позволяет пользователю создавшему документ обновить файл привязанный к документу.
     *
     * @param username Никнейм пользователя.
     * @param file Новый файл для добавления к документу.
     * @param documentId ID документа, к которому привязан файл.
     * @param fileId ID файла, который нужно обновить.
     * @throws DocumentDoesNotExistException если документ с указанным ID не существует.
     * @throws DocumentAccessException если этот документ пытается обновить не создатель документа.
     * @throws FileNotExistException если файл с указанным ID не существует.
     */
    @Transactional
    List<FileResponseDto> updateFile(String username, MultipartFile file,
                                     @Min(value = 1L, message = "Id can't be less than 1") Long documentId,
                                     @Min(value = 1L, message = "Id can't be less than 1") Long fileId);

    /**
     * Позволяет пользователю создавшему документ удалить привязанный к нему файл его по ID файла и ID документа.
     *
     * @param username Никнейм пользователя.
     * @param documentId ID документа, к которому привязан файл.
     * @param fileId ID файла, который нужно удалить.
     * @throws DocumentDoesNotExistException если документ с указанным ID не существует.
     * @throws FileNotExistException если файл с указанным ID не существует.
     * @throws DocumentAccessException если этот файл пытается удалить не создатель документа.
     */
    @Transactional
    boolean deleteFile(String username,
                                     @Min(value = 1L, message = "Id can't be less than 1") Long documentId,
                                     @Min(value = 1L, message = "Id can't be less than 1") Long fileId);
}
