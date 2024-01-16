package com.example.caselabproject.services;

import com.example.caselabproject.models.BillingDaysAndPrice;
import com.example.caselabproject.models.DTOs.request.organization.OrganizationIdRequestDto;
import jakarta.validation.Valid;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface BillingService {
    Map<Integer, Map<Month, List<BillingDaysAndPrice>>> calculationAllBilling(@Valid OrganizationIdRequestDto request);
}
