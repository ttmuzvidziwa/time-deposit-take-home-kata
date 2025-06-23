package org.ikigaidigital.domain.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GeneralUtilsTests {
    @Test
    @DisplayName("""
            test 'GENERATE TRACE ID' 
            when the 'generateTraceId' method
            should return a non-null trace ID of length 25 characters
            """)
    public void testGenerateTraceId_shouldGenerateValidTraceId() {
        // Perform SUT
        final String traceId = GeneralUtils.generateTraceId();

        // Verify results
        Assertions.assertNotNull(traceId);
        Assertions.assertEquals(36, traceId.length());
    }
}
