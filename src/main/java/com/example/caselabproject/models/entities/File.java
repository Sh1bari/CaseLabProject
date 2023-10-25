package com.example.caselabproject.models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    private Double size;
    private String path;
    private String type;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "document_id")
    private Document document;

}
