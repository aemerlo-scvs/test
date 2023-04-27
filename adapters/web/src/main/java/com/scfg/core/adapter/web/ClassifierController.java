package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.ClassifierUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Classifier;
import io.swagger.annotations.Api;
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
@RequestMapping(path = ClassifierEndpoint.CLASSIFIER_BASE_ROUTE)
@Api(value = "API Clasificadores")
public class ClassifierController  implements ClassifierEndpoint  {

    private final ClassifierUseCase classifierUseCase;

    @GetMapping
    @ApiOperation(value = "Lista todos los clasificadores")
    @Override
    public ResponseEntity getAllClassifiers() {
        List<Classifier> classifiers = classifierUseCase.getAllClassifiers();
        if (classifiers.isEmpty()) {
            return CustomErrorType.notContent(CLASSIFIER_TITLE, "No data");
        }
        return ok(classifiers);
    }

    // Not modifier
    @GetMapping(value = GET_CLASSIFIER_BY_REFERENCES_PARAMS)
    @ApiOperation(value = "Buscar un clasificador especifico por los codigos de referencia")
    public ResponseEntity getClassifierByReferencesIds(@PathVariable long classifierReferenceId,
                                                       @PathVariable long classifierTypeReferenceId) {
        Classifier classifierFind = null;
        try {
            classifierFind = classifierUseCase.getClassifierByReferencesIds(classifierReferenceId,classifierTypeReferenceId);
        } catch (NotDataFoundException e) {
            return CustomErrorType.notFound(CLASSIFIER_TITLE, e.getMessage());
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }

        return ok(classifierFind);
    }


    @GetMapping(value = RESOURCE_CLASSIFIER_TYPE)
    @ApiOperation(value = "Listar los clasificadores por tipo de clasificador (codigo de referencia")
    @Override
    public ResponseEntity getAllClassifiersByClassifierTypeReferenceId(@PathVariable long classifierTypeReferenceId) {
        List<Classifier> classifiers = classifierUseCase.getAllClassifiersByClassifierTypeReferenceId(classifierTypeReferenceId);
        if (classifiers.isEmpty()) {
            return CustomErrorType.notContent(CLASSIFIER_TITLE, "No data");
        }
        return ok(classifiers);
    }

    @GetMapping(value = GET_BY_PARENT_ID)
    @ApiOperation(value = "Lista los clasificadores por idPadre")
    @Override
    public ResponseEntity getAllClassifiersByParentId(@PathVariable long parentId) {
        List<Classifier> reportsForLoading = classifierUseCase.getAllReportsByPolicyType(parentId);
        if (reportsForLoading.isEmpty()) {
            return CustomErrorType.notContent(CLASSIFIER_TITLE, "No data");
        }
        return ok(reportsForLoading);
    }

    @GetMapping(value = GET_REPORTS_BY_POLICY_TYPE_ID)
    @ApiOperation(value = "Listar los tipos de reporte a cargar al sistema segun el tipo de poliza")
    @Override
    public ResponseEntity getAllReportsByPolicyType(@PathVariable long policyTypeId) {
        List<Classifier> reportsForLoading = classifierUseCase.getAllReportsByPolicyType(policyTypeId);
        if (reportsForLoading.isEmpty()) {
            return CustomErrorType.notContent(CLASSIFIER_TITLE, "No data");
        }
        return ok(reportsForLoading);
    }

    @PostMapping
    @ApiOperation(value = "Registra nuevo clasificador")
    @Override
    public ResponseEntity<PersistenceResponse> saveClassifier(@RequestBody Classifier classifier) {
        try {
            // Aplicando validaciones


            PersistenceResponse response = classifierUseCase.save(classifier);
            return ok(response);
        } catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest(CLASSIFIER_TITLE, e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Actualizar los datos de un clasificador especifico")
    @Override
    public ResponseEntity<PersistenceResponse> updateClassifier(@RequestBody Classifier classifier) {
        try {
            PersistenceResponse response = classifierUseCase.update(classifier);
            return ok(response);
        } catch (OperationException | NotDataFoundException e) {
            //log.error("Ocurrio un error al obtener el usuario: [{}]", classifier, e);
            return CustomErrorType.badRequest(CLASSIFIER_TITLE, e.getMessage());
        } catch (Exception e) {
            //log.error("Ocurrio un error al obtener el usuario: [{}]", classifier, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @DeleteMapping(value = CLASSIFIER_REFERENCE_ID_PARAM)
    @ApiOperation(value = "Dar de baja a un clasificodo especifico")
    @Override
    public ResponseEntity<PersistenceResponse> deleteClassifier(@PathVariable long classifierId) {
        try {
            PersistenceResponse response = classifierUseCase.delete(classifierId);
            return ok(response);
        } catch (NotDataFoundException e) {
            //log.error("Ocurrio un error al obtener el s: [{}]", classifierId, e);
            return CustomErrorType.badRequest(CLASSIFIER_TITLE, e.getMessage());
        } catch (Exception e) {
            //log.error("Ocurrio un error al obtener el usuario: [{}]", classifierId, e);
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }


}
