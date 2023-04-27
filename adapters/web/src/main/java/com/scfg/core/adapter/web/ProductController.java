package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ProductUseCase;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.smvs.SMVSResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = ProductEndPoint.BASE)
@Api(tags = "API REST Productos")
public class ProductController implements ProductEndPoint {

    private final ProductUseCase productUseCase;

    @GetMapping()
    @ApiOperation(value = "Retorna una lista de productos")
    ResponseEntity getAll() {
        try {
            List<Product> list = productUseCase.getAll();
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = BASE_PARAM_PLANS)
    @ApiOperation(value = "Retorna una lista de planes por el codigo de convenio")
    ResponseEntity getPlansByAgreementCode(@PathVariable int agreementCode) {
        try {
            SMVSResponseDTO smvsResponseDTO = productUseCase.getPlansByAgreementCode(agreementCode);
            return ok(smvsResponseDTO);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = ProductEndPoint.GETPRODUCTSBYBRANCHID)
    @ApiOperation(value = "Lista todos los productos de un ramo")
    public ResponseEntity getProductsByBranchId(@PathVariable long branchId){
        List<ObjectDTO> productList = productUseCase.getAllProductsByBranchId(branchId);
        if (productList.isEmpty()){
            return CustomErrorType.notContent("productos", "no data");
        }
        return ok(productList);
    }

}
