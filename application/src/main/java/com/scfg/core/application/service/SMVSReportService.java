package com.scfg.core.application.service;

import com.scfg.core.application.port.in.SMVSUseReport;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.MyProperties;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.*;
import com.scfg.core.domain.smvs.ParametersFromDTO;
import com.scfg.core.domain.smvs.TempCajerosDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.*;

import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMVSReportService implements SMVSUseReport {

    private final GeneralRequestPort generalRequestPort;
    private final PolicyPort policyPort;
    private final GenerateReportsService generateReportsService;
    private final ReportPort reportPort;
    private final MyProperties myProperties;
    private final TempCajerosPort tempCajerosPort;
    private final BankCashiersAgencyPort cashiersAgencyPort;


    @Override
    public PageableDTO getAllSMVSRequestByPage(ParametersFromDTO filters) {
        return generalRequestPort.findAllSMVSRequestByPage(filters);
    }

    @Override
    public FileDocumentDTO getReportSMVSFileExcel(Date startDate, Date toDate, Integer statusRequest) {
        FileDocumentDTO fileDocumentDTO = new FileDocumentDTO();
        List<String> formattedHeaderList = new ArrayList<>();
        formattedHeaderList.add("Nombre Completo Asegurado");
        formattedHeaderList.add("Número de CI");
        formattedHeaderList.add("Exp");
        formattedHeaderList.add("Nacionalidad");
        formattedHeaderList.add("Género");
        formattedHeaderList.add("Edad");
        formattedHeaderList.add("Estado Civil");
        formattedHeaderList.add("Fecha de Nacimiento");
        formattedHeaderList.add("Dirección Domicilio");
        formattedHeaderList.add("Telf. Domicilio");
        formattedHeaderList.add("Celular");
        formattedHeaderList.add("Correo Electrónico");
        formattedHeaderList.add("Actividad/Ocupación");
        formattedHeaderList.add("Fecha de Venta");
        formattedHeaderList.add("Sucursal");
        formattedHeaderList.add("Zona");
        formattedHeaderList.add("Agencia");
        formattedHeaderList.add("Vendedor");
        formattedHeaderList.add("No. Comprobante de Pago");
        formattedHeaderList.add("Monto Pago");
        formattedHeaderList.add("Estado Solicitud");
        formattedHeaderList.add("Número de la Póliza/Certificado");
        formattedHeaderList.add("Fecha de Activación/Inicio de Vigencia");
        formattedHeaderList.add("Fecha Fin de Vigencia");
        formattedHeaderList.add("Capital Asegurado");
        formattedHeaderList.add("Prima Total");
        formattedHeaderList.add("Prima Neta");
        formattedHeaderList.add("Comisión Intermediario");
        formattedHeaderList.add("Estado de la Póliza/Certificado");
        formattedHeaderList.add("Contador");

        List<Object> list = policyPort.getAllSMVSSubscriptionReport(startDate, toDate, statusRequest);

        if (list.size() > 0) {
            fileDocumentDTO = generateReportsService.generateExcelFileDocumentDTOFromRawObject(formattedHeaderList, list, "SMVS-REPORTE-VENTAS");
        }

        return fileDocumentDTO;
    }

    @Override
    public FileDocumentDTOInf ReportSUMSReportCommercials(ReportRequestPolicyDTO reportRequestPolicyDTO) {

        List<VCMAReportDTO> list = reportPort.getReportCommercials(reportRequestPolicyDTO.getDatefrom(), reportRequestPolicyDTO.getDateto());
        String path = myProperties.getPathReportCommercials();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        FileDocumentDTOInf fd = new FileDocumentDTOInf();
        // List<Object[]> lis= policyPort.getAsciiVentas(, );
        Integer cantAcumulado = 0;
        if (list != null && list.size() > 0) {
            try (InputStream inputStream = new FileInputStream(path)) {
                try {

                    XSSFWorkbook wb = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = wb.getSheet("DBA");
                    XSSFTable table = sheet.getTables().get(0);
                    LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
                    Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));

                    CTTable ctTable = table.getCTTable();
                    ctTable.setRef("A1:J" + (list.size() + 1));
                    int rowCount = (int) ctTable.getTotalsRowCount();
                    for (VCMAReportDTO vcmaReportDTO : list) {
                        XSSFRow row = sheet.createRow(++rowCount);
                        int columnCount = 0;
                        XSSFCell cell = row.createCell(columnCount);
                        cell = row.createCell(0);
                        cell.setCellValue(vcmaReportDTO.getManagerId());
                        cell = row.createCell(1);
                        cell.setCellValue(vcmaReportDTO.getNames());
                        cell = row.createCell(2);
                        cell.setCellValue(vcmaReportDTO.getAgencyId());
                        cell = row.createCell(3);
                        cell.setCellValue(vcmaReportDTO.getDescriptiona());
                        cell = row.createCell(4);
                        cell.setCellValue(vcmaReportDTO.getBranchOfficeId());
                        cell = row.createCell(5);
                        cell.setCellValue(vcmaReportDTO.getDescriptions());
                        cell = row.createCell(6);
                        cell.setCellValue(vcmaReportDTO.getZonesId());
                        cell = row.createCell(7);
                        cell.setCellValue(vcmaReportDTO.getDescriptionz());
                        cell = row.createCell(8);
                        cell.setCellValue(vcmaReportDTO.getNump());
                        cell = row.createCell(9);
                        cell.setCellValue(vcmaReportDTO.getMontop());
                        cantAcumulado = cantAcumulado + vcmaReportDTO.getNump();
                    }
                    //falto ocultar la  hoja de datos DBA
                    //cambiar el nombre del titulo
                    XSSFSheet dataSheet = wb.getSheet("SEGUIMIENTO COMERCIAL");
                    XSSFRow changeRowTittle = dataSheet.getRow(0);
                    XSSFCell changeTittle = changeRowTittle.getCell(0);
                    LocalDate firstDayMonth = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
                    String mensuales = "VENTAS ACUMULADO  DE LA FECHA  DESDE " + firstDayMonth.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                    changeTittle.setCellValue(mensuales);
//para la hoja tres
                    XSSFSheet Sheet1 = wb.getSheet("SEGUIMIENTO VENTAS X AGENCIA");
                    XSSFRow ChangeRowTitle1 = Sheet1.getRow(0);
                    XSSFCell ChangeColumnTiTle1 = ChangeRowTitle1.getCell(2);
                    String Title1 = "CANTIDAD DE PÓLIZAS VENDIDAS POR AGENCIAS, ACUMULADAS DESDE " + firstDayMonth.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                    ChangeColumnTiTle1.setCellValue(Title1);

                    XSSFSheet Sheet2 = wb.getSheet("RANKING SUCURSAL");
                    XSSFRow ChangeRowTitle2 = Sheet2.getRow(2);
                    XSSFCell ChangeColumnTiTle2 = ChangeRowTitle2.getCell(1);
                    String Title2 = formatter.format(reportRequestPolicyDTO.getDateto());
                    ChangeColumnTiTle2.setCellValue(Title2);
                    wb.setForceFormulaRecalculation(true);
                    //para la hoja de datos DBS
                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                    wb.write(boas);
                    fd.setContent(Base64.getEncoder().encodeToString(boas.toByteArray()));
                    fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    String titleexcel = "SMVS-REPORTE-COMERCIAL" + new SimpleDateFormat("ddMMyyyy").format(reportRequestPolicyDTO.getDateto());
                    fd.setName(titleexcel);
                    fd.setCantAcumulado(cantAcumulado);

                } catch (IOException e) {
                    log.error("ReportSUMSReportCommercials -->" + e.getMessage());
                }
            } catch (Exception ex) {
                log.error("ReportSUMSReportCommercials -->" + ex.getMessage());
            } finally {
                return fd;
            }
        }

        return fd;
    }

    @Override
    public FileDocumentDTOInf ReportSUMSReportCommercialsNew(ReportRequestPolicyDTO reportRequestPolicyDTO) {
        //List<VCMAReportDTO> list = reportPort.getReportCommercials(reportRequestPolicyDTO.getDatefrom(), reportRequestPolicyDTO.getDateto());

        String path = myProperties.getPathReportCommercials();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        FileDocumentDTOInf fd = new FileDocumentDTOInf();
        Map<String, Object> pr = reportPort.getRanking(reportRequestPolicyDTO.getDatefrom(), reportRequestPolicyDTO.getDateto());
        List<Object[]> lis = policyPort.getAsciiVentas(reportRequestPolicyDTO.getDatefrom(), reportRequestPolicyDTO.getDateto());
        List<AsciiVentasDto> AsciiVentasDtos = lis.stream()
                .map(AsciiVentasDto::new)
                .collect(Collectors.toList());

        Integer cantAcumulado = 0;
        if (AsciiVentasDtos != null && AsciiVentasDtos.size() > 0) {
            try (InputStream inputStream = new FileInputStream(path)) {
                try {

                    ZipSecureFile.setMinInflateRatio(0);

                    XSSFWorkbook wb = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = wb.getSheet("ASCII VENTAS");
                    XSSFTable table = sheet.getTables().get(0);
                    XSSFCreationHelper createHelper = wb.getCreationHelper();
                    XSSFCellStyle dateCellStyle = wb.createCellStyle();
                    dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
//                    LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
//                    Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));

                    CTTable ctTable = table.getCTTable();
                    ctTable.setRef("A1:O" + (AsciiVentasDtos.size() + 1));
                    int rowCount = 0;

                    for (AsciiVentasDto vcmaReportDTO : AsciiVentasDtos) {
                        XSSFRow row = sheet.createRow(++rowCount);
                        //  Row row = sheet.createRow(++rowCount);
                        int columnCount = 0;
                        //Cell cell = row.createCell(columnCount++);

                        XSSFCell cell = row.createCell(columnCount);
                        cell = row.createCell(0);
                        cell.setCellValue(vcmaReportDTO.getMonthYear());
                        cell = row.createCell(1);
                        cell.setCellValue((Date) vcmaReportDTO.getPaymentDate());
                        cell.setCellStyle(dateCellStyle);
                        cell = row.createCell(2);
                        cell.setCellValue(vcmaReportDTO.getStatus());
                        cell = row.createCell(3);
                        cell.setCellValue(vcmaReportDTO.getCantActivation());
                        cell = row.createCell(4);
                        cell.setCellValue(vcmaReportDTO.getBranchOfficeId());
                        cell = row.createCell(5);
                        cell.setCellValue(vcmaReportDTO.getBranch());
                        cell = row.createCell(6);
                        cell.setCellValue(vcmaReportDTO.getZones());
                        cell = row.createCell(7);
                        cell.setCellValue(vcmaReportDTO.getAgencyId());
                        cell = row.createCell(8);
                        cell.setCellValue(vcmaReportDTO.getAgency());
                        cell = row.createCell(9);
                        cell.setCellValue(vcmaReportDTO.getCodCajero());
                        cell = row.createCell(10);
                        cell.setCellValue(vcmaReportDTO.getNamesCajero());
                        cell = row.createCell(11);
                        cell.setCellValue(vcmaReportDTO.getCantVentas());
                        cell = row.createCell(12);
                        cell.setCellValue(vcmaReportDTO.getMonthYear());
                        cell = row.createCell(13);
                        cell.setCellValue(vcmaReportDTO.getYears());
                        cell = row.createCell(14);
                        cell.setCellValue((Date) vcmaReportDTO.getCourtDate());
                        cell.setCellStyle(dateCellStyle);

                        // cantAcumulado = cantAcumulado + vcmaReportDTO.getNump();
                    }
                    // Cargando a  los cajeros
                    XSSFSheet sheetCajeros = wb.getSheet("DB-CAJERO");
                    List<RankingCajeros> rankingCajerosList = (List<RankingCajeros>) pr.get("cajeros");
                    loadCajeros(rankingCajerosList, sheetCajeros);

                    //Cargando agencia
                    XSSFSheet sheetAgencia = wb.getSheet("DB-AGENCIA");
                    List<RankingAgenciasDto> rankingAgenciasDtos = (List<RankingAgenciasDto>) pr.get("agencias");
                    loadAgencia(rankingAgenciasDtos, sheetAgencia);

                    //cargando zonas
                    XSSFSheet sheetZona = wb.getSheet("DB-ZONA");
                    List<RankingZonasDto> rankingZonasDtos = (List<RankingZonasDto>) pr.get("zonas");
                    loadZonas(rankingZonasDtos, sheetZona);
                    // Cargando Sucursales
                    XSSFSheet sheetSucursal = wb.getSheet("DB-SUCURSAL");
                    List<RankingSucursalDto> rankingSucursalDtos = (List<RankingSucursalDto>) pr.get("sucursales");
                    loadSucursal(rankingSucursalDtos, sheetSucursal);


                    // prestaña agencias sin ventas
                    XSSFSheet sheetAgenciaSN = wb.getSheet("DB-AGENCIA-SNV");
                    List<RankingAgenciasDto> agenciasDtosSN = rankingAgenciasDtos.stream().filter(p -> p.getSales().equals(0)).collect(Collectors.toList());
                    //loadAgenciaSN(agenciasDtosSN,sheetAgenciaSN);
                    if (agenciasDtosSN.size() > 0)
                        loadAgencia(agenciasDtosSN, sheetAgenciaSN);

                    wb.setForceFormulaRecalculation(true);
                    //para la hoja de datos DBS
                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                    wb.write(boas);
                    fd.setContent(Base64.getEncoder().encodeToString(boas.toByteArray()));
                    fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    String titleexcel = "SMVS-REPORTE-COMERCIAL" + new SimpleDateFormat("ddMMyyyy").format(reportRequestPolicyDTO.getDateto());
                    fd.setName(titleexcel);
                    fd.setCantAcumulado(cantAcumulado);

                } catch (IOException e) {
                    log.error("ReportSUMSReportCommercials -->" + e.getMessage());
                }
            } catch (Exception ex) {
                log.error("ReportSUMSReportCommercials -->" + ex.getMessage());
            } finally {
                return fd;
            }
        }

        return fd;
    }

    private void loadSucursal(List<RankingSucursalDto> rankingSucursalDtos, XSSFSheet sheet) {
        XSSFTable table = sheet.getTables().get(0);
//                    LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
//                    Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));

        CTTable ctTable = table.getCTTable();
        ctTable.setRef("A1:K" + (rankingSucursalDtos.size() + 1));
        int rowCount = 0;
        int aux = 0;
        int index = -1;
        int index2 = -1;
        for (RankingSucursalDto cajeros : rankingSucursalDtos) {
            index2 = cajeros.getMes();
            aux = autoincermente(aux, index, index2);
            XSSFRow row = sheet.createRow(++rowCount);
            //  Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            //Cell cell = row.createCell(columnCount++);

            XSSFCell cell = row.createCell(columnCount);
            cell = row.createCell(0);
            cell.setCellValue(cajeros.getReporte());
            cell = row.createCell(1);
            cell.setCellValue((Integer) aux);
            cell = row.createCell(2);
            cell.setCellValue(cajeros.getCorteMes());
            cell = row.createCell(3);
            cell.setCellValue(cajeros.getMes());
            cell = row.createCell(4);
            cell.setCellValue(cajeros.getYears());
            cell = row.createCell(5);
            cell.setCellValue(cajeros.getBranchOffice());
            cell = row.createCell(6);
            cell.setCellValue(cajeros.getAmountZones());
            cell = row.createCell(7);
            cell.setCellValue(cajeros.getAverageSales());
            cell = row.createCell(8);
            cell.setCellValue(cajeros.getSales());
            cell = row.createCell(9);
            cell.setCellValue(cajeros.getAmountSales());
            cell = row.createCell(10);
            cell.setCellValue(cajeros.getObjectives());

            index = index2;
            // cantAcumulado = cantAcumulado + vcmaReportDTO.getNump();
        }
    }

    private void loadZonas(List<RankingZonasDto> rankingZonasDtos, XSSFSheet sheet) {
        XSSFTable table = sheet.getTables().get(0);
//                    LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
//                    Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));

        CTTable ctTable = table.getCTTable();
        ctTable.setRef("A1:L" + (rankingZonasDtos.size() + 1));
        int rowCount = 0;
        int aux = 0;
        int index = -1;
        int index2 = -1;
        for (RankingZonasDto cajeros : rankingZonasDtos) {
            index2 = cajeros.getMes();
            aux = autoincermente(aux, index, index2);
            XSSFRow row = sheet.createRow(++rowCount);
            //  Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            //Cell cell = row.createCell(columnCount++);

            XSSFCell cell = row.createCell(columnCount);
            cell = row.createCell(0);
            cell.setCellValue(cajeros.getReporte());
            cell = row.createCell(1);
            cell.setCellValue(aux);
            cell = row.createCell(2);
            cell.setCellValue(cajeros.getCorteMes());
            cell = row.createCell(3);
            cell.setCellValue(cajeros.getMes());
            cell = row.createCell(4);
            cell.setCellValue(cajeros.getYears());
            cell = row.createCell(5);
            cell.setCellValue(cajeros.getZonas());
            cell = row.createCell(6);
            cell.setCellValue(cajeros.getBranchOffice());
            cell = row.createCell(7);
            cell.setCellValue(cajeros.getAmountZones());
            cell = row.createCell(8);
            cell.setCellValue(cajeros.getSales());
            cell = row.createCell(9);
            cell.setCellValue(cajeros.getAmountSales());
            cell = row.createCell(10);
            cell.setCellValue(cajeros.getAverageSales());
            cell = row.createCell(11);
            cell.setCellValue(cajeros.getObjectives());
            index = index2;
            // cantAcumulado = cantAcumulado + vcmaReportDTO.getNump();
        }
    }

    private void loadAgencia(List<RankingAgenciasDto> rankingAgenciasDtos, XSSFSheet sheet) {
        XSSFTable table = sheet.getTables().get(0);
//                    LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
//                    Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));

        CTTable ctTable = table.getCTTable();
        ctTable.setRef("A1:N" + (rankingAgenciasDtos.size() + 1));
        int rowCount = 0;
        int aux = 0;
        int index = -1;
        int index2 = -1;
        for (RankingAgenciasDto cajeros : rankingAgenciasDtos) {
            index2 = cajeros.getMes();
            aux = autoincermente(aux, index, index2);
            XSSFRow row = sheet.createRow(++rowCount);
            //  Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            //Cell cell = row.createCell(columnCount++);

            XSSFCell cell = row.createCell(columnCount);
            cell = row.createCell(0);
            cell.setCellValue(cajeros.getReporte());
            cell = row.createCell(1);
            cell.setCellValue(aux);
            cell = row.createCell(2);
            cell.setCellValue(cajeros.getCorteMes());
            cell = row.createCell(3);
            cell.setCellValue(cajeros.getMes());
            cell = row.createCell(4);
            cell.setCellValue(cajeros.getYears());
            cell = row.createCell(5);
            cell.setCellValue(cajeros.getCodAgency());
            cell = row.createCell(6);
            cell.setCellValue(cajeros.getAgencies());
            cell = row.createCell(7);
            cell.setCellValue(cajeros.getBranchOffice());
            cell = row.createCell(8);
            cell.setCellValue(cajeros.getZonas());
            cell = row.createCell(9);
            cell.setCellValue(cajeros.getAmountAgency());
            cell = row.createCell(10);
            cell.setCellValue(cajeros.getSales());
            cell = row.createCell(11);
            cell.setCellValue(cajeros.getAmountSales());
            cell = row.createCell(12);
            cell.setCellValue(cajeros.getAverageSales());
            cell = row.createCell(13);
            cell.setCellValue(cajeros.getObjectives());

            index = index2;
            // cantAcumulado = cantAcumulado + vcmaReportDTO.getNump();
        }
    }

    private void loadCajeros(List<RankingCajeros> rankingCajerosList, XSSFSheet sheet) {

        XSSFTable table = sheet.getTables().get(0);
//                    LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
//                    Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));

        CTTable ctTable = table.getCTTable();
        ctTable.setRef("A1:T" + (rankingCajerosList.size() + 1));
        int rowCount = 0;
        int aux = 0;
        int index = -1;
        int index2 = -1;

        for (RankingCajeros cajeros : rankingCajerosList) {

            index2 = cajeros.getMes();
            aux = autoincermente(aux, index, index2);
            XSSFRow row = sheet.createRow(++rowCount);
            //  Row row = sheet.createRow(++rowCount);
            int columnCount = 0;
            //Cell cell = row.createCell(columnCount++);

            XSSFCell cell = row.createCell(columnCount);
            cell = row.createCell(0);
            cell.setCellValue(cajeros.getFunc());
            cell = row.createCell(1);
            cell.setCellValue(aux);
            cell = row.createCell(2);
            cell.setCellValue(cajeros.getCargo());
            cell = row.createCell(3);
            cell.setCellValue((Date) cajeros.getCorteMes());
            cell = row.createCell(4);
            cell.setCellValue(cajeros.getMes());
            cell = row.createCell(5);
            cell.setCellValue(cajeros.getYears());
            cell = row.createCell(6);
            cell.setCellValue(cajeros.getSellerId());
            cell = row.createCell(7);
            cell.setCellValue(cajeros.getSellerName());
            cell = row.createCell(8);
            cell.setCellValue(cajeros.getAgencyId());
            cell = row.createCell(9);
            cell.setCellValue(cajeros.getAgencyName());
            cell = row.createCell(10);
            cell.setCellValue(cajeros.getBranchOfficeId());
            cell = row.createCell(11);
            cell.setCellValue(cajeros.getBranchOffice());
            cell = row.createCell(12);
            cell.setCellValue(cajeros.getZones());
            cell = row.createCell(13);
            cell.setCellValue(cajeros.getRequestStatus());
            cell = row.createCell(14);
            cell.setCellValue(cajeros.getVentas());
            cell = row.createCell(15);
            cell.setCellValue(cajeros.getFechaIng());
            cell = row.createCell(16);
            cell.setCellValue(cajeros.getAntiguiedad());
            cell = row.createCell(17);
            cell.setCellValue(cajeros.getCantCajeros());
            cell = row.createCell(18);
            cell.setCellValue(cajeros.getTiene());
            cell = row.createCell(19);
            cell.setCellValue(cajeros.getRango());

            index = index2;
            // cantAcumulado = cantAcumulado + vcmaReportDTO.getNump();
        }
    }

    private int autoincermente(int aux, int index, int index2) {
        //aux=0
        //index=9
        //index1=10
        if (index != index2)
            return 1;
        else return aux + 1;
    }

    @Override
    public PersistenceResponse savecajeros(List<TempCajerosDto> tempCajerosDtoList) {
        PersistenceResponse persistenceResponse = tempCajerosPort.save(tempCajerosDtoList);
        String integer = tempCajerosPort.getAsignacionCajerosPorCadaMes();
        persistenceResponse.setData(integer);
        return persistenceResponse;
    }

    @Override
    public List<DetailsLoadCashiers1> getDetailLoadCashiers() {
        return cashiersAgencyPort.findDetailsLoadCashiers();
    }

    @Override
    public FileDocumentDTO getFormatLoadFileSales() {
        String path = myProperties.getPathFormatFileLoadSales();
        FileDocumentDTO fd = new FileDocumentDTO();
        try {
            InputStream imInputStream=new FileInputStream(path);
            byte[] bytes =new byte[imInputStream.available()];
            try {
                imInputStream.read(bytes);
                imInputStream.close();
                fd.setContent(Base64.getEncoder().encodeToString(bytes));
            } catch (IOException e) {
                log.error("SMVSReportService-getFormatLoadFileSales",e.getMessage());
                throw new RuntimeException(e);
            }
            fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String title = "Formato-capacidad instalada cajeros";
            fd.setName(title);

        } catch (FileNotFoundException e) {
            log.error("SMVSReportService-getFormatLoadFileSales",e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fd;
    }


}
