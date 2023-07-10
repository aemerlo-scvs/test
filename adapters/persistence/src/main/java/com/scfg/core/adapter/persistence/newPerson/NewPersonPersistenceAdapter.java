package com.scfg.core.adapter.persistence.newPerson;
import com.scfg.core.application.port.out.NewPersonPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.person.NewPerson;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.NTextType;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@PersistenceAdapter
@RequiredArgsConstructor
public class NewPersonPersistenceAdapter implements NewPersonPort {

    private final NewPersonRepository newPersonRepository;

    private final EntityManager em;

    private static String GET_SEARCH_PERSONS = "exec proc_search_persons :documentType,:documentNumber,:personName";

    @Override
    public long saveOrUpdate(NewPerson newPerson) {
        NewPersonJpaEntity newPersonJpaEntity = mapToJpaEntity(newPerson);
        newPersonJpaEntity = newPersonRepository.save(newPersonJpaEntity);
        return newPersonJpaEntity.getId();
    }

    public Object searchPerson(Long documentType, String documentNumber, String personName) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("proc_search_persons")
                .registerStoredProcedureParameter(
                        "documentType",
                        Integer.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "documentNumber",
                        String.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "personName",
                        String.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "result",
                        NTextType.class,
                        ParameterMode.OUT
                )
                .setParameter("documentType", documentType.intValue())
                .setParameter("documentNumber", documentNumber)
                .setParameter("personName", personName);
        query.execute();

        Object list = (Object) query.getOutputParameterValue("result");
        em.close();

        return list;
    }


    //#region Mappers

    public static NewPersonJpaEntity mapToJpaEntity(NewPerson newPerson) {
        NewPersonJpaEntity newPersonJpaEntity = NewPersonJpaEntity.builder()
                .id(newPerson.getId())
                .documentTypeIdc(newPerson.getDocumentTypeIdc())
                .identificationNumber(newPerson.getIdentificationNumber())
                .extIdc(newPerson.getExtIdc())
                .name(newPerson.getName())
                .lastName(newPerson.getLastName())
                .motherLastName(newPerson.getMotherLastName())
                .marriedLastName(newPerson.getMarriedLastName())
                .genderIdc(newPerson.getGenderIdc())
                .maritalStatusIdc(newPerson.getMaritalStatusIdc())
                .birthDate(newPerson.getBirthDate())
                .birthPlaceIdc(newPerson.getBirthPlaceIdc())
                .nationalityIdc(newPerson.getNationalityIdc())
                .residencePlaceIdc(newPerson.getResidencePlaceIdc())
                .activityIdc(newPerson.getActivityIdc())
                .professionIdc(newPerson.getProfessionIdc())
                .workerTypeIdc(newPerson.getWorkerTypeIdc())
//                .workerCompany(newPerson.getWorkerCompany())
//                .workEntryYear(newPerson.getWorkEntryYear())
//                .workPosition(newPerson.getWorkPosition())
                .monthlyIncomeRangeIdc(newPerson.getMonthlyIncomeRangeIdc())
                .yearlyIncomeRangeIdc(newPerson.getYearlyIncomeRangeIdc())
                .businessTypeIdc(newPerson.getBusinessTypeIdc())
//                .businessRegistrationNumber(newPerson.getBusinessRegistrationNumber())
//                .email(newPerson.getEmail())
//                .eventualClient(newPerson.getEventualClient())
//                .internalClientCode(newPerson.getInternalClientCode())
//                .institutionalClientCode(newPerson.getInstitutionalClientCode())
//                .assignedGroupIdc(newPerson.getAssignedGroupIdc())
                .createdAt(newPerson.getCreatedAt())
                .lastModifiedAt(newPerson.getLastModifiedAt())
                .build();
        return newPersonJpaEntity;
    }
}
