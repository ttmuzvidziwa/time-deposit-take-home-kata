package org.ikigaidigital.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TIME_DEPOSITS")
public class TimeDeposits {
    @Id
    @Column(name = "ID" , nullable = false, length = 10)
    private int id;

    @Column(name = "PLAN_TYPE", nullable = false, length = 8)
    private String planType;

    @Column(name = "DAYS", nullable = false)
    private int days;

    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;
}
