package com.scfg.core.adapter.persistence.annexeType;

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
@Table(name = "AnnexeType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnnexeTypeJpaEntity extends BaseJpaEntity {
    @Column(name = "internalCode")
    private Long internalCode;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

}
