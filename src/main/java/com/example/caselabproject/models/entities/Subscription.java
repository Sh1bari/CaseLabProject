package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.SubscriptionName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    //String
    private SubscriptionName subscriptionName;

    private String description;

    private Integer amountOfPeople;

    private Double cost;
}
