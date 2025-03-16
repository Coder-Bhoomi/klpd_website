package com.klpdapp.klpd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name ="Segment")
public class Segment {
    @Id
    @Column ( length = 15 , nullable = false)
    private String segmentId;

    @Column ( length = 60 , nullable = false)
    private String segmentName;

    @Column ( length = 60 , nullable = false)
    private String segmentImage;

    // Getters and Setters
   }
