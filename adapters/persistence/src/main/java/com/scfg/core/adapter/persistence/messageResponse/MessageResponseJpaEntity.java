package com.scfg.core.adapter.persistence.messageResponse;

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
@Table(name = "MessageResponse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MessageResponseJpaEntity extends BaseJpaEntity {

    @Column(name = "messageSentId")
    private Long messageSentId;

    @Column(name = "[message]")
    private String message;

    @Column(name = "[from]")
    private String from;
}
