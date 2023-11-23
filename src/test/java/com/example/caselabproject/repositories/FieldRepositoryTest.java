package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Field;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FieldRepositoryTest {

    @Autowired
    private FieldRepository underTest;

    @Test
    void findAllByNameIn_returnsValue() {
        // given
        Field field1 = Field.builder()
                .name("ИНН")
                .build();
        Field field2 = Field.builder()
                .name("СНИЛС")
                .build();
        Field field3 = Field.builder()
                .name("Номер паспорта")
                .build();
        underTest.saveAll(List.of(field1, field2, field3));

        // when
        List<Field> actual = underTest.findAllByNameIn(List.of("СНИЛС", "Номер паспорта"));

        // then
        assertThat(actual).hasSize(2);
    }
}