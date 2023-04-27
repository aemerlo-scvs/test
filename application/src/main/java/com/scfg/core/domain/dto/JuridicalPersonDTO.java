package com.scfg.core.domain.dto;

import com.scfg.core.domain.Policy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class JuridicalPersonDTO {
    private String name;
    private String imageLogo;
    private String mimeType;
    private Integer businessTypeIdc;
    private List<Policy> policyDto;
}
