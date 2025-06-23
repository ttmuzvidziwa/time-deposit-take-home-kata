package org.ikigaidigital.domain.service;


import org.ikigaidigital.domain.utils.LoggerUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class ScheduledTasksServiceTests {
    @Mock
    private TimeDepositService timeDepositService;

    @Mock
    private LoggerUtils loggerUtils;

    @InjectMocks
    private ScheduledTasksService scheduledTasksService;

    @Test
    @DisplayName("""
            test 'UPDATE ALL TIME DEPOSIT ACCOUNTS' scheduled task,
            when executed,
            then it should call the time deposit service to update all time deposit accounts with the trace ID.
            """)
    void testUpdateAllTimeDepositAccounts() {
        // Perform schedule SUT
        scheduledTasksService.updateAllTimeDepositAccounts();

        // Verify htat the schedule task was executed
        Mockito.verify(timeDepositService).updateAllTimeDepositAccounts(anyString());
    }
}