package com.scfg.core.adapter.persistence.roleMenu;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.menu.MenuJpaEntity;
import com.scfg.core.adapter.persistence.role.RoleJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Table(name = "RoleMenu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuJpaEntity {

    @EmbeddedId
    RoleMenuKey id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private RoleJpaEntity role;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "menuId", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private MenuJpaEntity menu;

}
