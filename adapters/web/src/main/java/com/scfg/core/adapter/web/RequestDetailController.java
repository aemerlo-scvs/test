package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.RequestDetailUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.domain.dto.credicasas.GelRequestDetailDTO;
import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requestDetail")
@Api(tags = "API RequestDetail")
public class RequestDetailController {
    private final RequestDetailUseCase requestDetailUseCase;

    @PostMapping(value = "/gel")
    @ApiOperation(value = "Retorna el detalle de solicitud por Id")
    ResponseEntity getRequestDetailById(@RequestBody RequestDetailDTO requestDetail){
        try {
            RequestDetailDTO requestDetailDTO = requestDetailUseCase.getRequestDetail(requestDetail);
            return ok(requestDetailDTO);
        } catch (NotDataFoundException e) {
            return CustomErrorType.badRequest("bad Request", e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server error", e.getMessage());
        }
    }

}
