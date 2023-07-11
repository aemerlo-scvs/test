package com.scfg.core.application.port.out;

import com.scfg.core.domain.person.ReferencePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferencePersonPort {

    ReferencePerson saveOrUpdate(ReferencePerson referencePerson);

    boolean saveOrUpdateAll(List<ReferencePerson> referencePersonList);
}
