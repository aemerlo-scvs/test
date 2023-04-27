package com.scfg.core.adapter.web.middlewares;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.application.port.in.BrokerSettlementCalculationsUseCase;
import com.scfg.core.application.port.in.MortgageReliefItemUseCase;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.BrokerSettlementCalculationsDhnDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class BrokerSettlementCalculationsInterceptor implements HandlerInterceptor {

    private final MortgageReliefItemUseCase mortgageReliefItemUseCase;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String val = req.getQueryString().split("&")[0].split("=")[1];

        if (req.getMethod().equalsIgnoreCase("POST")) {

            Map<String, String> queryParams = HelpersMethods.getQueryMap(req.getQueryString());


            // Get Query Params
            Map<String, String> routeParams = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            Long policyTypeCodeReference = Long.parseLong(routeParams.get("policyTypeReferenceId"));

            long monthId = Long.parseLong(queryParams.get("monthId"));
            long yearId = Long.parseLong(queryParams.get("yearId"));
            long reportTypeId = Long.parseLong(queryParams.get("reportTypeId"));
            long insurancePolicyHolderId = Long.parseLong(queryParams.get("insurancePolicyHolderId"));

            // Force register
            long overwrite = Long.parseLong(queryParams.getOrDefault("overwrite", "0"));

            boolean existsInformation;


            PersistenceResponse persistenceResponse = new PersistenceResponse<String>(ActionRequestEnum.PREVIOUS_CREATE);

            if (policyTypeCodeReference.equals(ClassifierEnum.RegulatedDH_PolicyType
                    .getReferenceCode())) {
                existsInformation = !mortgageReliefItemUseCase.getMortgageReliefItemByIDs(
                        monthId,
                        yearId,
                        reportTypeId,
                        ClassifierEnum.RegulatedDH_PolicyType
                                .getReferenceCode(),
                        insurancePolicyHolderId)
                        .isEmpty();
                persistenceResponse.setResourceName(BrokerSettlementCalculationsDhlDTO.class.getSimpleName());

            } else {
                existsInformation = !mortgageReliefItemUseCase.getMortgageReliefItemByIDs(
                        monthId,
                        yearId,
                        reportTypeId,
                        ClassifierEnum.UnregulatedDH_PolicyType
                                .getReferenceCode(),
                        insurancePolicyHolderId)
                        .isEmpty();
                persistenceResponse.setResourceName(BrokerSettlementCalculationsDhnDTO.class.getSimpleName());
            }

            if (existsInformation && overwrite == 0) {

                persistenceResponse.setData("Informacion registrada con anterioridad");
                ObjectMapper objectMapper = HelpersMethods.mapper();
                res.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                String responseContent = objectMapper.writeValueAsString(persistenceResponse);
                res.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                res.getWriter().write(responseContent);
                return false;
                //response.getWriter().write(testStr);
            } else {
                return true;
            }
        }
        return true;

        //return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
