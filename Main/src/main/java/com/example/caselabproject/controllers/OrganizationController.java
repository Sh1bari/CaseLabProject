package com.example.caselabproject.controllers;


import com.example.caselabproject.models.DTOs.request.organization.OrganizationChangeNameRequestDto;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationSubscriptionChangeRequestDto;
import com.example.caselabproject.models.DTOs.response.organization.OrganizationChangeNameResponseDto;
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
@RequestMapping("/organization")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PatchMapping("/changeName")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<OrganizationChangeNameResponseDto> changeOrganizationName(
            @RequestBody @Valid OrganizationChangeNameRequestDto organizationChangeNameRequestDto,
            Principal principal) {

        OrganizationChangeNameResponseDto responseDto = organizationService.changeOrganizationName(organizationChangeNameRequestDto, principal.getName());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }


//
//    @PatchMapping("/changeOrganizationSubscription")
//    @Secured("ROLE_ADMIN")
//    public ResponseEntity<OrganizationSubscriptionChangeResponseDto> changeSubscription(
//            @RequestBody @Valid OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto,
//            Principal principal) {
//
//        OrganizationSubscriptionChangeResponseDto responseDto = organizationService.changeSubscription(subscriptionChangeRequestDto, principal.getName());
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(responseDto);
//    }

//    @PatchMapping("/lower")
//    @Secured("ROLE_ADMIN")
//    public ResponseEntity<OrganizationSubscriptionChangeResponseDto> lowerOrganizationSubscription(
//            @RequestBody @Valid OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto,
//            Principal principal) {
//
//        OrganizationSubscriptionChangeResponseDto responseDto = organizationService.lowerSubscription(subscriptionChangeRequestDto);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(responseDto);
//    }
//
//    @PatchMapping("/raise")
//    @Secured("ROLE_ADMIN")
//    public ResponseEntity<OrganizationSubscriptionChangeResponseDto> raiseOrganizationSubscription(
//            @RequestBody @Valid OrganizationSubscriptionChangeRequestDto subscriptionChangeRequestDto,
//            Principal principal) {
//
//        OrganizationSubscriptionChangeResponseDto responseDto = organizationService.raiseSubscription(subscriptionChangeRequestDto);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(responseDto);
//    }

}
