package com.scfg.core.adapter.persistence.CommercialManagementView;

import com.scfg.core.application.port.out.CommercialManagementViewPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.util.ObjectMapperUtils;
import com.scfg.core.domain.dto.CommercialManagementDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class CommercialManagementViewPersistenceAdapter implements CommercialManagementViewPort {

    private final CommercialManagementViewRepository repository;
    @Override
    public List<CommercialManagementDTO> search(String phoneNumber) {
        List<CommercialManagementViewJpaEntity> cmvList = repository.getByPhoneNumber(phoneNumber);
        return cmvList.size() > 0 ?  ObjectMapperUtils.mapAll(cmvList, CommercialManagementDTO.class) : new ArrayList<>();
    }
    @Override
    public List<CommercialManagementDTO> search(String identificationNumber, boolean isIdentificationNumber) {
        List<CommercialManagementDTO> cmvList = repository.getByIdentificationNumber(identificationNumber);
        return cmvList.size() > 0 ?  cmvList : new ArrayList<>();
    }

    @Override
    public List<CommercialManagementDTO> search(String phoneNumber, String identificationNumber) {
        List<CommercialManagementDTO> cmvList = repository.getByPhoneNumberAndIdentificationNumber(phoneNumber,identificationNumber);
        return cmvList.size() > 0 ?  cmvList : new ArrayList<>();
    }


    @Override
    public List<CommercialManagementDTO> search(Integer status) {
        List<CommercialManagementDTO> cmvList = repository.getAllByStatus(status);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }
    @Override
    public List<CommercialManagementDTO> search(Integer status, String identificationNumber) {
        List<CommercialManagementDTO> cmvList = repository.getAllByStatusAndIdentificationNumber(status, identificationNumber);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }
    @Override
    public List<CommercialManagementDTO> search(Integer status, Integer subStatus) {
        List<CommercialManagementDTO> cmvList = repository.getAllByStatusAndSubStatus(status, subStatus);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }

    @Override
    public List<CommercialManagementDTO> search(Date fromDate, Date toDate) {
        List<CommercialManagementDTO> cmvList = repository.getAllByDates(fromDate, toDate);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }

    @Override
    public List<CommercialManagementDTO> search(Integer status, Date fromDate, Date toDate) {
        List<CommercialManagementDTO> cmvList = repository.getAllByStateAndDates(status, fromDate, toDate);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }

    @Override
    public List<CommercialManagementDTO> search(Integer status, Integer subStatus, Date fromDate, Date toDate) {
        List<CommercialManagementDTO> cmvList = repository.getAllByAllFilters(status, subStatus, fromDate, toDate);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }


}
