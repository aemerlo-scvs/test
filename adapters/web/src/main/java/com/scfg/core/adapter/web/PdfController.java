package com.scfg.core.adapter.web;

import com.scfg.core.application.port.in.PdfService;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/pdf")
@Api(tags = "API REST PDF")
public class PdfController {
    private final PdfService pdfService;
    @PostMapping("/generatePdfFile")
    public FileDocumentDTO  generatePdfFile(@RequestBody String contentToGenerate) throws IOException {
        FileDocumentDTO fileDocumentDTO = pdfService.convertHtmlToPdf(contentToGenerate);

        return fileDocumentDTO;
    }
}
