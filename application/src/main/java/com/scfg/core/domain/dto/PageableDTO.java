package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PageableDTO {
    private Object content;
    private boolean first;
    private boolean last;
    private boolean empty;
    private int totalElements;
    private int totalPages;
}
