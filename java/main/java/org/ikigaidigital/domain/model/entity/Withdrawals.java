package org.ikigaidigital.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "WITHDRAWALS")
public class Withdrawals {
    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private int id;

    @ManyToOne
    @JoinColumn(name = "TIME_DEPOSIT_ID", nullable = false)
    private TimeDeposits timeDepositId;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "DATE", nullable = false)
    private Date date;
}
