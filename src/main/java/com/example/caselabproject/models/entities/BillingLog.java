package com.example.caselabproject.models.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "billing_log")
public class BillingLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id")
    private Subscription currentSubscription;
    
    // @Basic
    private LocalDateTime subscriptionStart;
    
    @ManyToOne
    @JoinColumn(name = "id")
    private Organization organizationId;
    
    @ManyToOne
    @JoinColumn(name = "id")
    private Subscription lastSubscription;
}
