package com.scfg.core.application.service.sender;

import com.scfg.core.application.port.in.SenderUseCase;
import com.scfg.core.common.credentials.TwilioConfig;
import com.scfg.core.domain.dto.AttachmentDTO;
import com.scfg.core.domain.dto.MessageDTO;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("SmsSenderService")
@RequiredArgsConstructor
public class SmsSenderService implements SenderUseCase {

    private final TwilioConfig twilioConfig;

    @Override
    public boolean sendMessage(MessageDTO messageDTO) {
        boolean response = false;
        try {
            String fromNumber = twilioConfig.getCellphoneNumber();

            Twilio.init(twilioConfig.getAccountSID(), twilioConfig.getAuthToken());
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("+591" + messageDTO.getTo()),
                            new com.twilio.type.PhoneNumber(fromNumber),
                            messageDTO.getMessage())
                    .create();

            response = message.getErrorMessage() == null;
        } catch (Exception e) {
            String errorMsg = "Ocurrió un error al enviar el mensaje de Twilio: " + messageDTO.getTo();
            log.error(errorMsg + e.getMessage());
        }
        return response;
    }

    @Override
    public boolean sendMessageWithAttachment(MessageDTO messageDTO, List<AttachmentDTO> attachmentList) {
        return false;
    }
}
