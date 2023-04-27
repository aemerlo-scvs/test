package com.scfg.core.domain.smvs;

import com.scfg.core.common.enums.AlertEnum;
import com.scfg.core.common.enums.SMVSMessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SendMessageDTO {

    private Long requestId;

    private String activationCode;

    private String policyNumber;

    private String name;

    private String email;

    private String[] emails;

    private String[] ccEmails;

    private byte[] attachmentFile;

    private String attachmentName;

    private String phoneNumber;

    private AlertEnum messageTypeEnum;
}
