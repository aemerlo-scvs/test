package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ProductDocumentUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.ProductDocument;
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
@RequestMapping(path = ProductDocumentEndPoint.BASE)
@Api(tags = "API REST Productos Documentos")
public class ProductDocumentController {


    private final ProductDocumentUseCase productDocumentUseCase;

    @GetMapping(value = ProductDocumentEndPoint.GETALL)
    @ApiOperation(value = "Listado de documentos de productos")
    ResponseEntity getAll() {
        try {
            List<ProductDocument> list = productDocumentUseCase.getAll();
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = ProductDocumentEndPoint.GETBYID)
    @ApiOperation(value = "Documento de producto por id")
    ResponseEntity getById(@PathVariable Long id) {
        try {
            ProductDocument productDocument = productDocumentUseCase.getById(id);
            return ok(productDocument);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping(value = ProductDocumentEndPoint.SAVE)
    @ApiOperation(value = "Guardar documento de producto")
    public ResponseEntity save(@RequestBody ProductDocument obj) {
        try {
            PersistenceResponse response = productDocumentUseCase.save(obj);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Product Document", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }

    }
    @PostMapping(value = ProductDocumentEndPoint.UPDATE)
    @ApiOperation(value = "Actualizar documento de producto")
    public ResponseEntity update(@RequestBody ProductDocument obj) {
        try {
            PersistenceResponse response = productDocumentUseCase.update(obj);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Product Document", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @DeleteMapping(value = ProductDocumentEndPoint.DELETE)
    @ApiOperation(value = "Dar de baja documento de producto")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            PersistenceResponse response = productDocumentUseCase.delete(id);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Product Document", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
}
