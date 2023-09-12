package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.CommercialManagementDTO;
import com.scfg.core.domain.dto.CommercialManagementSearchFiltersDTO;

import java.util.Date;
import java.util.List;

public interface CommercialManagementViewPort {
    String searchJSON(Integer status, Integer subStatus, Date fromDate, Date toDate);
    List<CommercialManagementDTO> search(Integer status);
    List<CommercialManagementDTO> search(Integer status, Integer subStatus);
    List<CommercialManagementDTO> search(Date fromDate, Date toDate);

    List<CommercialManagementDTO> search(Integer status, Date fromDate, Date toDate);
    List<CommercialManagementDTO> search(Integer status, Integer subStatus, Date fromDate, Date toDate);



}
