package com.scfg.core.adapter.persistence;

import com.scfg.core.common.enums.PersistenceStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

import static com.scfg.core.common.util.HelpersMethods.*;

@MappedSuperclass
@Data
@EntityListeners({AuditingEntityListener.class, ChangeLogListener.class})
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate

    //@CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;


    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
//    @UpdateTimestamp
    @Column(name = "lastModifiedAt")
    private Date lastModifiedAt;


    @CreatedBy
    @Column(name = "createdBy", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "lastModifiedBy")
    private Long lastModifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
