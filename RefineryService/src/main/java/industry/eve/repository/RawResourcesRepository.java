package industry.eve.repository;

import industry.eve.model.RawResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RawResourcesRepository extends JpaRepository<RawResources, Long> {
    @Query("SELECT r from RawResources r where r.RawResourceName = ?1")
    Optional<RawResources> findByRawResourceName(String name);
}
