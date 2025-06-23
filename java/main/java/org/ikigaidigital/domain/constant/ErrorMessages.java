package org.ikigaidigital.domain.constant;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    TRACE_ID_NULL_OR_EMPTY("TraceId must not be null or empty"),

    TIME_DEPOSIT_ENTITY_NULL("Time Deposit entity is null"),
    TIME_DEPOSIT_PLAN_TYPE_NULL("Time Deposit entity planType is null"),
    TIME_DEPOSIT_BALANCE_NULL("Time Deposit entity balance is null"),

    ERROR_RETRIEVING_TIME_DEPOSIT_ACCOUNTS("Error retrieving time deposit accounts"),
    ERROR_COMPUTING_TIME_DEPOSIT_INTEREST("Error computing time deposit interest and or balances"),
    ERROR_UPDATING_TIME_DEPOSIT_ACCOUNTS("Error updating time deposit accounts in repository");

    private final String message;

    ErrorMessages(final String message) {
        this.message = message;
    }
}
