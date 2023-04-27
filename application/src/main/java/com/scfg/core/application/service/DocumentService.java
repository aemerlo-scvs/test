package com.scfg.core.application.service;

import com.scfg.core.application.port.in.DocumentUseCase;
import com.scfg.core.application.port.out.DocumentPort;
import com.scfg.core.domain.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService implements DocumentUseCase {
    private final DocumentPort documentPort;
    @Override
    public List<Document> getAll() {
        return documentPort.getAllDocument();
    }

    @Override
    public Boolean save(Document o) {
        return documentPort.saveOrUpdate(o);
    }

    @Override
    public Boolean update(Document o) {
        return documentPort.saveOrUpdate(o);
    }

    @Override
    public Boolean delete(Document o) {
        return null;
    }
}
