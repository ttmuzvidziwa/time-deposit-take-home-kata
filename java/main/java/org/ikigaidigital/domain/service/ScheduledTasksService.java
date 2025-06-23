package org.ikigaidigital.domain.service;

import lombok.RequiredArgsConstructor;
import org.ikigaidigital.domain.constant.Sources;
import org.ikigaidigital.domain.utils.GeneralUtils;
import org.ikigaidigital.domain.utils.LoggerUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTasksService {
    private final TimeDepositService timeDepositService;
    private final String traceId = GeneralUtils.generateTraceId();

    /**
     * Scheduled task that updates all time deposit accounts.
     * <p>
     * This method is executed on the last day of every month at midnight, as specified
     * by the cron expression "0 0 0 L * ?". It performs the following steps:
     * <li> - Logs the start of the scheduled task with a trace ID. </li>
     * <li> - Delegates the update operation to the `timeDepositService` with the trace ID. </li>
     *
     * @Scheduled(cron = "0 0 0 L * ?") Indicates the cron schedule for the task.
     */
    @Scheduled(cron = "0 0 0 L * ?")
    public void updateAllTimeDepositAccounts() {
        LoggerUtils.logDebug(traceId, Sources.SCHEDULED_SERVICE_LAYER, "Scheduled task to update all time deposit accounts started.");
        timeDepositService.updateAllTimeDepositAccounts(traceId);
    }
}
