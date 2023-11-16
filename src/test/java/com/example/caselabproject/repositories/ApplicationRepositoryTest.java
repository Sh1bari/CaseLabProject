package com.example.caselabproject.repositories;


import com.example.caselabproject.models.entities.Application;
import com.example.caselabproject.models.enums.ApplicationStatus;
import com.example.caselabproject.models.enums.RecordState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ApplicationRepositoryTest {
    @Autowired
    private ApplicationRepository underTest;

//    @BeforeEach
//    void setUp(@Autowired ApplicationRepository underTest) {
//        this.underTest = underTest;
//    }

    @Test /* Application#findAllByRecordStateAndApplicationStatusAndDeadlineDateBefore */
    void shouldReturnNotEmptyList_whenStateIsActiveAndStatusIsWaiting() {
        // given
        final Application firstApplication = Application.builder()
                .name("something")
                .recordState(RecordState.ACTIVE)
                .applicationStatus(ApplicationStatus.WAITING_FOR_ANSWER)
                .deadlineDate(LocalDateTime.now().minusWeeks(1))
                .build();
        final Application secondApplication = Application.builder()
                .name("something")
                .recordState(RecordState.ACTIVE)
                .applicationStatus(ApplicationStatus.WAITING_FOR_ANSWER)
                .deadlineDate(LocalDateTime.now().minusMinutes(1))
                .build();
        final Application thirdApplication = Application.builder()
                .name("something")
                .recordState(RecordState.DELETED)
                .applicationStatus(ApplicationStatus.WAITING_FOR_ANSWER)
                .deadlineDate(LocalDateTime.now().minusDays(4))
                .build();
        final Application fourthApplication = Application.builder()
                .name("something")
                .recordState(RecordState.DELETED)
                .applicationStatus(ApplicationStatus.WAITING_FOR_ANSWER)
                .deadlineDate(LocalDateTime.now().plusHours(4))
                .build();
        final List<Application> givenApplications = List.of(firstApplication, secondApplication, thirdApplication, fourthApplication);

        underTest.saveAll(givenApplications);

        // when
        List<Application> actualApplications = underTest
                .findAllByRecordStateAndApplicationStatusAndDeadlineDateBefore(
                        RecordState.ACTIVE, ApplicationStatus.WAITING_FOR_ANSWER, LocalDateTime.now());
        // then
        assertThat(actualApplications)
                .hasSize(2)
                .contains(firstApplication, secondApplication);
    }

    @Test /* Application#findAllByRecordStateAndApplicationStatusAndDeadlineDateBefore */
    void shouldReturnEmptyList_whenStateIsDeletedOrDeadlineAfterNow() {
        // given
        final Application firstApplication = Application.builder()
                .name("something")
                .recordState(RecordState.ACTIVE)
                .applicationStatus(ApplicationStatus.WAITING_FOR_ANSWER)
                .deadlineDate(LocalDateTime.now().minusWeeks(1))
                .build();
        final Application secondApplication = Application.builder()
                .name("something")
                .recordState(RecordState.ACTIVE)
                .applicationStatus(ApplicationStatus.WAITING_FOR_ANSWER)
                .deadlineDate(LocalDateTime.now().minusMinutes(1))
                .build();
        final Application thirdApplication = Application.builder()
                .name("something")
                .recordState(RecordState.DELETED)
                .applicationStatus(ApplicationStatus.WAITING_FOR_ANSWER)
                .deadlineDate(LocalDateTime.now().minusDays(4))
                .build();
        final Application fourthApplication = Application.builder()
                .name("something")
                .recordState(RecordState.DELETED)
                .applicationStatus(ApplicationStatus.WAITING_FOR_ANSWER)
                .deadlineDate(LocalDateTime.now().plusHours(4))
                .build();
        final List<Application> givenApplications = List.of(firstApplication, secondApplication, thirdApplication, fourthApplication);

        underTest.saveAll(givenApplications);

        // when
        List<Application> actualApplications = underTest
                .findAllByRecordStateAndApplicationStatusAndDeadlineDateBefore(
                        RecordState.ACTIVE, ApplicationStatus.WAITING_FOR_ANSWER, LocalDateTime.now());

        // then
        assertThat(actualApplications).isEmpty();
//        assertAll(
//                () -> assertThat(actualApplications).hasSize(2),
//                () -> assertThat(actualApplications).contains(firstApplication, secondApplication)
//        );
    }
}
