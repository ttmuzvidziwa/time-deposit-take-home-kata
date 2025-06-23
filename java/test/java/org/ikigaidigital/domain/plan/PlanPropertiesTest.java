package org.ikigaidigital.domain.plan;

    import org.ikigaidigital.domain.component.plan.PlanProperties;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Nested;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.context.properties.EnableConfigurationProperties;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.ActiveProfiles;
    import org.springframework.test.context.TestPropertySource;

    import java.math.BigDecimal;

    import static org.assertj.core.api.Assertions.assertThat;

    class PlanPropertiesTest {

        @Nested
        @SpringBootTest(classes = PlanProperties.class)
        @EnableConfigurationProperties(PlanProperties.class)
        @ActiveProfiles("test")
        @TestPropertySource(properties = {
                "application.plans[0].planType=BASIC",
                "application.plans[0].interestRate=2.5",
                "application.plans[0].interestFreeDays=30",
                "application.plans[0].interestEnds=true",
                "application.plans[0].interestEndsAfterDays=60"
        })
        class SinglePlanTest {
            @Autowired
            private PlanProperties planProperties;

            @Test
            @DisplayName("""
                    test 'PLAN PROPERTIES' given valid properties,
                    when bound to PlanProperties,
                    then it should bind a single plan correctly.
                    """)
            void testPlanProperties_givenValidProperties_shouldBindCorrectly() {
                assertThat(planProperties.getPlans()).hasSize(1);
                PlanProperties.Plan plan = planProperties.getPlans().get(0);
                assertThat(plan.getPlanType()).isEqualTo("BASIC");
                assertThat(plan.getInterestRate()).isEqualByComparingTo(BigDecimal.valueOf(2.5));
                assertThat(plan.getInterestFreeDays()).isEqualTo(30);
                assertThat(plan.isInterestEnds()).isTrue();
                assertThat(plan.getInterestEndsAfterDays()).isEqualTo(60);
            }
        }

        @Nested
        @SpringBootTest(classes = PlanProperties.class)
        @EnableConfigurationProperties(PlanProperties.class)
        @ActiveProfiles("test")
        @TestPropertySource(properties = {
                "application.plans[0].planType=BASIC",
                "application.plans[0].interestRate=2.5",
                "application.plans[0].interestFreeDays=30",
                "application.plans[0].interestEnds=true",
                "application.plans[0].interestEndsAfterDays=60",
                "application.plans[1].planType=PREMIUM",
                "application.plans[1].interestRate=3.5",
                "application.plans[1].interestFreeDays=45",
                "application.plans[1].interestEnds=false"
        })
        class MultiplePlansTest {
            @Autowired
            private PlanProperties planProperties;

            @Test
            @DisplayName("""
                    test 'PLAN PROPERTIES' given multiple properties,
                    when bound to PlanProperties,
                    then it should bind all plans correctly.
                    """)
            void testPlanProperties_givenMultipleProperties_shouldBindAllCorrectly() {
                assertThat(planProperties.getPlans()).hasSize(2);

                PlanProperties.Plan plan1 = planProperties.getPlans().get(0);
                assertThat(plan1.getPlanType()).isEqualTo("BASIC");
                assertThat(plan1.getInterestRate()).isEqualByComparingTo(BigDecimal.valueOf(2.5));
                assertThat(plan1.getInterestFreeDays()).isEqualTo(30);
                assertThat(plan1.isInterestEnds()).isTrue();
                assertThat(plan1.getInterestEndsAfterDays()).isEqualTo(60);

                PlanProperties.Plan plan2 = planProperties.getPlans().get(1);
                assertThat(plan2.getPlanType()).isEqualTo("PREMIUM");
                assertThat(plan2.getInterestRate()).isEqualByComparingTo(BigDecimal.valueOf(3.5));
                assertThat(plan2.getInterestFreeDays()).isEqualTo(45);
                assertThat(plan2.isInterestEnds()).isFalse();
                assertThat(plan2.getInterestEndsAfterDays()).isNull();
            }
        }

        @Nested
        @SpringBootTest(classes = PlanProperties.class)
        @EnableConfigurationProperties(PlanProperties.class)
        @ActiveProfiles("test")
        @TestPropertySource(properties = {
                "application.plans="
        })
        class EmptyPlansTest {
            @Autowired
            private PlanProperties planProperties;

            @Test
            @DisplayName("""
                    test 'PLAN PROPERTIES' given empty plans property,
                    when bound to PlanProperties,
                    then it should return an empty list.
                    """)
            void testPlanProperties_givenEmptyPlansProperty_shouldReturnNull() {
                assertThat(planProperties.getPlans()).isEmpty();
            }
        }
    }