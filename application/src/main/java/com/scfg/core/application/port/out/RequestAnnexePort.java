package com.scfg.core.application.port.out;


import com.scfg.core.domain.dto.vin.RequestAnnexeDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestAnnexePort {
    Long saveOrUpdate(RequestAnnexeDTO requestAnnexe);
    List<RequestAnnexeDTO> getRequestByPolicyIdAndAnnexeTypeIdAndStatus(Long policyId, Long annexeTypeId,
                                                                        Integer requestStatus);
}
