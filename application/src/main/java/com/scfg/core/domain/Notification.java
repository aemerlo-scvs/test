package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
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
public class Notification extends BaseDomain {

    private Long fromUserId;

    private String name;

    private String description;

    private String redirectionUrl;

    private Integer sentToGroup;

    private Long roleId;

}
