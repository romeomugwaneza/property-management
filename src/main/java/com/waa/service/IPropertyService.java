package com.waa.service;

import com.waa.domain.Property;
import com.waa.domain.request.PropertyRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPropertyService {
    void add(PropertyRequest propertyRequest);

    List<Property> findAll();

    Property findById(long propertyId);

    void delete(long propertyId);

    void changeStatus(int propertyId, String status) throws IllegalArgumentException;

    void update(long propertyId, PropertyRequest propertyRequest);
}
