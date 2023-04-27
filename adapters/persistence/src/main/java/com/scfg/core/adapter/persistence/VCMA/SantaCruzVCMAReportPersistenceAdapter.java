package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.models.*;
import com.scfg.core.application.port.out.SantaCruzVCMAReportPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.dto.VCMAManagerDTO;
import com.scfg.core.domain.managers.*;
import lombok.RequiredArgsConstructor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class SantaCruzVCMAReportPersistenceAdapter implements SantaCruzVCMAReportPort {

    private final AgencyPersistenceAdapter agencyPersistenceAdapter;
    private final Branch_OfficePersistenceAdapter branch_officePersistenceAdapter;
    private final Manager_AgencyPersistenceAdapter manager_agencyPersistenceAdapter;
    private final ManagerPersistenceAdapter managerPersistenceAdapter;
    private final ZonePersistenceAdapter zonePersistenceAdapter;


    @Override
    public boolean insert(List<VCMAManagerDTO> data) throws Exception {
        try {
            // Temporales del puerto
            List<Manager> managersPort = managerPersistenceAdapter.getAllManager();
            List<Agency> agenciesPort = agencyPersistenceAdapter.getAllAgency();
            List<Zone> zonesPort = zonePersistenceAdapter.getAllZone();
            List<Branch_Office> branchOfficesPort = branch_officePersistenceAdapter.getAllBranch();
            List<Manager_Agency> managerAgenciesPort = manager_agencyPersistenceAdapter.getAllManagerAgency();

            // Conversion a JPA
            List<ZoneJpaEntity> zones = (List<ZoneJpaEntity>)(List<?>) zonesPort;
            List<ManagerJpaEntity> managers = (List<ManagerJpaEntity>)(List<?>) managersPort;
            List<AgencyJpaEntity> agencies = (List<AgencyJpaEntity>)(List<?>) agenciesPort;
            List<Branch_OfficeJpaEntity> branchOffices = (List<Branch_OfficeJpaEntity>)(List<?>) branchOfficesPort;
            List<Manager_AgencyJpaEntity> managerAgencies = (List<Manager_AgencyJpaEntity>)(List<?>) managerAgenciesPort;

            for (VCMAManagerDTO verify : data) {
                ZoneJpaEntity tempZone = new ZoneJpaEntity();
                if (verify.getZoneName() == null || verify.getZoneName() == "") {
                    verify.setZoneName("SIN ZONA");
                }

                if (zones == null || zones.stream().noneMatch(e -> e.getDESCRIPTION().equals(verify.getZoneName()))) {
                    ZoneJpaEntity zone = new ZoneJpaEntity();
                    zone.setDESCRIPTION(verify.getZoneName());
                    zone.setSTATUS(1);
                    zone.setDATE_CREATE(getTimeNow());
                    tempZone = zonePersistenceAdapter.insertZone(zone);
                    zones.add(tempZone);
                } else {
//                    tempZone = zonePersistenceAdapter.mapToJpaEntity(zones.stream().filter(e -> e.getDESCRIPTION().equals(verify.getZoneName())).findFirst().get());
                    tempZone = zones.stream().filter(e -> e.getDESCRIPTION().equals(verify.getZoneName())).findFirst().get();
                }
                if (branchOffices.isEmpty() || branchOffices.stream().noneMatch(e -> e.getBRANCH_OFFICE_ID().equals(verify.getBranchOfficeId())
                        && e.getDESCRIPTION().equals(verify.getBranchOfficeName()))) {
                    Branch_OfficeJpaEntity branchOffice = new Branch_OfficeJpaEntity(verify.getBranchOfficeId(), verify.getBranchOfficeName(),
                            null, null, getTimeNow(), null, 1);
                    Branch_OfficeJpaEntity aux= branch_officePersistenceAdapter.insertBranch(branchOffice);
                    branchOffices.add(aux);
                }
                if (managers.isEmpty() || managers.stream().noneMatch(e -> e.getMANAGER_ID().equals(verify.getManagerId())
                        && e.getNAMES().equals(verify.getManagerName()))) {
                    ManagerJpaEntity manager = new ManagerJpaEntity(verify.getManagerId(), verify.getManagerName(), null, null,
                            convertStringToDate(verify.getInclusionDate()), null, verify.getStatus(), verify.getPosition());
                    ManagerJpaEntity ax= managerPersistenceAdapter.insertManager(manager);
                    managers.add(ax);
                }
                if (agencies.isEmpty() || agencies.stream().noneMatch(e -> e.getAGENCY_ID().equals(verify.getAgencyId()))) {
                    AgencyJpaEntity agency = new AgencyJpaEntity(verify.getAgencyId(), verify.getAgencyName(), null,
                            null, getTimeNow(), null, 1, verify.getBranchOfficeId(), tempZone.getZONES_ID());
                    AgencyJpaEntity jpaEntity= agencyPersistenceAdapter.insertAgency(agency);
                    agencies.add(jpaEntity);
                }
                if ((managerAgencies == null || managerAgencies.isEmpty()) || (managerAgencies.stream().noneMatch(e -> e.getAGENCY_ID().equals(verify.getAgencyId())
                        && e.getMANAGER_ID().equals(verify.getManagerId())) && managerAgencies.stream().filter(e -> e.getMANAGER_ID().equals(verify.getManagerId())).count()==0)) {
                    Manager_AgencyJpaEntity managerAgency = new Manager_AgencyJpaEntity();
                    managerAgency.setAGENCY_ID(verify.getAgencyId());
                    managerAgency.setMANAGER_ID(verify.getManagerId());
                    managerAgency.setSTATUS(1);
                    managerAgency.setINCLUSION_DATE(convertStringToDate(verify.getInclusionDate()));
                    managerAgency.setDATE_CREATE(getTimeNow());
                   Manager_AgencyJpaEntity jpaEntity= manager_agencyPersistenceAdapter.insertManagerAgency(managerAgency);
                   managerAgencies.add(jpaEntity);
                } else if (managerAgencies.stream().filter(e -> e.getMANAGER_ID().equals(verify.getManagerId()) &&
                        !e.getAGENCY_ID().equals(verify.getAgencyId())).count()>0){
                    Manager_AgencyJpaEntity update = managerAgencies.stream().filter(e -> e.getMANAGER_ID().equals(verify.getManagerId()) && e.getEXCLUSION_DATE() == null).findFirst().get();
                    managerAgencies.remove(update);
                    update.setSTATUS(0);
                    update.setEXCLUSION_DATE(convertStringToDate(verify.getInclusionDate()));
                    Manager_AgencyJpaEntity managerJpaEntityupd= manager_agencyPersistenceAdapter.updateManagerAgency(update);
                    managerAgencies.add(managerJpaEntityupd);
                    Manager_AgencyJpaEntity managerAgency = new Manager_AgencyJpaEntity();
                    managerAgency.setAGENCY_ID(verify.getAgencyId());
                    managerAgency.setMANAGER_ID(verify.getManagerId());
                    managerAgency.setSTATUS(1);
                    managerAgency.setINCLUSION_DATE(convertStringToDate(verify.getInclusionDate()));
                    managerAgency.setDATE_CREATE(getTimeNow());
                    Manager_AgencyJpaEntity insert= manager_agencyPersistenceAdapter.insertManagerAgency(managerAgency);
                    managerAgencies.add(insert);
                }
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }



    public static Date convertStringToDate(String strDate) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            // you can change format of date
            Date date = formatter.parse(strDate);
            return date;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }

    public static Date getTimeNow() {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            // you can change format of date
            Date date = formatter.parse(String.valueOf(new Date()));
            return date;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return new Date(); // SE esta enviando de esta forma, de manera temporal. Se tiene que modificar luego
        }
    }

    public static String getDateToBigInt(String strDate) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            // you can change format of date
            Date date = formatter.parse(strDate);
            long newDate = date.getTime();
            return String.valueOf(newDate);
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return "0";
        }
    }
}
