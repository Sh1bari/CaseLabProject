package com.example.caselabproject.controllers;

import com.example.caselabproject.models.BillingDaysAndPrice;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationIdRequestDto;
import com.example.caselabproject.services.BillingService;
import com.example.caselabproject.services.PdfFileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //TODO доделать описание свагера
    @GetMapping("/details")
    @PreAuthorize("@billingSecurityService.canGetBillingDetails(#principal.getUsername, #request.getId)")
    public ResponseEntity<Map<Integer, Map<Month, List<BillingDaysAndPrice>>>> getBillingDetailsByOrganization(
            @Valid @RequestBody OrganizationIdRequestDto request){
        var response = billingService.calculationAllBilling(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
