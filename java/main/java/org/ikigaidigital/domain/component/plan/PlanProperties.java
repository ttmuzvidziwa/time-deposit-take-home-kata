package org.ikigaidigital.domain.component.plan;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class PlanProperties {

    private List<Plan> plans;

    @Setter
    @Getter
    public static class Plan {
        private String planType;
        private BigDecimal interestRate;
        private int interestFreeDays;
        private boolean interestEnds;
        private Integer interestEndsAfterDays;
    }
}