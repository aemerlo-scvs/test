package com.scfg.core.adapter.persistence.naturalPerson;

import com.scfg.core.application.port.out.NewPersonPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.person.NewPerson;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.NTextType;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class NewPersonPersistenceAdapter implements NewPersonPort {

    private final EntityManager em;
    private static String GET_REQUEST_ALL_BY_FILTER = "exec proc_search_persons :documentType,:documentNumber,:personName";

//    @Override
//    public List<NewPerson> findAll() {
//        return null;
//    }
//    @Override
//    public NewPerson findById(long personId) {
//        return null;
//    }
//    @Override
//    public long saveOrUpdate(NewPerson person) {
//        return 0;
//    }
    @Override
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
}
