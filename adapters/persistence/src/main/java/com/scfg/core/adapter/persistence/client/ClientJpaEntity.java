package com.scfg.core.adapter.persistence.client;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.lastCasesObserved.LastObservedCaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = HelpersConstants.TABLE_CLIENT)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ClientJpaEntity extends BaseJpaEntity {


    @Column(name = "names")
    private String names;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "mothersLastName")
    private String mothersLastName;

    @Column(name = "marriedLastName")
    private String marriedLastName;

    @Column(name = "duplicateCopy")
    private String duplicateCopy;

    @Column(name = "extension")
    private String extension;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "documentNumber")
    private String documentNumber;

    @Column(name = "gender")
    private String gender;

    // For optimize response time
    @Column(name = "accumulatedAmountDhl")
    private Double accumulatedAmountDhl;

    @Column(name = "accumulatedAmountDhn")
    private Double accumulatedAmountDhn;

    // Custom Methods


    //@Formula(value = "COALESCE(names,CONCAT(names, ' ', lastName, ' ', mothersLastName, ' ', marriedLastName)")
    @Formula(value = "CONCAT(" +
            "    COALESCE(names, ''), " +
            "    COALESCE(' ' + lastName, ''), " +
            "    COALESCE(' ' + mothersLastName, ''), " +
            "    COALESCE(' ' + marriedLastName, ''))")
    private String fullname;


    // Outgoing Relationships
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "documentTypeIdc")
    @JsonBackReference
    private ClassifierJpaEntity documentType;


    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyIdc")
    @JsonBackReference
    private ClassifierJpaEntity currency;


    // incoming Relationship
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LastObservedCaseJpaEntity> observedCases;

    //

}
