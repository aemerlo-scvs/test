package com.scfg.core.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ReportExternalDTO {
    private String fileName;
    private List<SubReportExternalDTO> subReports;
}
