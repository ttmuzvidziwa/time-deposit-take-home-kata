package org.ikigaidigital.repository.time.deposits;

import org.ikigaidigital.domain.model.dto.TimeDepositsDto;

import java.util.List;

public interface TimeDepositsRepositoryCustom {
    void batchUpdateTimeDepositAccounts(final List<TimeDepositsDto> timeDepositsDtoList);
}