package com.scfg.core.adapter.web;

import static org.springframework.http.ResponseEntity.ok;
import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ClassifierTypeUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.ClassifierType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ClassifierTypeEndpoint.CLASSIFIER_TYPE_BASE_ROUTE)
@Api(tags = "API REST Tipos de Clasificador")
public class ClassifierTypeController implements ClassifierTypeEndpoint {

    private final ClassifierTypeUseCase classifierTypeUseCase;

    @GetMapping
    @ApiOperation(value = "Lista todos los tipos de clasificadores")
    @Override
    public ResponseEntity getAllClassifierTypes() {
        List<ClassifierType> classifierTypes = classifierTypeUseCase.getAll();
        if (classifierTypes.isEmpty()){
            return CustomErrorType.notContent(CLASSIFIER_TYPE_TITLE, "No data");
        }
        return ok(classifierTypes);
    }

    @GetMapping(value = ClassifierTypeEndpoint.DETAIL)
    @ApiOperation(value = "Lista todos los tipos de clasificadores con su relación clasificadores")
    @Override
    public ResponseEntity getAllClassifierTypesDetail() {
        List<ClassifierType> classifierTypes = classifierTypeUseCase.getAllDetail();
        if (classifierTypes.isEmpty()){
            return CustomErrorType.notContent(CLASSIFIER_TYPE_TITLE, "No data");
        }
        return ok(classifierTypes);
    }

    @GetMapping(value = ClassifierTypeEndpoint.PARAM_PAGE)
    @ApiOperation(value = "Lista paginada de todos los tipos de clasificadores")
    @Override
    public ResponseEntity getAllClassifierTypesByPage(@PathVariable int page, @PathVariable int size) {
        Object classifierTypes = classifierTypeUseCase.getAllByPage(page, size);
        return ok(classifierTypes);
    }


    @GetMapping(value = CLASSIFIER_TYPE_ID_PARAM)
    @ApiOperation(value = "Busca un tipo de clasificador especifico por el Id")
    @Override
    public ResponseEntity getClassifierTypeById(@PathVariable long classifierTypeId) {
        ClassifierType classifierTypeFind = null;
        try {
            classifierTypeFind = classifierTypeUseCase.getClassifierTypeById(classifierTypeId);
        } catch (NotDataFoundException e) {
            return CustomErrorType.notFound(CLASSIFIER_TYPE_TITLE, e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }

        return ok(classifierTypeFind);
    }

    @GetMapping(value = CLASSIFIER_REFERENCE_ID_PARAM)
    @ApiOperation(value = "Buscar un tipo de clasificador especifico por el Id de referencia")
    @Override
    public ResponseEntity existsClassifierTypeByReferenceId(@PathVariable long classifierReferenceId) {
        try {
            Boolean classifierType = classifierTypeUseCase.existsClassifierTypeByReferenceId(classifierReferenceId);
            return ok(classifierType);
        }  catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation(value = "Registra un nuevo tipo de clasificador")
    @Override
    public ResponseEntity<PersistenceResponse> saveClassifierType(@RequestBody ClassifierType classifierType) {
        try {
            PersistenceResponse response = classifierTypeUseCase.save(classifierType);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest(CLASSIFIER_TYPE_TITLE, e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value ="Actualiza un tipo de clasificador especifico")
    @Override
    public ResponseEntity<PersistenceResponse> updateClassifierType(@RequestBody ClassifierType classifierType) {
        try {
            PersistenceResponse response = classifierTypeUseCase.update(classifierType);
            return ok(response);
        } catch (OperationException | NotDataFoundException e) {
            //log.error("Ocurrio un error al obtener el usuario: [{}]", classifier, e);
            return CustomErrorType.badRequest(CLASSIFIER_TYPE_TITLE, e.getMessage());
        } catch (Exception e) {
            //log.error("Ocurrio un error al obtener el usuario: [{}]", classifier, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @DeleteMapping(value = CLASSIFIER_TYPE_ID_PARAM)
    @ApiOperation(value = "Elimina un tipo de clasificador seleccionado")
    @Override
    public ResponseEntity<PersistenceResponse> deleteClassifierType(@PathVariable long classifierTypeId) {
        try {
            PersistenceResponse response = classifierTypeUseCase.delete(classifierTypeId);
            return ok(response);
        } catch (NotDataFoundException e) {
            //log.error("Ocurrio un error al obtener el s: [{}]", classifierId, e);
            return CustomErrorType.badRequest(CLASSIFIER_TYPE_TITLE, e.getMessage());
        } catch (Exception e) {
            //log.error("Ocurrio un error al obtener el usuario: [{}]", classifierId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
}
