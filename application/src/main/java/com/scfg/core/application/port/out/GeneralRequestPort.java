package com.scfg.core.application.port.out;

import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.RequestDetailOperationDTO;
import com.scfg.core.domain.dto.RequestProposalSearchFiltersDto;
import com.scfg.core.domain.dto.credicasas.*;
import com.scfg.core.domain.dto.vin.VinDetailOperationDTO;
import com.scfg.core.domain.smvs.ContactCenterRequestDTO;
import com.scfg.core.domain.smvs.ParametersFromDTO;
import com.scfg.core.domain.smvs.VerifyActivationCodeDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface GeneralRequestPort {

    List<RequestDetailDTO> findAllGelBySubscriptionFilters(ClfSearchRequestDTO searchRequestDTO);
    SearchRequestResponseDTO findAllRequestsPaginated(ClfSearchRequestDTO searchRequestDTO, Integer page, Integer size);

    // SMVS - Reporte Contact Center
    List<ContactCenterRequestDTO> getAllPendingGeneralRequestByProductAgreementCode(Integer productAgreementCode);

    PageableDTO findAllSMVSRequestByPage(ParametersFromDTO filters);

    List<GeneralRequest> getAllGeneralRequestByPersonIdAndStatus(Long personId, Integer requestStatus);

    List<GeneralRequest> getAllGeneralRequestWitActivePoliciesByPersonId(Long personId);

    List<GeneralRequest> findAllGeneralRequestsByPersonId(Long personId);

    List<CurrentAmountRequestDTO> findAllCurrentAmountGeneralRequestByPersonId(Long personId);

    // CLF - Obtener todas las solicitudes
    List<RequestDTO> findAllClfRequestByFilters(PersonDTO personDTO, String sPlanIdList);

    GeneralRequest getGeneralRequest(long id);

    GeneralRequest findGeneralByReceiptById(long id);

    VerifyActivationCodeDTO existsActivationCode(String activationCode, String identificationNumber);

    boolean existsActivationCode(String activationCode);

    long saveOrUpdate(GeneralRequest generalRequest);
    List<GeneralRequest> getAllByBusinessGroup(Integer businessIdc);

    Boolean saveOrUpdateAll(List<GeneralRequest> generalRequest);

    int validateOperationNumber(String operationNumber, Integer requestId);

    List<GeneralRequest> getAllByPendingAndActivationCode(String fromDate, String toDate);
    List<GeneralRequest> getAllByPendingAndActivationCodeLastThirtyOneDay(LocalDateTime date);
    List<GeneralRequest> getAllByOperationNumberAndPlanId(String operatinNumber, Long planId);
    List<GeneralRequest> getAllPendingToActivationProposalToCancelation(LocalDateTime date);
    List<Integer> getIfExistOperationNumbers(String OperationNumber);
    boolean existOperationNumber(String OperationNumber, Integer productAgreementCode, Integer planAgreementCode);
    VinDetailOperationDTO getDetailOperation(RequestDetailOperationDTO requestDetailOperationDTO);
    Integer nextSequency(String initialProduct, String planName);
    PageableDTO findAllByPlanIdAndFilters(Long planId, RequestProposalSearchFiltersDto filtersDto, Integer page, Integer size);
    List<GeneralRequest> findAllByPlanIdAndCreditTermInYearsAndDateVIN(Long planId, Integer creditTermInYears, Date date);
}
