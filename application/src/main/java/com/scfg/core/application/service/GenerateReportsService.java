package com.scfg.core.application.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scfg.core.application.port.in.GenerateReportsUseCase;
import com.scfg.core.common.UseCase;
import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.apache.tomcat.jni.Local;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@UseCase
@RequiredArgsConstructor
public class GenerateReportsService implements GenerateReportsUseCase {

    /**
     * This method builds the Excel file from a generic list of DTOs and returns a FileDocumentDTO that contains the
     * data in base64 format
     *
     * @param firstColumn - The first letter (excel column) where the table will be drawn
     * @param finalColumn - The last letter (excel column) where the table will be drawn
     * @param columns     - The headers of the table
     * @param data        - List of DTOs: "Collections.singletonList(listDTO)" this is the correct way to pass a list of DTOs
     * @param fileName    - The file name
     * @return FileDocumentDTO
     */
    @Override
    public FileDocumentDTO generateExcel(String firstColumn, String finalColumn, List<String> columns, List<Object> data,
                                         String fileName) {
        FileDocumentDTO fd = new FileDocumentDTO();
        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Sheet 1");
            XSSFTable table = sheet.createTable();
            table.setDisplayName("Data");
            CTTable cttable = table.getCTTable();
            Gson g = new Gson();
            ArrayList<String> jsonList = new ArrayList<>();
            for (Object item : (ArrayList) data.get(0)) {
                jsonList.add(g.toJson(item));
            }
            cttable.setRef("" + firstColumn + "1:" + finalColumn + "" + (jsonList.size() + 1));
            XSSFRow rows = sheet.createRow(0);
            XSSFCell cells = rows.createCell(0);
            for (int i = 0; i < columns.size(); i++) {
                cells = rows.createCell(i);
                cells.setCellValue(columns.get(i));
            }
            int rowCount = (int) cttable.getTotalsRowCount();
            for (int i = 0; i < jsonList.size(); i++) {
                XSSFRow row = sheet.createRow(++rowCount);
                for (int j = 0; j < columns.size(); j++) {
                    XSSFCell cell = row.createCell(j);
                    JsonObject jsonObject = JsonParser.parseString(jsonList.get(i)).getAsJsonObject();
                    String aux = jsonObject.get(columns.get(j)).toString();
                    if (aux.startsWith("{")) {
                        JsonObject jsonDate = JsonParser.parseString(aux).getAsJsonObject();
                        String day = jsonDate.get("day").toString();
                        String month = jsonDate.get("month").toString();
                        String year = jsonDate.get("year").toString();
                        String date = day + "/" + month + "/" + year;
                        cell.setCellValue(date);
                    } else {
                        if (aux.endsWith("E7")) {
                            DecimalFormat format = new DecimalFormat("#0,0000.00");
                            String formattedNumber = format.format(jsonObject.get(columns.get(j)).getAsDouble());
                            cell.setCellValue(formattedNumber);
                        } else {
                            String cellValue = jsonObject.get(columns.get(j)).getAsString();
                            cell.setCellValue(cellValue);
                        }
                    }
                }
            }
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            wb.write(boas);
            fd.setContent(Base64.getEncoder().encodeToString(boas.toByteArray()));
            fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelFileName = fileName + new SimpleDateFormat("ddMMyyyy").format(new Date());
            fd.setName(excelFileName);
            return fd;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return fd;
        }
    }


    /**
     * This method builds the Excel file from a generic list of DTOs and returns a FileDocumentDTO that contains the
     * data in base64 format
     *
     * @param headerColumns - The headers of the table
     * @param data          - List of DTOs: "new ArrayList<>(data)" this is the correct way to pass a list of DTOs
     * @param fileName      - The file name
     * @return FileDocumentDTO
     */
    @Override
    public FileDocumentDTO generateExcelFileDocumentDTO(List<String> headerColumns, List<Object> data, String fileName) {
        FileDocumentDTO fd = null;
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            this.setWorkbook(wb, headerColumns, data);

            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            wb.write(boas);
            fd = new FileDocumentDTO();
            fd.setContent(Base64.getEncoder().encodeToString(boas.toByteArray()));
            fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelFileName = fileName + new SimpleDateFormat("ddMMyyyy").format(new Date());
            fd.setName(excelFileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fd;
    }

    @Override
    public FileDocumentDTO generateExcelFileDocumentDTOFromRawObject(List<String> headerColumns, List<Object> data, String fileName) {
        FileDocumentDTO fd = null;
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            this.setWorkbookRawObject(wb, headerColumns, data);

            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            wb.write(boas);
            fd = new FileDocumentDTO();
            fd.setContent(Base64.getEncoder().encodeToString(boas.toByteArray()));
            fd.setMime("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelFileName = fileName + new SimpleDateFormat("ddMMyyyy").format(new Date());
            fd.setName(excelFileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fd;
    }


        /**
         * This method builds the Excel file from a generic list of DTOs and returns a byte[]
         * that contains the data
         *
         * @param headerColumns - The headers of the table
         * @param data          - List of DTOs: "new ArrayList<>(data)" this is the correct way to pass a list of DTOs
         * @return byte[]
         */
    @Override
    public byte[] generateExcelByteArray(List<String> headerColumns, List<Object> data) {
        byte[] file = null;
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            this.setWorkbook(wb, headerColumns, data);

            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            wb.write(boas);
            file = boas.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }

    //#region Helpers

    private void setWorkbook(XSSFWorkbook wb, List<String> headerColumns, List<Object> data) {
        try {
            String sheetName = "Hoja 1";
            String tableName = "Data";

            XSSFSheet sheet = wb.createSheet(sheetName);

            List<String> columns = this.getColumns(data.get(0));

            AreaReference reference = new AreaReference(new CellReference(0, 0), new CellReference(data.size(), columns.size() - 1), SpreadsheetVersion.EXCEL2007);
            XSSFTable table = sheet.createTable(reference);
            table.setName(tableName);
            table.setDisplayName(tableName);

            CTTable cttable = table.getCTTable();
            cttable.setDisplayName(tableName);
            cttable.setName(tableName);
            cttable.setId(1);
            cttable.setTotalsRowShown(false);
            cttable.setRef(reference.formatAsString());
            CTAutoFilter autoFilter = cttable.addNewAutoFilter();
            autoFilter.setRef(reference.formatAsString());

            CTTableStyleInfo styleInfo = cttable.addNewTableStyleInfo();
            styleInfo.setName("TableStyleMedium6");
            styleInfo.setShowColumnStripes(false);
            styleInfo.setShowRowStripes(true);

            XSSFRow rows = sheet.createRow(0);
            XSSFCell cells = rows.createCell(0);

            XSSFCreationHelper createHelper = wb.getCreationHelper();

            XSSFCellStyle dateCellStyle = wb.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

            XSSFCellStyle dateTimeCellStyle = wb.createCellStyle();
            dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd hh:mm"));

            XSSFCellStyle doubleCellStyle = wb.createCellStyle();
            doubleCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));

            for (int i = 0; i < columns.size(); i++) { // Setting header columns
                cells = rows.createCell(i);
                cells.setCellValue(headerColumns.get(i));
            }

            int rowCount = table.getTotalsRowCount();
            for (int i = 0; i < data.size(); i++) {

                XSSFRow row = sheet.createRow(++rowCount);
                Object obj = data.get(i);

                for (int j = 0; j < columns.size(); j++) {
                    XSSFCell cell = row.createCell(j);

                    Field field = obj.getClass().getDeclaredField(columns.get(j));
                    field.setAccessible(true);
                    Object value = field.get(obj);


                    if (value == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(value.toString());
                    }

                    if ((value instanceof Long) || (value instanceof Integer)) {
                        cell.setCellValue(Long.parseLong(value.toString()));
                    }

                    if (value instanceof Double) {
                        Double number = new Double(value.toString());
                        cell.setCellValue(number);
                        cell.setCellStyle(doubleCellStyle);
                    }

                    if (value instanceof Date) {
                        Date myDate = (Date) value;
                        cell.setCellValue(myDate);
                        cell.setCellStyle(dateCellStyle);
                    }

                    if (value instanceof LocalDate) {
                        LocalDate myDate = (LocalDate) value;
                        cell.setCellValue(myDate);
                        cell.setCellStyle(dateCellStyle);
                    }

                    if (value instanceof LocalDateTime) {
                        LocalDateTime localDateTime = (LocalDateTime) value;
                        cell.setCellValue(localDateTime);
                        cell.setCellStyle(dateTimeCellStyle);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setWorkbookRawObject(XSSFWorkbook wb, List<String> headerColumns, List<Object> data) {
        try {
            String sheetName = "Hoja 1";
            String tableName = "Data";

            XSSFSheet sheet = wb.createSheet(sheetName);

            AreaReference reference = new AreaReference(new CellReference(0, 0)
                    , new CellReference(data.size(), headerColumns.size() - 1), SpreadsheetVersion.EXCEL2007);
            XSSFTable table = sheet.createTable(reference);
            table.setName(tableName);
            table.setDisplayName(tableName);

            CTTable cttable = table.getCTTable();
            cttable.setDisplayName(tableName);
            cttable.setName(tableName);
            cttable.setId(1);
            cttable.setTotalsRowShown(false);
            cttable.setRef(reference.formatAsString());
            CTAutoFilter autoFilter = cttable.addNewAutoFilter();
            autoFilter.setRef(reference.formatAsString());

            CTTableStyleInfo styleInfo = cttable.addNewTableStyleInfo();
            styleInfo.setName("TableStyleMedium6");
            styleInfo.setShowColumnStripes(false);
            styleInfo.setShowRowStripes(true);

            XSSFRow rows = sheet.createRow(0);
            XSSFCell cells = rows.createCell(0);

            XSSFCreationHelper createHelper = wb.getCreationHelper();

            XSSFCellStyle dateCellStyle = wb.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

            XSSFCellStyle dateTimeCellStyle = wb.createCellStyle();
            dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd hh:mm"));

            XSSFCellStyle doubleCellStyle = wb.createCellStyle();
            doubleCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));

            for (int i = 0; i < headerColumns.size(); i++) { // Setting header columns
                cells = rows.createCell(i);
                cells.setCellValue(headerColumns.get(i));
            }

            int rowCount = table.getTotalsRowCount();
            for (int i = 0; i < data.size(); i++) {

                XSSFRow row = sheet.createRow(++rowCount);
                Object obj = data.get(i);

                for (int j = 0; j < headerColumns.size(); j++) {
                    XSSFCell cell = row.createCell(j);

                    Object value = ((Object[]) obj)[j];

                    if (value == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(value.toString());
                    }

                    if ((value instanceof Long) || (value instanceof Integer)) {
                        cell.setCellValue(Long.parseLong(value.toString()));
                        continue;
                    }

                    if (value instanceof Double) {
                        Double number = new Double(value.toString());
                        cell.setCellValue(number);
                        cell.setCellStyle(doubleCellStyle);
                        continue;
                    }

                    if (value instanceof Timestamp) {
                        LocalDateTime myDate = ((Timestamp) value).toLocalDateTime();
                        cell.setCellValue(myDate);
                        cell.setCellStyle(dateTimeCellStyle);
                        continue;
                    }

                    if (value instanceof Date) {
                        Date myDate = (Date) value;
                        cell.setCellValue(myDate);
                        cell.setCellStyle(dateCellStyle);
                        continue;
                    }

                    if (value instanceof LocalDate) {
                        LocalDate myDate = (LocalDate) value;
                        cell.setCellValue(myDate);
                        cell.setCellStyle(dateCellStyle);
                        continue;
                    }

                    if (value instanceof LocalDateTime) {
                        LocalDateTime localDateTime = (LocalDateTime) value;
                        cell.setCellValue(localDateTime);
                        cell.setCellStyle(dateTimeCellStyle);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> getColumns(Object obj) {
        List<String> columns = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            columns.add(fields[i].getName());
        }
        return columns;
    }

    //#endregion

}
