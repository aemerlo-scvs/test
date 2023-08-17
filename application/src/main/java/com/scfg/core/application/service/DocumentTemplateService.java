package com.scfg.core.application.service;

import com.scfg.core.application.port.in.DocumentTemplateUse;
import com.scfg.core.application.port.out.DocumentTemplatePort;
import com.scfg.core.common.util.PDFMerger;
import com.scfg.core.domain.common.DocumentTemplate;
import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.File;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentTemplateService implements DocumentTemplateUse {
    private final DocumentTemplatePort documentTemplatePort;

    @Override
    public List<DocumentTemplate> getDocumentTemplateList(String reporting) {
        List<DocumentTemplate> list = documentTemplatePort.getAll();
        List<DocumentTemplate> templateList = new ArrayList<>();
        DocumentTemplate template = (DocumentTemplate) list.stream().filter(a -> a.getDescription().equals(reporting)).findFirst().get();
        templateList.add(template);
        List<DocumentTemplate> templates = list.stream().filter(a -> a.getIdDocumentTemplate() == template.getId()).collect(Collectors.toList());
        if (templates.size() > 0)
            templateList.addAll(templates);
        return templateList;
    }

    @Override
    public boolean save(DocumentTemplate documentTemplate) {
        return documentTemplatePort.save(documentTemplate);
    }

    @Override
    public FileDocumentDTO load() throws FileNotFoundException {

        BASE64Decoder decoder = new BASE64Decoder();
        List<InputStream> inputStreamList = new ArrayList<>();
        List<DocumentTemplate> list = documentTemplatePort.getDocumentByProductId(6L);
        String base64EncodedImageBytes="";
        List<byte[]> pdfFilesAsByteArray =new ArrayList<>();
        FileDocumentDTO file= new FileDocumentDTO();
        try {
            for (DocumentTemplate sw : list) {
                byte[] myBytes = decoder.decodeBuffer(sw.getContent());
//                InputStream myInputStream = new ByteArrayInputStream(myBytes);
//                inputStreamList.add(myInputStream);
                pdfFilesAsByteArray.add(myBytes);
            }
            byte [] content= PDFMerger.mergePDF(pdfFilesAsByteArray);
            file.setName("test");
            file.setContent(Base64.getEncoder().encodeToString(content));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }


}
