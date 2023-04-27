package com.scfg.core.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ReportDTO implements Serializable {
    private String fileName;
    @NotEmpty(
            message = "subReportsDto cannot be empty"
    )
    private List<SubReportDTO> subReportsDto;
}
