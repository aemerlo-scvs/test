package com.scfg.core.application.port.in;


import com.scfg.core.domain.smvs.MakePaymentDTO;
import com.scfg.core.domain.smvs.PaymentResponseDTO;
import com.scfg.core.domain.smvs.ReversalPaymentDTO;
import com.scfg.core.domain.smvs.ReversalPaymentResponseDTO;

public interface SMVSIntegrationUseCase {

    PaymentResponseDTO makePayment(MakePaymentDTO paymentDTO);

    ReversalPaymentResponseDTO reversalPayment(ReversalPaymentDTO reversalPaymentDTO);
}
