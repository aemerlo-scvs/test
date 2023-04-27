package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.Policy;
import com.scfg.core.domain.PolicyItem;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.configuracionesSistemas.BrokerDTO;
import com.scfg.core.domain.dto.CoverageDTO;
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
public class VinProposalDetail {
    private GeneralRequest request;
    private List<CoverageDTO> coverageList;
    private PolicyItem policyItem;
    private Person insurer;
    private Policy policy;
    private List<Beneficiary> beneficiaryList;
    private Person insurerCompany;
    private Person holderCompany;
    private List<Classifier> extList;
    private List<Classifier> relationShipList;
    private List<Classifier> moneyTypeList;
    private Account account;
    private BrokerDTO broker;
}
