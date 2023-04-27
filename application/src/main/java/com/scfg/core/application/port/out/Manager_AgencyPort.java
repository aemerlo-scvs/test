package com.scfg.core.application.port.out;


import com.scfg.core.domain.managers.Manager_Agency;

import java.util.List;

public interface Manager_AgencyPort {
//    Manager_Agency getManagerAgencyById(BigInteger id);
    List<Manager_Agency> getAllManagerAgency();
}
