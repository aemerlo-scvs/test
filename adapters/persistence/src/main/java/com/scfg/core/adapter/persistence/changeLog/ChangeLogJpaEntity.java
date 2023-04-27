package com.scfg.core.adapter.persistence.changeLog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChangeLog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChangeLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "[table]", nullable = false, length = 100)
    private String table;

    @Column(name = "referenceId")
    private Long referenceId;

    @Column(name = "referenceObject", columnDefinition = "TEXT")
    private String referenceObject;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "browser", length = 200)
    private String browser;

    @Column(name = "ip", length = 50)
    private String ip;

    @Column(name = "userId", nullable = false)
    private Long userId;

}
