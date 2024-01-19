package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.BillingDaysAndPrice;
import com.example.caselabproject.models.DTOs.JwtResponse;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationIdRequestDto;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.services.BillingService;
import com.example.caselabproject.services.OrganizationService;
import com.example.caselabproject.services.PdfFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.List;
import java.util.Map;

@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
@SecurityRequirement(name = "bearerAuth")
public class BillingController {
    private final PdfFileService pdfFileService;
    private final BillingService billingService;
    private final OrganizationService organizationService;

    @Operation(summary = "Receiving billing details in PDF file secured by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Resource.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Organization by id or user by username nor found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "409", description = "User is not Admin",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @SecurityRequirement(name = "bearerAuth")
    @Secured("ROLE_ADMIN")
    @GetMapping("/details")
    @PreAuthorize("@billingSecurityService.canGetBillingDetails(#principal.getUsername, #request.getId)")
    public ResponseEntity<Map<Integer, Map<Month, List<BillingDaysAndPrice>>>> getBillingDetailsByOrganization(
            @Valid @RequestBody OrganizationIdRequestDto request){
        var response = billingService.calculationAllBilling(request);
        var organization = organizationService.findOrganizationNameById(request);
        var resource = pdfFileService.generatePdfBillingDetailsFile(organization, response);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() + "\"")
                .body(response);
    }
}
