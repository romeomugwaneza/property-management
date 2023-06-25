package com.waa.domain;

import lombok.Data;
import lombok.Builder;

import java.util.List;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "properties_table")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private double price;
    private int numberOfBedRooms;
    private int numberOfBathRooms;
    private String imageUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime postedDate;
    @Enumerated(EnumType.STRING)
    private PropertyStatus propertyStatus;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Offer> offers;
    @ManyToOne
    private User owner;

}
