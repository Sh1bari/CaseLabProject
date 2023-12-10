package com.example.caselabproject.services;

import com.example.caselabproject.models.entities.Subscription;

public interface OrganizationService {

    Subscription changeSubscription();

    Integer getBill();

    Integer getEmployees();

}
