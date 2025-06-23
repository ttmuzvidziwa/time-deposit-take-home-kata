package org.ikigaidigital.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TimeDepositsDto {
    private int id;
    private String planType;
    private BigDecimal balance;
    private int days;
}
