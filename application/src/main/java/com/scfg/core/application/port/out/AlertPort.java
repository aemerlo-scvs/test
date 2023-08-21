package com.scfg.core.application.port.out;

import com.scfg.core.domain.Alert;

import java.util.List;

public interface AlertPort {
    Alert findByAlert(Integer id);

    List<Alert> loadAllAlerts();
    List<Alert> findByIdList(List<Integer> idList);
}
