package com.scfg.core.adapter.persistence.menu;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.action.ActionJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Menu")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class MenuJpaEntity extends BaseJpaEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "icon")
    private String icon;

//    @Column(name = "parentId")
//    private Long parentId;

    // @JsonIgnoreProperties({"parentId"})
    @ManyToOne
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private MenuJpaEntity parentId;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<MenuJpaEntity> subMenu;


//    @JsonManagedReference
//    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
//    private List<RoleMenuJpaEntity> roleMenu;

}
