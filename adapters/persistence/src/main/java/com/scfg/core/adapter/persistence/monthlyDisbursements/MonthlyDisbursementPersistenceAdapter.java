package com.scfg.core.adapter.persistence.monthlyDisbursements;


import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemAdapter;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemRepository;
import com.scfg.core.application.port.out.MonthlyDisbursementPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import com.scfg.core.domain.liquidationMortgageRelief.MonthlyDisbursement;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class MonthlyDisbursementPersistenceAdapter implements MonthlyDisbursementPort {

    private final MonthlyDisbursementRepository monthlyDisbursementRepository;
    private final MortgageReliefItemRepository mortgageReliefItemRepository;

    @Override
    public PersistenceResponse registerMonthlyDisbursementsForRegulatedPolicy(List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlDTOS, long overwrite) {
        List<MonthlyDisbursementJpaEntity> lastObservedCaseJpaEntities = monthlyDisbursementDhlDTOS.stream()
                .map(manualCertificate -> mapMonthlyDisbursementDhlDtoToJpaEntityForCreate(manualCertificate))
                .collect(Collectors.toList());
        monthlyDisbursementRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                MonthlyDisbursementDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public PersistenceResponse registerMonthlyDisbursementsForNotRegulatedPolicy(List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhlDTOS, long overwrite) {
        List<MonthlyDisbursementJpaEntity> lastObservedCaseJpaEntities = monthlyDisbursementDhlDTOS.stream()
                .map(manualCertificate -> mapMonthlyDisbursementDhnDtoToJpaEntityForCreate(manualCertificate))
                .collect(Collectors.toList());
        monthlyDisbursementRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                MonthlyDisbursementDhnDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }


    @Override
    public List<MonthlyDisbursementDhlDTO> getMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<MonthlyDisbursementJpaEntity> monthlyDisbursementJpaEntities = monthlyDisbursementRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCode(),
                ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return monthlyDisbursementJpaEntities.stream()
                .map(monthlyDisbursement -> mapMonthlyDisbursementJpaEntityToDtoDhl(monthlyDisbursement, true))
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyDisbursementDhnDTO> getMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<MonthlyDisbursementJpaEntity> monthlyDisbursementJpaEntities = monthlyDisbursementRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCode(),
                ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return monthlyDisbursementJpaEntities.stream()
                .map(monthlyDisbursement -> mapMonthlyDisbursementJpaEntityToDtoDhn(monthlyDisbursement, true))
                .collect(Collectors.toList());
    }


    @Override
    public List<MonthlyDisbursementDhlDTO> getPastMonthlyDisbursementDHLFilteredForPeriod(long monthNumber, long yearNumber, long insurancePolicyHolderId) {
        List<MonthlyDisbursementJpaEntity> monthlyDisbursementJpaEntities = monthlyDisbursementRepository
                .findAllByPolicyType_IdAndMonth_NumberAndYear_NumberAndInsurancePolicyHolder_Id(
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                        monthNumber,
                        yearNumber,
                        insurancePolicyHolderId,
                        ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCode(),
                        ClassifierEnum.MonthlyDisbursementsDHL_ReportType.getReferenceCodeType()
                );
        // Mapper data
        return monthlyDisbursementJpaEntities.stream()
                .map(monthlyDisbursement -> mapMonthlyDisbursementJpaEntityToDtoDhl(monthlyDisbursement, true))
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyDisbursementDhnDTO> getPastMonthlyDisbursementDHNFilteredForPeriod(long monthNumber, long yearNumber, long insurancePolicyHolderId) {
        List<MonthlyDisbursementJpaEntity> monthlyDisbursementJpaEntities = monthlyDisbursementRepository
                .findAllByPolicyType_IdAndMonth_NumberAndYear_NumberAndInsurancePolicyHolder_Id(
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                        ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                        monthNumber,
                        yearNumber,
                        insurancePolicyHolderId,
                        ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCode(),
                        ClassifierEnum.MonthlyDisbursementsDHN_ReportType.getReferenceCodeType()
                );
        // Mapper data
        return monthlyDisbursementJpaEntities.stream()
                .map(monthlyDisbursement -> mapMonthlyDisbursementJpaEntityToDtoDhn(monthlyDisbursement, true))
                .collect(Collectors.toList());
    }

    @Override
    public void updateCaseStatus(MonthlyDisbursement monthlyDisbursement) {
        monthlyDisbursementRepository.updateCaseStatus(
                monthlyDisbursement.getCreditOperationNumber(),
                monthlyDisbursement.getClientDocumentNumber(),
                monthlyDisbursement.getCaseStatus());
    }

    @Override
    public void updateAllCaseStatus(long monthId, long yearId, long insurancePolicyHolderId, long policyTypeId, int caseStatus) {
        monthlyDisbursementRepository.updateAllCaseStatus(monthId, yearId, insurancePolicyHolderId, policyTypeId, caseStatus);
    }


    public static MonthlyDisbursementJpaEntity mapMonthlyDisbursementDhlDtoToJpaEntityForCreate(MonthlyDisbursementDhlDTO monthlyDisbursementDhlDTO) {
        return MonthlyDisbursementJpaEntity.builder()
                /*.creditOperationNumber(monthlyDisbursementDhlDTO.getNRO_OPERACION())
                .clientNames(monthlyDisbursementDhlDTO.getNOMBRES())
                .clientLastname(monthlyDisbursementDhlDTO.getAPELLIDO_PATERNO())
                .clientMotherLastname(monthlyDisbursementDhlDTO.getAPELLIDO_MATERNO())
                .clientMarriedLastname(monthlyDisbursementDhlDTO.getAPELLIDO_CASADA())
                .documentNumber(monthlyDisbursementDhlDTO.getNRO_DOCUMENTO())
                .duplicateCopy(monthlyDisbursementDhlDTO.getCOPIA_DUPLICADO())
                .extension(monthlyDisbursementDhlDTO.getEXTENSION())*/

                .documentType(monthlyDisbursementDhlDTO.getTIPO_DOCUMENTO())
                .place(monthlyDisbursementDhlDTO.getPLAZA())
                //.disbursementDate(monthlyDisbursementDhlDTO.getFECHA_DESEMBOLSO())
                .disbursedAmount(monthlyDisbursementDhlDTO.getMONTO_DESEMBOLSADO())
                .insuredValue(monthlyDisbursementDhlDTO.getVALOR_ASEGURADO())
                //.birthDate(monthlyDisbursementDhlDTO.getFECHA_NACIMIENTO())
                .expirationDate(monthlyDisbursementDhlDTO.getFECHA_VENCIMIENTO())
                //.currencyType(monthlyDisbursementDhlDTO.getMONEDA())
                //.creditType(monthlyDisbursementDhlDTO.getTIPO_CREDITO())
                .borrowRole(monthlyDisbursementDhlDTO.getASEGURADO())
                .borrowCoverage(monthlyDisbursementDhlDTO.getCOBERTURA())
                //.gender(monthlyDisbursementDhlDTO.getSEXO())
                .period(monthlyDisbursementDhlDTO.getPERIODO())
                //.creditLine(monthlyDisbursementDhlDTO.getLINEA_CREDITO())
                .creditTermDays(monthlyDisbursementDhlDTO.getPLAZO_CREDITO_DIAS())
                .premiumAmount(monthlyDisbursementDhlDTO.getMONTO_PRIMA())
                .extraPremium(monthlyDisbursementDhlDTO.getEXTRAPRIMA())
                .ratePremium(monthlyDisbursementDhlDTO.getTASAX())
                //.nationality(monthlyDisbursementDhlDTO.getNACIONALIDAD())
                .paidFrom(monthlyDisbursementDhlDTO.getPAGADO_DESDE())
                .paidUp(monthlyDisbursementDhlDTO.getPAGADO_HASTA())

                // For relationship
                .client(ClientJpaEntity.builder().
                        id(monthlyDisbursementDhlDTO.getID_CLIENTE())
                        .build())
                .creditOperation(CreditOperationJpaEntity.builder()
                        .id(monthlyDisbursementDhlDTO.getID_OPERACION_CREDITICIA())
                        .build())
                .agency(AgencyJpaEntity.builder()
                        .AGENCY_ID(BigInteger.valueOf(monthlyDisbursementDhlDTO.getID_AGENCIA()))
                        .build())
                .creditType(ClassifierJpaEntity.builder()
                        .id(monthlyDisbursementDhlDTO.getID_TIPO_CREDITO())
                        .build())
                .coverage(ClassifierJpaEntity.builder()
                        .id(monthlyDisbursementDhlDTO.getID_TIPO_COBERTURA())
                        .build())
                .currency(ClassifierJpaEntity.builder()
                        .id(monthlyDisbursementDhlDTO.getID_MONEDA())
                        .build())


                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(monthlyDisbursementDhlDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                // For create
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        monthlyDisbursementDhlDTO.getITEM_DESGRAVAMEN()
                ))*/

                .build();
    }

    public static MonthlyDisbursementJpaEntity mapMonthlyDisbursementDhnDtoToJpaEntityForCreate(MonthlyDisbursementDhnDTO monthlyDisbursementDhnDTO) {
        return MonthlyDisbursementJpaEntity.builder()
                .documentType(monthlyDisbursementDhnDTO.getTIPO_DOCUMENTO())
                .place(monthlyDisbursementDhnDTO.getPLAZA())
                .disbursedAmount(monthlyDisbursementDhnDTO.getMONTO_DESEMBOLSADO())
                .insuredValue(monthlyDisbursementDhnDTO.getVALOR_ASEGURADO())
                .expirationDate(monthlyDisbursementDhnDTO.getFECHA_VENCIMIENTO())
                .borrowRole(monthlyDisbursementDhnDTO.getASEGURADO())
                .borrowCoverage(monthlyDisbursementDhnDTO.getCOBERTURA())
                .period(monthlyDisbursementDhnDTO.getPERIODO())
                .creditTermDays(monthlyDisbursementDhnDTO.getPLAZO_CREDITO())
                .premiumAmount(monthlyDisbursementDhnDTO.getPRIMA_BS())
                .extraPremium(monthlyDisbursementDhnDTO.getEXTRAPRIMA_BS())
                .ratePremium(monthlyDisbursementDhnDTO.getTASA_PRIMA())
                .paidFrom(null)
                .paidUp(null)

                // For relationship
                .client(ClientJpaEntity.builder().
                        id(monthlyDisbursementDhnDTO.getID_CLIENTE())
                        .build())
                .creditOperation(CreditOperationJpaEntity.builder()
                        .id(monthlyDisbursementDhnDTO.getID_OPERACION_CREDITICIA())
                        .build())
                .agency(AgencyJpaEntity.builder()
                        .AGENCY_ID(BigInteger.valueOf(monthlyDisbursementDhnDTO.getID_AGENCIA()))
                        .build())
                .creditType(ClassifierJpaEntity.builder()
                        .id(monthlyDisbursementDhnDTO.getID_TIPO_CREDITO())
                        .build())
                .coverage(ClassifierJpaEntity.builder()
                        .id(monthlyDisbursementDhnDTO.getID_TIPO_COBERTURA())
                        .build())
                .currency(ClassifierJpaEntity.builder()
                        .id(monthlyDisbursementDhnDTO.getID_MONEDA())
                        .build())

                .mortgageReliefItem(MortgageReliefItemJpaEntity.builder()
                        .id(monthlyDisbursementDhnDTO.getID_ITEM_DESGRAVAMEN())
                        .build())
                // For create
                /*.mortgageReliefItem(MortgageReliefItemAdapter.mapMortgageReliefItemDtoToJpaEntityForCreate(
                        monthlyDisbursementDhnDTO.getITEM_DESGRAVAMEN()
                ))*/

                .build();
    }

    public static MonthlyDisbursementDhlDTO mapMonthlyDisbursementJpaEntityToDtoDhl(
            MonthlyDisbursementJpaEntity monthlyDisbursementJpaEntity,
            boolean withRelations) {

        ClientJpaEntity client = monthlyDisbursementJpaEntity.getClient();
        CreditOperationJpaEntity creditOperation = monthlyDisbursementJpaEntity.getCreditOperation();
        AgencyJpaEntity agency = monthlyDisbursementJpaEntity.getAgency();
        ClassifierJpaEntity creditType = monthlyDisbursementJpaEntity.getCreditType();
        ClassifierJpaEntity coverageType = monthlyDisbursementJpaEntity.getCoverage();
        ClassifierJpaEntity currency = monthlyDisbursementJpaEntity.getCurrency();

        MonthlyDisbursementDhlDTO monthlyDisbursementDhlDTO = MonthlyDisbursementDhlDTO.builder()
                .NRO_OPERACION(creditOperation.getOperationNumber())
                .NOMBRES(client.getNames())
                .APELLIDO_PATERNO(client.getLastName())
                .APELLIDO_MATERNO(client.getMothersLastName())
                .APELLIDO_CASADA(client.getMarriedLastName())
                .TIPO_DOCUMENTO(monthlyDisbursementJpaEntity.getDocumentType())
                .NRO_DOCUMENTO(client.getDocumentNumber())
                .COPIA_DUPLICADO(client.getDuplicateCopy())
                .EXTENSION(client.getExtension())
                .PLAZA(monthlyDisbursementJpaEntity.getPlace())
                .FECHA_DESEMBOLSO(creditOperation.getDisbursementDate())
                .VALOR_ASEGURADO(creditOperation.getDisbursedAmount())
                .TASAX(monthlyDisbursementJpaEntity.getRatePremium())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .MONTO_DESEMBOLSADO(monthlyDisbursementJpaEntity.getDisbursedAmount())
                .FECHA_VENCIMIENTO(monthlyDisbursementJpaEntity.getExpirationDate())
                .MONEDA(currency.getDescription())
                .TIPO_CREDITO(creditType.getDescription())
                .ASEGURADO(monthlyDisbursementJpaEntity.getBorrowRole())
                .COBERTURA(monthlyDisbursementJpaEntity.getBorrowCoverage())
                .SEXO(client.getGender())
                .PERIODO(monthlyDisbursementJpaEntity.getPeriod())
                .LINEA_CREDITO(creditOperation.getCreditLine() == 1 ? "SI" : "NO")
                .PLAZO_CREDITO_DIAS(monthlyDisbursementJpaEntity.getCreditTermDays())
                .EXTRAPRIMA(monthlyDisbursementJpaEntity.getExtraPremium())
                .NACIONALIDAD(client.getNationality())
                .AGENCIA(agency.getDESCRIPTION())
                .MONTO_PRIMA(monthlyDisbursementJpaEntity.getPremiumAmount())
                .PAGADO_DESDE(monthlyDisbursementJpaEntity.getPaidFrom())
                .PAGADO_HASTA(monthlyDisbursementJpaEntity.getPaidUp())
//                .MONTH_ORDER(monthlyDisbursementJpaEntity.getMortgageReliefItem().getLoadMonth().getOrder())
//                .YEAR_ORDER(monthlyDisbursementJpaEntity.getMortgageReliefItem().getLoadYear().getOrder())
                .build()
                // For JSON API
                .hiddenRelationship();
        if (withRelations) {
            monthlyDisbursementDhlDTO.setID_CLIENTE(client.getId());
            monthlyDisbursementDhlDTO.setID_OPERACION_CREDITICIA(creditOperation.getId());
            monthlyDisbursementDhlDTO.setID_AGENCIA(agency.getAGENCY_ID()
                    .longValue());
            monthlyDisbursementDhlDTO.setID_TIPO_CREDITO(creditType.getId());
            monthlyDisbursementDhlDTO.setID_TIPO_COBERTURA(coverageType.getId());
            monthlyDisbursementDhlDTO.setID_MONEDA(currency.getId());
        }
        return monthlyDisbursementDhlDTO;
    }

    public static MonthlyDisbursementDhnDTO mapMonthlyDisbursementJpaEntityToDtoDhn(
            MonthlyDisbursementJpaEntity monthlyDisbursementJpaEntity,
            boolean withRelations) {
        ClientJpaEntity client = monthlyDisbursementJpaEntity.getClient();
        CreditOperationJpaEntity creditOperation = monthlyDisbursementJpaEntity.getCreditOperation();
        AgencyJpaEntity agency = monthlyDisbursementJpaEntity.getAgency();
        ClassifierJpaEntity creditType = monthlyDisbursementJpaEntity.getCreditType();
        ClassifierJpaEntity coverageType = monthlyDisbursementJpaEntity.getCoverage();
        ClassifierJpaEntity currency = monthlyDisbursementJpaEntity.getCurrency();

        MonthlyDisbursementDhnDTO monthlyDisbursementDhnDTO = MonthlyDisbursementDhnDTO.builder()
                .NRO_OPERACION(creditOperation.getOperationNumber())
                .NOMBRES(client.getNames())
                .APELLIDO_PATERNO(client.getLastName())
                .APELLIDO_MATERNO(client.getMothersLastName())
                .APELLIDO_CASADA(client.getMarriedLastName())
                .TIPO_DOCUMENTO(monthlyDisbursementJpaEntity.getDocumentType())
                .NRO_DOCUMENTO(client.getDocumentNumber())
                .COPIA_DUPLICADO(client.getDuplicateCopy())
                .EXTENSION(client.getExtension())
                .PLAZA(monthlyDisbursementJpaEntity.getPlace())
                .FECHA_DESEMBOLSO(creditOperation.getDisbursementDate())
                .VALOR_ASEGURADO(creditOperation.getDisbursedAmount())
                .TASA_PRIMA(monthlyDisbursementJpaEntity.getRatePremium())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .MONTO_DESEMBOLSADO(monthlyDisbursementJpaEntity.getDisbursedAmount())
                .FECHA_VENCIMIENTO(monthlyDisbursementJpaEntity.getExpirationDate())
                .MONEDA(currency.getDescription())
                .TIPO_CREDITO(creditType.getDescription())
                .TIPO_COBERTURA(coverageType.getDescription())
                .ASEGURADO(monthlyDisbursementJpaEntity.getBorrowRole())
                .COBERTURA(monthlyDisbursementJpaEntity.getBorrowCoverage())
                .SEXO(client.getGender())
                .PERIODO(monthlyDisbursementJpaEntity.getPeriod())
                .LINEA_CREDITO(creditOperation.getCreditLine() == 1 ? "SI" : "NO")
                .PLAZO_CREDITO(monthlyDisbursementJpaEntity.getCreditTermDays())
                .TASA_EXTRAPRIMA(monthlyDisbursementJpaEntity.getExtraPremium())
                .NACIONALIDAD(client.getNationality())
                .AGENCIA(agency.getDESCRIPTION())
                .PRIMA_BS(monthlyDisbursementJpaEntity.getPremiumAmount())
                .build()
                // For JSON API
                .hiddenRelationship();

        if (withRelations) {
            monthlyDisbursementDhnDTO.setID_CLIENTE(client.getId());
            monthlyDisbursementDhnDTO.setID_OPERACION_CREDITICIA(creditOperation.getId());
            monthlyDisbursementDhnDTO.setID_AGENCIA(agency.getAGENCY_ID()
                    .longValue());
            monthlyDisbursementDhnDTO.setID_TIPO_CREDITO(creditType.getId());
            monthlyDisbursementDhnDTO.setID_TIPO_COBERTURA(coverageType.getId());
            monthlyDisbursementDhnDTO.setID_MONEDA(currency.getId());
        }
        return monthlyDisbursementDhnDTO;
    }

}
