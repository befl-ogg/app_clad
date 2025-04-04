package com.spf.control.feature.studio.domain.repository;

import com.spf.control.feature.studio.domain.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    Optional<Studio> findByStudioCode(String studioCode);
}
