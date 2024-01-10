package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.organization.OrganizationSubscriptionChangeRequestDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationSubscriptionChangeResponseDto;
import com.example.caselabproject.services.OrganizationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/subscription")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final OrganizationService organizationService;

    @PatchMapping("/changeOrganizationSubscription")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<OrganizationSubscriptionChangeResponseDto> changeSubscription(
            @RequestBody @Valid OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto,
            Principal principal) {

        OrganizationSubscriptionChangeResponseDto responseDto = organizationService.changeSubscription(subscriptionChangeRequestDto, principal.getName());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
