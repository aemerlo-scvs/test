package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@SuperBuilder
public class Menu extends BaseDomain {

    private String name;

    private String description;

    private String url;

    private String icon;

    private Long parentId;

    private List<Menu> subMenu;

    private List<Action> actions;

}
