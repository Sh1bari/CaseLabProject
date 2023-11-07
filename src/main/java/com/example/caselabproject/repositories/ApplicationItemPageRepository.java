package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicationItemPageRepository extends PagingAndSortingRepository<ApplicationItem, Long> {
    Page<ApplicationItem> findAllByToUser_idAndRecordStateAndApplication_NameContainsIgnoreCase(Long toUserId, RecordState recordState, String applicationName, Pageable pageable);
    Page<ApplicationItem> findAllByToDepartment_idAndRecordStateAndApplication_NameContainsIgnoreCase(Long toUserId, RecordState recordState, String applicationName, Pageable pageable);

}
