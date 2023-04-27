package com.scfg.core.application.port.in;

import com.scfg.core.domain.Document;

import java.util.List;

public interface DocumentUseCase {
    List<Document> getAll();
    Boolean save(Document o);
    Boolean update(Document o);
    Boolean delete(Document o);
}
