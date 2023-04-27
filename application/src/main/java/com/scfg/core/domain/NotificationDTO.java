package com.scfg.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class NotificationDTO {

    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private String name;

    private String description;

    private String redirectionUrl;

    private Integer read;

    private String note;

    private Date createdAt;

    private Date lastModifiedAt;

}
