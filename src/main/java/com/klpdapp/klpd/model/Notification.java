package com.klpdapp.klpd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int NotificationId;

    @Column(length = 100)
    private String NotificationTitle;

    @Column(length = 250)
    private String Message;

}
