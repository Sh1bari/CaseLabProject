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


    private Float total;

    @Basic
    private LocalDateTime date;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ElementCollection
    @CollectionTable(name = "bill_details", joinColumns = @JoinColumn(name = "bill_id"))
    @MapKeyJoinColumn(name = "subscription_id")
    @Column(name = "days")
    @ToString.Exclude
    private Map<Subscription, Integer> details = new HashMap<>();

}
