package com.scfg.core.domain.dto.virh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class DebtRegisterUpdateDTO {
    private Long id;
    private String codigo_recaudacion;
    private String id_transaccion;
    private String url_pasarela_pagos;
    private String qr_simple_url;
}
