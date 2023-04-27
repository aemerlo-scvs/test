package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessageResponse extends BaseDomain {
    private Long messageSentId;
    private String message;
    private String from;
}
