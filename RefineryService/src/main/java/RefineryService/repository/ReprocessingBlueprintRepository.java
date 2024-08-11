package RefineryService.repository;

import RefineryService.model.RawResources;
import RefineryService.model.ReprocessingBlueprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReprocessingBlueprintRepository extends JpaRepository<ReprocessingBlueprint, Long> {
    @Query("SELECT a from ReprocessingBlueprint a join fetch a.MaterialAfterReprocessingId where a.rawResourceId = ?1")
    Optional<List<ReprocessingBlueprint>> findByRawResourceId(RawResources id);
}
