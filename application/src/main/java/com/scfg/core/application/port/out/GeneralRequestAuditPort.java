package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.GeneralRequestAudit;

public interface GeneralRequestAuditPort {
    long saveOrUpdate(GeneralRequestAudit generalRequestAudit);
}
