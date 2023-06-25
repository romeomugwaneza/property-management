package com.waa.service;

import java.util.List;
import com.waa.domain.Property;
import com.waa.domain.request.PropertyRequest;


public interface IPropertyService {
    void add(PropertyRequest propertyRequest);

    List<Property> findAll();

    Property findById(long propertyId);

    void delete(long propertyId);

    void changeStatus(int propertyId, String status) throws IllegalArgumentException;

    void update(long propertyId, PropertyRequest propertyRequest);
}
