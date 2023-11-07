package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.CreateApplicationItemRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationItemTakeResponseDto;
import com.example.caselabproject.models.DTOs.response.CreateApplicationItemResponseDto;
import com.example.caselabproject.services.ApplicationItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Validated
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ApplicationItemController {
    private final ApplicationItemService applicationItemService;
    @Operation(summary = "Send application to departments or users", description = "Secured by authorized users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application sent",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateApplicationItemResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Application, user or department not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "409", description = "Application item already has been sent to department/" +
                    "User or department has status DELETED",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PostMapping("/application/{id}/applicationItem")
    @Secured("ROLE_USER")
    public ResponseEntity<List<CreateApplicationItemResponseDto>> createApplicationItems(@PathVariable(name = "id")Long id,
                                                                                   Principal principal,
                                                                                   @RequestBody List<@Valid CreateApplicationItemRequestDto> applicationItemList){
        List<CreateApplicationItemResponseDto> res = applicationItemService.createApplicationItem(applicationItemList, id, principal.getName());
        return ResponseEntity
                .created(URI.create("/api/application/" + id + "/applicationItem/"))
                .body(res);
    }
    @Operation(summary = "Get application item by id", description = "Secured by authorized users, can be read only by admins, creator and employees in the department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application item by id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationItemGetByIdResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "User don't have enough rights for access to Application item",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "Application, application item or user not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @GetMapping("/application/{applicationId}/applicationItem/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity<ApplicationItemGetByIdResponseDto> getApplicationItemById(@PathVariable(name = "applicationId")Long applicationId,
                                                                                    @PathVariable(name = "id")Long id,
                                                                                    Principal principal){
        ApplicationItemGetByIdResponseDto res = applicationItemService.getApplicationItemById(applicationId, id, principal.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @Operation(summary = "Take application item by user", description = "Secured by authorized users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationItemGetByIdResponseDto.class))}),
            @ApiResponse(responseCode = "403", description = "User don't have enough rights for access to Application item",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "Application or application item not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "409", description = "Application or application is DELETED/application item has been already taken",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})

    @PostMapping("/application/{applicationId}/applicationItem/{id}/take")
    @Secured("ROLE_USER")
    public ResponseEntity<ApplicationItemTakeResponseDto> takeApplicationItem(@PathVariable(name = "applicationId")Long applicationId,
                                                                              @PathVariable(name = "id")Long id,
                                                                              Principal principal) {
        ApplicationItemTakeResponseDto res = applicationItemService.takeApplicationItem(applicationId, id, principal.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }


}
