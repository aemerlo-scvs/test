package com.scfg.core.adapter.web;

import com.scfg.core.adapter.web.util.CustomErrorType;
import com.scfg.core.application.port.in.PlanUseCase;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import com.scfg.core.domain.dto.credicasas.ClfPlanDTO;
import com.scfg.core.domain.dto.credicasas.PlanInformation;
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
@RequestMapping(path = PlanEndPoint.BASE)
@Api(tags = "API REST Planes")
public class PlanController implements PlanEndPoint{
    private final PlanUseCase planUseCase;

    @GetMapping
    @ApiOperation(value = "Retorna un plan acorde al codigo")
    ResponseEntity getPlanByAgreementCode(@RequestParam Integer agreementCode) {
        try {
            Plan list = planUseCase.getPlanByAgreementCode(agreementCode);
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @PostMapping(value = PlanEndPoint.SAVE)
    @ApiOperation(value = "Guardar planes")
    public ResponseEntity register(@RequestBody Plan plan) {
        try {
            PersistenceResponse response = planUseCase.register(plan);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("plan", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }

    }
    @PostMapping(value = PlanEndPoint.UPDATE)
    @ApiOperation(value = "Actualizar planes")
    public ResponseEntity update(@RequestBody Plan plan) {
        try {
            PersistenceResponse response = planUseCase.update(plan);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Planes", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @DeleteMapping(value = PlanEndPoint.DELETE)
    @ApiOperation(value = "Dar de baja la Planes")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            PersistenceResponse response = planUseCase.delete(id);
            return ok(response);
        }catch (NotDataFoundException | OperationException e) {
            return CustomErrorType.badRequest("Planes", e.getMessage());
        } catch (Exception ex) {
            return CustomErrorType.serverError("Server Error", ex.getMessage());
        }
    }
    @GetMapping(value = PlanEndPoint.GETALL)
    @ApiOperation(value = "Retorna lista de planes")
    ResponseEntity getAll() {
        try {
            List<Plan> list = planUseCase.getAll();
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @GetMapping(value = PlanEndPoint.GETBYID)
    @ApiOperation(value = "Retorna un plan por su if")
    ResponseEntity getById(@PathVariable Long id) {
        try {
            Plan plan = planUseCase.getById(id);
            return ok(plan);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }
    @PostMapping(value = PlanEndPoint.FILTER)
    @ApiOperation(value = "Listado de planes por filtro")
    public ResponseEntity getAllBranchParents(@RequestBody FilterParamenter paramenter) {
        try {
            List<Plan> listPlan = planUseCase.getfilterParamenter(paramenter);
            return ok(listPlan);
        }catch (Exception ex){
            return CustomErrorType.notContent("Obtener planes",ex.getMessage());
        }
    }
    @GetMapping(value = PlanEndPoint.CLF)
    @ApiOperation(value = "Retorna una lista de planes")
    ResponseEntity getAllCLF() {
        try {
            List<ClfPlanDTO> list = planUseCase.getAllPlans();
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }


    @GetMapping(value = PlanEndPoint.GEL)
    @ApiOperation(value = "Retorna una lista de planes")
    ResponseEntity getAllPlansByBusinessGroup(@RequestParam Integer businessGroupIdc) {
        try {
            List<Plan> list = planUseCase.getAllPlansByBusinessGroupId(businessGroupIdc);
            return ok(list);
        } catch (Exception e) {
            return CustomErrorType.serverError("Server Error", e.getMessage());
        }
    }

    @GetMapping(value = PlanEndPoint.GETPLANBYPRODUCTID)
    @ApiOperation(value = "Lista de planes por producto Id")
    public ResponseEntity findPlanByProductId(@PathVariable Long productId) {
        try {
            List<Plan> listPlan = planUseCase.findPlanByProductID(productId);
            return ok(listPlan);
        }catch (Exception ex){
            return CustomErrorType.notContent("Obtener planes",ex.getMessage());
        }
    }

    @PostMapping(value = PlanEndPoint.GETPLANBYREQUESTID)
    @ApiOperation(value = "Lista de planes por requestId")
    public ResponseEntity findPlanByProductId(@RequestBody List<Long> requestList) {
        try {
            List<PlanInformation> listPlan = planUseCase.findPlanByRequestId(requestList);
            return ok(listPlan);
        }catch (Exception ex){
            return CustomErrorType.notContent("Obtener planes",ex.getMessage());
        }
    }
}
