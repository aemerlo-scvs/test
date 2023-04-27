package com.scfg.core.application.port.out;

import com.scfg.core.domain.PolicyFileDocument;

import java.util.List;

public interface PolicyFileDocumentPort {
    List<PolicyFileDocument> findAll();
    Boolean saveAllOrUpdate(List<PolicyFileDocument> o);
    PolicyFileDocument saveOrUpdate(PolicyFileDocument o);

}
