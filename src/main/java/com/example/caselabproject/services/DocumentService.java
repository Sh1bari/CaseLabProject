package com.example.caselabproject.services;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.document.DocumentRequestDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Validated
public interface DocumentService {

    /**
     * Позволяет найти документ по его ID.
     *
     * @param username Никнейм пользователя для привязки к его ID документа.
     * @param request Тело документа для создания сущности.
     * @throws UserByPrincipalUsernameDoesNotExistException если пользователя с указанным именем не существует.
     * @throws DocumentConstructorTypeNameNotFoundException если не найден DocumentConstructorType.
     */
    @Transactional
    DocumentCreateResponseDto createDocument(@NotBlank(message = "Username cant be blank.") String username, @Valid DocumentRequestDto request);

    /**
     * Позволяет найти документ по его ID.
     *
     * @param documentId ID документа, который нужно найти.
     * @throws DocumentDoesNotExistException если документ с указанным ID не существует.
     */
    @Transactional
    DocumentResponseDto findDocument(@Min(value = 1L, message = "Id can't be less than 1") Long documentId);

    /**
     * Позволяет получить список документов с пагинцией и с опциями для удобного поиска.
     *
     * @param pageable Модель страницы для пагинации.
     * @param name Имя или часть имени документа.
     * @param recordState Состояние документа.
     * @param startDate Дата с которой нужно начать поиск.
     * @param endDate Дата по которую надо искать.
     * @throws NoDocumentPageFoundException если документов не найдено на странице.
     */
    @Transactional
    List<DocumentResponseDto> filteredDocument(Pageable pageable,
                                               String name,
                                               RecordState recordState,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate);

    /**
     * Позволяет пользователю создавшему документ обновить его по ID документа.
     *
     * @param username Никнейм пользователя.
     * @param request Запрос с новыми параметрами документа.
     * @param documentId ID документа, который нужно обновить.
     * @throws DocumentDoesNotExistException если документ с указанным ID не существует.
     * @throws DocumentAccessException если этот документ пытается обновить не создатель документа.
     * @throws DocumentConstructorTypeNameNotFoundException если не найден DocumentConstructorType.
     */
    @Transactional
    DocumentResponseDto updateDocument(@NotBlank(message = "Username cant be blank.")String username,
                                       @Valid DocumentRequestDto request,
                                       @Min(value = 1L, message = "Id can't be less than 1") Long documentId);

    /**
     * Позволяет пользователю создавшему документ удалить его по ID документа.
     *
     * @param username Никнейм пользователя.
     * @param documentId ID документа, который нужно удалить.
     * @throws DocumentDoesNotExistException если документ с указанным ID не существует.
     * @throws DocumentAccessException если этот документ пытается удалить не создатель документа.
     */
    @Transactional
    boolean deleteDocument(@NotBlank(message = "Username cant be blank.")String username,
                           @Min(value = 1L, message = "Id can't be less than 1") Long documentId);
}
