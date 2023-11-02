package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeRecoverResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeUpdateResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentConstructorTypeService {
    /**
     * Creates a new document type, that has unique identifier and name.
     *
     * @see com.example.caselabproject.models.entities.DocumentConstructorType
     */
    @Transactional
    DocumentConstructorTypeCreateResponseDto create(@Valid DocumentConstructorTypeRequestDto typeRequestDto);

    /**
     * Updates an existing document type (found by <code>id</code>) by changing its name and prefix.
     *
     * @see com.example.caselabproject.models.entities.DocumentConstructorType
     */
    @Transactional
    DocumentConstructorTypeUpdateResponseDto updateById(@Min(value = 1L, message = "id can't be less than 1") Long id,
                                                        @Valid DocumentConstructorTypePatchRequestDto typeRequestDto);

    /**
     * Deletes an existing document type (found by <code>id</code>)
     * by changing its state to {@link com.example.caselabproject.models.enums.RecordState#DELETED}.
     *
     * @see com.example.caselabproject.models.entities.DocumentConstructorType
     */
    @Transactional
    void deleteById(@Min(value = 1L, message = "id can't be less than 1") Long id);

    /**
     * Recovers a deleted document type (found by <code>id</code>)
     * by changing its state to {@link com.example.caselabproject.models.enums.RecordState#ACTIVE}.
     *
     * @see com.example.caselabproject.models.entities.DocumentConstructorType
     */
    @Transactional
    DocumentConstructorTypeRecoverResponseDto recoverById(@Min(value = 1L, message = "id can't be less than 1") Long id);

    /**
     * Finds an existing document type by <code>id</code>.
     *
     * @see com.example.caselabproject.models.entities.DocumentConstructorType
     */
    @Transactional
    DocumentConstructorTypeByIdResponseDto getById(@Min(value = 1L, message = "id can't be less than 1") Long id);

    /**
     * Finds a {@link List} of existing document types
     * by checking their <code>name</code> and {@link RecordState}.
     *
     * @see com.example.caselabproject.models.entities.DocumentConstructorType
     */
    @Transactional
    List<DocumentConstructorTypeByIdResponseDto> getAllContaining(
            @NotNull(message = "name must not be null and must contain at least one non-whitespace character.") String name,
            @NotNull(message = "state must not be null.") RecordState state,
            @Min(value = 0L, message = "page can't be less than 0") Integer page,
            @Min(value = 1L, message = "size can't be less than 1") Integer size);
}
