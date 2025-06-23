package org.ikigaidigital.repository.time.deposits;

import org.ikigaidigital.domain.model.entity.TimeDeposits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeDepositsRepository extends JpaRepository<TimeDeposits, Integer>, TimeDepositsRepositoryCustom {
    @Query(value = """
        SELECT td.* 
        FROM TIME_DEPOSITS td
        """, nativeQuery = true)
    Optional<List<TimeDeposits>> findAllTimeDeposits();
}
