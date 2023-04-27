package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.alert.AlertJpaEntity;
import com.scfg.core.adapter.persistence.alert.AlertRepository;
import com.scfg.core.adapter.persistence.VCMA.repository.ReportRequestPolicyRepository;
import com.scfg.core.application.port.out.ReportRequestPolicyPort;
import com.scfg.core.application.service.EmailService;
import com.scfg.core.common.util.MyProperties;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.Emailbody;
import com.scfg.core.domain.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class ReportRequestPolicyBsPersistenceAdapter implements ReportRequestPolicyPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    ReportRequestPolicyRepository reportRequestPolicyRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    AlertRepository alertRepository;
    @Autowired
    MyProperties path;

    public ReportRequestPolicyRepository getReportRequestPolicyRepository() {
        return reportRequestPolicyRepository;
    }

    public void setReportRequestPolicyRepository(ReportRequestPolicyRepository reportRequestPolicyRepository) {
        this.reportRequestPolicyRepository = reportRequestPolicyRepository;
    }

    @Override
    public FileDocumentDTOInf getReport(ReportRequestPolicyDTO reportRequestPolicyDTO) {
        return generateExcel(reportRequestPolicyDTO);
    }

    @Override
    public FileDocumentDTOInf getReports() {
        return generateExcels(null, null);
    }

    @Override
    public List<VCMAReportDTO> getReportDates(ReportRequestPolicyDTO requestPolicyDTO) {
        List<VCMAReportDTO> dtoList = reportRequestPolicyRepository.GetAllDataForDates(requestPolicyDTO.getDatefrom(), requestPolicyDTO.getDateto());
        return dtoList;
    }

    @Override
    public List<ResultPolicyDtO> getConsolidado() {
        return reportRequestPolicyRepository.SetConsolidarPolizas();
    }

    @Override
    public FileDocumentDTOInf getExcelSinGestoresConsolidados() {
        FileDocumentDTOInf fd = new FileDocumentDTOInf();
        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("SING");
            XSSFTable table = sheet.createTable();
            table.setDisplayName("tabla.polizas");
            CTTable cttable = table.getCTTable();
            List<ResultPolicyDtO> list = getConsolidado();
            cttable.setRef("A1:C" + (list.size() + 1));
            XSSFRow rows = sheet.createRow(0);
            XSSFCell cells = rows.createCell(0);
            cells = rows.createCell(0);
            cells.setCellValue("poliza Number");
            cells = rows.createCell(1);
            cells.setCellValue("Names manager");
            cells = rows.createCell(2);
            cells.setCellValue("Cantidad");

            int rowCount = (int) cttable.getTotalsRowCount();
            for (ResultPolicyDtO item : list) {
                XSSFRow row = sheet.createRow(++rowCount);
                int columnCount = 0;
                XSSFCell cell = row.createCell(columnCount);
                cell = row.createCell(0);
                cell.setCellValue(item.getPolicy_number());
                cell = row.createCell(1);
                cell.setCellValue(item.getNames_manager_incorrect());
                cell = row.createCell(2);
                cell.setCellValue(item.getRepitcount());
            }
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            wb.write(boas);
            fd.setContent(Base64.getEncoder().encodeToString(boas.toByteArray()));
            fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String titleexcel = "Polizas sin consolidar" + new SimpleDateFormat("ddMMyyyy").format(new Date());
            fd.setName(titleexcel);
            return fd;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return fd;
        }

    }

    @Override
    public int updatepolicymanager() {
        return reportRequestPolicyRepository.UpdatePolizas();
    }

    @Override
    public boolean sendReportforMail(FileDocumentDTOMail fileDocumentDTOMail) throws MessagingException {
        try {
            //1 sacar de la base de datos los de destinarios
            //2 armar el archivos
            //3 enviar
            Object objlist = alertRepository.findAll();
            List<AlertJpaEntity> list = (List<AlertJpaEntity>) objlist;
            Optional<AlertJpaEntity> obj = list.stream().filter(a -> a.getAlert_id() == 1 && a.getEnvironment_id() == 0 && a.getStatus().equals("A")).findFirst();
            if (fileDocumentDTOMail != null && obj.isPresent()) {
                AlertJpaEntity alertJpaEntity = obj.get();
                LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(fileDocumentDTOMail.getDateTo());
                Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));
                LocalDate datstrart = DateUtils.asDateToLocalDate(datestart);
                Emailbody emailbody = new Emailbody();
                Locale spanishLocale = new Locale("es", "ES");
                DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy", spanishLocale);
                DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("dd MMMM", spanishLocale);
//Local time instance
                LocalDate localDate = DateUtils.asDateToLocalDate(fileDocumentDTOMail.getDateTo());
                LocalDate datefrom=DateUtils.asDateToLocalDate(fileDocumentDTOMail.getDateFrom());
//Get formatted String
                String p1 = FOMATTER2.format(datstrart);
                String p2 = FOMATTER.format(localDate);
                String p3 = FOMATTER2.format(datefrom);
                String message = alertJpaEntity.getMail_body();
                message = message.replaceAll("<P1>", p1);
                //remplazar la etiquetas <P1>
                message = message.replaceAll("<P2>", p2);//remplazar las etiquetas <P2>
                // remplazar la etique p3
                message = message.replaceAll("<P3>", p3);
                String subject = alertJpaEntity.getMail_subject();
                subject = subject.replaceAll("<P2>", p2);
                emailbody.setContent(message);
                emailbody.setSubject(subject);
                if (!alertJpaEntity.getMail_to().isEmpty()) {
                    emailbody.setEmail(alertJpaEntity.getMail_to().split(";"));
                }
                if (!alertJpaEntity.getMail_cc().isEmpty()) {
                    emailbody.setEmailcopy(alertJpaEntity.getMail_cc().split(";"));
                }
                byte[] bytes = Base64.getDecoder().decode(fileDocumentDTOMail.getContent().getBytes(StandardCharsets.UTF_8));
                emailbody.setBytes(bytes);
                emailbody.setName_attachment(fileDocumentDTOMail.getName() + ".xlsx");
                boolean b = emailService.sendEmail(emailbody);
                return b;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private FileDocumentDTOInf generateExcel(ReportRequestPolicyDTO reportRequestPolicyDTO) {
        FileDocumentDTOInf fd = new FileDocumentDTOInf();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        int cantAcumulado = 0;
        int cantSemanal = 0;
        try (InputStream inputStream = new FileInputStream(path.getPathexcel())) {
            try {

                XSSFWorkbook wb = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = wb.getSheet("DBA");
                XSSFTable table = sheet.getTables().get(0);
                LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
                Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));
                List<VCMAReportDTO> vcmaReportDTOListAcumulado = reportRequestPolicyRepository.GetAllDataForDates(datestart, reportRequestPolicyDTO.getDateto());
                CTTable ctTable = table.getCTTable();
                ctTable.setRef("A1:P" + (vcmaReportDTOListAcumulado.size() + 1));
                int rowCount = (int) ctTable.getTotalsRowCount();
                for (VCMAReportDTO vcmaReportDTO : vcmaReportDTOListAcumulado) {
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
                    cell.setCellValue(vcmaReportDTO.getP1());
                    cell = row.createCell(9);
                    cell.setCellValue(vcmaReportDTO.getSp1());
                    cell = row.createCell(10);
                    cell.setCellValue(vcmaReportDTO.getP2());
                    cell = row.createCell(11);
                    cell.setCellValue(vcmaReportDTO.getSp2());
                    cell = row.createCell(12);
                    cell.setCellValue(vcmaReportDTO.getP3());
                    cell = row.createCell(13);
                    cell.setCellValue(vcmaReportDTO.getSp3());
                    cell = row.createCell(14);
                    cell.setCellValue(vcmaReportDTO.getNump());
                    cell = row.createCell(15);
                    cell.setCellValue(vcmaReportDTO.getMontop());
                    cantAcumulado = cantAcumulado + vcmaReportDTO.getNump();
                }
                //falto ocultar la  hoja de datos DBA
                //cambiar el nombre del titulo
                XSSFSheet dataSheet = wb.getSheet("SEGUIMIENTO ACUMULADO");
                XSSFRow changeRowTittle = dataSheet.getRow(0);
                XSSFCell changeTittle = changeRowTittle.getCell(0);
                LocalDate firstDayMonth = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
                String mensuales = "DETALLE DE LA VENTAS ACUMULADO  DE LA FECHA  DESDE " + firstDayMonth.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                changeTittle.setCellValue(mensuales);
//para la hoja tres
                XSSFSheet Sheet1 = wb.getSheet("ACUMULADO VENTAS X AGENCIA");
                XSSFRow ChangeRowTitle1 = Sheet1.getRow(0);
                XSSFCell ChangeColumnTiTle1 = ChangeRowTitle1.getCell(3);
                String Title1 = "CANTIDAD DE PÓLIZAS VENDIDAS POR AGENCIAS, ACUMULADAS DESDE " + firstDayMonth.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                ChangeColumnTiTle1.setCellValue(Title1);
                //para la hoja de datos DBS

                XSSFSheet dataSheet2 = wb.getSheet("SEGUIMIENTO SEMANAL");
                XSSFRow changeRowTittle2 = dataSheet2.getRow(0);
                XSSFCell changeTittle2 = changeRowTittle2.getCell(0);
                LocalDate firstDayMonth2 = LocalDate.now();
//                String semanales = "DETALLE DE LAS VENTAS SEMANAL  DESDE " + firstDayMonth2.with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AL " + firstDayMonth2.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String semanales = "DETALLE DE LAS VENTAS SEMANAL  DESDE " + formatter.format(reportRequestPolicyDTO.getDatefrom()) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                changeTittle2.setCellValue(semanales);

                //para la hoja tres
                XSSFSheet Sheet3 = wb.getSheet("SEMANAL VENTAS X AGENCIAS");
                XSSFRow ChangeRowTitle3 = Sheet3.getRow(0);
                XSSFCell ChangeColumnTiTle3 = ChangeRowTitle3.getCell(3);
                String Title3 = "CANTIDAD DE PÓLIZAS VENDIDAS POR AGENCIAS, SEMANALES DESDE " + formatter.format(reportRequestPolicyDTO.getDatefrom()) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                ChangeColumnTiTle3.setCellValue(Title3);

                XSSFSheet sheet2 = wb.getSheet("DBS");
                XSSFTable table2 = sheet2.getTables().get(0);
                List<VCMAReportDTO> vcmaReportDTOListSemanal = reportRequestPolicyRepository.GetAllDataForDates(reportRequestPolicyDTO.getDatefrom(), reportRequestPolicyDTO.getDateto());
                CTTable ctTable2 = table2.getCTTable();
                ctTable2.setRef("A1:P" + (vcmaReportDTOListSemanal.size() + 1));
                int rowCount2 = (int) ctTable2.getTotalsRowCount();
                for (VCMAReportDTO vcmaReportDTO : vcmaReportDTOListSemanal) {
                    XSSFRow row = sheet2.createRow(++rowCount2);
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
                    cell.setCellValue(vcmaReportDTO.getP1());
                    cell = row.createCell(9);
                    cell.setCellValue(vcmaReportDTO.getSp1());
                    cell = row.createCell(10);
                    cell.setCellValue(vcmaReportDTO.getP2());
                    cell = row.createCell(11);
                    cell.setCellValue(vcmaReportDTO.getSp2());
                    cell = row.createCell(12);
                    cell.setCellValue(vcmaReportDTO.getP3());
                    cell = row.createCell(13);
                    cell.setCellValue(vcmaReportDTO.getSp3());
                    cell = row.createCell(14);
                    cell.setCellValue(vcmaReportDTO.getNump());
                    cell = row.createCell(15);
                    cell.setCellValue(vcmaReportDTO.getMontop());
                    cantSemanal = cantSemanal + vcmaReportDTO.getNump();
                }
//                XSSFSheet xssfSheet = wb.getSheet("ACUMULADO VENTAS X AGENCIA");
//                XSSFDrawing drawing = xssfSheet.createDrawingPatriarch();
//                XSSFChart chart = drawing.getCharts().get(0);
//                chart.setTitleText(mensuales);
//
//                XSSFSheet xssfSheet2 = wb.getSheet("SEMANAL VENTAS X AGENCIAS");
//                XSSFDrawing drawing2 = xssfSheet2.getDrawingPatriarch();
//                XSSFChart chart2 = drawing2.getCharts().get(0);
//                chart2.setTitleText(semanales);

                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                wb.write(boas);
                fd.setContent(Base64.getEncoder().encodeToString(boas.toByteArray()));
                fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                String titleexcel = "Ventas del Seguro Masivo de Vida + Cáncer" + new SimpleDateFormat("ddMMyyyy").format(reportRequestPolicyDTO.getDateto());
                fd.setName(titleexcel);
                fd.setCantAcumulado(cantAcumulado);
                fd.setCantSemanal(cantSemanal);
                //  return fd;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception es) {
            System.out.println(es.getMessage());
        } finally {
            return fd;
        }
    }

    private FileDocumentDTOInf generateExcels(LocalDate datefrom, LocalDate dateto) {
        FileDocumentDTOInf fd = new FileDocumentDTOInf();
        try (InputStream inputStream = new FileInputStream(path.getPathexcel())) {
            try {
                XSSFWorkbook wb = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = wb.getSheet("DBA");
                XSSFTable table = sheet.getTables().get(0);
                CTTable ctTable = table.getCTTable();
                ctTable.setRef("A1:P3");

                Object[][] bookData = {
                        {487178, "OSINAGA GARCIA LUIS", 165, "ALTO SAN PEDRO", 101, "SANTA CRUZ", 14, "ZONA XII", 0, 0, 0, 0, 0, 0, 0, 0},
                        {556969, "SUAREZ DE UDAETA CARLA LORENA", 189, "MONSEÑOR RIVERO", 101, "SANTA CRUZ", 3, "ZONA II", 0, 0, 0, 0, 0, 0, 0, 0},

                };
                System.out.println("cont -> tork: " + bookData.length);

                int rowCount = (int) ctTable.getTotalsRowCount();
                for (Object[] aBook : bookData) {
                    XSSFRow row = sheet.createRow(++rowCount);

                    int columnCount = 0;

                    XSSFCell cell = row.createCell(columnCount);

                    for (Object field : aBook) {
                        cell = row.createCell(++columnCount - 1);
                        if (field instanceof String) {
                            cell.setCellValue((String) field);
                        } else if (field instanceof Integer) {
                            cell.setCellValue((Integer) field);
                        }
                    }

                }


                XSSFSheet sheets = wb.getSheet("SEGUIMIENTO ACUMULADO");
                System.out.printf("lengt :" + sheets.getTables().size());
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                wb.write(boas);
                fd.setContent(Base64.getEncoder().encodeToString(boas.toByteArray()));
                fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                fd.setName("Test");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception es) {
            System.out.println(es.getMessage());
        } finally {
            return fd;
        }
    }

    public byte[] generateExcelInputStream(ReportRequestPolicyDTO reportRequestPolicyDTO) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        int cantAcumulado = 0;
        int cantSemanal = 0;
        try (InputStream inputStream = new FileInputStream(path.getPathexcel())) {
            try {

                XSSFWorkbook wb = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = wb.getSheet("DBA");
                XSSFTable table = sheet.getTables().get(0);
                LocalDate firstDayMonth1 = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
                Date datestart = DateUtils.asDate(firstDayMonth1.with(TemporalAdjusters.firstDayOfMonth()));
                List<VCMAReportDTO> vcmaReportDTOListAcumulado = reportRequestPolicyRepository.GetAllDataForDates(datestart, reportRequestPolicyDTO.getDateto());
                CTTable ctTable = table.getCTTable();
                ctTable.setRef("A1:P" + (vcmaReportDTOListAcumulado.size() + 1));
                int rowCount = (int) ctTable.getTotalsRowCount();
                for (VCMAReportDTO vcmaReportDTO : vcmaReportDTOListAcumulado) {
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
                    cell.setCellValue(vcmaReportDTO.getP1());
                    cell = row.createCell(9);
                    cell.setCellValue(vcmaReportDTO.getSp1());
                    cell = row.createCell(10);
                    cell.setCellValue(vcmaReportDTO.getP2());
                    cell = row.createCell(11);
                    cell.setCellValue(vcmaReportDTO.getSp2());
                    cell = row.createCell(12);
                    cell.setCellValue(vcmaReportDTO.getP3());
                    cell = row.createCell(13);
                    cell.setCellValue(vcmaReportDTO.getSp3());
                    cell = row.createCell(14);
                    cell.setCellValue(vcmaReportDTO.getNump());
                    cell = row.createCell(15);
                    cell.setCellValue(vcmaReportDTO.getMontop());
                    cantAcumulado = cantAcumulado + vcmaReportDTO.getNump();
                }
                //falto ocultar la  hoja de datos DBA
                //cambiar el nombre del titulo
                XSSFSheet dataSheet = wb.getSheet("SEGUIMIENTO ACUMULADO");
                XSSFRow changeRowTittle = dataSheet.getRow(0);
                XSSFCell changeTittle = changeRowTittle.getCell(0);
                LocalDate firstDayMonth = DateUtils.asDateToLocalDate(reportRequestPolicyDTO.getDateto());
                String mensuales = "DETALLE DE LA VENTAS ACUMULADO  DE LA FECHA  DESDE " + firstDayMonth.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                changeTittle.setCellValue(mensuales);
//para la hoja tres
                XSSFSheet Sheet1 = wb.getSheet("ACUMULADO VENTAS X AGENCIA");
                XSSFRow ChangeRowTitle1 = Sheet1.getRow(0);
                XSSFCell ChangeColumnTiTle1 = ChangeRowTitle1.getCell(3);
                String Title1 = "CANTIDAD DE PÓLIZAS VENDIDAS POR AGENCIAS, ACUMULADAS DESDE " + firstDayMonth.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                ChangeColumnTiTle1.setCellValue(Title1);
                //para la hoja de datos DBS

                XSSFSheet dataSheet2 = wb.getSheet("SEGUIMIENTO SEMANAL");
                XSSFRow changeRowTittle2 = dataSheet2.getRow(0);
                XSSFCell changeTittle2 = changeRowTittle2.getCell(0);
                LocalDate firstDayMonth2 = LocalDate.now();
//                String semanales = "DETALLE DE LAS VENTAS SEMANAL  DESDE " + firstDayMonth2.with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " AL " + firstDayMonth2.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String semanales = "DETALLE DE LAS VENTAS SEMANAL  DESDE " + formatter.format(reportRequestPolicyDTO.getDatefrom()) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                changeTittle2.setCellValue(semanales);

                //para la hoja tres
                XSSFSheet Sheet3 = wb.getSheet("SEMANAL VENTAS X AGENCIAS");
                XSSFRow ChangeRowTitle3 = Sheet3.getRow(0);
                XSSFCell ChangeColumnTiTle3 = ChangeRowTitle3.getCell(3);
                String Title3 = "CANTIDAD DE PÓLIZAS VENDIDAS POR AGENCIAS, SEMANALES DESDE " + formatter.format(reportRequestPolicyDTO.getDatefrom()) + " AL " + formatter.format(reportRequestPolicyDTO.getDateto());
                ChangeColumnTiTle3.setCellValue(Title3);

                XSSFSheet sheet2 = wb.getSheet("DBS");
                XSSFTable table2 = sheet2.getTables().get(0);
                List<VCMAReportDTO> vcmaReportDTOListSemanal = reportRequestPolicyRepository.GetAllDataForDates(reportRequestPolicyDTO.getDatefrom(), reportRequestPolicyDTO.getDateto());
                CTTable ctTable2 = table2.getCTTable();
                ctTable2.setRef("A1:P" + (vcmaReportDTOListSemanal.size() + 1));
                int rowCount2 = (int) ctTable2.getTotalsRowCount();
                for (VCMAReportDTO vcmaReportDTO : vcmaReportDTOListSemanal) {
                    XSSFRow row = sheet2.createRow(++rowCount2);
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
                    cell.setCellValue(vcmaReportDTO.getP1());
                    cell = row.createCell(9);
                    cell.setCellValue(vcmaReportDTO.getSp1());
                    cell = row.createCell(10);
                    cell.setCellValue(vcmaReportDTO.getP2());
                    cell = row.createCell(11);
                    cell.setCellValue(vcmaReportDTO.getSp2());
                    cell = row.createCell(12);
                    cell.setCellValue(vcmaReportDTO.getP3());
                    cell = row.createCell(13);
                    cell.setCellValue(vcmaReportDTO.getSp3());
                    cell = row.createCell(14);
                    cell.setCellValue(vcmaReportDTO.getNump());
                    cell = row.createCell(15);
                    cell.setCellValue(vcmaReportDTO.getMontop());
                    cantSemanal = cantSemanal + vcmaReportDTO.getNump();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    wb.write(baos);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Close the document
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Create the input stream (do not forget to close the inputStream after use)
//                InputStream is = new ByteArrayInputStream(baos.toByteArray());

                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception es) {
            es.printStackTrace();
            return null;
        }
        return null;
    }


}
