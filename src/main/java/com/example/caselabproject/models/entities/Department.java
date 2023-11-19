package com.example.caselabproject.models.entities;

import com.example.caselabproject.models.enums.RecordState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "parentDepartment", orphanRemoval = true)
    private List<Department> childDepartments;

    private String name;

    @Column(unique = true)
    private String serialKey;

    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<User> users;

    @OneToMany(mappedBy = "toDepartment", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<ApplicationItem> applicationItems;

    @Enumerated(EnumType.STRING)
    private RecordState recordState;

    @ManyToOne
    @JoinColumn(name = "parent_department_id")
    private Department parentDepartment;


    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

}
