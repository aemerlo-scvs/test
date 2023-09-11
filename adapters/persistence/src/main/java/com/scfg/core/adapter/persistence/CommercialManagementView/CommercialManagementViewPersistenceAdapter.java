package com.scfg.core.adapter.persistence.CommercialManagementView;

import com.scfg.core.application.port.out.CommercialManagementViewPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.CommercialManagementDTO;
import com.scfg.core.domain.dto.CommercialManagementSearchFiltersDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.NTextType;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class CommercialManagementViewPersistenceAdapter implements CommercialManagementViewPort {

    private final EntityManager em;

    private final CommercialManagementViewRepository repository;


    @Override
    public String searchJSON(CommercialManagementSearchFiltersDTO filtersDTO) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("proc_filter_commercial_management_view")
                .registerStoredProcedureParameter(
                        "managementStatusIdc",
                        Integer.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "managementSubStatusIdc",
                        Integer.class,
                        ParameterMode.IN
                )

                .registerStoredProcedureParameter(
                        "fromDate",
                        Date.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "toDate",
                        Date.class,
                        ParameterMode.IN
                )

                .registerStoredProcedureParameter(
                        "result",
                        NTextType.class,
                        ParameterMode.OUT
                )
                .setParameter("managementStatusIdc", filtersDTO.getStatus())
                .setParameter("managementSubStatusIdc", filtersDTO.getSubStatus())
                .setParameter("fromDate", filtersDTO.getFromDate())
                .setParameter("toDate", filtersDTO.getToDate());
        query.execute();

        String list = (String) query.getOutputParameterValue("result");
        em.close();
        return list;
    }


    @Override
    public List<CommercialManagementDTO> search(Integer status) {
        List<CommercialManagementDTO> cmvList = repository.getAllByStatus(status);
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
