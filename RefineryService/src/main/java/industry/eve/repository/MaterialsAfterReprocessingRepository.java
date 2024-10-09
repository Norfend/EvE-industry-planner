package industry.eve.repository;

import industry.eve.model.MaterialsAfterReprocessing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialsAfterReprocessingRepository extends JpaRepository<MaterialsAfterReprocessing, Long> {
    @Query("SELECT m from MaterialsAfterReprocessing m where m.MaterialsAfterReprocessingName = ?1")
    Optional<MaterialsAfterReprocessing> findByMaterialsAfterReprocessingName(String name);
}
