package com.scfg.core.domain.dto.vin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class DocRescueStatement {
    private String nameOf;
    private String nameFor;
    private String requestDate;
    private String ref;
    private String areaSend;
    private String areaReception;
    private String paymentMethod;
    private String numberPaymentMethod;
    private String currencyPayment;
    private String toNamePayment;
    private String policyNumber;
    private String identificationNumber;
    private String extension;
    //todo REFACTORIZAR NOMBRES.
    private BigDecimal capitalAseguradoTotal;
    private BigDecimal valorRescateADevolver;
    private BigDecimal primaNetaRescatada;
    private BigDecimal primaAdicionalRescatada;
    private BigDecimal aps;
    private BigDecimal fpa;
    private BigDecimal primaRiesgo;
    private BigDecimal servicioCobranzaRescatado;
    private BigDecimal primaCedidaRescatada;
    private BigDecimal capitalCedidoRescatado;
    private BigDecimal reservaMatematica;
    private BigDecimal impuestosRemesas;
    private BigDecimal comisionBrokerRescata;
}
