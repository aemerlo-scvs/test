package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;
    private String company;

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
