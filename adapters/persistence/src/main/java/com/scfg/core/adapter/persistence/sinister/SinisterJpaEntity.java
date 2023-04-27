package com.scfg.core.adapter.persistence.sinister;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = HelpersConstants.TABLE_SINISTER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class SinisterJpaEntity extends BaseJpaEntity {

    @Column(name = "sinisterCode", nullable = false)
    private String sinisterCode;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "paymentDate", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "currentState", nullable = false)
    private String currentState;

    // Relationships
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", nullable = false, updatable = false)
    @JsonBackReference
    private ClientJpaEntity client;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "mortgageReliefItemId", nullable = false, updatable = false)
    @JsonBackReference
    private MortgageReliefItemJpaEntity mortgageReliefItem;
}
