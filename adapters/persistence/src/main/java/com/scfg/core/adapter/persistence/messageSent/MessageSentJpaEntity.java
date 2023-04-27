package com.scfg.core.adapter.persistence.messageSent;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.BaseJpaEntityNotId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MessageSent")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MessageSentJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "messageTypeIdc")
    private Integer messageTypeIdc;

    @Column(name = "referenceTableIdc")
    private Integer referenceTableIdc;

    @Column(name = "referenceId")
    private Long referenceId;

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

}
