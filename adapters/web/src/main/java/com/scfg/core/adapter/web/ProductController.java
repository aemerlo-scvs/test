package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ProductUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
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
@RequestMapping(path = "/product")
@Api(tags = "API REST Productos")
public class ProductController {

    private final ProductUseCase productUseCase;

    @GetMapping(value = "/plans/{agreementCode}")
    @ApiOperation(value = "Retorna una lista de planes por el codigo de convenio")
    ResponseEntity getPlansByAgreementCode(@PathVariable int agreementCode) {
        try {
            SMVSResponseDTO smvsResponseDTO = productUseCase.getPlansByAgreementCode(agreementCode);
            return ok(smvsResponseDTO);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = "/all")
    @ApiOperation(value = "Listado de productos")
    ResponseEntity getAll() {
        try {
            List<Product> list = productUseCase.getAll();
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "Guardar las coberturas")
    public ResponseEntity save(@RequestBody Product product) {
        try {
            PersistenceResponse response = productUseCase.registerProduct(product);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Coverage", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }

    }
    @PostMapping(value = "/update")
    @ApiOperation(value = "Actualizar las coberturas")
    public ResponseEntity update(@RequestBody Product product) {
        try {
            PersistenceResponse response = productUseCase.updateProduct(product);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Coverage", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @DeleteMapping(value = "/delete/{productId}")
    @ApiOperation(value = "Dar de baja la coberturas")
    public ResponseEntity delete(@PathVariable Long productId) {
        try {
            PersistenceResponse response = productUseCase.deleteProduct(productId);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Coverage", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @PostMapping(value = "/filter")
    @ApiOperation(value = "Lista de Coberturas por filtro")
    public ResponseEntity getAllBranchParents(@RequestBody FilterParamenter paramenter) {
        try {
            List<Product> branchList = productUseCase.getfilterParamenter(paramenter);
            return ok(branchList);
        }catch (Exception ex){
            return CustomErrorType.notContent("Get branchs",ex.getMessage());
        }
    }

    @GetMapping(value = "/productallbr")
    @ApiOperation(value = "Lista de Productos con nombre del Ramo")
    public ResponseEntity getAllProductWithBranchName(@PathVariable Long branchId) {
        try {
            List<Product> productList = productUseCase.getAllProductWithBranchName(branchId);

            return ok(productList);
        }catch (Exception ex){
            return CustomErrorType.notContent("Get products FAIL ",ex.getMessage());
        }
    }
    @GetMapping(value = "/getProductsByBranchId/{branchId}")
    @ApiOperation(value = "Lista todos los productos de un ramo")
    public ResponseEntity getProductsByBranchId(@PathVariable long branchId){
        List<ObjectDTO> productList = productUseCase.getAllProductsByBranchId(branchId);
        if (productList.isEmpty()){
            return CustomErrorType.notContent("productos", "no data");
        }
        return ok(productList);
    }

}
