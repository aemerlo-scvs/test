package com.scfg.core.application.service;

import com.scfg.core.application.port.in.PdfService;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.FileDocumentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {
    @Override
    public FileDocumentDTO convertHtmlToPdf(String htmlContent) {
        htmlContent="<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
//                "    <style>\n" +
//                "        h1 {\n" +
//                "            color: #25a7e7;\n" +
//                "            text-align: center;\n" +
//                "        }\n" +
//                "        .receipt-header {\n" +
//                "            width: 100%;\n" +
//                "        }\n" +
//                "        .receipt {\n" +
//                "            width: 100%;\n" +
//                "        }\n" +
//                "        .receipt, .receipt th, .receipt td {\n" +
//                "            border: 1px solid #25a7e7;\n" +
//                "            border-collapse: collapse;\n" +
//                "        }\n" +
//                "        .receipt th {\n" +
//                "            background-color: #25a7e7;\n" +
//                "            color: white;\n" +
//                "        }\n" +
//                "        .total {\n" +
//                "            text-align: right;\n" +
//                "        }\n" +
//                "    </style>\n" +
                "</head>\n" +
                "<body>\n "+
                /*"<h1>Receipt</h1>\n" +
                "<div>\n" +
                "    <table class=\"receipt-header\">\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "                <table>\n" +
                "                    <tr>\n" +
                "                        <th>Bill To:</th>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Company Name: Simple Solution</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Address: 123 Sample Street</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Email: info@simplesolution.dev</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Phone: 123 456 789</td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "            <td align=\"right\">\n" +
                "                <img width=\"140\" src=\"https://simplesolution.dev/images/Logo_S_v1.png\" />\n" +
                "                <br />\n" +
                "                Simple Solution\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</div>\n" +
                "\n" +
                "<br />\n" +
                "<table class=\"receipt\">\n" +
                "    <tr>\n" +
                "        <th>Item #</th>\n" +
                "        <th>Description</th>\n" +
                "        <th>Quantity</th>\n" +
                "        <th>Unit Price</th>\n" +
                "        <th>Total</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>1</td>\n" +
                "        <td>Item 1 Description</td>\n" +
                "        <td>5</td>\n" +
                "        <td>$100</td>\n" +
                "        <td>$500</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>2</td>\n" +
                "        <td>Item 2 Description</td>\n" +
                "        <td>10</td>\n" +
                "        <td>$20</td>\n" +
                "        <td>$200</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>3</td>\n" +
                "        <td>Item 3 Description</td>\n" +
                "        <td>2</td>\n" +
                "        <td>$50</td>\n" +
                "        <td>$100</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td class=\"total\" colspan=\"4\"><b>Total</b></td>\n" +
                "        <td><b>$800</b></td>\n" +
                "    </tr>\n" +
                "</table>\n" +*/
                htmlContent +
                "</body>\n" +
                "</html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream, false);
        renderer.finishPDF();
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        FileDocumentDTO fd = new FileDocumentDTO();
        fd.setContent(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        fd.setMime(HelpersConstants.PDF);
        fd.setName("Preuba pdf html");
        return fd;
    }
}
