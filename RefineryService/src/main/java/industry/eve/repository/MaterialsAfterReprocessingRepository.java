package industry.eve.repository;

import industry.eve.model.MaterialsAfterReprocessing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialsAfterReprocessingRepository extends JpaRepository<MaterialsAfterReprocessing, Long> {
}
