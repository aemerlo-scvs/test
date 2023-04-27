package com.scfg.core.application.service;

import com.scfg.core.application.port.in.RequestDetailUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.BusinessGroupEnum;
import com.scfg.core.common.enums.RequestStatusEnum;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.SearchPepDTO;
import com.scfg.core.domain.dto.credicasas.CurrentAmountRequestDTO;
import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import com.scfg.core.domain.person.Person;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@UseCase
@RequiredArgsConstructor
public class RequestDetailService implements RequestDetailUseCase {
    private final RequestDetailPort requestDetailPort;
    private final PlanPort planPort;
    private final GeneralRequestPort generalRequestPort;
    private final PersonPort personPort;
    private final PEPPort pepPort;

    @Override
    public RequestDetailDTO getRequestDetail(RequestDetailDTO requestDetailDTO) {

        Long idPerson = generalRequestPort.getGeneralRequest(requestDetailDTO.getId()).getPersonId();
        Person p = personPort.findById(idPerson);

        List<CurrentAmountRequestDTO> generalRequestList = generalRequestPort.findAllCurrentAmountGeneralRequestByPersonId(idPerson);

        List<Plan> planList = planPort.getPlanByFinancialGroup(BusinessGroupEnum.CREDICASAS.getValue());
        Set<Long> planIds = planList.stream().map(Plan::getId).collect(Collectors.toSet());

        CurrentAmountRequestDTO actualRequest = generalRequestList.stream().filter(o -> o.getId().equals(requestDetailDTO.getId())).findFirst().orElse(null);

        boolean isPEP = pepPort.existsByIdentificationNumberOrName(new SearchPepDTO(p.getNaturalPerson()));

        double actualAccumulatedAmount = generalRequestList.stream().filter(o -> o.getCurrentAmount() != null && planIds.contains(o.getPlanId())
                        && (o.getRequestStatusIdc() == RequestStatusEnum.FINALIZED.getValue() || o.getRequestStatusIdc() == RequestStatusEnum.PENDING.getValue()))
                .mapToDouble(CurrentAmountRequestDTO::getCurrentAmount).sum();

        String requestOrganization = requestDetailPort.getJuridicalNameByPolicyItemId(requestDetailDTO.getPolicyItemId());


        requestDetailDTO.setOrganizationName(requestOrganization);
        requestDetailDTO.setActualCurrentAmount(actualRequest != null ? actualRequest.getCurrentAmount() : 0);
        requestDetailDTO.setActualAccumulatedAmount(actualAccumulatedAmount);
        requestDetailDTO.setIsPep(isPEP);

        return requestDetailDTO;
    }

}
