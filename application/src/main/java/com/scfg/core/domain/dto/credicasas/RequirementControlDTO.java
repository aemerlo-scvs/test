package com.scfg.core.domain.dto.credicasas;

import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.RelationRequirements;
import com.scfg.core.domain.common.Classifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RequirementControlDTO {

    private Long id;
    private String description;
    private LocalDate requestDate;
    private LocalDate receptionDate;
    private String comment;
    private Long requirementId;
    private Long policyItemId;
    private FileDocument fileDocument;

    //#region Custom Constructors
    //GEL Constructors
    public RequirementControlDTO(RelationRequirements relationRequirements, List<Classifier> classifiers, long policyItemId) {
        String requirementName = classifiers.stream().filter(x -> x.getReferenceId().equals(relationRequirements.getRequirementIdc())).findFirst().get().getDescription();
        this.description = requirementName;
        this.requestDate = LocalDate.now();
        this.requirementId = relationRequirements.getId();
        this.policyItemId = policyItemId;
    }

    //#endregion
}
