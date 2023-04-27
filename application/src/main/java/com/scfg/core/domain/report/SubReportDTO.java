package com.scfg.core.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder

public class SubReportDTO implements Serializable {
    @NotEmpty(
            message = "resourceName cannot be empty"
    )
    private String resourceName;
    private Map<String, Object> fixedParameters;
    private List<Object> beans;
    private List<String> embeddedResourcesName;
}
