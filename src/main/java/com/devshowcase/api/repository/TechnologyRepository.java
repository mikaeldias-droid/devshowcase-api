package com.devshowcase.api.repository;

import com.devshowcase.api.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Technology> findByNameIgnoreCase(String name);
}
