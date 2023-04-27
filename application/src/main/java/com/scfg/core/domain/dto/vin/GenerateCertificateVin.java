package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.Policy;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.configuracionesSistemas.BrokerDTO;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.MessageDTO;
import com.scfg.core.domain.person.Person;
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
public class GenerateCertificateVin {
    private Person insurer;
    private Person insurerCompany;
    private Person holderCompany;
    private Policy policy;
    private List<CoverageDTO> coverageDTOList;
    private List<Beneficiary> beneficiaryList;
    private Classifier moneyType;
    private Classifier nationality;
    private Classifier maritalStatus;
    private List<Classifier> relationShipList;
    private GeneralRequest request;
    private Account account;
    private List<Classifier> extensions;
    private Classifier regional;
    private MessageDTO messageDTO;
    private BrokerDTO brokerDTO;
}
