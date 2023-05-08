package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.AnnexeTypeUseCase;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.common.AnnexeType;
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
@RequestMapping(path = "annexeType")
@Api(value = "API REST Tipos de anexo")
public class AnnexeTypeController {

    private final AnnexeTypeUseCase annexeTypeUseCase;

    @GetMapping
    @ApiOperation(value = "Retorna el tipo de anexo por el Id del producto y código interno")
    ResponseEntity<?> getByProductIdAndInternalCode(@RequestParam long productId,
                                                    @RequestParam long internalCode) {
        try {
            AnnexeType annexeType = annexeTypeUseCase.getByProductIdAndInternalCode(productId, internalCode);
            return ok(annexeType);
        } catch (OperationException e) {
            log.error("Ocurrio un error al obtener el tipo de anexo por el id del producto: [{}]", productId, e);
            return CustomErrorType.badRequest("Bad Request", e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener el tipo de anexo por el id del producto: [{}]", productId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }


}
