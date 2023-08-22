package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.CommercialManagementDTO;

import java.util.Date;
import java.util.List;

public interface CommercialManagementViewPort {
    List<CommercialManagementDTO> search(String status);
    List<CommercialManagementDTO> search(String status, String subStatus);
    List<CommercialManagementDTO> search(Date fromDate, Date toDate);

    List<CommercialManagementDTO> search(String status, Date fromDate, Date toDate);
    List<CommercialManagementDTO> search(String status, String subStatus, Date fromDate, Date toDate);

}
