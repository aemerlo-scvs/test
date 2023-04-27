package com.scfg.core.adapter.persistence;

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

@MappedSuperclass
@Data
@EntityListeners({AuditingEntityListener.class, ChangeLogListener.class})
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseJpaEntityNotId {
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
    //@CreationTimestamp
    @Column(name = "createdBy", nullable = false, updatable = false)
    private Long createdBy;


    @LastModifiedBy
    //@CreationTimestamp
    @Column(name = "lastModifiedBy", nullable = false, updatable = false)
    private Long lastModifiedBy;

}
