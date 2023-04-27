package com.scfg.core.adapter.persistence.pastMonthlyDisbursements;


import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.application.port.out.PastMonthlyDisbursementPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhlDTO;
import com.scfg.core.domain.dto.liquidationMortgageRelief.MonthlyDisbursementDhnDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class PastMonthlyDisbursementPersistenceAdapter implements PastMonthlyDisbursementPort {

    private final PastMonthlyDisbursementRepository pastMonthlyDisbursementRepository;

    @Override
    public PersistenceResponse savePastMonthlyDisbursementsForRegulatedPolicy(List<MonthlyDisbursementDhlDTO> monthlyDisbursementDhlDTOS, long overwrite) {
        List<PastMonthlyDisbursementJpaEntity> lastObservedCaseJpaEntities = monthlyDisbursementDhlDTOS.stream()
                .map(manualCertificate -> mapMonthlyDisbursementDhlDtoToJpaEntityForCreate(manualCertificate))
                .collect(Collectors.toList());
        pastMonthlyDisbursementRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                MonthlyDisbursementDhlDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public PersistenceResponse savaPastMonthlyDisbursementsForNotRegulatedPolicy(List<MonthlyDisbursementDhnDTO> monthlyDisbursementDhlDTOS, long overwrite) {
        List<PastMonthlyDisbursementJpaEntity> lastObservedCaseJpaEntities = monthlyDisbursementDhlDTOS.stream()
                .map(manualCertificate -> mapMonthlyDisbursementDhnDtoToJpaEntityForCreate(manualCertificate))
                .collect(Collectors.toList());
        pastMonthlyDisbursementRepository.saveAll(lastObservedCaseJpaEntities);
        return new PersistenceResponse(
                MonthlyDisbursementDhnDTO.class.getSimpleName(),
                overwrite == HelpersConstants.ACCEPT_OVERWRITE_INFORMATION
                        ? ActionRequestEnum.OVERWRITE
                        : ActionRequestEnum.CREATE,
                null
        );
    }

    @Override
    public List<MonthlyDisbursementDhlDTO> getPastMonthlyDisbursementDHLFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<PastMonthlyDisbursementJpaEntity> monthlyDisbursementJpaEntities = pastMonthlyDisbursementRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.RegulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.RegulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.PastMonthlyDisbursementsDHL_ReportType.getReferenceCode(),
                ClassifierEnum.PastMonthlyDisbursementsDHL_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return monthlyDisbursementJpaEntities.stream()
                .map(monthlyDisbursement -> mapMonthlyDisbursementJpaEntityToDtoDhl(monthlyDisbursement))
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyDisbursementDhnDTO> getPastMonthlyDisbursementDHNFiltered(long monthId, long yearId, long insurancePolicyHolderId) {
        List<PastMonthlyDisbursementJpaEntity> monthlyDisbursementJpaEntities = pastMonthlyDisbursementRepository.findAllByPolicyType_IdAndMonth_IdAndYear_IdAndInsurancePolicyHolder_Id(
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCode(),
                ClassifierEnum.UnregulatedDH_PolicyType.getReferenceCodeType(),
                monthId,
                yearId,
                insurancePolicyHolderId,
                ClassifierEnum.PastMonthlyDisbursementsDHN_ReportType.getReferenceCode(),
                ClassifierEnum.PastMonthlyDisbursementsDHN_ReportType.getReferenceCodeType()
        );
        // Mapper data
        return monthlyDisbursementJpaEntities.stream()
                .map(monthlyDisbursement -> mapMonthlyDisbursementJpaEntityToDtoDhn(monthlyDisbursement))
                .collect(Collectors.toList());
    }


    public static PastMonthlyDisbursementJpaEntity mapMonthlyDisbursementDhlDtoToJpaEntityForCreate(MonthlyDisbursementDhlDTO monthlyDisbursementDhlDTO) {
        return PastMonthlyDisbursementJpaEntity.builder()
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

    public static PastMonthlyDisbursementJpaEntity mapMonthlyDisbursementDhnDtoToJpaEntityForCreate(MonthlyDisbursementDhnDTO monthlyDisbursementDhnDTO) {
        return PastMonthlyDisbursementJpaEntity.builder()
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

    public static MonthlyDisbursementDhlDTO mapMonthlyDisbursementJpaEntityToDtoDhl(PastMonthlyDisbursementJpaEntity pastMonthlyDisbursementJpaEntity) {
        ClientJpaEntity client = pastMonthlyDisbursementJpaEntity.getClient();
        CreditOperationJpaEntity creditOperation = pastMonthlyDisbursementJpaEntity.getCreditOperation();
        AgencyJpaEntity agency = pastMonthlyDisbursementJpaEntity.getAgency();
        ClassifierJpaEntity creditType = pastMonthlyDisbursementJpaEntity.getCreditType();
        ClassifierJpaEntity coverageType = pastMonthlyDisbursementJpaEntity.getCoverage();
        ClassifierJpaEntity currency = pastMonthlyDisbursementJpaEntity.getCurrency();

        return MonthlyDisbursementDhlDTO.builder()
                .NRO_OPERACION(creditOperation.getOperationNumber())
                .NOMBRES(client.getNames())
                .APELLIDO_PATERNO(client.getLastName())
                .APELLIDO_MATERNO(client.getMothersLastName())
                .APELLIDO_CASADA(client.getMarriedLastName())
                .TIPO_DOCUMENTO(pastMonthlyDisbursementJpaEntity.getDocumentType())
                .NRO_DOCUMENTO(client.getDocumentNumber())
                .COPIA_DUPLICADO(client.getDuplicateCopy())
                .EXTENSION(client.getExtension())
                .PLAZA(pastMonthlyDisbursementJpaEntity.getPlace())
                .FECHA_DESEMBOLSO(creditOperation.getDisbursementDate())
                .VALOR_ASEGURADO(creditOperation.getDisbursedAmount())
                .TASAX(pastMonthlyDisbursementJpaEntity.getRatePremium())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .MONTO_DESEMBOLSADO(pastMonthlyDisbursementJpaEntity.getDisbursedAmount())
                .FECHA_VENCIMIENTO(pastMonthlyDisbursementJpaEntity.getExpirationDate())
                .MONEDA(currency.getDescription())
                .TIPO_CREDITO(creditType.getDescription())
                .ASEGURADO(pastMonthlyDisbursementJpaEntity.getBorrowRole())
                .COBERTURA(pastMonthlyDisbursementJpaEntity.getBorrowCoverage())
                .SEXO(client.getGender())
                .PERIODO(pastMonthlyDisbursementJpaEntity.getPeriod())
                .LINEA_CREDITO(creditOperation.getCreditLine() == 1 ? "SI" : "NO")
                .PLAZO_CREDITO_DIAS(pastMonthlyDisbursementJpaEntity.getCreditTermDays())
                .EXTRAPRIMA(pastMonthlyDisbursementJpaEntity.getExtraPremium())
                .NACIONALIDAD(client.getNationality())
                .AGENCIA(agency.getDESCRIPTION())
                .MONTO_PRIMA(pastMonthlyDisbursementJpaEntity.getPremiumAmount())
                .PAGADO_DESDE(pastMonthlyDisbursementJpaEntity.getPaidFrom())
                .PAGADO_HASTA(pastMonthlyDisbursementJpaEntity.getPaidUp())

                .build()
                // For JSON API
                .hiddenRelationship();
    }

    public static MonthlyDisbursementDhnDTO mapMonthlyDisbursementJpaEntityToDtoDhn(PastMonthlyDisbursementJpaEntity pastMonthlyDisbursementJpaEntity) {
        ClientJpaEntity client = pastMonthlyDisbursementJpaEntity.getClient();
        CreditOperationJpaEntity creditOperation = pastMonthlyDisbursementJpaEntity.getCreditOperation();
        AgencyJpaEntity agency = pastMonthlyDisbursementJpaEntity.getAgency();
        ClassifierJpaEntity creditType = pastMonthlyDisbursementJpaEntity.getCreditType();
        ClassifierJpaEntity coverageType = pastMonthlyDisbursementJpaEntity.getCoverage();
        ClassifierJpaEntity currency = pastMonthlyDisbursementJpaEntity.getCurrency();

        return MonthlyDisbursementDhnDTO.builder()
                .NRO_OPERACION(creditOperation.getOperationNumber())
                .NOMBRES(client.getNames())
                .APELLIDO_PATERNO(client.getLastName())
                .APELLIDO_MATERNO(client.getMothersLastName())
                .APELLIDO_CASADA(client.getMarriedLastName())
                .TIPO_DOCUMENTO(pastMonthlyDisbursementJpaEntity.getDocumentType())
                .NRO_DOCUMENTO(client.getDocumentNumber())
                .COPIA_DUPLICADO(client.getDuplicateCopy())
                .EXTENSION(client.getExtension())
                .PLAZA(pastMonthlyDisbursementJpaEntity.getPlace())
                .FECHA_DESEMBOLSO(creditOperation.getDisbursementDate())
                .VALOR_ASEGURADO(creditOperation.getDisbursedAmount())
                .TASA_PRIMA(pastMonthlyDisbursementJpaEntity.getRatePremium())
                .FECHA_NACIMIENTO(client.getBirthDate())
                .MONTO_DESEMBOLSADO(pastMonthlyDisbursementJpaEntity.getDisbursedAmount())
                .FECHA_VENCIMIENTO(pastMonthlyDisbursementJpaEntity.getExpirationDate())
                .MONEDA(currency.getDescription())
                .TIPO_CREDITO(creditType.getDescription())
                .TIPO_COBERTURA(coverageType.getDescription())
                .ASEGURADO(pastMonthlyDisbursementJpaEntity.getBorrowRole())
                .COBERTURA(pastMonthlyDisbursementJpaEntity.getBorrowCoverage())
                .SEXO(client.getGender())
                .PERIODO(pastMonthlyDisbursementJpaEntity.getPeriod())
                .LINEA_CREDITO(creditOperation.getCreditLine() == 1 ? "SI" : "NO")
                .PLAZO_CREDITO(pastMonthlyDisbursementJpaEntity.getCreditTermDays())
                .TASA_EXTRAPRIMA(pastMonthlyDisbursementJpaEntity.getExtraPremium())
                .NACIONALIDAD(client.getNationality())
                .AGENCIA(agency.getDESCRIPTION())
                .PRIMA_BS(pastMonthlyDisbursementJpaEntity.getPremiumAmount())

                .build()
                // For JSON API
                .hiddenRelationship();
    }

}
