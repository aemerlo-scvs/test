package com.scfg.core.application.service;

import com.scfg.core.application.port.in.FabolousUseCase;
import com.scfg.core.application.port.out.FabolousPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.FabolousDTO;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.fabolous.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class FabolousService implements FabolousUseCase {

    private final FabolousPort fabolousPort;
    private final GenerateReportsService generateReportsService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public PersistenceResponse registerReport(List<FabolousDTO> fabolousDTOS) {
        return fabolousPort.save(fabolousDTOS, true);
    }

    @Override
    public List<FabolousUploadDTO> getAllUploads() {
        return fabolousPort.getAllUpload();
    }

    @Override
    public FileDocumentDTO liquidationGenerateReport(FabolousReportDTO dates) {
        List<String> headers = new ArrayList<String>();
        headers.add("Nro Cuenta");
        headers.add("Sucursal");
        headers.add("Zona");
        headers.add("Agencia");
        headers.add("Código Cliente");
        headers.add("Nombre Completo del Cliente");
        headers.add("Identificación");
        headers.add("Ext");
        headers.add("Nacionalidad");
        headers.add("Fecha de Nacimiento");
        headers.add("Edad");
        headers.add("Estado Civil");
        headers.add("Domicilio");
        headers.add("Profesión");
        headers.add("Beneficiario");
        headers.add("Porcentaje");
        headers.add("Parentesco");
        headers.add("Gestor de Negocios");
        headers.add("Fecha Inicio de Vigencia");
        headers.add("Fin de Vigencia");
        headers.add("Capital Asegurado Bs.");
        headers.add("Prima");
        List<FabolousReportLiquidationDTO> getList = this.fabolousPort.liquidationGenerateReport(dates);
        FileDocumentDTO any = this.generateReportsService.generateExcelFileDocumentDTO(headers,new ArrayList(getList),"ReporteFabulosaLiquidacion");
        return any;
    }

    @Override
    public FileDocumentDTO liquidationGenerateDuplicateReport(FabolousReportDTO dates) {
        List<String> headers = new ArrayList<String>();
        headers.add("Nro Cuenta");
        headers.add("Sucursal");
        headers.add("Zona");
        headers.add("Agencia");
        headers.add("Código Cliente");
        headers.add("Nombre Completo del Cliente");
        headers.add("Identificación");
        headers.add("Ext");
        headers.add("Nacionalidad");
        headers.add("Fecha de Nacimiento");
        headers.add("Edad");
        headers.add("Estado Civil");
        headers.add("Domicilio");
        headers.add("Profesión");
        headers.add("Beneficiario");
        headers.add("Porcentaje");
        headers.add("Parentesco");
        headers.add("Gestor de Negocios");
        headers.add("Fecha Inicio de Vigencia");
        headers.add("Fin de Vigencia");
        headers.add("Capital Asegurado Bs.");
        headers.add("Prima");
        headers.add("Mes");
        headers.add("Duplicados");
        headers.add("Tipo de Exclusión");
        List<FabolousReportDuplicateLiquidationDTO> getList = this.fabolousPort.liquidationGenerateDuplicateReport(dates);
        FileDocumentDTO any = this.generateReportsService.generateExcelFileDocumentDTO(headers,new ArrayList(getList),"ReporteFabulosaDuplicados");
        return any;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public boolean deleteUploadReport(Long deleteId) {
        return this.fabolousPort.deleteUploadReport(deleteId);
    }

    @Override
    public FabolusResultResponseClient searchClient(FabolousSearchCltDTO client, Integer page, Integer size) {
        return fabolousPort.searchClient(client, page, size);
    }


}
