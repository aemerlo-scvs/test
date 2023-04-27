package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.ResultPolicyDtO;

import java.util.List;

public interface Knowledge_HistoryPortIn {
    boolean processExcelConsolidManagerPolicy(List<ResultPolicyDtO>list);
}
