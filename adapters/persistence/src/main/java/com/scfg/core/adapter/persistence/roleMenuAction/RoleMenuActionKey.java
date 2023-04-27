package com.scfg.core.adapter.persistence.roleMenuAction;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RoleMenuActionKey implements Serializable {

    @Column(name = "roleId")
    public Long roleId;

    @Column(name = "menuId")
    public Long menuId;

    @Column(name = "actionId")
    public Long actionId;
}
