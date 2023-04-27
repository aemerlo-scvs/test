package com.scfg.core.common.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "application")
public class MyProperties {
    private String pathexcel;
    private String pathdocument;
    private String pathCertificateCoverage;
    private String pathFbsExcel;
    private String pathFonts;
    private String pathImages;
    private String pathGenericDocuments;

    public String getPathImages() {
        return pathImages;
    }

    public void setPathImages(String pathImages) {
        this.pathImages = pathImages;
    }

    public String getPathFonts() {
        return pathFonts;
    }

    public void setPathFonts(String pathFonts) {
        this.pathFonts = pathFonts;
    }

    private String pathFormatFileLoadSales;

    public String getPathFormatFileLoadSales() {
        return pathFormatFileLoadSales;
    }

    public void setPathFormatFileLoadSales(String pathFormatFileLoadSales) {
        this.pathFormatFileLoadSales = pathFormatFileLoadSales;
    }

    public String getPathFbsExcel() {
        return pathFbsExcel;
    }

    public void setPathFbsExcel(String pathFbsExcel) {
        this.pathFbsExcel = pathFbsExcel;
    }

    public String getPathReportCommercials() {
        return pathReportCommercials;
    }

    public void setPathReportCommercials(String pathReportCommercials) {
        this.pathReportCommercials = pathReportCommercials;
    }

    private String pathReportCommercials;

    public String getPathexcel() {
        return pathexcel;
    }

    public void setPathexcel(String pathexcel) {
        this.pathexcel = pathexcel;
    }

    public String getPathdocument() {
        return pathdocument;
    }

    public void setPathdocument(String pathdocument) {
        this.pathdocument = pathdocument;
    }

    public String getPathCertificateCoverage() {
        return pathCertificateCoverage;
    }

    public void setPathCertificateCoverage(String pathCertificateCoverage) {
        this.pathCertificateCoverage = pathCertificateCoverage;
    }

    public String getPathGenericDocuments() {
        return pathGenericDocuments;
    }

    public void setPathGenericDocuments(String pathGenericDocuments) {
        this.pathGenericDocuments = pathGenericDocuments;
    }
}
