package com.scfg.core.domain.smvs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PendingActivateErrorDTO {
    private String name;
    private String documentNumber;
    private String message;
    private String activationCode;
    private Date requestDate;
    private String statusMessage;
}
