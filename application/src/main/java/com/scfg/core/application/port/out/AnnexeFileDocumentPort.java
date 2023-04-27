package com.scfg.core.application.port.out;

import com.scfg.core.domain.Document;

import java.util.List;

public interface AnnexeFileDocumentPort {
    List<Document> findAll();
    Long saveOrUpdate(Document document);
// TODO hacer consulta anidada con AnnexeRequirementControl para obtener
//  el documento que tiene relacion con el campo fileDocument de AnnexeRequirementControl


}
