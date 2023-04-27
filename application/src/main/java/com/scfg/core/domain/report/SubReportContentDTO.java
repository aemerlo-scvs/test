package com.scfg.core.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder

public class SubReportContentDTO implements Serializable {
    private Object content;
    private String contentImpl;
}
