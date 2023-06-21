package com.waa.service;

import com.waa.domain.Address;
import com.waa.domain.Property;
import com.waa.domain.PropertyStatus;
import com.waa.domain.User;
import com.waa.domain.request.PropertyRequest;
import com.waa.repository.PropertyRepository;
import com.waa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PropertyService implements IPropertyService{
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public void add(PropertyRequest propertyRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();

        System.out.println("User email: " + email);
        var userFromDb = userRepository.findByEmail(email).get();
        var address = Address.builder()
                .street(propertyRequest.address().getStreet())
                .city(propertyRequest.address().getCity())
                .state(propertyRequest.address().getState())
                .zipCode(propertyRequest.address().getZipCode())
                .build();

        var property = Property.builder()
                .title(propertyRequest.title())
                .description(propertyRequest.description())
                .price(propertyRequest.price())
                .numberOfBedRooms(propertyRequest.numberOfBedRooms())
                .numberOfBathRooms(propertyRequest.numberOfBathRooms())
                .imageUrl(propertyRequest.imageUrl())
                .postedDate(LocalDate.now())
                .propertyStatus(PropertyStatus.valueOf(propertyRequest.propertyStatus()))
                .user(userFromDb)
                .address(address)
                .build();

        propertyRepository.save(property);
    }
}
