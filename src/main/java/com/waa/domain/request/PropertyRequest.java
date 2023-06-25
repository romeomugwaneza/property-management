package com.waa.domain.request;

import com.waa.domain.Address;

public record PropertyRequest(String title,
                              String description,
                              double price,
                              int numberOfBedRooms,
                              int numberOfBathRooms,
                              String imageUrl,
                              String propertyStatus,
                              Address address){}
