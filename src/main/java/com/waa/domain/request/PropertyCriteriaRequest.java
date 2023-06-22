package com.waa.domain.request;

public record PropertyCriteriaRequest(
        String title,
        String description,
        Integer minPrice,
        Integer maxPrice,
        String city,
        String state,
        String zip)
{}
