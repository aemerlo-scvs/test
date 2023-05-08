package com.scfg.core.domain.common;

import com.scfg.core.common.enums.RequestAnnexeStatusEnum;
import com.scfg.core.domain.dto.vin.RequestAnnexeDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ApiModel(description = "Model para realizar la solicitud de anexo")
public class RequestAnnexe extends BaseDomain {

    private Integer reasonIdc;
    private Integer statusIdc;
    private String comment;
    private Long annexeTypeId;
    private Long policyId;
    private LocalDateTime requestDate;


    public RequestAnnexe(RequestAnnexeDTO requestAnnexeDTO) {
        this.reasonIdc = requestAnnexeDTO.getAnnulmentReasonIdc();
        this.statusIdc = requestAnnexeDTO.getRequestStatusEnum() == null ?
                        RequestAnnexeStatusEnum.PENDING.getValue() :
                        requestAnnexeDTO.getRequestStatusEnum().getValue();
        this.comment = requestAnnexeDTO.getComment();
        this.annexeTypeId = requestAnnexeDTO.getAnnexeTypeId();
        this.policyId = requestAnnexeDTO.getPolicyDetail().getId();
        this.requestDate = requestAnnexeDTO.getRequestDate();
    }
}
