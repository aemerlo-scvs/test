package com.scfg.core.adapter.persistence.roleMenuAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.action.ActionJpaEntity;
import com.scfg.core.adapter.persistence.roleMenu.RoleMenuJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "RoleMenuAction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuActionJpaEntity {

    @EmbeddedId
    RoleMenuActionKey id;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "roleId", insertable = false, updatable = false),
//            @JoinColumn(name = "menuId", insertable = false, updatable = false)
//    })
//    @JsonBackReference
//    private RoleMenuJpaEntity roleMenu;
//
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "actionId", insertable = false, updatable = false)
//    @JsonBackReference
//    private ActionJpaEntity action;

}
