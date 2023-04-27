package com.scfg.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class _NotificationDTO {
    private Long id;

    private Long fromUserId;

    private List<Long> toUserId;

    private String name;

    private String description;

    private String redirectionUrl;

    private Integer sendToGroup;

    private Long roleId;
}
