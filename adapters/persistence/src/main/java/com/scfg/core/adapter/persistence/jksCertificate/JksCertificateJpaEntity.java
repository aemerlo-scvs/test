package com.scfg.core.adapter.persistence.jksCertificate;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "JksCertificate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class JksCertificateJpaEntity extends BaseJpaEntity {

    @Column(name = "abbreviation", nullable = false, length = 50)
    private String abbreviation;
    @Column(name = "description", nullable = false, length = 150)
    private String description;

    @Column(name = "jks")
    @Lob
    private byte[] jks;

}
