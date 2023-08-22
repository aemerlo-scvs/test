package com.scfg.core.adapter.persistence.CommercialManagementView;

import com.scfg.core.application.port.out.CommercialManagementViewPort;
import com.scfg.core.common.PersistenceAdapter;
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
    public List<CommercialManagementDTO> search(String status) {
        List<CommercialManagementDTO> cmvList = repository.getAllByStatus(status);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }

    @Override
    public List<CommercialManagementDTO> search(String status, String subStatus) {
        List<CommercialManagementDTO> cmvList = repository.getAllByStatusAndSubStatus(status, subStatus);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }

    @Override
    public List<CommercialManagementDTO> search(Date fromDate, Date toDate) {
        List<CommercialManagementDTO> cmvList = repository.getAllByDates(fromDate, toDate);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }

    @Override
    public List<CommercialManagementDTO> search(String status, Date fromDate, Date toDate) {
        List<CommercialManagementDTO> cmvList = repository.getAllByStateAndDates(status, fromDate, toDate);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }

    @Override
    public List<CommercialManagementDTO> search(String status, String subStatus, Date fromDate, Date toDate) {
        List<CommercialManagementDTO> cmvList = repository.getAllByAllFilters(status, subStatus, fromDate, toDate);
        return cmvList.size() > 0 ? cmvList : new ArrayList<>();
    }
}
