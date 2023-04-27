package com.scfg.core.application.port.in;


import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.RequestCancelOperationDTO;
import com.scfg.core.domain.dto.RequestDetailOperationDTO;
import com.scfg.core.domain.dto.vin.OperationDetailDTO;
import com.scfg.core.domain.dto.vin.VinDetailOperationDTO;
import com.scfg.core.domain.dto.vin.VinProposalDetail;
import com.scfg.core.domain.dto.vin.VinReportFilterDTO;

import java.util.List;

public interface VinUseCase {

    void cancelPolicy(RequestCancelOperationDTO requestCancelOperationDTO);
    boolean isOperationDetailValid(OperationDetailDTO operationDetailDTO);
    void reSend(Long requestId, Integer reSendBy, String to);
    VinDetailOperationDTO getOperationDetail(RequestDetailOperationDTO detailOperationDTO);

    FileDocumentDTO generateProductionReport(VinReportFilterDTO filterDTO);
    FileDocumentDTO generateCommercialReport(VinReportFilterDTO filterDTO);
}
