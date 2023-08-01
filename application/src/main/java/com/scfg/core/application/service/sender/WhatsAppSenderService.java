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
import org.springframework.core.env.Environment;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("WhatsAppSenderService")
@RequiredArgsConstructor
public class WhatsAppSenderService implements SenderUseCase {

    private final TwilioConfig twilioConfig;
    private final Environment environment;

    private final String urlBase = "scvs-demo.azurewebsites.net";

    @Override
    public boolean sendMessage(MessageDTO messageDTO) {
        boolean response = false;
        try {
            String fromNumber = "";

            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                fromNumber = twilioConfig.getWhatsAppNumber();
            } else {
                fromNumber = twilioConfig.getWhatsAppNumber_sandBox();
            }

            Twilio.init(twilioConfig.getAccountSID(), twilioConfig.getAuthToken());

            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("whatsapp:+591" + messageDTO.getTo()),
                            new com.twilio.type.PhoneNumber("whatsapp:" + fromNumber),
                            messageDTO.getMessage())
                    .create();
            response = message.getErrorMessage() == null;
        } catch (Exception e) {
            String errorMsg = "Ocurrió un error al enviar el mensaje de WhatsApp desde Twilio: " + messageDTO.getTo();
            log.error(errorMsg + e.getMessage());
        }

        return response;
    }

    @Override
    public boolean sendMessageWithAttachment(MessageDTO messageDTO, List<AttachmentDTO> attachmentList) {
        boolean response = false;
        try {
            String fromNumber = "";
            
            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                fromNumber = twilioConfig.getWhatsAppNumber();
            } else {
                fromNumber = twilioConfig.getWhatsAppNumber_sandBox();
            }

            Twilio.init(twilioConfig.getAccountSID(), twilioConfig.getAuthToken());
            String methodName = "/scvs/smvs/download-doc?id=";
            List<URI> uriList = new ArrayList();
            if(!attachmentList.isEmpty()) {
                for (AttachmentDTO attachment: attachmentList) {
                    uriList.add(URI.create(urlBase+methodName+attachment.getFileName()));
                }
                Message message = Message.creator(
                                new com.twilio.type.PhoneNumber("whatsapp:+591" + messageDTO.getTo()),
                                new com.twilio.type.PhoneNumber("whatsapp:" + fromNumber),
                                messageDTO.getMessage())
                        .setMediaUrl(
                                uriList
                        )
                        .create();

                response = message.getErrorMessage() == null;
            }
        } catch (Exception e) {
            String errorMsg = "Ocurrió un error al enviar el mensaje de WhatsApp desde Twilio: " + messageDTO.getTo();
            log.error(errorMsg + e.getMessage());
        }
        return response;
    }
}
