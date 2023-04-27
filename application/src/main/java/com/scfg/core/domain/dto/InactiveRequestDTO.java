package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class InactiveRequestDTO {
    private Long id;
    private String comment;

    @Override
    public String toString() {
        return "InactiveRequestDTO{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                '}';
    }
}
