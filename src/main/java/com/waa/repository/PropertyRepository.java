package com.waa.repository;

import com.waa.domain.Property;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
