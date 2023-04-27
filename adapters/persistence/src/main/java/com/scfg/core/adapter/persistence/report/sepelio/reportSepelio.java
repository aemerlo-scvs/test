package com.scfg.core.adapter.persistence.report.sepelio;

import com.google.gson.Gson;
import com.scfg.core.application.port.out.ReportPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.*;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


@PersistenceAdapter
@RequiredArgsConstructor

public class reportSepelio implements ReportPort {
    @PersistenceContext
    private EntityManager em;

    private Gson gson;
    private static String GET_REPORT_COMMERCIALS_BY_DATES = "exec proc_svms_reporte_comercial :startDate,:toDate";
    private static String GET_RANKINGS = "exec proc_ranking_cajeros :datefrom,:dateto";
    private static String GET_RANKING_ZONA = "exec proc_ranking_zonas :datefrom,:dateto";
    private static String GET_RANKING_AGENCIA = "exec proc_ranking_agencias :datefrom,:dateto";
    private static String GET_RANKING_SUCURSAL = "exec proc_ranking_sucursales :datefrom,:dateto";


    @Override
    public List<VCMAReportDTO> getReportCommercials(Date startDate, Date toDate) {
        List<VCMAReportDTO> reportList = new ArrayList<>();

        Query query = em.createNativeQuery(GET_REPORT_COMMERCIALS_BY_DATES);
        query.setParameter("startDate", startDate);
        query.setParameter("toDate", toDate);
        List<Object[]> list = query.getResultList();

        if (!list.isEmpty()) {
            for (Object[] row : list) {
                VCMAReportDTO insertToList = new VCMAReportDTO();
                insertToList.setP1((Integer) row[0]);
                insertToList.setSp1(((BigDecimal) row[1]).intValue());
                insertToList.setManagerId((Integer) row[2]);
                insertToList.setNames((String) row[3]);
                insertToList.setAgencyId((Integer) row[4]);
                insertToList.setDescriptiona((String) row[5]);
                insertToList.setBranchOfficeId(((BigInteger) row[6]).intValue());
                insertToList.setDescriptions((String) row[7]);
                insertToList.setZonesId(((BigInteger) row[8]).intValue());
                insertToList.setDescriptionz((String) row[9]);
                insertToList.setNump((Integer) row[0]);
                insertToList.setMontop(((BigDecimal) row[1]).intValue());
                reportList.add(insertToList);
            }
        }
        return reportList;

    }

    @Override
    public Map<String, Object> getRanking(Date startDate, Date toDate) {
        Map<String, Object> list = new HashMap<>();

        Query query = em.createNativeQuery(GET_RANKINGS);
        query.setParameter("datefrom", startDate);
        query.setParameter("dateto", toDate);
        List<Object[]> lista = query.getResultList();
        List<RankingCajeros> rankingCajeros = lista.stream().map(RankingCajeros::new).collect(Collectors.toList());
        list.put("cajeros", rankingCajeros);
        Query zonas = em.createNativeQuery(GET_RANKING_ZONA);
        zonas.setParameter("datefrom", startDate);
        zonas.setParameter("dateto", toDate);
        List<Object[]> listaZonas = zonas.getResultList();
        List<RankingZonasDto> rankingZonasDtos = listaZonas.stream().map(RankingZonasDto::new).collect(Collectors.toList());
        list.put("zonas", rankingZonasDtos);
        Query agencias = em.createNativeQuery(GET_RANKING_AGENCIA);
        agencias.setParameter("datefrom", startDate);
        agencias.setParameter("dateto", toDate);
        List<Object[]> listaAgencias = agencias.getResultList();
        List<RankingAgenciasDto> rankingAgenciasDtos = listaAgencias.stream().map(RankingAgenciasDto::new).collect(Collectors.toList());
        list.put("agencias", rankingAgenciasDtos);
        Query sucursales = em.createNativeQuery(GET_RANKING_SUCURSAL);
        sucursales.setParameter("datefrom", startDate);
        sucursales.setParameter("dateto", toDate);
        List<Object[]> listaSucursales = sucursales.getResultList();
        List<RankingSucursalDto> rankingSucursalDtos = listaSucursales.stream().map(RankingSucursalDto::new).collect(Collectors.toList());
        list.put("sucursales", rankingSucursalDtos);
        return list;

    }
}
