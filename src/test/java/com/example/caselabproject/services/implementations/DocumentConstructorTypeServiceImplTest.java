package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.document.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.document.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.request.field.FieldRequestDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DocumentConstructorTypeServiceImplTest {

    @Mock
    private DocumentConstructorTypeRepository typeRepository;
    @Mock
    private DocumentRepository documentRepository;
    private DocumentConstructorTypeService underTest;

    private static DocumentConstructorTypeRequestDto getDocumentConstructorTypeRequestDto() {
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto =
                new DocumentConstructorTypeRequestDto();
        documentConstructorTypeRequestDto.setName("Приказ");
        documentConstructorTypeRequestDto.setPrefix("П-");

        FieldRequestDto fieldRequestDto1 = new FieldRequestDto();
        fieldRequestDto1.setName("Основной текст");
        FieldRequestDto fieldRequestDto2 = new FieldRequestDto();
        fieldRequestDto2.setName("Руководитель организации");

        documentConstructorTypeRequestDto.setFields(List.of(fieldRequestDto1, fieldRequestDto2));
        return documentConstructorTypeRequestDto;
    }

    @BeforeEach
    void setUp() {
        underTest = new DocumentConstructorTypeServiceImpl(typeRepository, documentRepository);
    }

    @Test
    void createDocumentConstructorType_canCreateDocumentConstructorType() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        DocumentConstructorType documentConstructorType = documentConstructorTypeRequestDto.mapToEntity();
        documentConstructorType.setId(1L);
        given(typeRepository.saveAndFlush(any()))
                .willReturn(documentConstructorType);

        // when
        underTest.create(documentConstructorTypeRequestDto);

        // then
        verify(typeRepository).saveAndFlush(any());
    }

    @Test
    void createDocumentConstructorType_willThrowWhenNameIsTaken() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        given(typeRepository.saveAndFlush(any()))
                .willThrow(DataIntegrityViolationException.class);

        // when
        // then
        assertThatThrownBy(() -> underTest.create(documentConstructorTypeRequestDto))
                .isInstanceOf(DocumentConstructorTypeNameExistsException.class)
                .hasMessageContaining("Document type " + documentConstructorTypeRequestDto.getName()
                        + " already exists.");
    }

    @Test
    void updateNameAndPrefix_canUpdateNameAndPrefix() {
        // given
        DocumentConstructorTypePatchRequestDto documentConstructorTypePatchRequestDto =
                new DocumentConstructorTypePatchRequestDto();
        documentConstructorTypePatchRequestDto.setName("Приказ");
        documentConstructorTypePatchRequestDto.setPrefix("П-");

        DocumentConstructorType documentConstructorType = documentConstructorTypePatchRequestDto.mapToEntity();
        documentConstructorType.setId(1L);
        given(typeRepository.saveAndFlush(any()))
                .willReturn(documentConstructorType);

        given(typeRepository.findById(1L))
                .willReturn(Optional.of(documentConstructorType));

        // when
        underTest.updateNameAndPrefix(1L, documentConstructorTypePatchRequestDto);

        // then
        verify(typeRepository).saveAndFlush(any());
    }

    @Test
    void updateNameAndPrefix_willThrowWhenDocumentConstructorTypeNotFound() {
        // given
        DocumentConstructorTypePatchRequestDto documentConstructorTypePatchRequestDto =
                new DocumentConstructorTypePatchRequestDto();
        documentConstructorTypePatchRequestDto.setName("Приказ");
        documentConstructorTypePatchRequestDto.setPrefix("П-");

        given(typeRepository.findById(1L))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updateNameAndPrefix(1L, documentConstructorTypePatchRequestDto))
                .isInstanceOf(DocumentConstructorTypeNotFoundException.class)
                .hasMessageContaining("Document type with id " + 1L + " isn't found");

        verify(typeRepository, never()).saveAndFlush(any());
    }

    @Test
    void update_canUpdate() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        DocumentConstructorType documentConstructorType = documentConstructorTypeRequestDto.mapToEntity();
        documentConstructorType.setId(1L);

        given(typeRepository.findById(1L))
                .willReturn(Optional.of(documentConstructorType));

        given(documentRepository.existsByDocumentConstructorType(any()))
                .willReturn(false);

        given(typeRepository.saveAndFlush(any()))
                .willReturn(documentConstructorType);

        // when
        underTest.update(1L, documentConstructorTypeRequestDto);

        // then
        verify(typeRepository).saveAndFlush(any());
    }

    @Test
    void update_willThrowWhenDocumentConstructorTypeIsUsedInAnyDocument() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        DocumentConstructorType documentConstructorType = documentConstructorTypeRequestDto.mapToEntity();
        documentConstructorType.setId(1L);

        given(typeRepository.findById(1L))
                .willReturn(Optional.of(documentConstructorType));

        given(documentRepository.existsByDocumentConstructorType(any()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.update(1L, documentConstructorTypeRequestDto))
                .isInstanceOf(DocumentConstructorTypeHasAssociatedDocumentsException.class)
                .hasMessageContaining("DocumentConstructorType with id "
                        + 1L + " has associated documents.");

        verify(typeRepository, never()).saveAndFlush(any());
    }

    @Test
    void deleteById_canDelete() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        DocumentConstructorType documentConstructorType = documentConstructorTypeRequestDto.mapToEntity();
        documentConstructorType.setId(1L);
        documentConstructorType.setRecordState(RecordState.ACTIVE);

        given(typeRepository.findById(1L))
                .willReturn(Optional.of(documentConstructorType));

        given(typeRepository.saveAndFlush(any()))
                .willReturn(documentConstructorType);

        // when
        underTest.deleteById(1L);

        // then
        verify(typeRepository).saveAndFlush(any());
    }

    @Test
    void deleteById_willThrowWhenAlreadyDeleted() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        DocumentConstructorType documentConstructorType = documentConstructorTypeRequestDto.mapToEntity();
        documentConstructorType.setId(1L);
        documentConstructorType.setRecordState(RecordState.DELETED);

        given(typeRepository.findById(1L))
                .willReturn(Optional.of(documentConstructorType));

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteById(1L))
                .isInstanceOf(DocumentConstructorTypeAlreadyDeletedException.class)
                .hasMessageContaining("Document type with id " + 1L + " already deleted.");

        verify(typeRepository, never()).saveAndFlush(any());
    }

    @Test
    void recoverById_canRecover() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        DocumentConstructorType documentConstructorType = documentConstructorTypeRequestDto.mapToEntity();
        documentConstructorType.setId(1L);
        documentConstructorType.setRecordState(RecordState.DELETED);

        given(typeRepository.findById(1L))
                .willReturn(Optional.of(documentConstructorType));

        given(typeRepository.saveAndFlush(any()))
                .willReturn(documentConstructorType);

        // when
        underTest.recoverById(1L);

        // then
        verify(typeRepository).saveAndFlush(any());
    }

    @Test
    void recoverById_willThrowWhenAlreadyActive() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        DocumentConstructorType documentConstructorType = documentConstructorTypeRequestDto.mapToEntity();
        documentConstructorType.setId(1L);
        documentConstructorType.setRecordState(RecordState.ACTIVE);

        given(typeRepository.findById(1L))
                .willReturn(Optional.of(documentConstructorType));

        // when
        // then
        assertThatThrownBy(() -> underTest.recoverById(1L))
                .isInstanceOf(DocumentConstructorTypeAlreadyActiveException.class)
                .hasMessageContaining("Document type with id " + 1L + " already active.");

        verify(typeRepository, never()).saveAndFlush(any());
    }

    @Test
    void getById_canGetDocumentConstructorType() {
        // given
        DocumentConstructorTypeRequestDto documentConstructorTypeRequestDto = getDocumentConstructorTypeRequestDto();

        DocumentConstructorType documentConstructorType = documentConstructorTypeRequestDto.mapToEntity();
        documentConstructorType.setId(1L);

        given(typeRepository.findById(1L))
                .willReturn(Optional.of(documentConstructorType));

        // when
        underTest.getById(1L);

        // then
        verify(typeRepository).findById(any());
    }

    @Test
    void getAllContaining_canGet() {
        // given
        String name = "Приказ";
        RecordState recordState = RecordState.ACTIVE;
        int page = 10;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        given(typeRepository.findAllByNameContainingIgnoreCaseAndRecordState(name, recordState, pageable))
                .willReturn(new PageImpl<>(List.of(new DocumentConstructorType())));

        // when
        underTest.getAllContaining(name, recordState, page, size);

        // then
        verify(typeRepository)
                .findAllByNameContainingIgnoreCaseAndRecordState(
                        name, recordState, pageable);
    }

    /*@Test
    void getAllContaining_willThrowWhenPageIsEmpty() {
        // given
        String name = "Приказ";
        RecordState recordState = RecordState.ACTIVE;
        int page = 10;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        given(typeRepository.findAllByNameContainingIgnoreCaseAndRecordState(name, recordState, pageable))
                .willReturn(Page.empty());

        // then
        assertThatThrownBy(() -> underTest.getAllContaining(name, recordState, page, size))
                .isInstanceOf(PageNotFoundException.class)
                .hasMessageContaining("Page with number " + page + " not found");
    }*/
}