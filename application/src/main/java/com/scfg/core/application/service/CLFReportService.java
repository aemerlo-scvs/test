package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CLFReportUseCase;
import com.scfg.core.application.port.out.CLFReportPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.ClfProcessRequestDTO;
import com.scfg.core.domain.dto.credicasas.CommercialReportDTO;
import com.scfg.core.domain.dto.credicasas.SuscriptionReportDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.SearchReportParamDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class CLFReportService implements CLFReportUseCase {

    private final CLFReportPort clfReportPort;
    private final GenerateReportsService generateReportsService;

    @Override
    public ClfProcessRequestDTO getReportData(SearchReportParamDTO searchReportParamDTO) {
        List susListRep = clfReportPort.getReport(searchReportParamDTO);
        List<String> headers = new ArrayList<>();
        headers.add("CARTERA");
        headers.add("POLIZA");
        headers.add("FECHA LLENADO DJS");
        headers.add("NUMERO DE OPERACION");
        headers.add("TIPO DE OPERACIÓN");
        headers.add("No. SOLICITUD");
        headers.add("No. CERTIFICADO");
        headers.add("ASEGURADO");
        headers.add("GENERO");
        headers.add("CEDULA DE IDENTIDAD");
        headers.add("EX");
        headers.add("FECHA DE NACIMIENTO");
        headers.add("EDAD");
        headers.add("NACIONALIDAD");
        headers.add("PESO");
        headers.add("ESTATURA");
        headers.add("TIPO DE ASEGURADO");
        headers.add("MONEDA");
        headers.add("MONTO VIGENTE");
        headers.add("MONTO SOLICITADO");
        headers.add("MONTO ACUMULADO");
        headers.add("PLAZO CREDITO");
        headers.add("ESTADO");
        headers.add("MOTIVO PENDIENTE");
        headers.add("MOTIVO INACTIVACIÓN");
        headers.add("TIPO DE ACEPTACIÓN");
        headers.add("% DE EXTRA PRIMA");
        headers.add("MOTIVO EXTRA PRIMA");
        headers.add("MOTIVO EXCLUSION");
        headers.add("MOTIVO RECHAZO");
        headers.add("COMENTARIO RECHAZO");
        headers.add("COBERTURA OTORGADA");
        headers.add("FECHA DE PRONUNCIAMIENTO");
        headers.add("FECHA DE CARGA DJS");
        headers.add("FECHA DE CARGA CERTIFICADO");
        headers.add("FECHA DE CARGA DE CARTA");
        headers.add("CLIENTE PEP");
        headers.add("ESTADO PEP");
        headers.add("FECHA DE PRONUNCIAMIENTO PEP");
        headers.add("SUCURSAL");
        headers.add("GESTOR");
        headers.add("FECHA DE LA OPERACION");
        headers.add("TIPO DE SUSCRIPCION");
        headers.add("TIPO DE FIRMA");
        List<FileDocumentDTO> files = new ArrayList<>();
        FileDocumentDTO file = this.generateReportsService.generateExcelFileDocumentDTOFromRawObject(headers,new ArrayList<>(susListRep),"Reporte-Suscripcion-Lafuente");
        files.add(file);

        ClfProcessRequestDTO clfProcessRequestDTO = ClfProcessRequestDTO.builder()
                .files(files)
                .response(1)
                .message("Reporte de Suscripción generado exitósamente")
                .build();

        return clfProcessRequestDTO;
    }

    @Override
    public ClfProcessRequestDTO getComercialReportData(SearchReportParamDTO searchReportParamDTO) {
        List susListRep = clfReportPort.getComercialReport(searchReportParamDTO);
        List<String> headers = new ArrayList<>();
        headers.add("CARTERA");
        headers.add("POLIZA");
        headers.add("FECHA LLENADO DJS");
        headers.add("NUMERO DE OPERACION");
        headers.add("TIPO DE OPERACIÓN");
        headers.add("No. SOLICITUD");
        headers.add("No. CERTIFICADO");
        headers.add("ASEGURADO");
        headers.add("GENERO");
        headers.add("CEDULA DE IDENTIDAD");
        headers.add("EX");
        headers.add("FECHA DE NACIMIENTO");
        headers.add("EDAD");
        headers.add("NACIONALIDAD");
        headers.add("PESO");
        headers.add("ESTATURA");
        headers.add("TIPO DE ASEGURADO");
        headers.add("MONEDA");
        headers.add("MONTO VIGENTE");
        headers.add("MONTO SOLICITADO");
        headers.add("MONTO ACUMULADO");
        headers.add("PLAZO CREDITO");
        headers.add("ESTADO");
        headers.add("MOTIVO PENDIENTE");
        headers.add("MOTIVO INACTIVACIÓN");
        headers.add("TIPO DE ACEPTACIÓN");
        headers.add("% DE EXTRA PRIMA");
        headers.add("MOTIVO EXTRA PRIMA");
        headers.add("MOTIVO EXCLUSION");
        headers.add("MOTIVO RECHAZO");
        headers.add("COMENTARIO RECHAZO");
        headers.add("COBERTURA OTORGADA");
        headers.add("FECHA DE PRONUNCIAMIENTO");
        headers.add("FECHA DE CARGA DJS");
        headers.add("FECHA DE CARGA CERTIFICADO");
        headers.add("FECHA DE CARGA DE CARTA");
        headers.add("SUCURSAL");
        headers.add("GESTOR");
        headers.add("FECHA DE LA OPERACION");
        headers.add("TIPO DE SUSCRIPCION");
        headers.add("TIPO DE FIRMA");
        List<FileDocumentDTO> files = new ArrayList<>();
        FileDocumentDTO file = this.generateReportsService.generateExcelFileDocumentDTOFromRawObject(headers,new ArrayList<>(susListRep),"Reporte-Comercial-Lafuente");
        files.add(file);

        ClfProcessRequestDTO clfProcessRequestDTO = ClfProcessRequestDTO.builder()
                .files(files)
                .response(1)
                .message("Reporte Comercial generado exitósamente")
                .build();

        return clfProcessRequestDTO;
    }
}
