package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SignDocumentDTO {
    private List<String> ownerSigns;
    private String base64Document;
}
