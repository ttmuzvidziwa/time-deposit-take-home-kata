package org.ikigaidigital.domain.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ikigaidigital.domain.model.dto.TimeDepositsDto;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class TimeDepositAccountsResponse {
    private int count;
    private List<TimeDepositsDto> accounts = List.of();
}
