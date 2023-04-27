package com.scfg.core.domain.smvs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ParametersFromDTO {
    private Date startDate;
    private Date toDate;
    private Integer statusRequest;
    private Integer page;
    private Integer size;

    @Override
    public String toString() {
        return "ParametersFromDTO{" +
                "startDate=" + startDate +
                ", toDate=" + toDate +
                ", statusRequest=" + statusRequest +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
