package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DocumentConstructorTypeRepositoryTest {

    @Autowired
    private DocumentConstructorTypeRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenNameIsEmptyAndStatusActive() {
        // given
        DocumentConstructorType type1 = DocumentConstructorType.builder()
                .name("Служебная записка")
                .prefix("СЗ-")
                .recordState(RecordState.ACTIVE)
                .build();
        DocumentConstructorType type2 = DocumentConstructorType.builder()
                .name("Приказ")
                .prefix("П-")
                .recordState(RecordState.DELETED)
                .build();

        underTest.saveAll(List.of(type1, type2));

        // when
        Page<DocumentConstructorType> actual = underTest
                .findAllByNameContainingIgnoreCaseAndRecordState(
                        "",
                        RecordState.ACTIVE,
                        PageRequest.of(0, 2));

        // then
        assertThat(actual.getContent().size()).isEqualTo(1);
    }

    @Test
    void itShouldCheckWhenNameIsNotEmptyAndStatusDeleted() {
        // given
        DocumentConstructorType type1 = DocumentConstructorType.builder()
                .name("Служебная записка")
                .prefix("СЗ-")
                .recordState(RecordState.ACTIVE)
                .build();
        DocumentConstructorType type2 = DocumentConstructorType.builder()
                .name("Приказ")
                .prefix("П-")
                .recordState(RecordState.DELETED)
                .build();

        underTest.saveAll(List.of(type1, type2));

        // when
        Page<DocumentConstructorType> actual = underTest
                .findAllByNameContainingIgnoreCaseAndRecordState(
                        "ика",
                        RecordState.DELETED,
                        PageRequest.of(0, 2));

        // then
        assertThat(actual.getContent().size()).isEqualTo(1);
    }
}