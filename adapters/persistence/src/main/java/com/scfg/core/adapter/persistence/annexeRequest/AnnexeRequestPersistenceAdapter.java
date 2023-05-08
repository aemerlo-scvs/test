package com.scfg.core.adapter.persistence.annexeRequest;

import com.scfg.core.application.port.out.RequestAnnexePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.enums.RequestAnnexeStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.RequestAnnexeSearchFiltersDto;
import com.scfg.core.domain.common.RequestAnnexe;
import com.scfg.core.domain.dto.vin.ResponseAnnexeRequestDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class AnnexeRequestPersistenceAdapter implements RequestAnnexePort {

    private final AnnexeRequestRepository annexeRequestRepository;
    private final EntityManager em;
    @Override
    public Long saveOrUpdate(RequestAnnexe o) {
        AnnexeRequestJpaEntity annexeRequestJpaEntity = mapToJpaEntity(o);
        annexeRequestJpaEntity = annexeRequestRepository.save(annexeRequestJpaEntity);
        return annexeRequestJpaEntity.getId();
    }

    @Override
    public List<RequestAnnexe> getRequestByPolicyIdAndAnnexeTypeId(Long policyId, Long annexeTypeId) {
        List<Integer> requestAnnexeStatusList = new ArrayList<>();
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.PENDING.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.OBSERVED.getValue());
        requestAnnexeStatusList.add(RequestAnnexeStatusEnum.REQUESTED.getValue());
        List<AnnexeRequestJpaEntity> list = annexeRequestRepository.findAllByPolicyIdAndAnnexe(
                policyId,
                annexeTypeId,
                PersistenceStatusEnum.CREATED_OR_UPDATED.getValue(),
                requestAnnexeStatusList
        );
        return list.stream().map(o -> new ModelMapperConfig().getStrictModelMapper()
                .map(o, RequestAnnexe.class)).collect(Collectors.toList());
    }

    @Override
    public List<RequestAnnexe> findAllRequestByPolicyIdAndAnnexeTypeIdAndRequestStatus(Long policyId, Long annexeTypeId, List<Integer> requestAnnexeStatusList) {
        return null;
    }

    @Override
    public PageableDTO findAllPageByFilters(RequestAnnexeSearchFiltersDto filtersDto, Integer page, Integer size) throws ParseException {
        String filters = filtersDto != null ? this.getFindAnnexeFilters(filtersDto) : this.getFirstStatus();
        int initRange = HelpersMethods.getPageInitRange(page, size);
        String query = this.annexeRequestRepository.getFindAllPageByFiltersSelectQuery() + filters + getOrderByDateAnnexe();
        String countQuery = this.annexeRequestRepository.getFindAllPageByFiltersCountQuery() + filters;
        List<ResponseAnnexeRequestDto> responseAnnexeRequestDtoList = em.createQuery(query)
                .setFirstResult(initRange)
                .setMaxResults(size).getResultList();
        em.close();

        Long count = (Long) em.createQuery(countQuery).getSingleResult();
        em.close();

        return PageableDTO.builder()
                .content(responseAnnexeRequestDtoList)
                .totalElements(count.intValue())
                .build();
    }

    @Override
    public RequestAnnexe findRequestAnnexeIdOrThrowExcepcion(Long requestAnnexeId) {
        AnnexeRequestJpaEntity annexeRequestJpaEntity = this.annexeRequestRepository.findOptionalById(
                requestAnnexeId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()
        ).orElseThrow(() -> new NotDataFoundException("Solicitud de anexo no encontrada"));
        return new ModelMapperConfig().getStrictModelMapper().map(annexeRequestJpaEntity, RequestAnnexe.class);
    }

    public String getFindAnnexeFilters(RequestAnnexeSearchFiltersDto searchRequestDTO) throws ParseException {
        String filters = "";
        List<String> filterList = new ArrayList<>();
        if (!searchRequestDTO.getIdentificationNumber().isEmpty()) {
            filterList.add("np.identificationNumber = " + "'" + searchRequestDTO.getIdentificationNumber().trim() + "'");
        }
        if (searchRequestDTO.getRequestStatusIdc() != null && searchRequestDTO.getRequestStatusIdc() != 0 ) {
            filterList.add("ra.statusIdc = " + searchRequestDTO.getRequestStatusIdc());
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
            Date fromDateWhitMinutes = DateUtils.formatToStartOrNull(searchRequestDTO.getFromDate());
            Date toDateWhitMinutes = DateUtils.formatToEnd(searchRequestDTO.getToDate());
            filterList.add("ra.createdAt BETWEEN "
                    + "'" + HelpersMethods.formatStringDate(DateUtils.FORMAT_DATE_TIME, fromDateWhitMinutes) + "'" + "  AND "
                    + "'" + HelpersMethods.formatStringDate(DateUtils.FORMAT_DATE_TIME,toDateWhitMinutes) + "'" );
        }
        if (filterList.size() > 0) {
            filters = "AND " + String.join(" AND ", filterList) + " \n";
        }
        return filters;
    }
    public String getFirstStatus() {
        String filters = "";
        List<String> filterList = new ArrayList<>();
        int  status = RequestAnnexeStatusEnum.PENDING.getValue();
        filterList.add("ra.statusIdc <> "+ status);
        if (filterList.size() > 0) {
            filters = "AND " + String.join(" AND ", filterList) + " \n";
        }
        return filters;
    }


    public String getOrderByDateAnnexe(){
        return  "ORDER BY ra.createdAt asc ";
    }

    //#region Mappers

    private AnnexeRequestJpaEntity mapToJpaEntity(RequestAnnexe requestAnnexe) {
        return new ModelMapperConfig().getStrictModelMapper().map(requestAnnexe, AnnexeRequestJpaEntity.class);
    }

    //#endregion
}
