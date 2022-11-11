package com.capco.featureflagservice.data.repository;

import com.capco.featureflagservice.data.model.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    List<FeatureFlag> findAllByGlobal(boolean global);
}
