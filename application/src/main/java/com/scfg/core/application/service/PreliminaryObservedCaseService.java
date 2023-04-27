package com.scfg.core.application.service;

import com.scfg.core.application.port.in.PreliminaryObservedCaseUseCase;
import com.scfg.core.application.port.out.ObservedCasePort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.PreliminaryObservedCaseDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class PreliminaryObservedCaseService implements PreliminaryObservedCaseUseCase {

    private final ObservedCasePort observedCasePort;

    @Override
    public List<PreliminaryObservedCaseDTO> getPreliminaryObservedCasesDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return observedCasePort.getPreliminaryObservedCasesDHLFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<PreliminaryObservedCaseDTO> getPreliminaryObservedCasesDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        return observedCasePort.getPreliminaryObservedCasesDHNFiltered(monthId, yearId, insurancePolicyHolderId);
    }

    @Override
    public List<String> getPreliminaryObservedCasesColumns() {
        List<String> columns = new ArrayList<>();
        columns.addAll(HelpersConstants.COLUMN_NAMES_PRELIMINARY_OBSERVED_CASE);
        return columns;

        /*columns.add("NUMERO_OPERACION");
        columns.add("NOMBRE_COMPLETO_ASEGURADO");
        columns.add("CEDULA_ASEGURADO");
        columns.add("MONTO_ACUMULADO");
        columns.add("COMENTARIOS_MES_ACTUAL");
        columns.add("DESEMBOLSO_MES_ACTUAL");
        columns.add("DESEMBOLSO_MES_ANTERIOR");*/

    }
}
