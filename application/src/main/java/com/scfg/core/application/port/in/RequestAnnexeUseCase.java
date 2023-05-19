package com.scfg.core.application.port.in;

import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.RequestAnnexeCancelaltionDto;
import com.scfg.core.domain.dto.RequestAnnexeSearchFiltersDto;
import com.scfg.core.domain.dto.RequestSaveVoucherPaymentDto;
import com.scfg.core.domain.dto.vin.*;
import com.scfg.core.domain.common.AnnexeRequirementControl;

import java.text.ParseException;
import java.util.List;

public interface RequestAnnexeUseCase {
    RequestAnnexeDetailDTO getRequestAnnexeDetail(Long policyId, Long annexeTypeId) throws OperationException;
    List<AnnexeRequirementControl> processRequest(RequestAnnexeDTO requestAnnexeDTO);
    List<AnnexeRequirementDto> processRequest(UpdateRequestAnnexeDTO requestAnnexeDTO);
    RequestAnnexeFileDocumentDTO hasPendingRequests(Long policyId, Long annexeTypeId);
    PageableDTO getAllPageByAnnexeFilters(Integer page, Integer size, RequestAnnexeSearchFiltersDto filtersDto) throws ParseException;
    void processCancellationRequest(Long requestAnnexeId, RequestAnnexeCancelaltionDto requestAnnexeCancelaltionDto);
    Long validateVoucherPayment(Long requestAnnexeId);
    void saveVoucherPayment(Long requestAnnexeId, RequestSaveVoucherPaymentDto requestSaveVoucherPaymentDto);
    void updateRequirements(RequestAnnexeCancelaltionDto requestAnnexeCancelaltionDto, Long requestAnnexeId);
}
