package com.scfg.core.application.port.out;

import com.scfg.core.domain.dto.virh.CommercialManagementViewWppSenderDTO;

import java.util.List;

public interface CommercialManagementViewWppSenderPort {
    List<CommercialManagementViewWppSenderDTO> findAll();
}
