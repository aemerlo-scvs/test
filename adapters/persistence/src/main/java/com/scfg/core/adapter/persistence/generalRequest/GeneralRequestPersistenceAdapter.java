package com.scfg.core.adapter.persistence.generalRequest;

import com.scfg.core.application.port.out.GeneralRequestPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.enums.PolicyStatusEnum;
import com.scfg.core.common.enums.RequestStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.dto.*;
import com.scfg.core.domain.dto.credicasas.*;
import com.scfg.core.domain.dto.vin.ResponseProposalDto;
import com.scfg.core.domain.dto.vin.VinDetailOperationDTO;
import com.scfg.core.domain.smvs.ContactCenterRequestDTO;
import com.scfg.core.domain.smvs.ParametersFromDTO;
import com.scfg.core.domain.smvs.SMVSRequestDTO;
import com.scfg.core.domain.smvs.VerifyActivationCodeDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class GeneralRequestPersistenceAdapter implements GeneralRequestPort {

    private final GeneralRequestRepository generalRequestRepository;
    private final EntityManager em;

    @Override
    public List<RequestDetailDTO> findAllGelBySubscriptionFilters(ClfSearchRequestDTO searchRequestDTO) {

        String filters = this.getGelSubscriptionFilters(searchRequestDTO);
        String additionalJoins = this.getGelAdditionalJoinsInSubscriptionFilters(searchRequestDTO);
        String order = "ORDER BY gr.createdAt DESC";

        String query = generalRequestRepository.getFindAllByFiltersBaseQuery(additionalJoins) + filters + order;
        List<RequestDetailDTO> requestDetailDTOList = em.createQuery(query).getResultList();
        em.close();
        return requestDetailDTOList;
    }

    @Override
    public SearchRequestResponseDTO findAllRequestsPaginated(ClfSearchRequestDTO searchRequestDTO, Integer page, Integer size) {

        String filters = this.getGelSubscriptionFilters(searchRequestDTO);
        String additionalJoins = this.getGelAdditionalJoinsInSubscriptionFilters(searchRequestDTO);
        String order = "ORDER BY gr.createdAt DESC";

        int initRange = HelpersMethods.getPageInitRange(page, size);

        String query = generalRequestRepository.getFindAllByFiltersSelectQuery(additionalJoins) + filters + order;
        String countQuery = generalRequestRepository.getFindAllByFiltersCountQuery(additionalJoins) + filters;

        List<RequestDetailDTO> requestDetailDTOList = em.createQuery(query)
                .setFirstResult(initRange).setMaxResults(size).getResultList();
        em.close();

        Long count = (Long) em.createQuery(countQuery).getSingleResult();
        em.close();

        return SearchRequestResponseDTO.builder()
                .requestDetailDTOList(requestDetailDTOList)
                .requestsQuantity(count)
                .build();
    }

    @Override
    public List<ContactCenterRequestDTO> getAllPendingGeneralRequestByProductAgreementCode(Integer productAgreementCode) {
        List<ContactCenterRequestDTO> list = generalRequestRepository.customFindAllByRequestStatusIdcAndProductAgreementCode(RequestStatusEnum.PENDING.getValue(), productAgreementCode);
        return list;
    }

    @Override
    public PageableDTO findAllSMVSRequestByPage(ParametersFromDTO filters) {

        String additionalWhere = "";
        if (filters.getStatusRequest() > 0) {
            additionalWhere = " AND requestStatusIdc = :requestStatusIdc ";
        }

        String baseQuery = generalRequestRepository.getFindAllSMVSRequestSelectQuery(additionalWhere);
        String countBaseQuery = generalRequestRepository.getFindAllSMVSRequestCountQuery(additionalWhere);

        Query query = em.createQuery(baseQuery);
        query.setParameter("status", PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        query.setParameter("agreementCode", 746);
        query.setParameter("startDate", filters.getStartDate());
        query.setParameter("toDate", filters.getToDate());
        if (!additionalWhere.isEmpty()) {
            query.setParameter("requestStatusIdc", filters.getStatusRequest());
        }

        Query countQuery = em.createQuery(countBaseQuery);
        countQuery.setParameter("status", PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        countQuery.setParameter("agreementCode", 746);
        countQuery.setParameter("startDate", filters.getStartDate());
        countQuery.setParameter("toDate", filters.getToDate());
        if (!additionalWhere.isEmpty()) {
            countQuery.setParameter("requestStatusIdc", filters.getStatusRequest());
        }

        int initRange = HelpersMethods.getPageInitRange(filters.getPage(), filters.getSize());

        List<SMVSRequestDTO> list = (List<SMVSRequestDTO>) query.setFirstResult(initRange).setMaxResults(filters.getSize()).getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();

        em.close();

        return PageableDTO.builder()
                .content(list)
                .totalElements(totalElements.intValue())
                .build();
    }

    @Override
    public List<GeneralRequest> getAllGeneralRequestByPersonIdAndStatus(Long personId, Integer requestStatus) {
        Object list = generalRequestRepository.findAllByPersonIdAndRequestStatusIdc(personId, requestStatus);
        return (List<GeneralRequest>) list;
    }

    @Override
    public List<GeneralRequest> getAllGeneralRequestWitActivePoliciesByPersonId(Long personId) {
        Object list = generalRequestRepository.customFindAllWithActivePoliciesByPersonId(personId, RequestStatusEnum.FINALIZED.getValue(), PolicyStatusEnum.ACTIVE.getValue());
        return (List<GeneralRequest>) list;
    }

    @Override
    public List<GeneralRequest> findAllGeneralRequestsByPersonId(Long personId) {
        List<GeneralRequestJpaEntity> generalRequestJpaEntityList = generalRequestRepository.findAllRequestsByPersonId(personId);
        return generalRequestJpaEntityList.stream().map(o -> new ModelMapper().map(o, GeneralRequest.class)).collect(Collectors.toList());
    }

    @Override
    public List<CurrentAmountRequestDTO> findAllCurrentAmountGeneralRequestByPersonId(Long personId) {
        List<Object[]> listAux = generalRequestRepository.findAllCurrentAmountRequestsByPersonId(personId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        List<CurrentAmountRequestDTO> list = listAux.stream().map(CurrentAmountRequestDTO::new).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<RequestDTO> findAllClfRequestByFilters(PersonDTO personDTO, String sPlanIdList) {
        String query = generalRequestRepository.getFindAllClfByFiltersBaseQuery() + getClfRequestFilters(personDTO, sPlanIdList);
        List<RequestDTO> requestDTOList = em.createQuery(query).getResultList();
        em.close();

        return requestDTOList;
    }

    @Override
    public GeneralRequest getGeneralRequest(long id) {
        GeneralRequestJpaEntity generalRequestJpaEntity = generalRequestRepository.findById(id)
                .orElseThrow(() -> new NotDataFoundException("Solicitud no encontrada."));

        return new ModelMapper().map(generalRequestJpaEntity, GeneralRequest.class);
    }

    @Override
    public VerifyActivationCodeDTO existsActivationCode(String activationCode, String identificationNumber) {
        VerifyActivationCodeDTO verifyActivationCodeDTO = generalRequestRepository.findByActivationCodeAndIdentificationNumber(activationCode, identificationNumber, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return verifyActivationCodeDTO;
    }

    @Override
    public GeneralRequest findGeneralByReceiptById(long id) {
        GeneralRequestJpaEntity generalRequest = generalRequestRepository.findGeneralByReceiptById(id);

        return generalRequest != null ? mapToDomain(generalRequest, false) : null;
    }

    @Override
    public boolean existsActivationCode(String activationCode) {
        GeneralRequestJpaEntity generalRequestJpaEntity = generalRequestRepository.findByActivationCode(activationCode);
        return generalRequestJpaEntity != null;

    }

    @Override
    public long saveOrUpdate(GeneralRequest generalRequest) {
        GeneralRequestJpaEntity generalRequestJpaEntity = mapToJpaEntity(generalRequest);
        generalRequestJpaEntity = generalRequestRepository.save(generalRequestJpaEntity);
        return generalRequestJpaEntity.getId();
    }

    @Override
    public int validateOperationNumber(String operationNumber, Integer requestId) {
        return generalRequestRepository.verifyOperationNumber(operationNumber, requestId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue(),
                RequestStatusEnum.FINALIZED.getValue(), RequestStatusEnum.PENDING.getValue());
    }

    @Override
    public List<GeneralRequest> getAllByPendingAndActivationCode(String fromDate, String toDate) {
        List<GeneralRequestJpaEntity> generalRequestJpaEntityList = generalRequestRepository.finAllRequestPendingForMoreThanThirtyDays(
                HelpersMethods.formatStringToLocalDateTime(fromDate),HelpersMethods.formatStringToLocalDateTime(toDate),RequestStatusEnum.PENDING.getValue(),PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()
        );
        return generalRequestJpaEntityList.stream().map(o -> new ModelMapper().map(o, GeneralRequest.class)).collect(Collectors.toList());
    }

    @Override
    public List<GeneralRequest> getAllByPendingAndActivationCodeLastThirtyOneDay(LocalDateTime date) {
        List<GeneralRequestJpaEntity> generalRequestJpaEntityList = generalRequestRepository.finAllRequestPendingForMoreThanThirtyOneDays(
                date,RequestStatusEnum.PENDING.getValue(),PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()
        );
        return generalRequestJpaEntityList.stream().map(o -> new ModelMapper().map(o, GeneralRequest.class)).collect(Collectors.toList());
    }

    @Override
    public List<GeneralRequest> getAllByOperationNumberAndPlanId(String operationNumber, Long planId) {
        List<GeneralRequestJpaEntity> generalRequestJpaEntityList = generalRequestRepository.
                findAllProposalRequestByOperationNumberAndPlanId(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue(),
                        planId,operationNumber);
        return generalRequestJpaEntityList.stream().map(o -> new ModelMapper().map(o, GeneralRequest.class)).collect(Collectors.toList());
    }

    @Override
    public List<GeneralRequest> getAllPendingToActivationProposalToCancelation(LocalDateTime date) {
        List<GeneralRequestJpaEntity> generalRequestJpaEntityList = generalRequestRepository.
                finAllRequestProposalPendingToCancel(date, RequestStatusEnum.PENDING.getValue(), PersistenceStatusEnum.CREATED_OR_UPDATED.getValue(),
                        "VIDA INDIVIDUAL INCLUSIVO (VIN)");
        return generalRequestJpaEntityList.stream().map(o -> new ModelMapper().map(o, GeneralRequest.class)).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getIfExistOperationNumbers(String OperationNumber) {
        return this.generalRequestRepository.findIfExistOperationNumbers(OperationNumber, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
    }

    @Override
    public boolean existOperationNumber(String operationNumber, Integer productAgreementCode, Integer planAgreementCode) {
        Integer response = this.generalRequestRepository.existOperationNumber(operationNumber, productAgreementCode,
                planAgreementCode, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return response > 0;
    }

    @Override
    public VinDetailOperationDTO getDetailOperation(RequestDetailOperationDTO requestDetailOperationDTO) {
        List<VinDetailOperationDTO> list = this.generalRequestRepository.findAPersonForOperationDetail(
                requestDetailOperationDTO.getTipo_documento(),
                requestDetailOperationDTO.getNro_documento(),
                requestDetailOperationDTO.getNro_operacion(),
                requestDetailOperationDTO.getExtensionIdc().equals(0) ? 0 : requestDetailOperationDTO.getExtensionIdc(),
                requestDetailOperationDTO.getComplemento().equals("") ? null : requestDetailOperationDTO.getComplemento(),
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Integer nextSequency(String initialProduct, String planName) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("proc_get_next_sequency")
                .registerStoredProcedureParameter("productInitial", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("planName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("sequencyValue", Integer.class, ParameterMode.OUT)
                .setParameter("productInitial",initialProduct)
                .setParameter("planName",planName);
        query.execute();
        Integer result = (Integer) query.getOutputParameterValue("sequencyValue");
        em.close();
        return result;
    }

    @Override
    public PageableDTO findAllByPlanIdAndFilters(Long planId, RequestProposalSearchFiltersDto filtersDto, Integer page, Integer size) {
        String filters = "";
        if (filtersDto != null) {
            filters = this.getFindProposalFilters(filtersDto);
        }
        int initRange = HelpersMethods.getPageInitRange(page, size);

        String query = this.generalRequestRepository.getFindAllByIdPlanFiltersSelectQuery(planId) + filters + getOrderByDateProposal();

        String countQuery = this.generalRequestRepository.getFindAllByIdPlanFiltersCountQuery(planId) + filters;

        List<Object[]> listAux = em.createNativeQuery(query)
                .setFirstResult(initRange)
                .setMaxResults(size).getResultList();

        List<ResponseProposalDto> list = listAux.stream().map(ResponseProposalDto::new).collect(Collectors.toList());

        em.close();

        Long count = Long.parseLong(em.createNativeQuery(countQuery).getSingleResult().toString());
        em.close();

        return PageableDTO.builder()
                .content(list)
                .totalElements(count.intValue())
                .build();
    }

    @Override
    public List<GeneralRequest> findAllByPlanIdAndCreditTermInYearsAndDateVIN(Long planId, Integer creditTermInYears, Date date) {
        List<GeneralRequestJpaEntity> listAux = this.generalRequestRepository.getAllByPlanIdAndCreditTermInYearsAndDateVIN(planId, creditTermInYears,
                date, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return listAux.stream().map(o -> new ModelMapper().map(o, GeneralRequest.class)).collect(Collectors.toList());
    }

    //Todo Eliminar luego de utilizarlo cuando se deba / Delete this when no have more uses
    @Override
    public List<GeneralRequest> getAllByBusinessGroup(Integer businessIdc) {
        List<GeneralRequestJpaEntity> generalRequestJpaEntityList = generalRequestRepository.findAllRequestToChangeData(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return generalRequestJpaEntityList.stream().map(o -> new ModelMapper().map(o, GeneralRequest.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean saveOrUpdateAll(List<GeneralRequest> generalRequest) {
        List<GeneralRequestJpaEntity> generalRequestJpaEntityList = generalRequest.stream().map(GeneralRequestPersistenceAdapter::mapToJpaEntity).collect(Collectors.toList());
        generalRequestRepository.saveAll(generalRequestJpaEntityList);
        return true;
    }

    //#region Mappers

    public static GeneralRequestJpaEntity mapToJpaEntity(GeneralRequest generalRequest) {
        GeneralRequestJpaEntity generalRequestJpaEntity = GeneralRequestJpaEntity.builder()
                .id(generalRequest.getId() == null ? null : generalRequest.getId())
                .requestNumber(generalRequest.getRequestNumber())
                .requestDate(generalRequest.getRequestDate())
                .description(generalRequest.getDescription())
                .weight(generalRequest.getWeight())
                .height(generalRequest.getHeight())
                .creditNumber(generalRequest.getCreditNumber())
                .requestedAmount(generalRequest.getRequestedAmount())
                .currentAmount(generalRequest.getCurrentAmount())
                .accumulatedAmount(generalRequest.getAccumulatedAmount())
                .creditTerm(generalRequest.getCreditTerm())
                .creditTermInYears(generalRequest.getCreditTermInYears())
                .creditTermInDays(generalRequest.getCreditTermInDays())
                .acceptanceReasonIdc(generalRequest.getAcceptanceReasonIdc())
                .rejectedReasonIdc(generalRequest.getRejectedReasonIdc())
                .pendingReason(generalRequest.getPendingReason())
                .exclusionComment(generalRequest.getExclusionComment())
                .rejectedComment(generalRequest.getRejectedComment())
                .inactiveComment(generalRequest.getInactiveComment())
                .legalHeirs(generalRequest.getLegalHeirs())
                .activationCode(generalRequest.getActivationCode())
                .requestStatusIdc(generalRequest.getRequestStatusIdc())
                .planId(generalRequest.getPlanId())
                .personId(generalRequest.getPersonId())
                .insuredTypeIdc(generalRequest.getInsuredTypeIdc()) // TODO Verificar si en SEPELIO lo mata este campo
                .createdAt(generalRequest.getCreatedAt())
                .lastModifiedAt(generalRequest.getLastModifiedAt())
                .createdBy(generalRequest.getCreatedBy())
                .lastModifiedBy(generalRequest.getLastModifiedBy())
                .typeIdc(generalRequest.getTypeIdc())
                .build();

        return generalRequestJpaEntity;
    }

    private GeneralRequest mapToDomain(GeneralRequestJpaEntity generalRequestJpaEntity, boolean b) {
        GeneralRequest generalRequest = GeneralRequest.builder()
                .id(generalRequestJpaEntity.getId())
                .description(generalRequestJpaEntity.getDescription())
                .height(generalRequestJpaEntity.getHeight())
                .weight(generalRequestJpaEntity.getWeight())
                .creditNumber(generalRequestJpaEntity.getCreditNumber())
                .creditTerm(generalRequestJpaEntity.getCreditTerm())
                .creditTermInYears(generalRequestJpaEntity.getCreditTermInYears())
                .creditTermInDays(generalRequestJpaEntity.getCreditTermInDays())
                .acceptanceReasonIdc(generalRequestJpaEntity.getAcceptanceReasonIdc())
                .rejectedReasonIdc(generalRequestJpaEntity.getRejectedReasonIdc())
                .pendingReason(generalRequestJpaEntity.getPendingReason())
                .exclusionComment(generalRequestJpaEntity.getExclusionComment())
                .rejectedComment(generalRequestJpaEntity.getRejectedComment())
                .inactiveComment(generalRequestJpaEntity.getInactiveComment())
                .legalHeirs(generalRequestJpaEntity.getLegalHeirs())
                .requestedAmount(generalRequestJpaEntity.getRequestedAmount())
                .currentAmount(generalRequestJpaEntity.getCurrentAmount())
                .accumulatedAmount(generalRequestJpaEntity.getAccumulatedAmount())
                .activationCode(generalRequestJpaEntity.getActivationCode())
                .requestStatusIdc(generalRequestJpaEntity.getRequestStatusIdc())
                .requestDate(generalRequestJpaEntity.getRequestDate())
                .requestNumber(generalRequestJpaEntity.getRequestNumber())
                .insuredTypeIdc(generalRequestJpaEntity.getInsuredTypeIdc()) // TODO Verificar si en SEPELIO lo mata este campo
                .planId(generalRequestJpaEntity.getPlanId())
                .personId(generalRequestJpaEntity.getPersonId())
                .createdAt(generalRequestJpaEntity.getCreatedAt())
                .lastModifiedAt(generalRequestJpaEntity.getLastModifiedAt())
                .typeIdc(generalRequestJpaEntity.getTypeIdc())
                .build();
        return generalRequest;
    }

    //#endregion

    //#region AuxiliaryMethods

    public String getFindProposalFilters(RequestProposalSearchFiltersDto searchRequestDTO){
        String filters = "";
        List<String> filterList = new ArrayList<>();
        if (searchRequestDTO.getUserId() != null && searchRequestDTO.getUserId() !=0) {
            filterList.add("gr.createdBy = " + searchRequestDTO.getUserId());
        }
        if (!searchRequestDTO.getIdentificationNumber().isEmpty()) {
            filterList.add("np.identificationNumber = " + "'" + searchRequestDTO.getIdentificationNumber().trim() + "'");
        }
        if (!searchRequestDTO.getOperationNumber().isEmpty()) {
            filterList.add("gr.creditNumber = " + searchRequestDTO.getOperationNumber());
        }
        if (searchRequestDTO.getRequestStatusIdc() !=null && searchRequestDTO.getRequestStatusIdc() != 0 ) {
            filterList.add("gr.requestStatusIdc = " + searchRequestDTO.getRequestStatusIdc());
        }
        if (!searchRequestDTO.getNames().isEmpty()) {
            filterList.add("np.name = " + "'" + searchRequestDTO.getNames().toUpperCase().trim() + "'");
        }
        if (!searchRequestDTO.getLastname().isEmpty()) {
            filterList.add("np.lastName = " + "'" + searchRequestDTO.getLastname().toUpperCase().trim() + "'");
        }
        if (!searchRequestDTO.getMotherLastname().isEmpty()) {
            filterList.add("np.motherLastName = " + "'" + searchRequestDTO.getMotherLastname().toUpperCase().trim() + "'");
        }
        if (searchRequestDTO.getFromDate()!=null && searchRequestDTO.getToDate()!= null) {
            filterList.add("gr.requestDate BETWEEN "
                    + "'" + HelpersMethods.formatStringDate("dd/MM/yyyy",
                    DateUtils.asDate(LocalDateTime.parse(searchRequestDTO.getFromDate(), DateTimeFormatter.ISO_DATE_TIME))) + "'" + "  AND "
                    + "'" + HelpersMethods.formatStringDate("dd/MM/yyyy",
                    DateUtils.asDate(LocalDateTime.parse(searchRequestDTO.getToDate(), DateTimeFormatter.ISO_DATE_TIME))) + "'" );
        }
        if (filterList.size() > 0) {
            filters = "AND " + String.join(" AND ", filterList) + " \n";
        }
        return filters;
    }

    public String getOrderByDateProposal(){
        return  "ORDER BY gr.requestDate DESC ";
    }


    private String getClfRequestFilters(PersonDTO personDTO, String sPlanIdList) {
        String filters = "";
        List<String> filterList = new ArrayList<>();
        filterList.add("gr.planId IN (" + sPlanIdList + ")");
        filterList.add("gr.requestStatusIdc <> " + RequestStatusEnum.INACTIVE.getValue());

        if (personDTO.getRequestNumber() != 0) {
            filterList.add("gr.requestNumber = " + personDTO.getRequestNumber());
        }

        if (!personDTO.getIdentificationNumber().isEmpty()) {
            filterList.add("np.identificationNumber = " + "'" + personDTO.getIdentificationNumber().trim() + "'");
        }

        if (personDTO.getExtIdc() != 0) {
            filterList.add("np.extIdc = " + personDTO.getExtIdc());
        }

        if (!personDTO.getComplement().isEmpty()) {
            filterList.add("np.complement = " + "'" + personDTO.getComplement() + "'");
        }

        if (!personDTO.getNames().isEmpty()) {
            filterList.add("np.name = " + "'" + personDTO.getNames().toUpperCase().trim() + "'");
        }

        if (!personDTO.getLastname().isEmpty()) {
            filterList.add("np.lastName = " + "'" + personDTO.getLastname().toUpperCase().trim() + "'");
        }

        if (!personDTO.getMothersLastname().isEmpty()) {
            filterList.add("np.motherLastName = " + "'" + personDTO.getMothersLastname().toUpperCase().trim() + "'");
        }

        if (!personDTO.getMarriedLastname().isEmpty()) {
            filterList.add("np.marriedLastName = " + "'" + personDTO.getMarriedLastname().toUpperCase().trim() + "'");
        }

        if (filterList.size() > 1) {
            filters = String.join(" AND ", filterList);
        }
        return filters;
    }

    public String getFindAllByFilters(SearchRequestDTO searchRequestDTO, String additionalFilters) {
        String filters = "";
        List<String> filterList = new ArrayList<>();

        if (searchRequestDTO.getProductId() != 0) {
            filterList.add("cp.productId = " + searchRequestDTO.getProductId());
        }

        if (searchRequestDTO.getPlanId() != 0) {
            filterList.add("gr.planId = " + searchRequestDTO.getPlanId());
        }

        if (searchRequestDTO.getRequestStatusIdc() != 0) {
            filterList.add("gr.requestStatusIdc = " + searchRequestDTO.getRequestStatusIdc());
        }
        if (searchRequestDTO.getRequestNumber() != 0) {
            filterList.add("gr.requestNumber = " + searchRequestDTO.getRequestNumber());
        }
        if (searchRequestDTO.getFromDate() != null) {
            filterList.add("CAST(gr.createdAt AS date) >= CAST('" + new SimpleDateFormat("yyyy-MM-dd").format(searchRequestDTO.getFromDate()).toString() + "' AS date)");
        }
        if (searchRequestDTO.getToDate() != null) {
            filterList.add("CAST(gr.createdAt AS date) <= CAST('" + new SimpleDateFormat("yyyy-MM-dd").format(searchRequestDTO.getToDate()).toString() + "' AS date)");
        }
        if (!searchRequestDTO.getName().isEmpty()) {
            filterList.add("np.name LIKE " + "'%" + searchRequestDTO.getName().toUpperCase().trim() + "%'");
        }

        if (!searchRequestDTO.getLastName().isEmpty()) {
            filterList.add("np.lastName LIKE " + "'%" + searchRequestDTO.getLastName().toUpperCase().trim() + "%'");
        }
        if (!searchRequestDTO.getMotherLastName().isEmpty()) {
            filterList.add("np.motherLastName LIKE " + "'%" + searchRequestDTO.getMotherLastName().toUpperCase().trim() + "%'");
        }
        if (!searchRequestDTO.getIdentificationNumber().isEmpty()) {
            filterList.add("np.identificationNumber = " + "'" + searchRequestDTO.getIdentificationNumber().trim() + "'");
        }
        if (filterList.size() > 0) {
            filters = "AND " + String.join(" AND ", filterList) + additionalFilters + " \n";
        }
        return filters;
    }
    private String getGelSubscriptionFilters(ClfSearchRequestDTO searchRequestDTO) {
        String additionalFilters = "";

        if (searchRequestDTO.getSearchRequest().getRequestStatusIdc() == RequestStatusEnum.REJECTED.getValue()) {
            int isSigned = searchRequestDTO.getIsUnsigned() ? 0 : 1;
            additionalFilters = " AND pd.isSigned = " + isSigned + " AND fd.typeDocument = 6 ";
        }

        if (searchRequestDTO.getSearchRequest().getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()) {
            String isPending = searchRequestDTO.getIsPendingRequirement() ? "IS NULL " : "IS NOT NULL";
            additionalFilters = " AND rc.fileDocumentId " + isPending;
        }

        return this.getFindAllByFilters(searchRequestDTO.getSearchRequest(), additionalFilters);
    }
    private String getGelAdditionalJoinsInSubscriptionFilters(ClfSearchRequestDTO searchRequestDTO){
        String additionalJoins = "";

        if (searchRequestDTO.getSearchRequest().getRequestStatusIdc() == RequestStatusEnum.REJECTED.getValue()) {
            additionalJoins = "LEFT JOIN PolicyFileDocumentJpaEntity pd ON pd.policyItemId = pt.id\n" +
                    "LEFT JOIN FileDocumentJpaEntity fd ON fd.id = pd.fileDocumentId\n";
        }

        if (searchRequestDTO.getSearchRequest().getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()) {
            additionalJoins = "LEFT JOIN RequirementControlJpaEntity rc on rc.policyItemId = pt.id\n";
        }
        return additionalJoins;
    }

    //#endregion

}
