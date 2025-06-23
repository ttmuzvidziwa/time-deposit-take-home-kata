package org.ikigaidigital.domain.service;

import org.assertj.core.api.Assertions;
import org.ikigaidigital.domain.component.calculator.TimeDepositCalculator;
import org.ikigaidigital.domain.exception.TimeDepositException;
import org.ikigaidigital.domain.mapper.TimeDepositMapper;
import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.ikigaidigital.domain.model.entity.TimeDeposits;
import org.ikigaidigital.domain.model.response.TimeDepositAccountsResponse;
import org.ikigaidigital.repository.time.deposits.TimeDepositsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
public class TimeDepositServiceTests {
    private static final String TRACE_ID = "test-trace-id";

    @Autowired
    private TimeDepositService timeDepositService;
    @Autowired
    private TimeDepositMapper timeDepositMapper;

    @MockBean
    private TimeDepositCalculator timeDepositCalculator;
    @MockBean
    private TimeDepositsRepository timeDepositsRepository;

    @Test
    @DisplayName("""
            test 'UPDATE ALL TIME DEPOSIT ACCOUNTS' given deposit accounts exist,
            when updateAllTimeDepositAccounts is called,
            then it should update all accounts balances accordingly.
            """)
    public void testUpdateAllTimeDepositAccounts_givenDepositAccountsExists_thenShouldUpdateAllAccountsBalances() {
        // Setup expectations
        final TimeDeposits timeDeposits = new TimeDeposits();
        timeDeposits.setId(1);
        timeDeposits.setPlanType("basic");
        timeDeposits.setBalance(BigDecimal.valueOf(150.00).setScale(2, RoundingMode.HALF_UP));
        timeDeposits.setDays(15);

        final TimeDepositsDto timeDepositsDto = new TimeDepositsDto();
        timeDepositsDto.setId(timeDeposits.getId());
        timeDepositsDto.setPlanType(timeDeposits.getPlanType());
        timeDepositsDto.setBalance(timeDeposits.getBalance());
        timeDepositsDto.setDays(timeDeposits.getDays());

        final List<TimeDepositsDto> timeDepositsDtoList = List.of(timeDepositsDto);

        final List<TimeDeposits> timeDepositsList = List.of(timeDeposits);

        final Optional<List<TimeDeposits>> optionalTimeDeposits = Optional.of(timeDepositsList);

        Mockito.when(timeDepositsRepository.findAllTimeDeposits()).thenReturn(optionalTimeDeposits);
        Mockito.doNothing().when(timeDepositCalculator).updateBalance(timeDepositsDtoList);
        Mockito.doNothing().when(timeDepositsRepository).batchUpdateTimeDepositAccounts(timeDepositsDtoList);

        // Perform SUT
        timeDepositService.updateAllTimeDepositAccounts(TRACE_ID);

        // Verify results
        Mockito.verify(timeDepositsRepository, times(1)).findAllTimeDeposits();
        Mockito.verify(timeDepositCalculator, times(1)).updateBalance(timeDepositsDtoList);
        Mockito.verify(timeDepositsRepository, times(1)).batchUpdateTimeDepositAccounts(timeDepositsDtoList);
    }

    @Test
    @DisplayName("""
            test 'UPDATE ALL TIME DEPOSIT ACCOUNTS' given no deposit accounts exist,
            when updateAllTimeDepositAccounts is called,
            then it should not update any account(s) balances.
            """)
    public void testUpdateAllTimeDepositAccounts_givenNoDepositAccountsExists_thenShouldUpdateAllAccountsBalances() {
        // Setup expectations
        final Optional<List<TimeDeposits>> optionalTimeDeposits = Optional.of(List.of());

        Mockito.when(timeDepositsRepository.findAllTimeDeposits()).thenReturn(optionalTimeDeposits);

        // Perform SUT
        timeDepositService.updateAllTimeDepositAccounts(TRACE_ID);

        // Verify results
        Mockito.verify(timeDepositsRepository, times(1)).findAllTimeDeposits();
        Mockito.verify(timeDepositCalculator, times(0)).updateBalance(any());
        Mockito.verify(timeDepositsRepository, times(0)).batchUpdateTimeDepositAccounts(any());
    }

