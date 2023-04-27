package com.scfg.core.application.port.out;


import com.scfg.core.domain.managers.Agency;

import java.util.List;
import java.util.Map;

public interface AgencyPort {
//    Agency getAgencyById (BigInteger id);
    List<Agency> getAllAgency();

    Agency getAgencyByDescription(String angencyDescription);

    Map<String, Object> findOrUpsert(Agency agencyDomain) throws NoSuchFieldException, IllegalAccessException;
}
