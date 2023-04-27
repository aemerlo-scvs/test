package com.scfg.core.adapter.persistence.role;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "Role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class RoleJpaEntity extends BaseJpaEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
//    private List<RoleMenuJpaEntity> roleMenu;

}
