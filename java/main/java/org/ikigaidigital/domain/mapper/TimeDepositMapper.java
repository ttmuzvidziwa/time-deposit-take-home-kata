package org.ikigaidigital.domain.mapper;

import lombok.RequiredArgsConstructor;
import org.ikigaidigital.domain.exception.DataConversionException;
import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.ikigaidigital.domain.model.entity.TimeDeposits;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class TimeDepositMapper {
    /**
     * Converts a TimeDeposits entity to a TimeDepositsDto.
     *
     * @param timeDeposits The  {@link TimeDeposits} entity to be converted. Must not be null.
     * @return A {@link TimeDepositsDto} object containing the data from the provided entity.
     * @throws DataConversionException If the provided entity or any required field is null.
     */
    public TimeDepositsDto toDto(final TimeDeposits timeDeposits) {
        if (timeDeposits == null) {
            return null;
        }

        TimeDepositsDto dto = new TimeDepositsDto();
        dto.setId(timeDeposits.getId());
        dto.setPlanType(timeDeposits.getPlanType());
        if (timeDeposits.getBalance() != null) {
            dto.setBalance(timeDeposits.getBalance().setScale(2, RoundingMode.HALF_UP));
        }
        dto.setDays(timeDeposits.getDays());

        return dto;
    }
}