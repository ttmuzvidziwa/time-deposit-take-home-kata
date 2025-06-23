package org.ikigaidigital.domain.exception;

import org.ikigaidigital.domain.constant.ErrorMessages;

/**
 * Base custom exception for the solution.
 * All other custom exceptions should extend this class.
 */
public class TimeDepositException extends RuntimeException {
    public TimeDepositException(final String message) {
        super(message);
    }

    public TimeDepositException(final ErrorMessages message) {
        super(message != null ? message.getMessage() : "Undefined time deposit error");
    }
}
