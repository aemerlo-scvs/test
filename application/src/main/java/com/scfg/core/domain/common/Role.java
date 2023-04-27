package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@SuperBuilder
public class Role extends BaseDomain {
//    @Getter
//    private Long id;

    private String name;

    private String description;

//    @Getter
//    private LocalDateTime createdAt;
//    @Getter
//    private LocalDateTime lastModifiedAt;
}
