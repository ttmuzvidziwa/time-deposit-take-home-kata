package org.ikigaidigital.domain.service;

import org.ikigaidigital.domain.model.response.TimeDepositAccountsResponse;

public interface TimeDepositService {
    void updateAllTimeDepositAccounts(final String traceId);
    TimeDepositAccountsResponse getAllTimeDepositAccounts(final String traceId);
}
