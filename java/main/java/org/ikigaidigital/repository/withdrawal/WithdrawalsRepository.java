package org.ikigaidigital.repository.withdrawal;

import org.ikigaidigital.domain.model.entity.Withdrawals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalsRepository extends JpaRepository<Withdrawals, Integer> {
    // Custom query methods can be defined here if needed
}
