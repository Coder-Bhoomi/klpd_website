package com.klpdapp.klpd.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name ="Segment")
public class Segment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int segmentId;

    @Column ( length = 60 , nullable = false)
    private String segmentName;

    @Column ( length = 60 , nullable = false)
    private String segmentImage;

    @OneToMany(mappedBy = "segment", cascade = CascadeType.ALL)
    private List<Category> category;
    // Getters and Setters
   }
