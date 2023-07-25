package com.scfg.core.adapter.persistence.personRole;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PersonRole")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PersonRoleJpaEntity extends BaseJpaEntity {
    @Column(name = "personId")
    private Long personId;

    @Column(name = "relatedPersonId")
    private Long relatedPersonId;

    @Column(name = "relationshipRolIdc")
    private Integer relationshipRolIdc;
}
