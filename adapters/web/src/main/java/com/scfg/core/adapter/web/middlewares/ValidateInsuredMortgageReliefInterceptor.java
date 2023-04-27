package com.scfg.core.adapter.web.middlewares;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.application.port.in.PreliminaryObservedCaseUseCase;
import com.scfg.core.application.port.in.ValidateInsuredMortgageReliefUseCase;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.common.util.mortgageRelief.ValidateInsuredsResponse;
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
public class ValidateInsuredMortgageReliefInterceptor implements HandlerInterceptor {

    private final ValidateInsuredMortgageReliefUseCase validateInsuredMortgageReliefUseCase;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //String val = req.getQueryString().split("&")[0].split("=")[1];

        if (req.getMethod().equalsIgnoreCase("POST")) {

            Map<String, String> queryParams = HelpersMethods.getQueryMap(req.getQueryString());

            // Get Query Params
            Map<String, String> routeParams = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            Long policyTypeCodeReference = Long.parseLong(routeParams.get("policyTypeReferenceId"));

            long policyTypeId = Long.parseLong(queryParams.get("policyTypeId"));
            long monthId = Long.parseLong(queryParams.get("monthId"));
            long yearId = Long.parseLong(queryParams.get("yearId"));
            long insurancePolicyHolderId = Long.parseLong(queryParams.get("insurancePolicyHolderId"));

            PersistenceResponse persistenceResponse = new PersistenceResponse<String>(ActionRequestEnum.PREVIOUS_CREATE);

            boolean existsValidationInsureds = validateInsuredMortgageReliefUseCase.existsValidationInsureds(
                    monthId,yearId,insurancePolicyHolderId, policyTypeId, policyTypeCodeReference);

            /*boolean isRegulatedPolicyType =policyTypeCodeReference.equals(ClassifierEnum.RegulatedDH_PolicyType
                    .getReferenceCode());*/

            persistenceResponse.setResourceName(ValidateInsuredsResponse.class.getSimpleName());


            if (existsValidationInsureds) {

                persistenceResponse.setData("Validacion de asegurados ya fue realizada con anterioridad");
                ObjectMapper objectMapper = HelpersMethods.mapper();
                res.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                String responseContent = objectMapper.writeValueAsString(persistenceResponse);
                res.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                res.getWriter().write(responseContent);
                return false;
            } else {
                return true;
            }
        }
        return true;
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
