package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessageDecideResponseDTO {
    private Long requestId;
    private Integer response;
    private Integer referenceTable;

    @Override
    public String toString() {
        return "{" +
                "\"requestId\":" + requestId +
                ", \"response\":" + response +
                ", \"referenceTable\":" + referenceTable +
                '}';
    }
}
