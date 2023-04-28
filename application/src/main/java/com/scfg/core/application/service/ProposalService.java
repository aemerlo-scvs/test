package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ProposalUseCase;
import com.scfg.core.application.port.out.BeneficiaryPort;
import com.scfg.core.application.port.out.GeneralRequestPort;
import com.scfg.core.application.port.out.TelephonePort;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.domain.dto.PageableDTO;
import com.scfg.core.domain.dto.RequestProposalSearchFiltersDto;
import com.scfg.core.domain.dto.vin.ResponseProposalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProposalService implements ProposalUseCase {
    private final GeneralRequestPort generalRequestPort;
    private final BeneficiaryPort beneficiaryPort;

    private final TelephonePort telephonePort;
    @Override
    public PageableDTO getAllByIdPlanPageAndProposalFilters(Integer page, Integer size, Long planId,
                                                            RequestProposalSearchFiltersDto filtersDto) throws ParseException {
        if(!filtersDto.isInitialSearch()){
            this.validateFilters(filtersDto);
        }

        PageableDTO pageableDTO = this.generalRequestPort.findAllByPlanIdAndFilters(planId, filtersDto, page, size);
        List<ResponseProposalDto> list = (List<ResponseProposalDto>) pageableDTO.getContent();
        list.forEach(proposal -> {
            proposal.setBeneficiaryList(this.beneficiaryPort.findAllByGeneralRequestId(proposal.getId()));
            proposal.setCellphoneNumbers(
                    telephonePort.getAllByPersonId(proposal.getPersonId())
                            .stream().map(o -> o.getNumber()).collect(Collectors.toList())
            );
        });
        pageableDTO.setContent(list);
        return pageableDTO;
    }

    public void validateFilters(RequestProposalSearchFiltersDto filtersDto) throws ParseException {
        if (filtersDto != null) {
            if (filtersDto.getIdentificationNumber().isEmpty()) {
                throw new OperationException("El campo cédula de identidad no puede estar vacio");
            }
            if (filtersDto.getFromDate() != null || filtersDto.getToDate() != null) {
                if(filtersDto.getToDate() == null || filtersDto.getToDate().isEmpty()) {
                    throw new OperationException("El campo fecha final no debe estar vacio");
                }
                if(filtersDto.getFromDate() == null || filtersDto.getFromDate().isEmpty()) {
                    throw new OperationException("El campo fecha de inicio no debe estar vacio");
                }
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                Date initDate = date.parse(filtersDto.getFromDate());
                Date endDate= date.parse(filtersDto.getToDate());
                if(initDate.after(endDate)){
                    throw new OperationException("La fecha de inicio no puede ser mayor a la fecha de vencimiento");
                }
            }
        }
    }
}
