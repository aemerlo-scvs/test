package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ChangeLog {

    private Long id;

    private String action;
    private String table;
    private Long referenceId;
    private String referenceObject;
    private LocalDateTime createdAt;
    private String browser;
    private String ip;
    private Long userId;

}
