package com.capco.featureflagservice.data.repository;

import com.capco.featureflagservice.data.model.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    List<FeatureFlag> findAllByGlobal(boolean global);

    Optional<FeatureFlag> findByName(String name);
}
