package com.waa.domain;

import lombok.Data;
import lombok.Builder;
import java.util.List;
import java.time.LocalDate;
import jakarta.persistence.*;
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
    private String numberOfBedRooms;
    private String numberOfBathRooms;
    private String imageUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate postedDate;
    @Enumerated(EnumType.STRING)
    private PropertyStatus propertyStatus;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Offer> offers;
    @ManyToOne
    private User user;

}
