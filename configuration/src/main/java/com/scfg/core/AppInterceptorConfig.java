package com.scfg.core;

import com.scfg.core.adapter.web.middlewares.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AppInterceptorConfig implements WebMvcConfigurer {

    private final BrokerSettlementCalculationsInterceptor brokerSettlementCalculationsInterceptor;
    private final LastObservedCaseInterceptor lastObservedCaseInterceptor;
    private final ManualCertificateInterceptor manualCertificateInterceptor;
    private final ConsolidatedObservedCaseInterceptor consolidatedObservedCaseInterceptor;
    private final SubscriptionTrackingInterceptor subscriptionTrackingInterceptor;
    private final OldMonthlyDisbursementInterceptor oldMonthlyDisbursementInterceptor;
    private final MonthlyDisbursementInterceptor monthlyDisbursementInterceptor;
    private final SinisterInterceptor sinisterInterceptor;
    private final ValidateInsuredMortgageReliefInterceptor validateInsuredMortgageReliefInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(brokerSettlementCalculationsInterceptor)
                .addPathPatterns("/brokerSettlementCalculations/**");

        registry.addInterceptor(lastObservedCaseInterceptor)
                .addPathPatterns("/observedCases/past/**");

        registry.addInterceptor(manualCertificateInterceptor)
                .addPathPatterns("/manualCertificate/**");

        registry.addInterceptor(consolidatedObservedCaseInterceptor)
                .addPathPatterns("/consolidatedObservedCase/**");

        registry.addInterceptor(subscriptionTrackingInterceptor)
                .addPathPatterns("/subscriptionTracking/**");

        registry.addInterceptor(oldMonthlyDisbursementInterceptor)
                .addPathPatterns("/oldMonthlyDisbursement/**");

        registry.addInterceptor(monthlyDisbursementInterceptor)
                .addPathPatterns("/monthlyDisbursement/**");

        registry.addInterceptor(sinisterInterceptor)
                .addPathPatterns("/sinister/**");

        registry.addInterceptor(validateInsuredMortgageReliefInterceptor)
                .addPathPatterns("/validateInsuredMortgageRelief/**");



    }
}
