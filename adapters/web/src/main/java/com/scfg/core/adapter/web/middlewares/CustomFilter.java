package com.scfg.core.adapter.web.middlewares;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ManualCertificateDhlDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@Component
public class CustomFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setAttribute("forceWrite",1);
        log.info("Request URI is: {}", req.getRequestURI());
        ObjectMapper objectMapper = HelpersMethods.mapper();

        String query = req.getQueryString();


        boolean b= true;
        if ( b){
            res.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            String testStr = objectMapper.writeValueAsString(new PersistenceResponse<ManualCertificateDhlDTO>("r", ActionRequestEnum.CREATE, null));
            res.getWriter().write(testStr);
        } else {
            chain.doFilter(request, response);
        }
        //chain.
        //log.info("Response Status Code is: {} ",res.getStatus());
    }
}
