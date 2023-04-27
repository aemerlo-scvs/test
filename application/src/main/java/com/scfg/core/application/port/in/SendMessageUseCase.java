package com.scfg.core.application.port.in;

import com.scfg.core.domain.SendSmsDTO;

import java.io.UnsupportedEncodingException;

public interface SendMessageUseCase {

    Boolean sendSMS(SendSmsDTO smsDTO);

    Boolean sendWhatsApp(SendSmsDTO smsDTO);

}
