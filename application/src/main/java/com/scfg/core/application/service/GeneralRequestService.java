package com.scfg.core.application.service;

import com.scfg.core.application.port.in.GeneralRequestUseCase;
import com.scfg.core.application.port.out.CoveragePort;
import com.scfg.core.application.port.out.GeneralRequestPort;
import com.scfg.core.application.port.out.PlanPort;
import com.scfg.core.common.enums.BusinessGroupEnum;
import com.scfg.core.common.enums.RequestStatusEnum;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.GeneralRequest;
import com.scfg.core.domain.Plan;
import com.scfg.core.domain.dto.InactiveRequestDTO;
import com.scfg.core.domain.dto.PersonDTO;
import com.scfg.core.domain.dto.credicasas.ClfSearchRequestDTO;
import com.scfg.core.domain.dto.credicasas.RequestDTO;
import com.scfg.core.domain.dto.credicasas.RequestDetailDTO;
import com.scfg.core.domain.dto.credicasas.SearchRequestResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneralRequestService implements GeneralRequestUseCase {

    private final GeneralRequestPort generalRequestPort;
    private final PlanPort planPort;
    private final CoveragePort coveragePort;


    @Override
    public List<RequestDetailDTO> getAllClfBySubscriptionFilters(ClfSearchRequestDTO searchRequestDTO) {

        List<RequestDetailDTO> requestDetailDTOList = generalRequestPort.findAllGelBySubscriptionFilters(searchRequestDTO);

        requestDetailDTOList.forEach(r -> {
            r.setCoverages(coveragePort.getAllCoverageNamesByGeneralRequestId(r.getId()));
        });

        return requestDetailDTOList;
    }

    @Override
    public List<RequestDTO> getAllClfByFilters(PersonDTO personDTO) {
        List<Plan> planList = planPort.getPlanByFinancialGroup(BusinessGroupEnum.CREDICASAS.getValue());
        String sPlanIdList = planList.stream().map(plan -> plan.getId().toString()).collect(Collectors.joining(", "));
        return generalRequestPort.findAllClfRequestByFilters(personDTO, sPlanIdList);
    }

    @Override
    public SearchRequestResponseDTO getPaginatedRequests(ClfSearchRequestDTO clfSearchRequestDTO, Integer page, Integer size) {
        SearchRequestResponseDTO searchRequestResponseDTO = generalRequestPort.findAllRequestsPaginated(clfSearchRequestDTO, page, size);

        searchRequestResponseDTO.getRequestDetailDTOList().forEach(r -> {
            r.setCoverages(coveragePort.getAllCoverageNamesByGeneralRequestId(r.getId()));
        });

        return searchRequestResponseDTO;
    }

    @Override
    public Boolean inactiveRequest(InactiveRequestDTO requestDTO) {
        GeneralRequest request = this.generalRequestPort.getGeneralRequest(requestDTO.getId());

        request.setInactiveComment(requestDTO.getComment());
        request.setRequestStatusIdc(RequestStatusEnum.INACTIVE.getValue());
        long requestId = this.generalRequestPort.saveOrUpdate(request);

        return requestId > 0;
    }

    @Override
    public Boolean isOperationNumberDuplicated(String operationNumber, Integer requestId) {
        Integer registerQuantity = this.generalRequestPort.validateOperationNumber(operationNumber, requestId);
        if (registerQuantity > 0) {
            return true;
        }
        return false;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {OperationException.class, Exception.class})
    @Override
    public Boolean updateDataRequestGel(Integer companyIdc) {
        List<GeneralRequest> generalRequestList = this.generalRequestPort.getAllByBusinessGroup(companyIdc);
        if (generalRequestList.size() > 0){
            List<GeneralRequest> tempRequestChange = new ArrayList<>();
            List<Long> personIdList = generalRequestList.stream().map(GeneralRequest::getPersonId).distinct().collect(Collectors.toList());
            DecimalFormat df = new DecimalFormat("0.00");

            for (Long pid: personIdList) {
                List<GeneralRequest> perList = generalRequestList.stream().filter(x -> x.getPersonId().equals(pid))
                        .sorted(Comparator.comparing(GeneralRequest::getCreatedAt)).collect(Collectors.toList());
                for (int i = 0; i <perList.size(); i++) {
                    GeneralRequest o = perList.get(i);
                    if (i == 0) {
                        o.setCurrentAmount(0.0);
                        o.setAccumulatedAmount(o.getRequestedAmount());
                    } else {

                        Double toReduce = perList.stream().filter(x -> (x.getRequestStatusIdc().equals(RequestStatusEnum.INACTIVE.getValue())
                                        || x.getRequestStatusIdc().equals(RequestStatusEnum.REJECTED.getValue())) &&
                                        (x.getLastModifiedAt().getTime() < o.getCreatedAt().getTime()))
                                .mapToDouble(GeneralRequest::getRequestedAmount).sum();

                        Double toAdd = perList.stream().filter(x ->
                                        (x.getCreatedAt().getTime() < o.getCreatedAt().getTime()))
                                .mapToDouble(GeneralRequest::getRequestedAmount).sum();

                            o.setCurrentAmount(round(toAdd - toReduce, 2));
                            o.setAccumulatedAmount(round(o.getRequestedAmount() + toAdd - toReduce,2));
                    }
                    tempRequestChange.add(o);
                }
            }
            return generalRequestPort.saveOrUpdateAll(tempRequestChange);
        }

        return false;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
