package com.scfg.core.application.port.in;

import com.scfg.core.domain.FileDocument;

import com.scfg.core.domain.dto.FileDocumentDTO;

import java.io.IOException;

public interface VIRHUseCase {
    String getDataInformationPolicy(String param);
    FileDocument getDocument(Long id);
    Boolean sendWhatsApp(String number, String message, Long requestId, Integer referecenteTableIdc);
    Boolean sendWhatsAppWithAttachment(String number, String message, Long requestId, Long docId, Integer referecenteTableIdc);
    FileDocumentDTO generate() throws IOException;
    String saveInformationPolicy(String data);

    String getWelcomeMessageText(String insuredName, String oldProduct);

    String savePayment(String transactionId, String paymentMethod);
}
