package com.scfg.core.adapter.persistence.roleMenu;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RoleMenuKey implements Serializable {
    @Column(name = "roleId")
    Long roleId;

    @Column(name = "menuId")
    Long menuId;
}
