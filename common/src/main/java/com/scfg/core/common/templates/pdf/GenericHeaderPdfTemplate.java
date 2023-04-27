package com.scfg.core.common.templates.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.scfg.core.common.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

public class GenericHeaderPdfTemplate extends PdfPageEventHelper {
    @Autowired
    private final MyProperties properties;

    public GenericHeaderPdfTemplate(MyProperties properties) {
        this.properties = properties;
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

    }

    public void onCloseDocument(PdfWriter writer, Document document) {

    }
}
