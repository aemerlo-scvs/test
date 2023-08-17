package com.scfg.core.application.port.in;

import com.scfg.core.domain.FileDocument;

import com.scfg.core.domain.dto.FileDocumentDTO;

import java.io.IOException;

public interface VIRHUseCase {
    String getDataInformationPolicy(String param);
    FileDocument getDocument(Long id);
    Boolean sendWhatsApp(String number, String message, Long requestId);
    Boolean sendWhatsAppWithAttachment(String number, String message, Long requestId, Long docId);
    FileDocumentDTO generate() throws IOException;
}
