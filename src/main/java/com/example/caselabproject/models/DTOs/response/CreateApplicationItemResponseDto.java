package com.example.caselabproject.models.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationItemResponseDto {
    private Long id;
}
