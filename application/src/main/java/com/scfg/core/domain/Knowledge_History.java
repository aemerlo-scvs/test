package com.scfg.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Knowledge_History {
    private  String policy_number;
    private String names_incorrect;
    private String names_correct;
    private float manager_code;
}
