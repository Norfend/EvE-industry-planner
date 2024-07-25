package SettingsService.repository;

import SettingsService.model.RawResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawResourcesRepository extends JpaRepository<RawResources, Long> {
}
