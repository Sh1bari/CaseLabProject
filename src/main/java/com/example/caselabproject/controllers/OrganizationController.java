package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.department.DepartmentChildDto;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationSubscriptionChangeRequestDto;
import com.example.caselabproject.models.DTOs.response.department.DepartmentGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationSubscriptionChangeResponseDto;
import com.example.caselabproject.models.DTOs.response.user.UserGetByIdResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DepartmentService;
import com.example.caselabproject.services.OrganizationService;
import com.example.caselabproject.validation.annotations.CheckOrganization;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/organization")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PatchMapping("/lower")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<OrganizationSubscriptionChangeResponseDto> lowerOrganizationSubscription(
            @RequestBody @Valid OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto,
            Principal principal) {

        OrganizationSubscriptionChangeResponseDto responseDto = organizationService.lowerSubscription(subscriptionChangeRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @PatchMapping("/raise")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<OrganizationSubscriptionChangeResponseDto> raiseOrganizationSubscription(
            @RequestBody @Valid OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto,
            Principal principal) {

        OrganizationSubscriptionChangeResponseDto responseDto = organizationService.raiseSubscription(subscriptionChangeRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

}
