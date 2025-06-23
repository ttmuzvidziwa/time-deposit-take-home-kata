package org.ikigaidigital.domain.component.calculator;

import lombok.RequiredArgsConstructor;
import org.ikigaidigital.domain.component.plan.PlanProperties;
import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TimeDepositCalculator {
    private final PlanProperties planProperties;

    /**
     * Updates the balance of a list of time deposits based on their plan type and duration.
     *
     * For each deposit in the list:
     * <li> 1 Finds the corresponding plan based on the plan type. </li>
     * <li> 2 If no matching plan is found, skips the deposit. </li>
     * <li> 3 Calculates the new balance using the plan's interest rate and duration. </li>
     * <li> 4 Updates the deposit's balance with the newly calculated value. </li>
     *
     * @param deposits A list of {@link TimeDepositsDto} objects representing the time deposits to update.
     */
    public void updateBalance(List<TimeDepositsDto> deposits) {
        for (TimeDepositsDto dto : deposits) {
            PlanProperties.Plan plan = null;
            for (PlanProperties.Plan p : planProperties.getPlans()) {
                if (p.getPlanType().equalsIgnoreCase(dto.getPlanType())) {
                    plan = p;
                    break;
                }
            }

            if (plan == null) {
                continue;
            }

            int days = dto.getDays();
            BigDecimal balance = dto.getBalance();
            BigDecimal newBalance = calculateUpdatedBalance(days, plan, balance);
            dto.setBalance(newBalance);
        }
    }

    /**
     * Calculates the new balance of a time deposit based on the number of days,
     * the plan's interest rate, and the current balance.
     *
     * <p>The method performs the following steps:
     * <li> 1. Initializes the interest to zero. </li>
     * <li> 2. Checks if the number of days exceeds the interest-free period. </li>
     * <li> 3. If the interest-free period is exceeded: </li>
     *    - Verifies if the interest ends after a certain period. </li>
     *    - If interest is applicable, calculates it using the formula: </li>
     *      (balance * interestRate) / 12, rounded to 8 decimal places. </li>
     * <li> 4. Adds the calculated interest to the current balance and rounds it to 2 decimal places. </li>
     * </p>
     * @param days    The number of days the deposit has been held.
     * @param plan    The {@link PlanProperties.Plan} object containing the plan's details.
     * @param balance The current balance of the time deposit.
     * @return The updated balance after applying the interest.
     */
    private static BigDecimal calculateUpdatedBalance(int days, PlanProperties.Plan plan, BigDecimal balance) {
        BigDecimal interest = BigDecimal.ZERO;

        // Check interest-free period
        if (days > plan.getInterestFreeDays()) {
            // Check if interest ends after a certain period
            if (!plan.isInterestEnds() || (plan.getInterestEndsAfterDays() == null || days <= plan.getInterestEndsAfterDays())) {
                interest = balance
                        .multiply(plan.getInterestRate())
                        .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP);
            }
        }

        return balance.add(interest).setScale(2, RoundingMode.HALF_UP);
    }
}
