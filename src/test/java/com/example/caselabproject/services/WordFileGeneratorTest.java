package com.example.caselabproject.services;

import com.example.caselabproject.models.entities.*;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.services.implementations.WordFileGeneratorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordFileGeneratorTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private WordFileGeneratorImpl wordFileGenerator;

    @Test
    void generateWordFileForDocumentById_generatedFileSizeGreaterThanZero_Test() {
        when(documentRepository.findById(1L))
                .thenReturn(Optional.of(getDocument()));
        byte[] wordFile = wordFileGenerator.generateWordFileForDocumentById(1L);
        assertTrue(wordFile.length > 0, "Размер сгенерированного файла больше 0");
    }

    private Document getDocument() {
        return Document.builder()
                .id(1L)
                .name("Название документа")
                .creationDate(LocalDateTime.now())
                .creator(getUser())
                .documentConstructorType(getDocumentConstructorType())
                .fieldsValues(getFieldsValues())
                .build();
    }

    private Map<Field, String> getFieldsValues() {
        Map<Field, String> fieldsValues = new HashMap<>();
        List<Field> fields = getFields();
        fieldsValues.put(fields.get(0), "Индивидуальный предприниматель Воронин Михаил Юрьевич");
        fieldsValues.put(fields.get(1), "ООО АГРОПРОМСТРОЙ");
        fieldsValues.put(fields.get(2), "125 000.93, сто двадцать пять тысяч рублей девяносто три копейки");
        fieldsValues.put(fields.get(3), "679724933665");
        fieldsValues.put(fields.get(4), "884094971");
        fieldsValues.put(fields.get(5), "129972, Иркутская область, город Павловский Посад, ул. Ладыгина, 54");
        fieldsValues.put(fields.get(6), "50972873500000005184");
        fieldsValues.put(fields.get(7), "50365238600000005149");
        fieldsValues.put(fields.get(8), "РусБанк");
        fieldsValues.put(fields.get(9), "893596593");
        fieldsValues.put(fields.get(10), "89805462135");
        return fieldsValues;
    }

    private DocumentConstructorType getDocumentConstructorType() {
        return DocumentConstructorType.builder()
                .id(1L)
                .name("Акт об оказании услуг")
                .prefix("АООУ/")
                .fields(getFields())
                .build();
    }

    private List<Field> getFields() {
        Field field1 = Field.builder()
                .id(1L)
                .name("Исполнитель")
                .build();
        Field field2 = Field.builder()
                .id(2L)
                .name("Заказчик")
                .build();
        Field field3 = Field.builder()
                .id(3L)
                .name("Общая стоимость выполненных работ, оказанных услуг")
                .build();
        Field field4 = Field.builder()
                .id(4L)
                .name("ИНН Исполнителя")
                .build();
        Field field5 = Field.builder()
                .id(5L)
                .name("КПП Исполнителя")
                .build();
        Field field6 = Field.builder()
                .id(6L)
                .name("Адрес Исполнителя")
                .build();
        Field field7 = Field.builder()
                .id(7L)
                .name("Расчетный счет Исполнителя")
                .build();
        Field field8 = Field.builder()
                .id(8L)
                .name("Корреспондентский счет Исполнителя")
                .build();
        Field field9 = Field.builder()
                .id(9L)
                .name("Банк Исполнителя")
                .build();
        Field field10 = Field.builder()
                .id(10L)
                .name("БИК Исполнителя")
                .build();
        Field field11 = Field.builder()
                .id(11L)
                .name("Телефон Исполнителя")
                .build();
        return List.of(field1, field2, field3, field4, field5, field6, field7,
                field8, field9, field10, field11);
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .department(getDepartment())
                .position("Заместитель начальника")
                .personalUserInfo(getPersonalUserInfo())
                .build();
    }

    private Department getDepartment() {
        return Department.builder()
                .id(1L)
                .name("Отдел информатизации и коммуникации")
                .build();
    }

    private PersonalUserInfo getPersonalUserInfo() {
        return PersonalUserInfo.builder()
                .id(1L)
                .firstName("Максим")
                .lastName("Романов")
                .patronymic("Викторович")
                .build();
    }
}
