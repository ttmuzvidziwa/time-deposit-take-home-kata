package org.ikigaidigital.repository.time.deposits;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.RoundingMode;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TimeDepositsRepositoryImpl implements TimeDepositsRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Modifying
    @Transactional
    public void batchUpdateTimeDepositAccounts(List<TimeDepositsDto> timeDepositsDtoList) {
        final String sql = "UPDATE time_deposits SET plan_type = ?, balance = ?, days = ? WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, timeDepositsDtoList, timeDepositsDtoList.size(),
                (ps, dto) -> {
                    ps.setString(1, dto.getPlanType());
                    ps.setBigDecimal(2, dto.getBalance().setScale(2, RoundingMode.HALF_UP));
                    ps.setInt(3, dto.getDays());
                    ps.setLong(4, dto.getId());
                });
    }
}