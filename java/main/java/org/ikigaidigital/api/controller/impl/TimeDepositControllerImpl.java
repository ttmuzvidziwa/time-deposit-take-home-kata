package org.ikigaidigital.api.controller.impl;

import lombok.RequiredArgsConstructor;
import org.ikigaidigital.api.constant.UriConstants;
import org.ikigaidigital.api.controller.TimeDepositController;
import org.ikigaidigital.domain.constant.Sources;
import org.ikigaidigital.domain.exception.TimeDepositException;
import org.ikigaidigital.domain.model.response.TimeDepositAccountsResponse;
import org.ikigaidigital.domain.service.TimeDepositService;
import org.ikigaidigital.domain.utils.GeneralUtils;
import org.ikigaidigital.domain.utils.LoggerUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TimeDepositControllerImpl implements TimeDepositController {
    private final TimeDepositService timeDepositService;

    @Override
    @PatchMapping(UriConstants.PATCH_UPDATE_ALL_TIME_DEPOSIT_ACCOUNTS_URL)
    public ResponseEntity<String> updateAllTimeDepositAccounts() {
        final String traceId = GeneralUtils.generateTraceId();
        try {
            timeDepositService.updateAllTimeDepositAccounts(traceId);
            return new ResponseEntity<>("Success!", HttpStatus.OK);
        } catch (TimeDepositException tDEx) {
            LoggerUtils.logError(traceId, Sources.CONTROLLER_LAYER, "{} error updating time deposit accounts: {}", tDEx.getClass().getSimpleName(), tDEx.getMessage());
            return new ResponseEntity<>(tDEx.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            LoggerUtils.logError(traceId, Sources.CONTROLLER_LAYER, "Unexpected error updating time deposit accounts: {}", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @GetMapping (UriConstants.GET_ALL_TIME_DEPOSIT_ACCOUNTS_URL)
    public ResponseEntity<?> getAllTimeDepositAccounts() {
        final String traceId = GeneralUtils.generateTraceId();
        try {
            LoggerUtils.logDebug(traceId, Sources.CONTROLLER_LAYER, "Fetching all time deposit accounts.");
            final TimeDepositAccountsResponse response = timeDepositService.getAllTimeDepositAccounts(traceId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (TimeDepositException tDEx) {
            LoggerUtils.logError(traceId, Sources.CONTROLLER_LAYER, "{} error fetching time deposit accounts: {}", tDEx.getClass().getSimpleName(), tDEx.getMessage());
            return new ResponseEntity<>(tDEx.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception eX) {
            LoggerUtils.logError(traceId, Sources.CONTROLLER_LAYER, "Unexpected error fetching time deposit accounts: {}", eX.getMessage());
            return new ResponseEntity<>(eX.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
