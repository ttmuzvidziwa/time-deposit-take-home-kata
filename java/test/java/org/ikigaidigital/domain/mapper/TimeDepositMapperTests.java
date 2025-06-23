package org.ikigaidigital.domain.mapper;

import org.ikigaidigital.domain.model.dto.TimeDepositsDto;
import org.ikigaidigital.domain.model.entity.TimeDeposits;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
@ActiveProfiles("test")
public class TimeDepositMapperTests {
    @Autowired
    private TimeDepositMapper timeDepositMapper;

    @Test
    @DisplayName("""
            test 'CONVERT TO DTO' given a valid TimeDeposits entity
            when the 'convertToDto' method is called
            should return a TimeDepositsDto with the same data
            """)
    public void testConvertToDto_givenValidEntity_shouldReturnDto() {
        // Setup expectations
        // Create a TimeDeposits entity with sample data
        TimeDeposits timeDeposits = new TimeDeposits();
        timeDeposits.setId(1);
        timeDeposits.setPlanType("Standard");
        timeDeposits.setBalance(new BigDecimal("1000.00").setScale(2, RoundingMode.HALF_UP));
        timeDeposits.setDays(30);

        // Perform SUT
        TimeDepositsDto dto = timeDepositMapper.toDto(timeDeposits);

        // Verify results
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(timeDeposits.getId(), dto.getId());
        Assertions.assertEquals(timeDeposits.getPlanType(), dto.getPlanType());
        Assertions.assertEquals(timeDeposits.getBalance(), dto.getBalance());
        Assertions.assertEquals(timeDeposits.getDays(), dto.getDays());
    }

    @Test
    @DisplayName("""
            test 'CONVERT TO DTO' given a null TimeDeposits entity
            when the 'convertToDto' method is called
            should return null
            """)
    public void testConvertToDto_givenNullEntity_shouldReturnNull() {
        // Perform SUT
        TimeDepositsDto dto = timeDepositMapper.toDto(null);

        // Verify results
        Assertions.assertNull(dto);
    }

    @Test
    @DisplayName("""
            test 'CONVERT TO DTO' given a TimeDeposits entity with null object fields
            when the 'convertToDto' method is called
            should return a TimeDepositsDto with null fields
            """)
    public void testConvertToDto_givenEntityWithNullFields_shouldReturnDtoWithNullFields() {
        // Setup expectations
        // Create a TimeDeposits entity with some null fields
        TimeDeposits timeDeposits = new TimeDeposits();
        timeDeposits.setId(2);
        timeDeposits.setPlanType(null);
        timeDeposits.setBalance(null);
        timeDeposits.setDays(60);

        // Perform SUT
        TimeDepositsDto dto = timeDepositMapper.toDto(timeDeposits);

        // Verify results
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(timeDeposits.getId(), dto.getId());
        Assertions.assertNull(dto.getPlanType());
        Assertions.assertNull(dto.getBalance());
        Assertions.assertEquals(timeDeposits.getDays(), dto.getDays());
    }
}
