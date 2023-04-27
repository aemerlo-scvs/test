package com.scfg.core.domain.dto;

import java.io.Serializable;
import java.util.Date;

public class FileDocumentDTOMail extends FileDocumentDTO implements Serializable {
    Date dateFrom;
    Date dateTo;

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
