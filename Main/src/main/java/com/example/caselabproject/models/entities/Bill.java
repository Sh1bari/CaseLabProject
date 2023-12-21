package com.example.caselabproject.models.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    @JoinColumn(name = "organization_id")
    private Organization organization;
    
    @Basic
    private LocalDateTime date;
    
    private Float total;
    
    @ElementCollection
    @CollectionTable(name = "bill_details", joinColumns = @JoinColumn(name = "bill_id"))
    @MapKeyJoinColumn(name = "subscription_id")
    @Column(name = "days")
    @ToString.Exclude
    private Map<Subscription, Integer> details = new HashMap<>();
}
