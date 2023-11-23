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

    @Test /* Application#findAllByRecordStateAndApplicationStatusAndDeadlineDateBefore */
    void shouldReturnNotEmptyList_whenStateIsActiveAndStatusIsWaiting() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final Application first = getApplication(
                RecordState.ACTIVE, ApplicationStatus.WAITING_FOR_ANSWER, now.minusWeeks(1));
        final Application second = getApplication(
                RecordState.ACTIVE, ApplicationStatus.WAITING_FOR_ANSWER, now.minusMinutes(1));
        final Application third = getApplication(
                RecordState.DELETED, ApplicationStatus.WAITING_FOR_ANSWER, now.minusWeeks(1));
        final Application fourth = getApplication(
                RecordState.DELETED, ApplicationStatus.WAITING_FOR_ANSWER, now.plusHours(4));
        final Application fifth = getApplication(
                RecordState.ACTIVE, ApplicationStatus.DENIED, now.minusWeeks(1));
        final Application sixth = getApplication(
                RecordState.ACTIVE, ApplicationStatus.WAITING_FOR_ANSWER, now.plusMinutes(1));
        final Application seventh = getApplication(
                RecordState.DELETED, ApplicationStatus.WAITING_FOR_ANSWER, now.minusWeeks(1));

        underTest.saveAll(List.of(first, second, third, fourth, fifth, sixth, seventh));

        // when
        final List<Application> actualApplications = underTest
                .findAllByRecordStateAndApplicationStatusAndDeadlineDateBefore(
                        RecordState.ACTIVE, ApplicationStatus.WAITING_FOR_ANSWER, now);

        // then
        assertThat(actualApplications)
                .hasSize(2)
                .contains(first, second);
    }

    private Application getApplication(
            RecordState state, ApplicationStatus status, LocalDateTime deadline) {
        return Application.builder()
                .name("something")
                .recordState(state)
                .applicationStatus(status)
                .deadlineDate(deadline)
                .build();
    }
}
