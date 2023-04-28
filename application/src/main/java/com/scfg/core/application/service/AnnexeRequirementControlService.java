package com.scfg.core.application.service;

import com.scfg.core.application.port.in.AnnexeRequirementControlUseCase;
import com.scfg.core.application.port.out.AnnexeRequirementControlPort;

import com.scfg.core.application.port.out.FileDocumentPort;
import com.scfg.core.application.port.out.RequestAnnexePort;
import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.common.enums.AnnexeRequirementEnum;
import com.scfg.core.common.enums.TypesAttachmentsEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.Document;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.PolicyItem;
import com.scfg.core.domain.common.RequestAnnexe;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.vin.AnnexeRequirementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnexeRequirementControlService implements AnnexeRequirementControlUseCase {

    private final AnnexeRequirementControlPort annexeRequirementControlPort;
    private final FileDocumentPort fileDocumentPort;
    private final RequestAnnexePort requestAnnexePort;
    private final PolicyItemPort policyItemPort;

    @Override
    public List<AnnexeRequirementDto> getAllByRequestAnnexeIdAndAnnexeTypeId(Long requestAnnexeId, Long annexeTypeId) {
        RequestAnnexe requestAnnexe = requestAnnexePort.findRequestAnnexeIdOrThrowExcepcion(requestAnnexeId);
        PolicyItem policyItem = policyItemPort.findByPolicyId(requestAnnexe.getPolicyId());

        List<AnnexeRequirementDto> list = new ArrayList<>();

        //#region Add Certificate Coverage

        FileDocument certDocument = fileDocumentPort.findLastByPolicyItemIdAndDocumentTypeIdc(policyItem.getId(),
                TypesAttachmentsEnum.COVERAGECERTIFICATE.getValue());

        if (certDocument != null) {
            Document certificateCoverageFileDocument = Document.builder()
                    .mimeType(HelpersConstants.PDF)
                    .description(certDocument.getDescription())
                    .content(certDocument.getContent())
                    .documentTypeIdc(certDocument.getTypeDocument())
                    .build();

            AnnexeRequirementDto reqCertificateCoverage = new AnnexeRequirementDto();
            reqCertificateCoverage.setDescription(AnnexeRequirementEnum.CERTIFICATE_COVERAGE.getValue());
            reqCertificateCoverage.setComment("OBTENIDO DEL SISTEMA");
            reqCertificateCoverage.setEdited(false);
            reqCertificateCoverage.setSigned(false);
            reqCertificateCoverage.setFileDocument(certificateCoverageFileDocument);

            list.add(reqCertificateCoverage);
        }

        //#endregion

        list.addAll(annexeRequirementControlPort.findAllByRequestAnnexeIdAndAnnexeTypeId(requestAnnexeId));

        return list;
    }
}
