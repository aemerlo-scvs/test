package com.scfg.core.domain.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BfsAuthenticationRequest extends AuthenticationRequest {
    private Long code;
    private String name;
    private String email;
    private Long officeCode;
    private String officeName;

    @Override
    public String toString() {
        return "BfsAuthenticationRequest{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", officeCode=" + officeCode +
                ", officeName='" + officeName + '\'' +
                '}';
    }
}
