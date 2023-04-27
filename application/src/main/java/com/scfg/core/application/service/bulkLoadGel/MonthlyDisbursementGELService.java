package com.scfg.core.application.service.bulkLoadGel;

import com.scfg.core.application.port.out.PolicyPort;
import com.scfg.core.application.service.GenerateReportsService;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Policy;
import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyDisbursementGELService {

    private final BulkLoadGelRepository bulkLoadGelRepository;
    private final EntityManager em;
    private final GenerateReportsService generateReportsService;
    private final PolicyPort policyPort;
    private static String SP_BULK_LOAD_GEL = "exec sp_Process_BulkLoad_Gel :userId, :year, :month, :overwrite, :planId, :policyId";

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {
            Exception.class
    })
    public PersistenceResponse saveMonthlyDisbursements(long planId, long policyId, long monthId, long yearId, long userId, long overwrite, String data) {
        BulkLoadGelJpaEntity bulkLoadGelJpaEntity = new BulkLoadGelJpaEntity();

        Policy policy = policyPort.findByPlanId(planId, policyId);
        LocalDate fromDate = policy.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int fromMonth = fromDate.getMonthValue();
        int fromYear = fromDate.getYear();
        LocalDate toDate = policy.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int toMonth = toDate.getMonthValue();
        int toYear = toDate.getYear();

        if ((yearId < fromYear) || (yearId >= fromYear && monthId < fromMonth)) {
            return new PersistenceResponse()
                    .setResourceName("Carga no dentro de vigencia de póliza")
                    .setActionRequestEnum(ActionRequestEnum.VALIDITY_OBSERVATION);
        }

        if ((yearId > toYear) || (yearId == toYear && monthId > toMonth)) {
            return new PersistenceResponse()
                    .setResourceName("Carga no dentro de vigencia de póliza")
                    .setActionRequestEnum(ActionRequestEnum.VALIDITY_OBSERVATION);
        }

        Long id = (bulkLoadGelRepository.findFirstId() != null) ? bulkLoadGelRepository.findFirstId() : 0;
        bulkLoadGelJpaEntity.setId(id);
        bulkLoadGelJpaEntity.setContent(data);
        bulkLoadGelRepository.save(bulkLoadGelJpaEntity);

        Query storedProcedureQuery = this.em.createNativeQuery(SP_BULK_LOAD_GEL);
        storedProcedureQuery.setParameter("userId", userId);
        storedProcedureQuery.setParameter("year", yearId);
        storedProcedureQuery.setParameter("month", monthId);
        storedProcedureQuery.setParameter("overwrite", overwrite);
        storedProcedureQuery.setParameter("planId", planId);
        storedProcedureQuery.setParameter("policyId", policyId);
        List storeProcedureResult = storedProcedureQuery.getResultList();
        if (storeProcedureResult.size() == 1 && storeProcedureResult.get(0).getClass().getSimpleName().equals("Integer")) {
            if (Integer.parseInt(storeProcedureResult.get(0).toString()) == ActionRequestEnum.PREVIOUS_CREATE.getIdentifier()) {
                if (monthId < LocalDate.now().getMonthValue() - 1) {
                    return new PersistenceResponse()
                            .setResourceName("Confirmar sobrescritura de información")
                            .setActionRequestEnum(ActionRequestEnum.PREVIOUS_CREATE_OBSERVATION);
                }
                return new PersistenceResponse()
                        .setResourceName("Confirmar sobrescritura de información")
                        .setActionRequestEnum(ActionRequestEnum.PREVIOUS_CREATE);
            }
            if (Integer.parseInt(storeProcedureResult.get(0).toString()) == ActionRequestEnum.CREATE.getIdentifier()) {
                return new PersistenceResponse()
                        .setResourceName("Desembolso - Mensual")
                        .setActionRequestEnum(ActionRequestEnum.CREATE);
            }
            if (Integer.parseInt(storeProcedureResult.get(0).toString()) == ActionRequestEnum.OVERWRITE.getIdentifier()) {
                return new PersistenceResponse()
                        .setResourceName("Desembolso - Mensual")
                        .setActionRequestEnum(ActionRequestEnum.OVERWRITE);
            }
        }
        Object obj = storeProcedureResult.get(0);
        int actionRequestCode = Integer.parseInt(((Object[]) obj)[((Object[]) obj).length - 1].toString());
        FileDocumentDTO fileDocumentDTO;
        PersistenceResponse persistenceResponse = new PersistenceResponse();

        if (actionRequestCode == ActionRequestEnum.OBSERVATION.getIdentifier()) {
            fileDocumentDTO = generateReportsService
                    .generateExcelFileDocumentDTOFromRawObject(setObservationsReportHeaders(), storeProcedureResult,
                            "Desembolso mensual - Observaciones");
            persistenceResponse.setResourceName(fileDocumentDTO.getName());
            persistenceResponse.setActionRequestEnum(ActionRequestEnum.OBSERVATION);
            persistenceResponse.setData(fileDocumentDTO);
        }
        if (actionRequestCode == ActionRequestEnum.RESOURCE_NOT_FOUND.getIdentifier()) {
            fileDocumentDTO = generateReportsService
                    .generateExcelFileDocumentDTOFromRawObject(setObservationRequestHeaders(), storeProcedureResult,
                            "Desembolso Mensual - Números de operación duplicados");
            persistenceResponse.setResourceName(fileDocumentDTO.getName());
            persistenceResponse.setActionRequestEnum(ActionRequestEnum.RESOURCE_NOT_FOUND);
            persistenceResponse.setData(fileDocumentDTO);
        }
        return persistenceResponse;
    }

    public PersistenceResponse exportFormatErrors(List data) {
        FileDocumentDTO fileDocumentDTO = generateReportsService
                .generateExcelFileDocumentDTO(setObservationsReportHeaders(), data,
                        "Desembolso mensual - Errores de formato");

        return new PersistenceResponse()
                .setResourceName(fileDocumentDTO.getName())
                .setData(fileDocumentDTO);
    }

    public List setObservationsReportHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("NUMERO_OPERACION");
        headers.add("FECHA_DESEMBOLSO");
        headers.add("SEGURO");
        headers.add("NUMERO_SOLICITUD");
        headers.add("ASEGURADO");
        headers.add("SEXO");
        headers.add("NUMERO_IDENTIFICACION");
        headers.add("EX");
        headers.add("FECHA_NACIMIENTO");
        headers.add("EDAD");
        headers.add("NACIONALIDAD");
        headers.add("PESO");
        headers.add("ESTATURA");
        headers.add("ESTADO");
        headers.add("PLAZO");
        headers.add("ESTADO_CONTRATO");
        headers.add("MONEDA");
        headers.add("SALDO_CONTRATO");
        headers.add("TASA_MENSUAL");
        headers.add("PRIMA_MENSUAL");
        headers.add("OBSERVACIONES");
        return headers;
    }
    public List setObservationRequestHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("FECHA_SOLICITUD");
        headers.add("DESCRIPCION");
        headers.add("NUMERO_OPERACION");
        headers.add("PLAZO");
        headers.add("MONTO_SOLICITADO");
        headers.add("NUMERO_SOLICITUD");
        return headers;
    }
}
