package com.scfg.core.adapter.persistence.subscriptionReport;

import com.scfg.core.application.port.out.SubscriptionReportPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.liquidationMortgageRelief.SubscriptionReportDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class SubscriptionReportAdapter implements SubscriptionReportPort {

    private final SubscriptionReportRepository subscriptionReportRepository;

    @Override
    public List<SubscriptionReportDTO> getSubscriptionReportSingleFiltered(String ci, String operationNumber) {
        try {
            List<SubscriptionReportJpaEntity> subscriptionReportJpaEntityList = subscriptionReportRepository
                    .findAllByHdocumentNumberAndDoperationNumber(ci, operationNumber);
            return subscriptionReportJpaEntityList.stream()
                    .map(subscriptionReportJpaEntity -> mapSubscriptionReportJpaEntityToDto(subscriptionReportJpaEntity))
                    .collect(Collectors.toList());
        } catch(Exception e) {
            String error = e.getMessage();
        }
        return null;
    }

    @Override
    public List<SubscriptionReportDTO> getSubscriptionReportFiltered(String policyName, long monhtNumber, long year) {
        return null;
    }


    public static SubscriptionReportDTO mapSubscriptionReportJpaEntityToDto(SubscriptionReportJpaEntity subscriptionReportJpaEntity) {
        try {
            SubscriptionReportDTO subscriptionReportDTO = new SubscriptionReportDTO();
            subscriptionReportDTO.setAge(subscriptionReportJpaEntity.getKage());
            subscriptionReportDTO.setSubscriptionStatus(subscriptionReportJpaEntity.getNsubscriptionStatus());
            subscriptionReportDTO.setBirthDate(subscriptionReportJpaEntity.getJbirthDate());
            subscriptionReportDTO.setCoverage(subscriptionReportJpaEntity.getRcoverage());
            subscriptionReportDTO.setCurrency(subscriptionReportJpaEntity.getLcurrency());
            subscriptionReportDTO.setDjsFillDate(subscriptionReportJpaEntity.getCdjsFillDate());
            subscriptionReportDTO.setDocumentNumber(subscriptionReportJpaEntity.getHdocumentNumber());
            subscriptionReportDTO.setExtension(subscriptionReportJpaEntity.getIextension());
            subscriptionReportDTO.setExtraPremiumRate(subscriptionReportJpaEntity.getOextraPremiumRate());
            subscriptionReportDTO.setGender(subscriptionReportJpaEntity.getGgender());
            subscriptionReportDTO.setGrantedCoverage(subscriptionReportJpaEntity.getPgrantedCoverage());
            subscriptionReportDTO.setInsured(subscriptionReportJpaEntity.getFinsured());
            subscriptionReportDTO.setOperationNumber(subscriptionReportJpaEntity.getDoperationNumber());
            subscriptionReportDTO.setPolicyCode(subscriptionReportJpaEntity.getBpolicyCode());
            subscriptionReportDTO.setPolicyType(subscriptionReportJpaEntity.getApolicyType());
            subscriptionReportDTO.setPronouncementDate(subscriptionReportJpaEntity.getQpronouncementDate());
            subscriptionReportDTO.setRequestAmountBs(subscriptionReportJpaEntity.getMrequestAmountBs());
            subscriptionReportDTO.setRequestNumber(subscriptionReportJpaEntity.getErequestNumber());
            return subscriptionReportDTO;
        } catch (Exception e) {
            String x = e.getMessage();
        }
        return null;

    }
}
