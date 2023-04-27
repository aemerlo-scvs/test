package com.scfg.core.common.templates.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.scfg.core.common.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

public class HeaderFooterPdfTemplate extends PdfPageEventHelper {
    @Autowired
    private final MyProperties properties;

    private Long certificateNumber;
    private Long djsNumber;

    public HeaderFooterPdfTemplate(MyProperties properties, Long certificateNumber, Long djsNumber) {
        this.properties = properties;
        this.certificateNumber = certificateNumber;
        this.djsNumber = djsNumber;
    }

    public PdfPTable table;
    /** The Graphic state */
    public PdfGState gstate;
    /** A template that will hold the total number of pages. */
    public PdfTemplate tpl;

    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            // initialization of the template
            tpl = writer.getDirectContent().createTemplate(100, 100);
            tpl.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        }
        catch(Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void onStartPage(PdfWriter writer, Document document) {
        try {
            Image img = Image.getInstance(properties.getPathImages()+"SC-Vida-y-Salud-fondo-blanco.jpg");
            img.scaleToFit(200,300);
            img.setAbsolutePosition(25,876);
            document.add(img);
            Paragraph jumpLine = new Paragraph("\n");
            document.add(jumpLine);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void onEndPage(PdfWriter writer, Document document) {
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        Font newFont = FontFactory.getFont("ARIAL", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, Color.BLACK);
        BaseFont baseFont = newFont.getBaseFont();
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        String text = "";
        int removeSpace = 0;
        if (certificateNumber > 0) {
            text = "Página " + writer.getPageNumber() + " - No. Certificado: " + this.certificateNumber + " - No. DJS: " + this.djsNumber;
//            text = "No. Certificado " + this.certificateNumber + " - No. DJS: " + this.djsNumber + " - Página " + writer.getPageNumber() + " de ";
//            removeSpace = 150;
        } else {
            text =  "Página " + writer.getPageNumber() + " - No. DJS: " + this.djsNumber;
//            text = "No. DJS: " + this.djsNumber + " - Página " + writer.getPageNumber() + " de ";
//            removeSpace = 80;
        }
        float textSize = 0f; //15f
        float textBase = document.bottom() - 20;
        cb.beginText();
        cb.setFontAndSize(baseFont, 8);
        float adjust = baseFont.getWidthPoint("0", 8);
        cb.setTextMatrix(document.left() - textSize - adjust - removeSpace, textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(tpl, document.left() - adjust, textBase);
        cb.saveState();
        // draw a Rectangle around the page
        cb.setLineWidth(2);
        cb.stroke();
        cb.restoreState();
        // starting on page 3, a watermark with an Image that is made transparent
        if (writer.getPageNumber() >= 3) {
//            cb.setGState(gstate);
//            cb.setColorFill(Color.red);
//            cb.beginText();
//            cb.setFontAndSize(baseFont, 48);
//            cb.showTextAligned(Element.ALIGN_CENTER, "Watermark Opacity " + writer.getPageNumber(), document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
//            cb.endText();
        }
        cb.restoreState();
        cb.sanityCheck();
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
//        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
//        Font newFont = FontFactory.getFont("ARIAL", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, Color.BLACK);
//        BaseFont baseFont = newFont.getBaseFont();
//        tpl.beginText();
//        tpl.setFontAndSize(baseFont, 8);
//        tpl.setTextMatrix(0, 0);
//        tpl.showText(Integer.toString(writer.getPageNumber() - 1));
//        tpl.endText();
//        tpl.sanityCheck();
    }
}
