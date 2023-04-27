package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.repository.ReportRequestPolicyRepository;
import com.scfg.core.domain.dto.ResultPolicyDtO;
import com.scfg.core.domain.dto.VCMAReportDTO;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class VCMAReportExcelPersistenceAdapter implements ReportRequestPolicyRepository {

    @PersistenceContext
    private EntityManager em;
    private static String GET_ALL_MANAGER = "exec SP_SEGUIMIENTO_VENTAS_DIARIAS :start,:finish,1";
    private static String GET_ALL_DATA_ACUMULADA = "exec SP_SEGUIMIENTO_VENTAS_DIARIAS :start,:finish,0";
    private static String SET_CONSOLIDAR_POLIZAS = "exec SP_CONSILIDAR_PARTE1";
    private static String QUERYUPDATEMANAGER = "UPDATE POLICY_MANAGER set MANAGER_CODE=x.COD from (SELECT s.POLICY_NUMBER AS PNUMBER,s.MANAGER_CODE AS COD  FROM KNOWLEDGE_HISTORY S INNER JOIN POLICY_MANAGER t on s.POLICY_NUMBER=t.POLICY_NUMBER where t.MANAGER_CODE =0 )x where x.PNUMBER=POLICY_NUMBER";

    @Override
    public List<VCMAReportDTO> GetAllDataForDates(Date start, Date finish) {
        List<VCMAReportDTO> reportDTOS = new ArrayList<>();

        Query query = em.createNativeQuery(GET_ALL_MANAGER);
        query.setParameter("start", start);
        query.setParameter("finish", finish);

        List<Object[]> list = query.getResultList();
        if (!list.isEmpty()) {
            for (Object[] row : list) {
                VCMAReportDTO insertToList = new VCMAReportDTO();
                insertToList.setManagerId(new BigInteger(row[0].toString()).intValue());
                insertToList.setNames((String) row[1]);
                insertToList.setAgencyId(new BigInteger(row[2].toString()).intValue());
                insertToList.setDescriptiona((String) row[3]);
                insertToList.setBranchOfficeId(new BigInteger(row[4].toString()).intValue());
                insertToList.setDescriptions((String) row[5]);
                insertToList.setZonesId(new BigInteger(row[6].toString()).intValue());
                insertToList.setDescriptionz((String) row[7]);
                insertToList.setP1((Integer) row[8]);
                insertToList.setSp1((Integer) row[9]);
                insertToList.setP2((Integer) row[10]);
                insertToList.setSp2((Integer) row[11]);
                insertToList.setP3((Integer) row[12]);
                insertToList.setSp3((Integer) row[13]);
                insertToList.setNump((Integer) row[14]);
                insertToList.setMontop((Integer) row[15]);
                reportDTOS.add(insertToList);
            }
        }
        return reportDTOS;
    }

    @Override
    public List<VCMAReportDTO> GetAllDataAcumulado(Date start, Date finish) {
        List<VCMAReportDTO> reportDTOS = new ArrayList<>();

        Query query = em.createNativeQuery(GET_ALL_DATA_ACUMULADA);
        query.setParameter("start", start);
        query.setParameter("finish", finish);

        List<Object[]> lists = query.getResultList();
        if (!lists.isEmpty()) {
            for (Object[] row : lists) {
                VCMAReportDTO insertToList = new VCMAReportDTO();
                insertToList.setManagerId(new BigInteger(row[0].toString()).intValue());
                insertToList.setNames((String) row[1]);
                insertToList.setAgencyId(new BigInteger(row[2].toString()).intValue());
                insertToList.setDescriptiona((String) row[3]);
                insertToList.setBranchOfficeId(new BigInteger(row[4].toString()).intValue());
                insertToList.setDescriptions((String) row[5]);
                insertToList.setZonesId(new BigInteger(row[6].toString()).intValue());
                insertToList.setDescriptionz((String) row[7]);
                insertToList.setP1((Integer) row[8]);
                insertToList.setSp1((Integer) row[9]);
                insertToList.setP2((Integer) row[10]);
                insertToList.setSp2((Integer) row[11]);
                insertToList.setP3((Integer) row[12]);
                insertToList.setSp3((Integer) row[13]);
                insertToList.setNump((Integer) row[14]);
                insertToList.setMontop((Integer) row[15]);
                reportDTOS.add(insertToList);
            }
        }
        return reportDTOS;
    }

    @Override
    public List<ResultPolicyDtO> SetConsolidarPolizas() {
        List<ResultPolicyDtO> reportDTOS = new ArrayList<>();
        Query query = em.createNativeQuery(SET_CONSOLIDAR_POLIZAS);
        List<Object[]> result = query.getResultList();
        for (Object[] row : result) {
            ResultPolicyDtO resultPolicyDtO = new ResultPolicyDtO();
            resultPolicyDtO.setPolicy_number((String) row[0]);
            resultPolicyDtO.setNames_manager_incorrect((String) row[1]);
            resultPolicyDtO.setRepitcount((Integer) row[2]);
            reportDTOS.add(resultPolicyDtO);
        }
        return reportDTOS;
    }
    @Transactional
    @Override
    public int UpdatePolizas() {
        int i=0;
        //em.getTransaction().begin();
        try{
            Query query = em.createNativeQuery(QUERYUPDATEMANAGER);
            i = query.executeUpdate();
          //  em.getTransaction().commit();
        }catch (TransactionRequiredException ex) {
            //em.getTransaction().rollback();
            ex.printStackTrace();
            i = 0;
        }
        return i;
    }

}
