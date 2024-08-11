package SettingsService.repository;

import SettingsService.model.ReprocessingBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReprocessingBlueprintRepository extends JpaRepository<ReprocessingBlueprint, Long> {
}
