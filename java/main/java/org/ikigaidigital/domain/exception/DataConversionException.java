package org.ikigaidigital.domain.exception;

import org.ikigaidigital.domain.constant.ErrorMessages;

/**
 * Exception thrown when a data conversion error occurs in the application.
 * Typically used to indicate issues during type transformation or parsing.
 */
public class DataConversionException extends TimeDepositException {

    /**
     * Constructs a new DataConversionException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public DataConversionException(final ErrorMessages message) {
        super(message != null ? message.getMessage() : "Undefined data conversion error");
    }
}