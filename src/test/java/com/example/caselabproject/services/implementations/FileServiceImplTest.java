package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentAccessException;
import com.example.caselabproject.exceptions.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.FileNotExistException;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.entities.File;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.repositories.FileRepository;
import com.example.caselabproject.repositories.UserRepository;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FileRepository fileRepository;

    private FileServiceImpl underTest;

    private static MultipartFile getMockMultipartFile() {

        return new MockMultipartFile(
                "file",
                "hello_text.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );
    }

    @BeforeEach
    void setUp() {
        underTest = new FileServiceImpl(fileRepository, documentRepository, userRepository);
    }

    @Test
    void addFile_CanAddFileToDocument() throws IOException {
        Document document = new Document();
        User user = new User();
        user.setUsername("username");
        document.setId(1L);
        document.setCreator(user);
        document.setFiles(new ArrayList<>());
        document.setDocumentConstructorType(new DocumentConstructorType());

        given(documentRepository.findById(1L))
                .willReturn(Optional.of(document));
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);

        underTest.addFile("username", getMockMultipartFile(), 1L);

        Files.delete(Path.of("src/main/resources/fileBase/username/docId_1_hello_text.txt"));

        verify(documentRepository).save(any());
    }

    @Test
    void addFile_CanThrowUserException() {
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.addFile("username", getMockMultipartFile(), 1L))
                .isInstanceOf(DocumentAccessException.class)
                .hasMessageContaining("User username does not have access to this document!");
    }

    @Test
    void addFile_CanThrowDocumentException() {
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);

        assertThatThrownBy(() -> underTest.addFile("username", getMockMultipartFile(), 1L))
                .isInstanceOf(DocumentDoesNotExistException.class)
                .hasMessageContaining("Document with id:1 does not exist!");
    }

    @Test
    void getFiles_CanReturnListOfFiles() {
        File file = new File();
        Page<File> page = new PageImpl<>(List.of(file));

        given(fileRepository.findAllByDocument_Id(1L, PageRequest.of(0, 1)))
                .willReturn(page);

        underTest.getFiles(1L, PageRequest.of(0, 1));

        verify(fileRepository).findAllByDocument_Id(1L, PageRequest.of(0, 1));
    }

    @Test
    void downloadFile_CanSendFileToDownload() throws IOException {
        File file = new File();
        FileUtils.writeByteArrayToFile(new java.io.File(
                        "src\\test\\resources\\hello_text.txt"),
                getMockMultipartFile().getBytes()
        );
        file.setPath("src\\test\\resources\\hello_text.txt");
        file.setId(1L);

        given(fileRepository.findById(1L))
                .willReturn(Optional.of(file));
        given(documentRepository.existsById(1L))
                .willReturn(true);

        underTest.downloadFile(1L, 1L);

        Files.delete(Path.of("src/test/resources/hello_text.txt"));

        verify(fileRepository).findById(any());
    }

    @Test
    void downloadFile_CanThrowDocumentException() {
        given(documentRepository.existsById(1L))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.downloadFile(1L, 1L))
                .isInstanceOf(DocumentDoesNotExistException.class)
                .hasMessageContaining("Document with id:1 does not exist!");
    }

    @Test
    void downloadFile_CanThrowFileException() {
        given(documentRepository.existsById(1L))
                .willReturn(true);

        assertThatThrownBy(() -> underTest.downloadFile(1L, 1L))
                .isInstanceOf(FileNotExistException.class)
                .hasMessageContaining("File does not exist!");
    }

    @Test
    void updateFile_CanUpdateFile() {
        User user = new User();
        Document document = new Document();
        File file = new File();
        user.setUsername("username");
        document.setId(1L);
        document.setCreator(user);
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        file.setDocument(document);
        document.setFiles(files);

        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);
        given(fileRepository.findById(1L))
                .willReturn(Optional.of(file));
        given(documentRepository.save(any()))
                .willReturn(document);
        given(documentRepository.findById(1L))
                .willReturn(Optional.of(document));
        given(fileRepository.save(any()))
                .willReturn(file);

        underTest.updateFile("username", getMockMultipartFile(), 1L, 1L);

        verify(documentRepository).save(any());
    }

    @Test
    void updateFile_CanThrowUserException() {
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.updateFile("username", getMockMultipartFile(), 1L, 1L))
                .isInstanceOf(DocumentAccessException.class)
                .hasMessageContaining("User username does not have access to this document!");
    }

    @Test
    void updateFile_CanThrowFileException() {
        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);

        assertThatThrownBy(() -> underTest.updateFile("username", getMockMultipartFile(), 1L, 1L))
                .isInstanceOf(FileNotExistException.class)
                .hasMessageContaining("File does not exist!");
    }

    @Test
    void updateFile_CanThrowDocumentException() {
        User user = new User();
        Document document = new Document();
        File file = new File();
        user.setUsername("username");
        document.setId(1L);
        document.setCreator(user);
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        file.setDocument(document);
        document.setFiles(files);

        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);
        given(fileRepository.findById(1L))
                .willReturn(Optional.of(file));

        assertThatThrownBy(() -> underTest.updateFile("username", getMockMultipartFile(), 1L, 1L))
                .isInstanceOf(DocumentDoesNotExistException.class)
                .hasMessageContaining("Document with id:1 does not exist!");
    }

    @Test
    void deleteFile_CanDeleteFile() throws IOException {
        Document document = new Document();
        document.setId(1L);
        File file = new File();
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        document.setFiles(files);
        FileUtils.writeByteArrayToFile(new java.io.File(
                        "src\\test\\resources\\hello_text.txt"),
                getMockMultipartFile().getBytes()
        );
        file.setPath("src\\test\\resources\\hello_text.txt");

        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);
        given(documentRepository.findById(1L))
                .willReturn(Optional.of(document));
        given(fileRepository.findById(1L))
                .willReturn(Optional.of(file));
        given(documentRepository.save(any()))
                .willReturn(document);

        underTest.deleteFile("username", 1L, 1L);

        verify(documentRepository).save(any());
    }

    @Test
    void deleteFile_CanThrowUserException() {

        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.deleteFile("username", 1L, 1L))
                .isInstanceOf(DocumentAccessException.class)
                .hasMessageContaining("User username does not have access to this document!");
    }

    @Test
    void deleteFile_CanThrowDocumentException() {

        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);

        assertThatThrownBy(() -> underTest.deleteFile("username", 1L, 1L))
                .isInstanceOf(DocumentDoesNotExistException.class)
                .hasMessageContaining("Document with id:1 does not exist!");
    }

    @Test
    void deleteFile_CanThrowFileException() {

        given(userRepository.existsByUsernameAndDocuments_id("username", 1L))
                .willReturn(true);
        given(documentRepository.findById(1L))
                .willReturn(Optional.of(new Document()));

        assertThatThrownBy(() -> underTest.deleteFile("username", 1L, 1L))
                .isInstanceOf(FileNotExistException.class)
                .hasMessageContaining("File does not exist!");
    }
}