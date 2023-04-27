package com.scfg.core.domain.configuracionesSistemas;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FilterParamenter {
    private String name;
    private Date datefrom;
    private Date dateto;
    private Integer status;
    private Long branchId;
}
