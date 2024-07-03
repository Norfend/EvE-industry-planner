package parsingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parsingservice.model.MaterialsAfterReprocessing;

@Repository
public interface MaterialsAfterReprocessingRepository extends JpaRepository<MaterialsAfterReprocessing, Long> {}
