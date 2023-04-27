package com.scfg.core.application.port.out;


import com.scfg.core.domain.managers.Branch_Office;

import java.util.List;

public interface Branch_OfficePort {
//    Branch_Office getBranchById(BigInteger id);
    List<Branch_Office> getAllBranch();
}
