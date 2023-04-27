package com.scfg.core.domain.dto.fabolous;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class FabolousUploadDTO implements Serializable {

    private Long uploadId;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date reportDateUpload;

    private String user;

    private String reportType;

    private String policy;

    private String month;

    private String year;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;

    private Long totalUpload;

    @Builder
    public FabolousUploadDTO(Long uploadId, Date reportDateUpload, String user, String reportType, String policy, String month, String year, Date createdAt, Long totalUpload) {
        this.uploadId = uploadId;
        this.reportDateUpload = reportDateUpload;
        this.user = user;
        this.reportType = reportType;
        this.policy = policy;
        this.month = month;
        this.year = year;
        this.createdAt = createdAt;
        this.totalUpload = totalUpload;
    }

    public FabolousUploadDTO() {
    }
}
