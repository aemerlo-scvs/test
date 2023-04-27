package com.scfg.core.adapter.persistence.messageTosend;


import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.BaseJpaEntityNotId;
import com.scfg.core.adapter.persistence.ChangeLogListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "MessageToSend")
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MessageToSendJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "messageTypeIdc")
    private Integer messageTypeIdc;

    @Column(name = "referenceId")
    private Long referenceId;

    @Column(name = "referenceTableIdc")
    private Integer referenceTableIdc;

    @Column(name = "message")
    private String message;

    @Column(name = "[subject]")
    private String subject;

    @Column(name = "[to]")
    private String to;

    @Column(name = "cc")
    private String cc;

    @Column(name = "observation", length = 1000)
    private String observation;

    @Column(name = "numberOfAttempt")
    private Integer numberOfAttempt;

//    @Temporal(TemporalType.TIMESTAMP)
//    @CreatedDate
//    @Column(name = "createdAt", nullable = false, updatable = false)
//    private Date createdAt;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @LastModifiedDate
//    @Column(name = "lastModifiedAt")
//    private Date lastModifiedAt;

}
