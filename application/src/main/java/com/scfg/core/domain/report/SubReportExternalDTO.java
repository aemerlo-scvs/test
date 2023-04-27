package com.scfg.core.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SubReportExternalDTO {
    private String resourceName;
    private Map<String, SubReportContentDTO> fixedParameters;
    private List<SubReportContentDTO> beans;
    private List<String> embeddedResourcesName;
}
