package com.scfg.core.application.port.in;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.FabolousDTO;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.fabolous.*;

import java.util.List;

public interface FabolousUseCase {

    PersistenceResponse registerReport(List<FabolousDTO> fabolousDTOS);

    List<FabolousUploadDTO> getAllUploads();

    FileDocumentDTO liquidationGenerateReport(FabolousReportDTO dates);
    FileDocumentDTO liquidationGenerateDuplicateReport(FabolousReportDTO dates);

    boolean deleteUploadReport(Long deleteId);

    FabolusResultResponseClient searchClient(FabolousSearchCltDTO client, Integer page, Integer size);
}
