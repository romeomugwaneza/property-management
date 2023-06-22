package com.waa.controller;

import com.waa.domain.Property;
import lombok.RequiredArgsConstructor;
import com.waa.service.PropertyService;
import org.springframework.http.HttpStatus;
import com.waa.domain.request.PropertyRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.waa.repository.PropertyCriteriaRepository;
import com.waa.domain.request.PropertyCriteriaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/properties")
public class PropertyController {
    @Autowired
    PropertyService propertyService;
    @Autowired
    PropertyCriteriaRepository propertyCriteriaRepository;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody PropertyRequest propertyRequest) {
        propertyService.add(propertyRequest);
    }

    @GetMapping
    List<Property> findAll(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "zip", required = false) String zipCode
    ) {
        if (title != null || description != null || minPrice != null || maxPrice != null || city != null || state != null || zipCode != null) {
            var propertyCriteria = new PropertyCriteriaRequest(title, description, minPrice, maxPrice, city, state, zipCode);
            System.out.println(propertyCriteria);
            return propertyCriteriaRepository.findAllByCriteria(propertyCriteria);
        }
        return propertyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPropertyById(@RequestParam("id") long propertyId) {
        return ResponseEntity.ok().body(propertyService.findById(propertyId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long propertyId) {
        System.out.println("Received request.");
        propertyService.delete(propertyId);
    }

    @PatchMapping("/{id}/{status}")
    public void changeStatus(
            @PathVariable("id") int propertyId,
            @PathVariable("status") String status) {
        propertyService.changeStatus(propertyId, status);

    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") long propertyId, @RequestBody PropertyRequest propertyRequest) {
        propertyService.update(propertyId, propertyRequest);
    }
}
