package com.scfg.core.application.port.out;



import com.scfg.core.domain.managers.Zone;

import java.util.List;

public interface ZonePort {
//    Zone getZoneById(BigInteger id);
    List<Zone> getAllZone();
}
