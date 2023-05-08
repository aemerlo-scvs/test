package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

public interface PdfService {
    FileDocumentDTO convertHtmlToPdf(String htmlContent);
}
