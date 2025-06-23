package org.ikigaidigital.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ikigaidigital.api.controller.TimeDepositController;
import org.ikigaidigital.api.controller.impl.TimeDepositControllerImpl;
import org.ikigaidigital.domain.exception.TimeDepositException;
import org.ikigaidigital.domain.model.response.TimeDepositAccountsResponse;
import org.ikigaidigital.domain.service.TimeDepositService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TimeDepositControllerImpl.class, TimeDepositController.class})
public class TimeDepositControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TimeDepositService timeDepositService;

    @Test
    @DisplayName("""
            test 'PATCH /update-all-accounts' given successful update of all time deposit accounts
            when the 'updateAllTimeDepositAccounts' method is called
            should return a 200 OK response with a success message
            """)
    public void testUpdateAllTimeDepositAccounts_givenSuccessfulUpdate_shouldReturnOkResponse() throws Exception {
        // Setup expectations
        Mockito.doNothing().when(timeDepositService).updateAllTimeDepositAccounts(anyString());

        // Perform SUT and verify results
        mockMvc.perform(patch("/update-all-accounts"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success!"));
    }

    @Test
    @DisplayName("""
            test 'PATCH /update-all-accounts' given an error during update of all time deposit accounts
            when the 'updateAllTimeDepositAccounts' method receives a TimeDepositException
            should return a 400 Bad Request response with the error message
            """)
    public void testUpdateAllTimeDepositAccounts_givenErrorDuringUpdate_shouldReturnBadRequestResponse() throws Exception {
        // Setup expectations
        Mockito.doThrow(new TimeDepositException("Update failed")).when(timeDepositService).updateAllTimeDepositAccounts(anyString());

        // Perform SUT and verify results
        mockMvc.perform(patch("/update-all-accounts"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Update failed"));
    }

    @Test
    @DisplayName("""
            test 'PATCH /update-all-accounts' given an unexpected error during update of all time deposit accounts
            when the 'updateAllTimeDepositAccounts' method receives a generic exception
            should return a 500 Internal Server Error response with the error message
            """)
    public void testUpdateAllTimeDepositAccounts_givenUnexpectedError_shouldReturnInternalServerErrorResponse() throws Exception {
        // Setup expectations
        Mockito.doThrow(new RuntimeException("Unexpected error")).when(timeDepositService).updateAllTimeDepositAccounts(anyString());

        // Perform SUT and verify results
        mockMvc.perform(patch("/update-all-accounts"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }

    @Test
    @DisplayName("""
            test 'GET /get-all-accounts' given successful retrieval of all time deposit accounts
            when the 'getAllTimeDepositAccounts' method is called
            should return a 200 OK response with a list of time deposit accounts
            """)
    public void testGetAllTimeDepositAccounts_givenSuccessfulRetrieval_shouldReturnOkResponse() throws Exception {
        // Setup expectations
        Mockito.when(timeDepositService.getAllTimeDepositAccounts(anyString()))
                .thenReturn(new TimeDepositAccountsResponse());

        // Perform SUT and verify results
        mockMvc.perform(get("/get-all-accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("""
            test 'GET /get-all-accounts' given an error during retrieval of all time deposit accounts
            when the 'getAllTimeDepositAccounts' method receives a TimeDepositException
            should return a 400 Bad Request response with the error message
            """)
    public void testGetAllTimeDepositAccounts_givenErrorDuringRetrieval_shouldReturnBadRequestResponse() throws Exception {
        // Setup expectations
        Mockito.when(timeDepositService.getAllTimeDepositAccounts(anyString()))
                .thenThrow(new TimeDepositException("Retrieval failed"));

        // Perform SUT and verify results
        mockMvc.perform(get("/get-all-accounts"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Retrieval failed"));
    }

    @Test
    @DisplayName("""
            test 'GET /get-all-accounts' given an unexpected error during retrieval of all time deposit accounts
            when the 'getAllTimeDepositAccounts' method receives a generic exception
            should return a 500 Internal Server Error response with the error message
            """)
    public void testGetAllTimeDepositAccounts_givenUnexpectedError_shouldReturnInternalServerErrorResponse() throws Exception {
        // Setup expectations
        Mockito.when(timeDepositService.getAllTimeDepositAccounts(anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Perform SUT and verify results
        mockMvc.perform(get("/get-all-accounts"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }
}
