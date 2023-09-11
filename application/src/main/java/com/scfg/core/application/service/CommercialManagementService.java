package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CommercialManagementUseCase;
import com.scfg.core.application.port.out.CommercialManagementPort;
import com.scfg.core.application.port.out.CommercialManagementViewPort;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.CommercialManagement;
import com.scfg.core.domain.dto.CommercialManagementDTO;
import com.scfg.core.domain.dto.CommercialManagementSearchFiltersDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommercialManagementService implements CommercialManagementUseCase {
    private final CommercialManagementPort port;
    private final CommercialManagementViewPort portView;

    @Override
    public PersistenceResponse save(CommercialManagement obj) {
        PersistenceResponse response = port.save(obj, true);
        return response;
    }


    @Override
    public PersistenceResponse update(CommercialManagement obj) {
        PersistenceResponse response = port.update(obj);
        return response;
    }
    @Override
    public PersistenceResponse updateSomeFields(CommercialManagement obj) {
        CommercialManagement cm = port.findById(obj.getId());
        cm.setAssignedUserId(obj.getAssignedUserId());
        cm.setManagementStatusIdc(obj.getManagementStatusIdc());
        cm.setManagementSubStatusIdc(obj.getManagementSubStatusIdc());
        PersistenceResponse response = port.update(cm);
        return response;
    }


    @Override
    public List<CommercialManagementDTO> search(CommercialManagementSearchFiltersDTO filtersDTO) {

        if (filtersDTO.getStatus() != null
                && filtersDTO.getSubStatus() != null
                && (filtersDTO.getFromDate() != null && filtersDTO.getToDate() != null)
        ) {
            return portView.search(filtersDTO.getStatus(), filtersDTO.getSubStatus(), filtersDTO.getFromDate(), filtersDTO.getToDate());

        }
        if (filtersDTO.getStatus() != null
                && (filtersDTO.getFromDate() != null && filtersDTO.getToDate() != null)
        ) {
            return portView.search(filtersDTO.getStatus(), filtersDTO.getFromDate(), filtersDTO.getToDate());
        }
        if (filtersDTO.getStatus() == null
                && filtersDTO.getSubStatus() == null
                && (filtersDTO.getFromDate() != null && filtersDTO.getToDate() != null)
        ) {
            return portView.search(filtersDTO.getFromDate(), filtersDTO.getToDate());
        }
        if (filtersDTO.getStatus() != null
                && filtersDTO.getSubStatus() != null
                && (filtersDTO.getFromDate() == null || filtersDTO.getToDate() == null)
        ) {
            return portView.search(filtersDTO.getStatus(), filtersDTO.getSubStatus());
        }
        if (filtersDTO.getStatus() != null
                && filtersDTO.getSubStatus() == null
                && filtersDTO.getFromDate() == null
        ) {
            return portView.search(filtersDTO.getStatus());
        }
        return new ArrayList<>();
    }

    @Override
    public String searchJSON(CommercialManagementSearchFiltersDTO commercialManagementSearchFiltersDto) {
        return portView.searchJSON(commercialManagementSearchFiltersDto);
    }

    @Override
    public CommercialManagement findById(Long id) {
        return port.findById(id);
    }

    @Override
    public Boolean saveAll(List<CommercialManagement> commercialManagementList) {
        return port.saveAll(commercialManagementList);
    }

    @Override
    public Boolean existsComercialManagementId(String comercialManagementId) {
        if (!comercialManagementId.isEmpty() && comercialManagementId.length()==36){

        return port.existsComercialManagementId(comercialManagementId);}
        else return false;
    }


}
