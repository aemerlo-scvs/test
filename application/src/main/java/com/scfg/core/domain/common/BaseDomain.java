package com.scfg.core.domain.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BaseDomain {

    private Long id;

//    @NotNull
//    private Integer status;

    @NotNull
    private Date createdAt;

    private Date lastModifiedAt;

    private Long createdBy;

    private Long lastModifiedBy;
}
