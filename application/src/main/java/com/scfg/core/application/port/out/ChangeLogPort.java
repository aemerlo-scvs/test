package com.scfg.core.application.port.out;

import com.scfg.core.domain.common.ChangeLog;
import org.springframework.data.domain.Page;


public interface ChangeLogPort {
    Object findAllByPage(int page, int size);
    Boolean save(ChangeLog changeLog);
}
