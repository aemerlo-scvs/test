package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CoverageUseCase;
import com.scfg.core.application.port.out.CoveragePort;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.Coverage;
import com.scfg.core.domain.configuracionesSistemas.FilterParamenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoverageService implements CoverageUseCase {
    private final CoveragePort coveragePort;

    @Override
    public List<Coverage> getAllCoverage() {
        return coveragePort.getAllCoverage();
    }

    @Override
    public PersistenceResponse registerCoverage(Coverage coverage) {
        return coveragePort.save(coverage, true);
    }

    @Override
    public PersistenceResponse updateCoverage(Coverage coverage) {
        return coveragePort.update(coverage);
    }

    @Override
    public PersistenceResponse deleteCoverage(Long id) {
        return coveragePort.delete(id);
    }

    @Override
    public List<Coverage> getfilterParamenter(FilterParamenter parameters) {
        List<Coverage> list1 = coveragePort.getfilterParamenters(parameters);

        if (parameters.getName() != null && !parameters.getName().isEmpty()) {
            list1 = list1.stream().filter((s) -> (s.getName().toUpperCase().contains(parameters.getName().toUpperCase()))).collect(Collectors.toList());
        }
        if (list1.size() > 0 && parameters.getDateto() != null && parameters.getDatefrom() != null) {
            list1 = list1.stream().filter(re -> re.getCreatedAt().after(parameters.getDatefrom()) && re.getCreatedAt().before(parameters.getDateto())).collect(Collectors.toList());
        }
        if (list1.size() > 0 && parameters.getStatus() != null) {
            list1 = list1.stream().filter(re -> re.getStatus() == parameters.getStatus()).collect(Collectors.toList());
        }

        return list1;
    }

    @Override
    public List<Coverage> getAllCoverageByProductId(Long productId) {
        return coveragePort.findAllCoverageByProductId(productId);
    }

//    @Override
//    public List<Coverage> findByBranchId(Long branchId) {
//        return ;
//    }


}
