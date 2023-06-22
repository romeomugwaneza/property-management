package com.waa.service;

import com.waa.domain.Address;
import com.waa.domain.Property;
import com.waa.domain.PropertyStatus;
import lombok.RequiredArgsConstructor;
import com.waa.repository.UserRepository;
import com.waa.repository.PropertyRepository;
import com.waa.domain.request.PropertyRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    @Override
    public Property findById(long propertyId) {
        return propertyRepository.findById(propertyId).orElseThrow();
    }

    @Override
    public void delete(long propertyId) {
        propertyRepository.deleteById(propertyId);
    }

    @Override
    public void changeStatus(int propertyId, String status) throws IllegalArgumentException {
        try {
            PropertyStatus.valueOf(status);
            var property = findById(propertyId);
            property.setPropertyStatus(PropertyStatus.valueOf(status));
            propertyRepository.save(property);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Status not permitted.");
        }
    }

    @Override
    public void update(long propertyId, PropertyRequest propertyRequest) {
        var property = findById(propertyId);
        updateProperty(property, propertyRequest);
        propertyRepository.save(property);
    }

    private void updateProperty(Property property, PropertyRequest propertyRequest) {
        property.setDescription(propertyRequest.description());
        property.setPrice(propertyRequest.price());
        property.setNumberOfBathRooms(propertyRequest.numberOfBathRooms());
        property.setNumberOfBedRooms(propertyRequest.numberOfBedRooms());
        property.setTitle(propertyRequest.title());
        if (propertyRequest.address()!=null)
            property.setAddress(propertyRequest.address());
    }
}
