package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ProductUseCase;
import com.scfg.core.application.port.out.ProductPort;
import com.scfg.core.common.enums.SMVSResponseEnum;
import com.scfg.core.domain.Product;
import com.scfg.core.domain.SendSmsDTO;
import com.scfg.core.domain.common.ObjectDTO;
import com.scfg.core.domain.smvs.PlanDTO;
import com.scfg.core.domain.smvs.SMVSResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;

    @Override
    public List<Product> getAll() {
        return productPort.getProductList();
    }

    @Override
    public SMVSResponseDTO getPlansByAgreementCode(int agreementCode) {
        Integer responseCode = SMVSResponseEnum.OK.getValue();
        String message = "Producto encontrado correctamente";

        Boolean existsProduct = productPort.existsProductByAgreementCode(agreementCode);
        if(!existsProduct){
            responseCode = SMVSResponseEnum.ERROR.getValue();
            message = "Producto con código de convenio: " + agreementCode + ", no encontrado";
            return setSMVSResponseDTO(responseCode, message, new ArrayList<>());
        }

        List<PlanDTO> data = productPort.findAllPlansByAgreementCode(agreementCode);
        if(data.size() <= 0){
            message = "Producto sin planes";
        }

        return setSMVSResponseDTO(responseCode, message, data);
    }

    @Override
    public List<ObjectDTO> getAllProductsByBranchId(long branchId){
        return productPort.getAllProductsByBranchId(branchId);
    }


    //#region Helpers
    SMVSResponseDTO setSMVSResponseDTO(Integer responseCode, String message, Object data) {
        return SMVSResponseDTO.builder()
                .codigo_respuesta(responseCode)
                .mensaje(message)
                .data(data)
                .build();
    }
    //#endregion
}
