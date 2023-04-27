package com.scfg.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SendSmsDTO {
    private String number;

    private String message;

    private String filename;

    private String base64Data;

    private Boolean sendToWhatsApp;
}
