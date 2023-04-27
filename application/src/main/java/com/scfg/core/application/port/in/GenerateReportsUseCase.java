package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.FileDocumentDTO;

import java.util.List;

public interface GenerateReportsUseCase {
    FileDocumentDTO generateExcel(String firstColumn, String finalColumn, List<String> columns, List<Object> data, String fileName);

    FileDocumentDTO generateExcelFileDocumentDTO(List<String> formattedColumns, List<Object> data, String fileName);

    FileDocumentDTO generateExcelFileDocumentDTOFromRawObject(List<String> headerColumns, List<Object> data, String fileName);

    byte[] generateExcelByteArray(List<String> formattedColumns, List<Object> data);
}
