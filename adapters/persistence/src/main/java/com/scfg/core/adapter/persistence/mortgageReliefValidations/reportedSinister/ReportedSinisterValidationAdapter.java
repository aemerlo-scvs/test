package com.scfg.core.adapter.persistence.mortgageReliefValidations.reportedSinister;

import com.scfg.core.adapter.persistence.sinister.SinisterJpaEntity;
import com.scfg.core.adapter.persistence.sinister.SinisterRepository;
import com.scfg.core.application.port.out.mortgageReliefValidations.ReportedSinisterValidationPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.dto.liquidationMortgageRelief.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.scfg.core.common.util.HelpersConstants.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class ReportedSinisterValidationAdapter implements ReportedSinisterValidationPort {

    private final SinisterRepository sinisterRepository;

    @Override
    public ValidationResponseDTO reportedSinisterDHL(String documentNumber, Long insurancePolicyHolder) {
        List<SinisterJpaEntity> sinisterJpaEntityList = sinisterRepository
                .findAllByClient_DocumentNumber(
                        documentNumber,
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.SinisterDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.SinisterDHL_ReportType.getReferenceCodeType());
        if (sinisterJpaEntityList.size() > 0) {
            SinisterJpaEntity sinisterJpaEntity = sinisterJpaEntityList.get(0);
            if (sinisterJpaEntity.getCurrentState().equals(HelpersConstants.PAID)) {
                return new ValidationResponseDTO(false, VR_REPORT_PAID_SINISTER);
            }
            return new ValidationResponseDTO(true, null);
        }
        return new ValidationResponseDTO(true, null);
    }

    @Override
    public ValidationResponseDTO reportedSinisterDHN(String documentNumber, Long insurancePolicyHolder) {
        List<SinisterJpaEntity> sinisterJpaEntityList = sinisterRepository
                .findAllByClient_DocumentNumber(
                        documentNumber,
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        insurancePolicyHolder,
                        ClassifierEnum.SinisterDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.SinisterDHN_ReportType.getReferenceCodeType());
        if (sinisterJpaEntityList.size() > 0) {
            SinisterJpaEntity sinisterJpaEntity = sinisterJpaEntityList.get(0);
            if (sinisterJpaEntity.getCurrentState().equals(HelpersConstants.PAID)) {
                return new ValidationResponseDTO(false, VR_REPORT_PAID_SINISTER);
            }
            return new ValidationResponseDTO(true, null);
        }
        return new ValidationResponseDTO(true, null);
    }
}