    @Test
    @DisplayName("""
            test 'UPDATE ALL TIME DEPOSIT ACCOUNTS' given an exception occurs while retrieving accounts,
            when updateAllTimeDepositAccounts is called,
            then it should throw a TimeDepositException.
            """)
    public void testUpdateAllTimeDepositAccounts_givenExceptionOccursWhileRetrievingAccounts_thenShouldThrowTimeDepositException() {
        // Setup expectations
        final String expectedErrorMessage = "Error retrieving time deposit accounts";

        Mockito.when(timeDepositsRepository.findAllTimeDeposits()).thenThrow(new RuntimeException("Database error"));

        // Perform SUT and verify exception
        Assertions.assertThatThrownBy(() -> timeDepositService.updateAllTimeDepositAccounts(TRACE_ID))
                .isInstanceOf(TimeDepositException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("""
            test 'UPDATE ALL TIME DEPOSIT ACCOUNTS' given an exception occurs during interest calculation,
            when updateAllTimeDepositAccounts is called,
            then it should throw a TimeDepositException.
            """)
    public void testUpdateAllTimeDepositAccounts_givenExceptionOccursDuringInterestCalculation_thenShouldThrowTimeDepositException() {
        // Setup expectations
        final String expectedErrorMessage = "Error computing time deposit interest and or balances";
        
        final TimeDeposits timeDeposits = new TimeDeposits();
        timeDeposits.setId(1);
        timeDeposits.setPlanType("basic");
        timeDeposits.setBalance(BigDecimal.valueOf(150.00).setScale(2, RoundingMode.HALF_UP));
        timeDeposits.setDays(15);

        final TimeDepositsDto timeDepositsDto = new TimeDepositsDto();
        timeDepositsDto.setId(timeDeposits.getId());
        timeDepositsDto.setPlanType(timeDeposits.getPlanType());
        timeDepositsDto.setBalance(timeDeposits.getBalance());
        timeDepositsDto.setDays(timeDeposits.getDays());

        final List<TimeDepositsDto> timeDepositsDtoList = List.of(timeDepositsDto);

        Mockito.when(timeDepositsRepository.findAllTimeDeposits()).thenReturn(Optional.of(List.of(timeDeposits)));
        Mockito.doThrow(new RuntimeException("Interest calculation error")).when(timeDepositCalculator).updateBalance(timeDepositsDtoList);

        // Perform SUT and verify exception
        Assertions.assertThatThrownBy(() -> timeDepositService.updateAllTimeDepositAccounts(TRACE_ID))
                .isInstanceOf(TimeDepositException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("""
            test 'UPDATE ALL TIME DEPOSIT ACCOUNTS' given an exception occurs while updating accounts,
            when updateAllTimeDepositAccounts is called,
            then it should throw a TimeDepositException.
            """)
    public void testUpdateAllTimeDepositAccounts_givenExceptionOccursWhileUpdatingAccounts_thenShouldThrowTimeDepositException() {
        // Setup expectations
        final String expectedErrorMessage = "Error updating time deposit accounts in repository";

        final TimeDeposits timeDeposits = new TimeDeposits();
        timeDeposits.setId(1);
        timeDeposits.setPlanType("basic");
        timeDeposits.setBalance(BigDecimal.valueOf(150.00).setScale(2, RoundingMode.HALF_UP));
        timeDeposits.setDays(15);

        final TimeDepositsDto timeDepositsDto = new TimeDepositsDto();
        timeDepositsDto.setId(timeDeposits.getId());
        timeDepositsDto.setPlanType(timeDeposits.getPlanType());
        timeDepositsDto.setBalance(timeDeposits.getBalance());
        timeDepositsDto.setDays(timeDeposits.getDays());

        final List<TimeDepositsDto> timeDepositsDtoList = List.of(timeDepositsDto);

        Mockito.when(timeDepositsRepository.findAllTimeDeposits()).thenReturn(Optional.of(List.of(timeDeposits)));
        Mockito.doNothing().when(timeDepositCalculator).updateBalance(timeDepositsDtoList);
        Mockito.doThrow(new RuntimeException("Database update error")).when(timeDepositsRepository).batchUpdateTimeDepositAccounts(timeDepositsDtoList);

        // Perform SUT and verify exception
        Assertions.assertThatThrownBy(() -> timeDepositService.updateAllTimeDepositAccounts(TRACE_ID))
                .isInstanceOf(TimeDepositException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("""
            test 'UPDATE ALL TIME DEPOSIT ACCOUNTS' given null trace ID,
            when updateAllTimeDepositAccounts is called,
            then it should throw a TimeDepositException.
            """)
    public void testUpdateAllTimeDepositAccounts_givenNullTraceId_thenShouldThrowIllegalArgumentException() {
        // Setup expectations
        final String expectedErrorMessage = "TraceId must not be null or empty";

        // Perform SUT and verify exception
        Assertions.assertThatThrownBy(() -> timeDepositService.updateAllTimeDepositAccounts(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("""
            test 'UPDATE ALL TIME DEPOSIT ACCOUNTS' given empty trace ID,
            when updateAllTimeDepositAccounts is called,
            then it should throw a TimeDepositException.
            """)
    public void testUpdateAllTimeDepositAccounts_givenEmptyTraceId_thenShouldThrowIllegalArgumentException() {
        // Setup expectations
        final String expectedErrorMessage = "TraceId must not be null or empty";

        // Perform SUT and verify exception
        Assertions.assertThatThrownBy(() -> timeDepositService.updateAllTimeDepositAccounts(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("""
            test 'GET ALL TIME DEPOSIT ACCOUNTS' given time deposit accounts exist,
            when getAllTimeDepositAccounts is called,
            then it should return a list of time deposit accounts.
            """)
    public void testGetAllTimeDepositAccounts_givenTimeDepositAccountsExist_thenShouldReturnListOfTimeDepositAccounts() {
        // Setup expectations
        final TimeDeposits timeDeposits = new TimeDeposits();
        timeDeposits.setId(1);
        timeDeposits.setPlanType("basic");
        timeDeposits.setBalance(BigDecimal.valueOf(150.00).setScale(2, RoundingMode.HALF_UP));
        timeDeposits.setDays(15);

        final TimeDepositsDto timeDepositsDto = new TimeDepositsDto();
        timeDepositsDto.setId(timeDeposits.getId());
        timeDepositsDto.setPlanType(timeDeposits.getPlanType());
        timeDepositsDto.setBalance(timeDeposits.getBalance());
        timeDepositsDto.setDays(timeDeposits.getDays());

        final List<TimeDepositsDto> expectedTimeDepositList = List.of(timeDepositsDto);

        final TimeDepositAccountsResponse expectedResponse = new TimeDepositAccountsResponse();
        expectedResponse.setAccounts(expectedTimeDepositList);
        expectedResponse.setCount(1);

        final Optional<List<TimeDeposits>> optionalTimeDeposits = Optional.of(List.of(timeDeposits));

        Mockito.when(timeDepositsRepository.findAllTimeDeposits()).thenReturn(optionalTimeDeposits);

        // Perform SUT
        final TimeDepositAccountsResponse actualResponse = timeDepositService.getAllTimeDepositAccounts(TRACE_ID);

        // Verify results
        Assertions.assertThat(expectedResponse).isEqualTo(actualResponse);
        Mockito.verify(timeDepositsRepository, times(1)).findAllTimeDeposits();
    }

    @Test
    @DisplayName("""
            test 'GET ALL TIME DEPOSIT ACCOUNTS' given no time deposit accounts exist,
            when getAllTimeDepositAccounts is called,
            then it should return an empty list of time deposit accounts.
            """)
    public void testGetAllTimeDepositAccounts_givenNoTimeDepositAccountsExist_thenShouldReturnEmptyListOfTimeDepositAccounts() {
        // Setup expectations
        final TimeDepositAccountsResponse expectedResponse = new TimeDepositAccountsResponse();
        expectedResponse.setAccounts(List.of());
        expectedResponse.setCount(0);

        Mockito.when(timeDepositsRepository.findAllTimeDeposits()).thenReturn(Optional.of(List.of()));

        // Perform SUT
        final TimeDepositAccountsResponse actualResponse = timeDepositService.getAllTimeDepositAccounts(TRACE_ID);

        // Verify results
        Assertions.assertThat(expectedResponse).isEqualTo(actualResponse);
        Mockito.verify(timeDepositsRepository, times(1)).findAllTimeDeposits();
    }

    @Test
    @DisplayName("""
            test 'GET ALL TIME DEPOSIT ACCOUNTS' given an exception occurs while retrieving accounts,
            when getAllTimeDepositAccounts is called,
            then it should throw a TimeDepositException.
            """)
    public void testGetAllTimeDepositAccounts_givenExceptionOccursWhileRetrievingAccounts_thenShouldThrowTimeDepositException() {
        // Setup expectations
        final String expectedErrorMessage = "Error retrieving time deposit accounts";

        Mockito.when(timeDepositsRepository.findAllTimeDeposits()).thenThrow(new RuntimeException("Database error"));

        // Perform SUT and verify exception
        Assertions.assertThatThrownBy(() -> timeDepositService.getAllTimeDepositAccounts(TRACE_ID))
                .isInstanceOf(TimeDepositException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("""
            test 'GET ALL TIME DEPOSIT ACCOUNTS' given null trace ID,
            when getAllTimeDepositAccounts is called,
            then it should throw an IllegalArgumentException.
            """)
    public void testGetAllTimeDepositAccounts_givenNullTraceId_thenShouldThrowIllegalArgumentException() {
        // Setup expectations
        final String expectedErrorMessage = "TraceId must not be null or empty";

        // Perform SUT and verify exception
        Assertions.assertThatThrownBy(() -> timeDepositService.getAllTimeDepositAccounts(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedErrorMessage);
    }

    @Test
    @DisplayName("""
            test 'GET ALL TIME DEPOSIT ACCOUNTS' given empty trace ID,
            when getAllTimeDepositAccounts is called,
            then it should throw an IllegalArgumentException.
            """)
    public void testGetAllTimeDepositAccounts_givenEmptyTraceId_thenShouldThrowIllegalArgumentException() {
        // Setup expectations
        final String expectedErrorMessage = "TraceId must not be null or empty";

        // Perform SUT and verify exception
        Assertions.assertThatThrownBy(() -> timeDepositService.getAllTimeDepositAccounts(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedErrorMessage);
    }
}
