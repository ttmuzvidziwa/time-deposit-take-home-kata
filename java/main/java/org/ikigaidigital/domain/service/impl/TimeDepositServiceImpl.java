package org.ikigaidigital.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.domain.component.calculator.TimeDepositCalculator;
import org.ikigaidigital.domain.constant.ErrorMessages;
import org.ikigaidigital.domain.constant.Sources;
import org.ikigaidigital.domain.exception.DataConversionException;
import org.ikigaidigital.domain.exception.TimeDepositException;
import org.ikigaidigital.domain.mapper.TimeDepositMapper;
import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.ikigaidigital.domain.model.entity.TimeDeposits;
import org.ikigaidigital.domain.model.response.TimeDepositAccountsResponse;
import org.ikigaidigital.domain.service.TimeDepositService;
import org.ikigaidigital.domain.utils.LoggerUtils;
import org.ikigaidigital.repository.time.deposits.TimeDepositsRepository;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeDepositServiceImpl implements TimeDepositService {
    private final TimeDepositCalculator timeDepositCalculator;
    private final TimeDepositsRepository timeDepositsRepository;
    private final TimeDepositMapper timeDepositsMapper;

    /**
     * Updates all time deposit accounts by recalculating their balances and saving the updates to the repository.
     * <p>
     * This method performs the following steps:
     * <li> 1. Validates the provided traceId to ensure it is not null or empty. </li>
     * <li> 2. Logs the start of the update process. </li>
     * <li> 3. Retrieves all time deposit accounts as DTOs. </li>
     * <li> 4. Updates the balances of the retrieved accounts using the TimeDepositCalculator. </li>
     * <li> 5. Saves the updated accounts back to the repository in batch. </li>
     * <li> 6. Logs the completion of the update process. </li>
     * </p>
     *
     * @param traceId A unique identifier for tracing the request through the system.
     *                Must not be null or empty.
     * @throws IllegalArgumentException If the provided traceId is null or empty.
     */
    @Override
    @Transactional
    public void updateAllTimeDepositAccounts(final String traceId) {
        if (traceId == null || traceId.isEmpty()) {
            LoggerUtils.logError("N/A", Sources.SERVICE_LAYER, "TraceId is null or empty.");
            throw new IllegalArgumentException(ErrorMessages.TRACE_ID_NULL_OR_EMPTY.getMessage());
        }

        LoggerUtils.logInfo(traceId, Sources.SERVICE_LAYER, "Retrieve and update all time deposit accounts.");

        final List<TimeDepositsDto> timeDepositsDtoList;
        try {
            LoggerUtils.logDebug(traceId, Sources.SERVICE_LAYER, "Fetching all time deposit accounts.");
            timeDepositsDtoList = retrieveTimeDepositAccounts(traceId);
        } catch (Exception ex) {
            LoggerUtils.logError(traceId, Sources.SERVICE_LAYER, "Error retrieving time deposit accounts: {}, {}", ex.getClass().getSimpleName(), ex.getMessage());
            throw new TimeDepositException(ErrorMessages.ERROR_RETRIEVING_TIME_DEPOSIT_ACCOUNTS);
        }

        if (!timeDepositsDtoList.isEmpty()) {
            try {
                timeDepositCalculator.updateBalance(timeDepositsDtoList);
                LoggerUtils.logDebug(traceId, Sources.SERVICE_LAYER, "Updating time deposit accounts in the repository.");
            } catch (Exception ex) {
                LoggerUtils.logError(traceId, Sources.SERVICE_LAYER, "Error updating time deposit accounts: {}, {}", ex.getClass().getSimpleName(), ex.getMessage());
                throw new TimeDepositException(ErrorMessages.ERROR_COMPUTING_TIME_DEPOSIT_INTEREST);
            }

            try {
            timeDepositsRepository.batchUpdateTimeDepositAccounts(timeDepositsDtoList);
            LoggerUtils.logDebug(traceId, Sources.SERVICE_LAYER, "All time deposit accounts update completed.");
            } catch (Exception ex) {
                LoggerUtils.logError(traceId, Sources.SERVICE_LAYER, "Error saving updated time deposit accounts: {}, {}", ex.getClass().getSimpleName(), ex.getMessage());
                throw new TimeDepositException(ErrorMessages.ERROR_UPDATING_TIME_DEPOSIT_ACCOUNTS);
            }

            return;
        }

        LoggerUtils.logDebug(traceId, Sources.SERVICE_LAYER, "No time deposit accounts found to update.");
    }

    /**
     * Retrieves all time deposit accounts and returns them in a response object.
     * <p>
     * This method performs the following steps:
     * <li> 1. Validates the provided traceId to ensure it is not null or empty.</li>
     * <li> 2. Logs the start of the retrieval process.</li>
     * <li> 3. Fetches all time deposit accounts as DTOs.</li>
     * <li> 4. Populates the response object with the retrieved accounts and their count.</li>
     * </p>
     *
     * @param traceId A unique identifier for tracing the request through the system.
     *                Must not be null or empty.
     * @return A {@link TimeDepositAccountsResponse} object containing the list of time deposit accounts
     *         and the total count of accounts.
     * @throws IllegalArgumentException If the provided traceId is null or empty.
     */
    @Override
    public TimeDepositAccountsResponse getAllTimeDepositAccounts(final String traceId) {
        if (traceId == null || traceId.isEmpty()) {
            LoggerUtils.logError("N/A", Sources.SERVICE_LAYER, "TraceId is null or empty.");
            throw new IllegalArgumentException(ErrorMessages.TRACE_ID_NULL_OR_EMPTY.getMessage());
        }

        final TimeDepositAccountsResponse response = new TimeDepositAccountsResponse();

        LoggerUtils.logInfo(traceId, Sources.SERVICE_LAYER, "Fetching all time deposit accounts.");

        try {
            final List<TimeDepositsDto> timeDepositsDtoList = retrieveTimeDepositAccounts(traceId);
            response.setAccounts(timeDepositsDtoList);
            response.setCount(timeDepositsDtoList.size());
        } catch (Exception ex) {
            LoggerUtils.logError(traceId, Sources.SERVICE_LAYER, "Error retrieving time deposit accounts: {}, {}", ex.getClass().getSimpleName(), ex.getMessage());
            throw new TimeDepositException(ErrorMessages.ERROR_RETRIEVING_TIME_DEPOSIT_ACCOUNTS);
        }

        return response;
    }

    /**
     * Retrieves all time deposit accounts from the repository and converts them to DTOs.
     *
     * @param traceId A unique identifier for tracing the request through the system.
     * @return A list of {@link TimeDepositsDto} objects representing the time deposit accounts.
     * If no accounts are found, an empty list is returned.
     */
    private List<TimeDepositsDto> retrieveTimeDepositAccounts(final String traceId) {
        final List<TimeDepositsDto> timeDepositsList = new ArrayList<>();

        try {
            final Optional<List<TimeDeposits>> timeDeposits = timeDepositsRepository.findAllTimeDeposits();

            if (timeDeposits.isPresent() && !timeDeposits.get().isEmpty()) {
                for (TimeDeposits timeDeposit : timeDeposits.get()) {
                    timeDepositsList.add(convertTimeDepositEntityToDto(traceId, timeDeposit));
                }
                LoggerUtils.logDebug(traceId, Sources.SERVICE_LAYER, "Successfully fetched {} time deposit accounts.", timeDeposits.get().size());
                return timeDepositsList;
            }

            LoggerUtils.logDebug(traceId, Sources.SERVICE_LAYER, "No time deposit accounts found.");
            return timeDepositsList;
        } catch (Exception ex) {
            LoggerUtils.logError(traceId, Sources.SERVICE_LAYER, "Error retrieving time deposit accounts: {}, {}", ex.getClass().getSimpleName(), ex.getMessage());
            throw new TimeDepositException(ErrorMessages.ERROR_RETRIEVING_TIME_DEPOSIT_ACCOUNTS);
        }
    }

    /**
     * Converts a TimeDeposits entity to a TimeDepositsDto.
     *
     * @param traceId The trace ID for logging purposes.
     * @param timeDeposit The TimeDeposits entity to be converted. Must not be null.
     * @return A TimeDepositsDto object containing the data from the provided entity.
     * @throws DataConversionException If the provided entity or any required field is null.
     */
    private TimeDepositsDto convertTimeDepositEntityToDto(final String traceId, final TimeDeposits timeDeposit) throws DataConversionException {
        if (timeDeposit == null) {
            LoggerUtils.logError(traceId, Sources.SERVICE_LAYER, "Time Deposit entity is null while converting to DTO.");
            throw new DataConversionException(ErrorMessages.TIME_DEPOSIT_ENTITY_NULL);
        }
        if (timeDeposit.getPlanType() == null) {
            throw new DataConversionException(ErrorMessages.TIME_DEPOSIT_PLAN_TYPE_NULL);
        }
        if (timeDeposit.getBalance() == null) {
            LoggerUtils.logError(traceId, Sources.SERVICE_LAYER, "Time Deposit entity balance is null while converting to DTO.");
            throw new DataConversionException(ErrorMessages.TIME_DEPOSIT_BALANCE_NULL);
        }
        // Validation passed, delegate to mapper
        TimeDepositsDto dto = timeDepositsMapper.toDto(timeDeposit);
        LoggerUtils.logDebug(traceId, Sources.SERVICE_LAYER, "Time Deposit entity converted {}.", dto.toString());
        return dto;
    }
}
