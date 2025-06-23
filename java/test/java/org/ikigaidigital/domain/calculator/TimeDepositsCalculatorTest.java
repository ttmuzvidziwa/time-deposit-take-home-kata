package org.ikigaidigital.domain.calculator;

import org.ikigaidigital.domain.component.calculator.TimeDepositCalculator;
import org.ikigaidigital.domain.component.plan.PlanProperties;
import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TimeDepositsCalculatorTest {
    @Autowired
    private TimeDepositCalculator timeDepositCalculator;
    @Autowired
    private PlanProperties planProperties;


    @Test
    @DisplayName("updateBalance_givenBasicPlan_shouldApply1PercentAfter30Days")
    void updateBalance_givenBasicPlan_shouldApply1PercentAfter30Days() {
        //Setup expectations
        TimeDepositsDto plan = new TimeDepositsDto(1, "basic", BigDecimal.valueOf(1000), 60);
        List<TimeDepositsDto> plans = Collections.singletonList(plan);

        BigDecimal interest = BigDecimal.valueOf(1000)
                .multiply(BigDecimal.valueOf(0.01))
                .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal expected = BigDecimal.valueOf(1000).add(interest).setScale(2, RoundingMode.HALF_UP);

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // Verify results
        assertThat(plan.getBalance()).isEqualByComparingTo(expected);
    }

    @Test
    @DisplayName("""
            test 'UPDATE BALANCE' given a student plan within interest period,
            when updateBalance method is called,
            then it should apply 3 percent interest for days > 30 and < 366.
            """)
    void updateBalance_givenStudentPlanWithinInterestPeriod_shouldApply3Percent() {
        // Setup expectations
        TimeDepositsDto plan = new TimeDepositsDto(1, "student", BigDecimal.valueOf(2000), 100);
        List<TimeDepositsDto> plans = Collections.singletonList(plan);

        /* Only days > 30 and < 366, so 3% annual*/
        BigDecimal interest = BigDecimal.valueOf(2000)
                .multiply(BigDecimal.valueOf(0.03))
                .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal expected = BigDecimal.valueOf(2000).add(interest).setScale(2, RoundingMode.HALF_UP);

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // Verify results
        assertThat(plan.getBalance()).isEqualByComparingTo(expected);
    }

    @Test
    @DisplayName("""
            test 'UPDATE BALANCE' given a student plan after interest ends,
            when updateBalance method is called,
            then it should not apply interest for days > 365.
            """)
    void updateBalance_givenStudentPlanAfterInterestEnds_shouldNotApplyInterest() {
        // Setup expectations
        TimeDepositsDto plan = new TimeDepositsDto(1, "student", BigDecimal.valueOf(2000), 400);
        List<TimeDepositsDto> plans = Collections.singletonList(plan);

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // After 365 days, no interest
        assertThat(plan.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("""
            test 'UPDATE BALANCE' given a premium plan after 45 days,
            when updateBalance method is called,
            then it should apply 5 percent interest for days > 45.
            """)
    void updateBalance_givenPremiumPlan_shouldApply5PercentAfter45Days() {
        // Setup expectations
        TimeDepositsDto plan = new TimeDepositsDto(1, "premium", BigDecimal.valueOf(3000), 60);
        List<TimeDepositsDto> plans = Collections.singletonList(plan);

        /* Only days > 45 get interest, so 5% annual */
        BigDecimal interest = BigDecimal.valueOf(3000)
                .multiply(BigDecimal.valueOf(0.05))
                .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal expected = BigDecimal.valueOf(3000).add(interest).setScale(2, RoundingMode.HALF_UP);

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // Verify results
        assertThat(plan.getBalance()).isEqualByComparingTo(expected);
    }

    @Test
    @DisplayName("""
            test 'UPDATE BALANCE' given multiple plans,
            when updateBalance method is called,
            then it should update all balances correctly based on their plan types.
            """)
    void updateBalance_givenMultiplePlans_shouldUpdateAllBalancesCorrectly() {
        // Setup expectations
        TimeDepositsDto plan1 = new TimeDepositsDto(1, "basic", BigDecimal.valueOf(2000.00), 60);
        TimeDepositsDto plan2 = new TimeDepositsDto(2, "premium", BigDecimal.valueOf(3000.00), 60);
        List<TimeDepositsDto> plans = Arrays.asList(plan1, plan2);

        BigDecimal interest1 = BigDecimal.valueOf(2000)
                .multiply(BigDecimal.valueOf(0.01))
                .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal expected1 = BigDecimal.valueOf(2000).add(interest1).setScale(2, RoundingMode.HALF_UP);

        BigDecimal interest2 = BigDecimal.valueOf(3000)
                .multiply(BigDecimal.valueOf(0.05))
                .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal expected2 = BigDecimal.valueOf(3000).add(interest2).setScale(2, RoundingMode.HALF_UP);

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // Verify results
        assertThat(plan1.getBalance()).isEqualByComparingTo(expected1);
        assertThat(plan2.getBalance()).isEqualByComparingTo(expected2);
    }

    @Test
    @DisplayName("""
            test 'UPDATE BALANCE' given a plan with zero balance,
            when updateBalance method is called,
            then it should remain zero.
            """)
    void updateBalance_givenZeroBalance_shouldRemainZero() {
        // Setup expectations
        TimeDepositsDto plan = new TimeDepositsDto(1, "basic", BigDecimal.ZERO, 60);
        List<TimeDepositsDto> plans = Collections.singletonList(plan);

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // Verify results
        assertThat(plan.getBalance()).isEqualByComparingTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("""
            test 'UPDATE BALANCE' given a plan new plan with zero days
            when updateBalance method is called,
            then it should not change the balance.
            """)
    void updateBalance_givenZeroDays_shouldNotChangeBalance() {
        // Setup expectations
        TimeDepositsDto plan = new TimeDepositsDto(1, "basic", BigDecimal.valueOf(1000.00), 0);
        List<TimeDepositsDto> plans = Collections.singletonList(plan);

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // Verify results
        assertThat(plan.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("""
            test 'UPDATE BALANCE' given an unknown plan type,
            when updateBalance method is called,
            then it should not change the balance.
            """)
    void updateBalance_givenUnknownPlanType_shouldNotChangeBalance() {
        // Setup expectations
        TimeDepositsDto plan = new TimeDepositsDto(1, "unknown", BigDecimal.valueOf(1000.00), 60);
        List<TimeDepositsDto> plans = Collections.singletonList(plan);

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // Verify results
        assertThat(plan.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("""
            test 'UPDATE BALANCE' given an empty list of plans,
            when updateBalance method is called,
            then it should not throw any exception, call the calculate methods now process the list.
            """)
    void updateBalance_givenEmptyList_shouldNotThrow() {
        // Setup expectations
        List<TimeDepositsDto> plans = new ArrayList<>();

        // Perform SUT
        timeDepositCalculator.updateBalance(plans);

        // Verify results
        assertThat(plans).isEmpty();
    }
}
