package com.scfg.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest implements Serializable {
    private String username;
    private String newPassword;
    private String confirmPassword;
}
