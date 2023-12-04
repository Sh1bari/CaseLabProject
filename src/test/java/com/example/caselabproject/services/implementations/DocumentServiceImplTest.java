package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.document.DocumentAccessException;
import com.example.caselabproject.exceptions.document.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.document.NoDocumentPageFoundException;
import com.example.caselabproject.exceptions.documentConsType.DocumentConstructorTypeNotFoundException;
import com.example.caselabproject.exceptions.user.UserByPrincipalUsernameDoesNotExistException;
import com.example.caselabproject.models.DTOs.request.DocumentRequestDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.DocumentPageRepository;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DocumentConstructorTypeRepository typeRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentPageRepository pageRepository;

    @InjectMocks
    private DocumentServiceImpl underTest;

    private static DocumentRequestDto getDocumentRequestDto() {
        DocumentRequestDto documentRequestDto = new DocumentRequestDto();
        documentRequestDto.setName("doc_test");
        documentRequestDto.setConstructorTypeId(1L);
        return documentRequestDto;
    }

    @BeforeEach
    void setUp() {
        underTest = new DocumentServiceImpl(
                documentRepository, pageRepository, typeRepository, userRepository
        );
    }

    @Test
    void createDocument_CanCreateDocument() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        Document document = documentRequestDto.mapToEntity();

        given(userRepository.findByUsername("username"))
                .willReturn(Optional.of(new User()));
        given(typeRepository.findById(1L))
                .willReturn(Optional.of(new DocumentConstructorType()));
        given(documentRepository.save(any()))
                .willReturn(document);

        underTest.createDocument("username", documentRequestDto);

        verify(documentRepository).save(any());
    }

    @Test
    void createDocument_CanThrowUserException() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        assertThatThrownBy(() -> underTest.createDocument("user_exception", documentRequestDto))
                .isInstanceOf(UserByPrincipalUsernameDoesNotExistException.class)
                .hasMessageContaining("Server can not find user with username user_exception");
    }

    @Test
    void createDocument_CanThrowTypeException() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        given(userRepository.findByUsername("username"))
                .willReturn(Optional.of(new User()));

        assertThatThrownBy(() -> underTest.createDocument("username", documentRequestDto))
                .isInstanceOf(DocumentConstructorTypeNotFoundException.class)
                .hasMessageContaining("Document type with id 1 isn't found");
    }

    @Test
    void findDocument_CanReturnDocument() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        User user = new User();
        user.setUsername("username");
        user.setId(1L);

        Document document = documentRequestDto.mapToEntity();
        document.setId(1L);
        document.setCreator(user);
        document.setFiles(List.of());
        document.setRecordState(RecordState.ACTIVE);
        document.setDocumentConstructorType(new DocumentConstructorType());

        given(documentRepository.findById(1L))
                .willReturn(Optional.of(document));

        underTest.findDocument(1L);

        verify(documentRepository).findById(any());
    }

    @Test
    void findDocument_CanThrowDocumentException() {
        assertThatThrownBy(() -> underTest.findDocument(1L))
                .isInstanceOf(DocumentDoesNotExistException.class)
                .hasMessageContaining("Document with id:1 does not exist!");
    }

    @Test
    void filteredDocument_CanReturnPageWithAllByNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndRecordState() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        User user = new User();
        user.setUsername("username");
        user.setId(1L);

        Document document = documentRequestDto.mapToEntity();
        document.setId(1L);
        document.setCreator(user);
        document.setFiles(List.of());
        document.setCreationDate(LocalDateTime.of(2020, 1, 1, 0, 0));
        document.setDocumentConstructorType(new DocumentConstructorType());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Document> page = new PageImpl<>(List.of(document));

        given(pageRepository
                .findAllByNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndRecordState(
                        "doc_test", pageable, LocalDateTime.of(1970, 1, 1, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0),
                        RecordState.ACTIVE
                )).willReturn(page);

        underTest.filteredDocument(pageable, "doc_test", RecordState.ACTIVE,
                LocalDateTime.of(1970, 1, 1, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0));

        verify(pageRepository).findAllByNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndRecordState(
                "doc_test", pageable, LocalDateTime.of(1970, 1, 1, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0), RecordState.ACTIVE
        );
    }

    @Test
    void filteredDocument_CanReturnPageWithAllByCreationDateAfterAndCreationDateBeforeAndRecordState() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        User user = new User();
        user.setUsername("username");
        user.setId(1L);

        Document document = documentRequestDto.mapToEntity();
        document.setId(1L);
        document.setCreator(user);
        document.setFiles(List.of());
        document.setCreationDate(LocalDateTime.of(2020, 1, 1, 0, 0));
        document.setDocumentConstructorType(new DocumentConstructorType());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Document> page = new PageImpl<>(List.of(document));

        given(pageRepository
                .findAllByCreationDateAfterAndCreationDateBeforeAndRecordState(
                        pageable, LocalDateTime.of(1970, 1, 1, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0), RecordState.ACTIVE
                )).willReturn(page);

        underTest.filteredDocument(pageable, "", RecordState.ACTIVE,
                LocalDateTime.of(1970, 1, 1, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0));

        verify(pageRepository).findAllByCreationDateAfterAndCreationDateBeforeAndRecordState(
                pageable, LocalDateTime.of(1970, 1, 1, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0), RecordState.ACTIVE
        );
    }

    @Test
    void filteredDocument_CanThrowEmptyException() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Document> page = new PageImpl<>(List.of());

        given(pageRepository
                .findAllByCreationDateAfterAndCreationDateBeforeAndRecordState(
                        pageable, LocalDateTime.of(1970, 1, 1, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0), RecordState.ACTIVE
                )).willReturn(page);

        assertThatThrownBy(() -> underTest.filteredDocument(pageable, "", RecordState.ACTIVE,
                LocalDateTime.of(1970, 1, 1, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0)))
                .isInstanceOf(NoDocumentPageFoundException.class)
                .hasMessageContaining("No content found by page 0");
    }

    @Test
    void updateDocument_CanUpdateDocument() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        User user = new User();
        user.setUsername("username");
        user.setId(1L);

        Document document = documentRequestDto.mapToEntity();
        document.setId(1L);
        document.setCreator(user);
        document.setFiles(List.of());

        given(documentRepository.existsById(1L))
                .willReturn(true);
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);
        given(documentRepository.findById(1L))
                .willReturn(Optional.of(document));
        given(typeRepository.findById(1L))
                .willReturn(Optional.of(new DocumentConstructorType()));

        underTest.updateDocument("username", documentRequestDto, 1L);

        verify(documentRepository).save(any());
    }

    @Test
    void updateDocument_CanThrowDocumentException() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        given(documentRepository.existsById(1L))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.updateDocument("username", documentRequestDto, 1L))
                .isInstanceOf(DocumentDoesNotExistException.class)
                .hasMessageContaining("Document with id:1 does not exist!");
    }

    @Test
    void updateDocument_CanThrowUserException() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        given(documentRepository.existsById(1L))
                .willReturn(true);
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.updateDocument("username", documentRequestDto, 1L))
                .isInstanceOf(DocumentAccessException.class)
                .hasMessageContaining("User username does not have access to this document!");
    }

    @Test
    void updateDocument_CanThrowTypeException() {
        DocumentRequestDto documentRequestDto = getDocumentRequestDto();

        User user = new User();
        user.setUsername("username");
        user.setId(1L);

        Document document = documentRequestDto.mapToEntity();
        document.setId(1L);
        document.setCreator(user);
        document.setFiles(List.of());

        given(documentRepository.existsById(1L))
                .willReturn(true);
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);
        given(documentRepository.findById(1L))
                .willReturn(Optional.of(document));

        assertThatThrownBy(() -> underTest.updateDocument("username", documentRequestDto, 1L))
                .isInstanceOf(DocumentConstructorTypeNotFoundException.class)
                .hasMessageContaining("Document type with id 1 isn't found");
    }

    @Test
    void deleteDocument_CanDeleteDocument() {
        DocumentRequestDto requestDto = getDocumentRequestDto();

        Document document = requestDto.mapToEntity();
        document.setId(1L);
        document.setRecordState(RecordState.ACTIVE);

        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);
        given(documentRepository.findById(1L))
                .willReturn(Optional.of(document));

        underTest.deleteDocument("username", 1L);

        verify(documentRepository).save(any());
    }

    @Test
    void deleteDocument_CanThrowDocumentException() {
        Document document = new Document();
        document.setRecordState(RecordState.DELETED);

        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);
        given(documentRepository.findById(1L))
                .willReturn(Optional.of(document));

        assertThatThrownBy(() -> underTest.deleteDocument("username", 1L))
                .isInstanceOf(DocumentDoesNotExistException.class)
                .hasMessageContaining("Document with id:1 does not exist!");
    }

    @Test
    void deleteDocument_CanThrowUserException() {
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.deleteDocument("username", 1L))
                .isInstanceOf(DocumentAccessException.class)
                .hasMessageContaining("User username does not have access to this document!");
    }
}