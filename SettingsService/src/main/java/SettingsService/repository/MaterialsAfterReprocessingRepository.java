package SettingsService.repository;

import SettingsService.model.MaterialsAfterReprocessing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialsAfterReprocessingRepository extends JpaRepository<MaterialsAfterReprocessing, Long> {
}
