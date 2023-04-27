package com.scfg.core.application.port.out;

import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.FabolousDTO;
import com.scfg.core.domain.dto.fabolous.*;

import java.util.List;

public interface FabolousPort {

    PersistenceResponse save(List<FabolousDTO> fabolousDTOS, boolean returnEntity);

    List<FabolousUploadDTO> getAllUpload();

    List<FabolousReportLiquidationDTO> liquidationGenerateReport(FabolousReportDTO date);
    List<FabolousReportDuplicateLiquidationDTO> liquidationGenerateDuplicateReport(FabolousReportDTO date);

    boolean deleteUploadReport(Long deleteId);

    FabolusResultResponseClient searchClient(FabolousSearchCltDTO client, Integer page, Integer size);
}
