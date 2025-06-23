package org.ikigaidigital.repository;

import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.ikigaidigital.domain.model.entity.TimeDeposits;
import org.ikigaidigital.repository.time.deposits.TimeDepositsRepository;
import org.ikigaidigital.repository.withdrawal.WithdrawalsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TimeDepositsRepositoryTests {
    @Autowired
    private TimeDepositsRepository timeDepositsRepository;
    @Autowired
    private WithdrawalsRepository withdrawalsRepository;

    @Test
    @DisplayName("""
            test 'FIND ALL TIME DEPOSITS' given time deposit accounts exist in the database
            when the 'findAllTimeDeposits' method is called
            should return a list of all time deposits loaded from the data-test.sql file
            """)
    public void testFindAllTimeDeposits_givenTimeDepositsExist_shouldReturnAllTimeDeposits() {
        // Perform SUT
        final Optional<List<TimeDeposits>> timeDepositsList = timeDepositsRepository.findAllTimeDeposits();

        // Verify results
        Assertions.assertTrue(timeDepositsList.isPresent());
        Assertions.assertFalse(timeDepositsList.get().isEmpty());
        Assertions.assertEquals(10, timeDepositsList.get().size());
    }

    @Test
    @DisplayName("""
            test 'FIND ALL TIME DEPOSITS' given no time deposit accounts exist in the database
            when the 'findAllTimeDeposits' method is called
            should return an empty list
            """)
    public void testFindAllTimeDeposits_givenNoTimeDepositsExist_shouldReturnEmptyList() {
        // Setup expectations
        withdrawalsRepository.deleteAll();
        timeDepositsRepository.deleteAll();

        // Perform SUT
        final Optional<List<TimeDeposits>> timeDepositsList = timeDepositsRepository.findAllTimeDeposits();

        // Verify results
        Assertions.assertTrue(timeDepositsList.isPresent());
        Assertions.assertTrue(timeDepositsList.get().isEmpty());
        Assertions.assertEquals(0, timeDepositsList.get().size());
    }

    @Test
    @DisplayName("""
            test 'BATCH UPDATE TIME DEPOSIT ACCOUNTS' given time deposit account exists
            when the 'batchUpdateTimeDeposits' method is called
            should update the time deposit account
            """)
    public void testBatchUpdateTimeDeposits_givenTimeDepositExists_shouldUpdateTimeDeposit() {
        // Setup expectations
        withdrawalsRepository.deleteAll();
        timeDepositsRepository.deleteAll();

        final TimeDeposits timeDeposit = new TimeDeposits();
        timeDeposit.setId(1);
        timeDeposit.setBalance(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP));
        timeDeposit.setPlanType("PREMIUM");
        timeDeposit.setDays(30);

        final TimeDepositsDto timeDepositsDto = new TimeDepositsDto();
        timeDepositsDto.setId(timeDeposit.getId());
        timeDepositsDto.setBalance(timeDeposit.getBalance());
        timeDepositsDto.setPlanType(timeDeposit.getPlanType());
        timeDepositsDto.setDays(timeDeposit.getDays());

        timeDeposit.setBalance(BigDecimal.valueOf(1200.00).setScale(2, RoundingMode.HALF_UP));

        final List<TimeDepositsDto> timeDepositsDtoList = List.of(timeDepositsDto);

        timeDepositsRepository.save(timeDeposit);

        // Perform SUT
        timeDepositsRepository.batchUpdateTimeDepositAccounts(timeDepositsDtoList);

        // Verify results
        final Optional<TimeDeposits> updatedTimeDeposit = timeDepositsRepository.findById(timeDeposit.getId());
        Assertions.assertTrue(updatedTimeDeposit.isPresent());
        Assertions.assertEquals(BigDecimal.valueOf(1200.00).setScale(2, RoundingMode.HALF_UP), updatedTimeDeposit.get().getBalance());
    }

    @Test
    @DisplayName("""
            test 'BATCH UPDATE TIME DEPOSIT ACCOUNTS' given no time deposit accounts exist
            when the 'batchUpdateTimeDeposits' method is called
            """)
    public void testBatchUpdateTimeDeposits_givenNoTimeDepositsExist_shouldNotUpdate() {
        // Setup expectations
        withdrawalsRepository.deleteAll();
        timeDepositsRepository.deleteAll();

        final TimeDepositsDto timeDepositsDto = new TimeDepositsDto();
        timeDepositsDto.setId(1);
        timeDepositsDto.setBalance(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP));
        timeDepositsDto.setPlanType("PREMIUM");
        timeDepositsDto.setDays(30);

        final List<TimeDepositsDto> timeDepositsDtoList = List.of(timeDepositsDto);

        // Perform SUT
        timeDepositsRepository.batchUpdateTimeDepositAccounts(timeDepositsDtoList);

        // Verify results
        final Optional<List<TimeDeposits>> timeDepositsList = timeDepositsRepository.findAllTimeDeposits();
        Assertions.assertTrue(timeDepositsList.isPresent());
        Assertions.assertTrue(timeDepositsList.get().isEmpty());
    }
}
