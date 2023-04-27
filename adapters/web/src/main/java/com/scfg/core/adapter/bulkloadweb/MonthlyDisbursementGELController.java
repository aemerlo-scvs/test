package com.scfg.core.adapter.bulkloadweb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.application.service.bulkLoadGel.MonthlyDisbursementGELService;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.credicasas.groupthefont.MonthlyDisbursementGELDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/gelMonthlyDisbursement")
@ApiOperation(value = "APIs Desembolsos mensuales")
public class MonthlyDisbursementGELController {
    private final MonthlyDisbursementGELService monthlyDisbursementGELService;

    @PostMapping(value = "/register")
    public ResponseEntity<PersistenceResponse> registerMonthlyDisbursementGEL(
            @RequestParam Long planId,
            @RequestParam Long policyId,
            @RequestParam Long monthId,
            @RequestParam Long yearId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") Long overwrite,
            @RequestBody String data) {
        return ok (monthlyDisbursementGELService.saveMonthlyDisbursements(planId, policyId, monthId, yearId, userId, overwrite, data));
    }

    @PostMapping(value = "/format")
    public ResponseEntity<PersistenceResponse> exportFormatErrors(@RequestBody List<Object> data) {
        ObjectMapper objectMapper = HelpersMethods.mapper();
        List<MonthlyDisbursementGELDTO> monthlyDisbursementGELDTOList = objectMapper.convertValue(data, new TypeReference<List<MonthlyDisbursementGELDTO>>() {
        });
        return ok (monthlyDisbursementGELService.exportFormatErrors(monthlyDisbursementGELDTOList));
    }
}
