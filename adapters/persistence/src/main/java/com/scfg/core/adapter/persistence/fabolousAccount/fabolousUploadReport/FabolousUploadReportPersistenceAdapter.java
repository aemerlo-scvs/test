package com.scfg.core.adapter.persistence.fabolousAccount.fabolousUploadReport;

import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierRepository;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency.FabolousAgencyJpaEntity;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency.FabolousAgencyPersistenceAdapter;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousBeneficiary.FabolousBeneficiaryJpaEntity;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousBeneficiary.FabolousBeneficiaryPersistenceAdapter;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousBranch.FabolousBranchJpaEntity;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousBranch.FabolousBranchPersistenceAdapter;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousClient.FabolousClientJpaEntity;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousClient.FabolousClientPersistenceAdapter;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousInsurance.FabolousInsuranceJpaEntity;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousInsurance.FabolousInsurancePersistenceAdapter;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousManager.FabolousManagerJpaEntity;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousManager.FabolousManagerPersistenceAdapter;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousManagerAgency.FabolousManagerAgencyJpaEntity;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousManagerAgency.FabolousManagerAgencyPersistenceAdapter;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousZone.FabolousZoneJpaEntity;
import com.scfg.core.adapter.persistence.fabolousAccount.fabolousZone.FabolousZonePersistenceAdapter;
import com.scfg.core.adapter.persistence.user.UserRepository;
import com.scfg.core.application.port.out.FabolousPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.dto.FabolousDTO;
import com.scfg.core.domain.dto.credicasas.CurrentAmountRequestDTO;
import com.scfg.core.domain.dto.fabolous.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Precision;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class FabolousUploadReportPersistenceAdapter implements FabolousPort {

    private final FabolousUploadReportRepository fabolousUploadReportRepository;

    private final FabolousAgencyPersistenceAdapter fabolousAgencyPersistenceAdapter;
    private final FabolousBeneficiaryPersistenceAdapter fabolousBeneficiaryPersistenceAdapter;
    private final FabolousBranchPersistenceAdapter fabolousBranchPersistenceAdapter;
    private final FabolousClientPersistenceAdapter fabolousClientPersistenceAdapter;
    private final FabolousInsurancePersistenceAdapter fabolousInsurancePersistenceAdapter;
    private final FabolousManagerPersistenceAdapter fabolousManagerPersistenceAdapter;
    private final FabolousManagerAgencyPersistenceAdapter fabolousManagerAgencyPersistenceAdapter;
    private final FabolousZonePersistenceAdapter fabolousZonePersistenceAdapter;

    private final ClassifierRepository classifierRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;
    private static String GET_REPORT_LIQUIDATION = "";

    private Long MONTH_REFERENCE = 4L;
    private Long YEAR_REFERENCE = 5L;


    @Override
    public PersistenceResponse save(List<FabolousDTO> fabolousDTOS, boolean returnEntity) {
        FabolousUploadReportJpaEntity verify;
        try {
            List<FabolousBranchJpaEntity> fbsBranch = fabolousBranchPersistenceAdapter.getAllBranch();
            List<FabolousAgencyJpaEntity> fbsAgency = fabolousAgencyPersistenceAdapter.getAllAgency();
            List<FabolousZoneJpaEntity> fbsZone = fabolousZonePersistenceAdapter.getAllZone();
            List<FabolousManagerJpaEntity> fbsManager = fabolousManagerPersistenceAdapter.getAllManager();
            List<FabolousManagerAgencyJpaEntity> fbsManagerAgency = fabolousManagerAgencyPersistenceAdapter.getAllManagerAgency();
            List<FabolousBeneficiaryJpaEntity> fbsBeneficiary = fabolousBeneficiaryPersistenceAdapter.getAllBeneficiary();
            List<FabolousClientJpaEntity> fbsClient = fabolousClientPersistenceAdapter.getAllClient();
            List<FabolousInsuranceJpaEntity> fbsInsurance = fabolousInsurancePersistenceAdapter.getAllInsurance();
            List<ClassifierJpaEntity> extensions = classifierRepository.findAllByClassifierTypeReferenceId(18);
//            List<ClassifierJpaEntity> proffessions = classifierRepository.findAllByClassifierTypeReferenceId(16);
            List<ClassifierJpaEntity> relationShip = classifierRepository.findAllByClassifierTypeReferenceId(13);
            List<ClassifierJpaEntity> maritalStatus = classifierRepository.findAllByClassifierTypeReferenceId(14);
            List<ClassifierJpaEntity> monthList = classifierRepository.findAllByClassifierTypeReferenceId(MONTH_REFERENCE);
            List<ClassifierJpaEntity> yearList = classifierRepository.findAllByClassifierTypeReferenceId(YEAR_REFERENCE);

            FabolousUploadReportJpaEntity upload = new FabolousUploadReportJpaEntity();
            upload.setReportDateUpload(getTimeNow());
            upload.setMonthIdc(fabolousDTOS.get(0).getMonth());
            upload.setYearIdc(fabolousDTOS.get(0).getYear());
            upload.setUser(userRepository.findById(fabolousDTOS.get(0).getUser()).get());
            upload.setPolicyIdc(fabolousDTOS.get(0).getPolicy());
            upload.setReportTypeIdc(fabolousDTOS.get(0).getReportType());
            upload.setTotalUpload(fabolousDTOS.stream().count());

            verify = this.insert(upload);

            for (FabolousDTO data: fabolousDTOS) {

                FabolousBranchJpaEntity tempBranch;
                FabolousZoneJpaEntity tempZone;
                FabolousAgencyJpaEntity tempAgency;
                FabolousManagerJpaEntity tempManager;
                FabolousManagerAgencyJpaEntity tempManagerAgency;
                FabolousClientJpaEntity tempClient;
                FabolousBeneficiaryJpaEntity tempBeneficiary;
                FabolousInsuranceJpaEntity tempInsurance;
                double premiumAmount = 5;

                if (fbsBranch == null || fbsBranch.stream().noneMatch(e -> e.getBranchName().equals(data.getSucursal()))){
                    FabolousBranchJpaEntity branch = new FabolousBranchJpaEntity();
                    branch.setBranchName(data.getSucursal());
                    branch.setStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    tempBranch = fabolousBranchPersistenceAdapter.insert(branch);
                    fbsBranch.add(tempBranch);
                } else {
                    tempBranch = fbsBranch.stream().filter(e -> e.getBranchName().equals(data.getSucursal())).findFirst().get();
                }
                if (fbsZone.isEmpty() || fbsZone.stream().noneMatch(e -> e.getZoneName().equals(data.getZona()) && e.getFabolousBranch().equals(tempBranch.getId()))){
                    FabolousZoneJpaEntity zone = new FabolousZoneJpaEntity();
                    zone.setZoneName(data.getZona());
                    zone.setFabolousBranch(tempBranch.getId());
                    zone.setStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    tempZone = fabolousZonePersistenceAdapter.insert(zone);
                    fbsZone.add(tempZone);
                } else {
                    tempZone = fbsZone.stream().filter(e -> e.getZoneName().equals(data.getZona()) && e.getFabolousBranch().equals(tempBranch.getId())).findFirst().get();
                }
                if (fbsAgency.isEmpty() || fbsAgency.stream().noneMatch(e -> e.getAgencyName().equals(data.getAgencia()) && e.getFabolousZone().equals(tempZone.getId()))){
                    FabolousAgencyJpaEntity agency = new FabolousAgencyJpaEntity();
                    agency.setAgencyName(data.getAgencia());
                    agency.setFabolousZone(tempZone.getId());
                    agency.setStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    tempAgency = fabolousAgencyPersistenceAdapter.insert(agency);
                    fbsAgency.add(tempAgency);
                } else {
                    tempAgency = fbsAgency.stream().filter(e -> e.getAgencyName().equals(data.getAgencia()) && e.getFabolousZone().equals(tempZone.getId())).findFirst().get();
                }
                if (fbsManager.isEmpty() || fbsManager.stream().noneMatch(e -> e.getManagerName().equals(data.getGestor()))){
                    FabolousManagerJpaEntity manager = new FabolousManagerJpaEntity();
                    manager.setManagerName(data.getGestor());
                    manager.setManagerCode((long) -100);
                    manager.setStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    tempManager = fabolousManagerPersistenceAdapter.insert(manager);
                    fbsManager.add(tempManager);
                } else {
                    tempManager = fbsManager.stream().filter(e -> e.getManagerName().equals(data.getGestor())).findFirst().get();
                }
                if (fbsManagerAgency.isEmpty() || (fbsManagerAgency.stream().noneMatch(e -> e.getFabolousManager().equals(tempManager.getId())
                && e.getFabolousAgency().equals(tempAgency.getId()))) && fbsManagerAgency.stream().filter(e -> e.getFabolousManager().equals(tempManager.getId())).count()==0){
                    FabolousManagerAgencyJpaEntity managerAgency = new FabolousManagerAgencyJpaEntity();
                    managerAgency.setFabolousAgency(tempAgency.getId());
                    managerAgency.setFabolousManager(tempManager.getId());
                    managerAgency.setAgencyStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    managerAgency.setManagerStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    managerAgency.setStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    managerAgency.setExclusionDate(null);
                    tempManagerAgency = fabolousManagerAgencyPersistenceAdapter.insert(managerAgency);
                    fbsManagerAgency.add(tempManagerAgency);
                } else if (fbsManagerAgency.stream().filter(e -> e.getFabolousManager().equals(tempManager.getId()) &&
                        !e.getFabolousAgency().equals(tempAgency.getId())).count()>0){
                    FabolousManagerAgencyJpaEntity update = fbsManagerAgency.stream().filter(e -> e.getFabolousManager().equals(tempManager.getId()) && e.getExclusionDate() == null).findFirst().get();
                    fbsManagerAgency.remove(update);
                    update.setStatus(PersistenceStatusEnum.DELETED.getValue());
                    update.setExclusionDate(getTimeNow());
                    FabolousManagerAgencyJpaEntity changeData = fabolousManagerAgencyPersistenceAdapter.update(update);
                    fbsManagerAgency.add(changeData);
                    FabolousManagerAgencyJpaEntity managerAgencyNew = new FabolousManagerAgencyJpaEntity();
                    managerAgencyNew.setFabolousAgency(tempAgency.getId());
                    managerAgencyNew.setFabolousManager(tempManager.getId());
                    managerAgencyNew.setAgencyStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    managerAgencyNew.setManagerStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    managerAgencyNew.setStatus(PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
                    managerAgencyNew.setExclusionDate(null);
                    tempManagerAgency = fabolousManagerAgencyPersistenceAdapter.insert(managerAgencyNew);
                    fbsManagerAgency.add(tempManagerAgency);
                } else {
                    tempManagerAgency = fbsManagerAgency.stream().filter(e -> e.getFabolousAgency().equals(tempAgency.getId()) && e.getFabolousManager().equals(tempManager.getId())).findFirst().get();
                }
                if (fbsClient.isEmpty() || fbsClient.stream().noneMatch(e -> e.getClientCode().equals(data.getCod_cliente()))){
                    String ext = data.getIdentificacion().substring(data.getIdentificacion().length()-2,data.getIdentificacion().length());
                    FabolousClientJpaEntity client = new FabolousClientJpaEntity();
                    client.setClientCode(data.getCod_cliente());
                    client.setBirthDate(data.getFecha_nacimiento());
                    client.setFullName(data.getNombre_cliente());
                    client.setHomeAddress(data.getDomicilio());
                    if (maritalStatus.stream().filter(e -> e.getAbbreviation().equals(data.getEstado_civil().toLowerCase(Locale.ROOT))).findAny().isPresent()){
                        client.setMaritalStatusIdc(maritalStatus.stream().filter(e -> e.getAbbreviation().equals(data.getEstado_civil().toLowerCase(Locale.ROOT))).findFirst().get().getId());
                    } else {
                        client.setMaritalStatusIdc((long) -1);
                    }
                    client.setNationalityIdc(data.getNacionalidad());
                    if (extensions.stream().filter(e -> e.getAbbreviation().equals(ext)).findAny().isPresent()){
                        client.setExtension(ext);
                        client.setIdentification(data.getIdentificacion().substring(0,data.getIdentificacion().length()-2));
                    } else {
                        client.setIdentification(data.getIdentificacion());
                        client.setExtension("");
                    }
                    if (data.getProfesion().isEmpty() || data.getProfesion().equals("")){
                        client.setProfessionIdc("SIN ASIGNAR");
                    } else {
                        client.setProfessionIdc(data.getProfesion().toUpperCase(Locale.ROOT));
                    }
                    tempClient = fabolousClientPersistenceAdapter.insert(client);
                    fbsClient.add(tempClient);
                } else {
                    FabolousClientJpaEntity client = fbsClient.stream().filter(e -> e.getClientCode().equals(data.getCod_cliente())).findFirst().get();
                    if (client.getHomeAddress() != data.getDomicilio()){
                        client.setHomeAddress(data.getDomicilio());
                    }
                    if (client.getProfessionIdc() != data.getProfesion()){
                        if (data.getProfesion().isEmpty() || data.getProfesion().equals("")){
                            client.setProfessionIdc("SIN ASIGNAR");
                        } else {
                            client.setProfessionIdc(data.getProfesion().toUpperCase(Locale.ROOT));
                        }
                    }
                    tempClient = fabolousClientPersistenceAdapter.insert(client);
                    List<FabolousClientJpaEntity> tempClientList = new ArrayList<FabolousClientJpaEntity>();
                    for (FabolousClientJpaEntity replace: fbsClient) {
                        if (replace.getClientCode().equals(tempClient.getClientCode())){
                            tempClientList.add(tempClient);
                        } else {
                            tempClientList.add(replace);
                        }
                    }
                    fbsClient.clear();
                    fbsClient.addAll(tempClientList);
                }
//                if (fbsInsurance.isEmpty() || fbsInsurance.stream().noneMatch(e -> e.getFabolousClient().equals(tempClient) && e.getFabolousManagerAgency().equals(tempManagerAgency))){

                double numberOfMonths = ChronoUnit.MONTHS.between((tempClient.getBirthDate() == null ? data.getFecha_inicio_vigencia() : data.getFecha_nacimiento()), data.getFecha_inicio_vigencia()) / 12.0;

                FabolousInsuranceJpaEntity insurance = new FabolousInsuranceJpaEntity();
                    insurance.setFabolousClient(tempClient.getId());
                    insurance.setFabolousManagerAgency(tempManagerAgency.getId());
                    insurance.setFinishDate(data.getFecha_fin_vigencia());
                    insurance.setStartDate(data.getFecha_inicio_vigencia());
                    insurance.setInsuredCapital(data.getCapital_asegurado_bs());
                    insurance.setPremium(premiumAmount);
                    insurance.setFabolousUploadReportId(verify.getId());
                    insurance.setEntryAge(numberOfMonths);
                    insurance.setAccountNumber(data.getNro_cuenta());
                    tempInsurance = fabolousInsurancePersistenceAdapter.insertIns(insurance);
                    fbsInsurance.add(tempInsurance);
//                } else {
//                    break;
//                }
                if (data.getBeneficiario().isEmpty() || data.getBeneficiario().equals("")){

                } else {
                    if (fbsBeneficiary.isEmpty() || fbsBeneficiary.stream().noneMatch(e -> e.getFullName().equals(data.getBeneficiario())
                            && e.getFabolousInsurance().equals(tempInsurance.getId()))){
                        FabolousBeneficiaryJpaEntity beneficiary = new FabolousBeneficiaryJpaEntity();
                        beneficiary.setFullName(data.getBeneficiario());
                        beneficiary.setPercentage(data.getPorcentaje());
                        if (data.getParentesco().isEmpty() || data.getParentesco().equals("")){
                            beneficiary.setRelationshipIdc("SIN ASIGNAR");
                        } else {
                            beneficiary.setRelationshipIdc(data.getParentesco().toUpperCase(Locale.ROOT));
                        }
                        beneficiary.setFabolousInsurance(tempInsurance.getId());
                        beneficiary.setCreatedAt(getTimeNow());
                        beneficiary.setStatus(1);
                        tempBeneficiary = fabolousBeneficiaryPersistenceAdapter.insert(beneficiary);
                        fbsBeneficiary.add(tempBeneficiary);
                    }
                }
            }
        } catch (Exception e){
            throw e;
        }
        return new PersistenceResponse(
                FabolousUploadDTO.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                returnEntity ? verify : null
        );
    }

    @Override
    public List<FabolousUploadDTO> getAllUpload() {
        try {
            return fabolousUploadReportRepository.getAllUploads();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<FabolousReportLiquidationDTO> liquidationGenerateReport(FabolousReportDTO date) {
        Date from = this.changeTimeToZero(date.getDateFrom());
        Date to = this.changeTimeToZero(date.getDateTo());
        GET_REPORT_LIQUIDATION = "EXEC proc_repfabolous_monthly_liquidation :dateFrom, :dateTo";
        Query query = em.createNativeQuery(GET_REPORT_LIQUIDATION);
        query.setParameter("dateFrom", from);
        query.setParameter("dateTo", to);

        List<FabolousReportLiquidationDTO> reportDTOS = new ArrayList<>();

        List<Object[]> list = query.getResultList();
        if (!list.isEmpty()) {
            for (Object[] row : list) {
                FabolousReportLiquidationDTO insertToList = new FabolousReportLiquidationDTO();
                insertToList.setNro_cuenta(new Long(row[0].toString()));
                insertToList.setSucursal(row[1].toString());
                insertToList.setZona(row[2].toString());
                insertToList.setAgencia(row[3].toString());
                insertToList.setCod_cliente(new Long(row[4].toString()));
                insertToList.setNombre_cliente(row[5].toString());
                insertToList.setIdentificacion(row[6].toString());
                insertToList.setExt(row[7].toString());
                insertToList.setNacionalidad(row[8].toString());
                insertToList.setFecha_nacimiento(HelpersMethods.formatStringToDate(row[9].toString()));
                insertToList.setEdad(Double.parseDouble(row[10].toString()));
                insertToList.setEstado_civil(row[11].toString());
                insertToList.setDomicilio(row[12].toString());
                insertToList.setProfesion(row[13].toString());
                insertToList.setBeneficiario(row[14].toString());
                insertToList.setPorcentaje(Double.parseDouble(row[15].toString()));
                insertToList.setParentesco(row[16].toString());
                insertToList.setGestor(row[17].toString());
                insertToList.setFecha_inicio_vigencia(HelpersMethods.formatStringToDate(row[18].toString()));
                insertToList.setFecha_fin_vigencia(HelpersMethods.formatStringToDate(row[19].toString()));
                insertToList.setCapital_asegurado_bs(Double.parseDouble(row[20].toString()));
                insertToList.setPrima(Double.parseDouble(row[21].toString()));

                reportDTOS.add(insertToList);
            }
        }
        return reportDTOS;
    }

    @Override
    public List<FabolousReportDuplicateLiquidationDTO> liquidationGenerateDuplicateReport(FabolousReportDTO date) {
        Date from = this.changeTimeToZero(date.getDateFrom());
        Date to = this.changeTimeToZero(date.getDateTo());
        GET_REPORT_LIQUIDATION = "EXEC proc_repfabolous_duplicate_monthly_liquidation :dateFrom, :dateTo";
        Query query = em.createNativeQuery(GET_REPORT_LIQUIDATION);
        query.setParameter("dateFrom", from);
        query.setParameter("dateTo", to);

        List<FabolousReportDuplicateLiquidationDTO> reportDTOS = new ArrayList<>();

        List<Object[]> list = query.getResultList();
        if (!list.isEmpty()) {
            for (Object[] row : list) {
                FabolousReportDuplicateLiquidationDTO insertToList = new FabolousReportDuplicateLiquidationDTO();
                insertToList.setNro_cuenta(new Long(row[0].toString()));
                insertToList.setSucursal(row[1].toString());
                insertToList.setZona(row[2].toString());
                insertToList.setAgencia(row[3].toString());
                insertToList.setCod_cliente(new Long(row[4].toString()));
                insertToList.setNombre_cliente(row[5].toString());
                insertToList.setIdentificacion(row[6].toString());
                insertToList.setExt(row[7].toString());
                insertToList.setNacionalidad(row[8].toString());
                insertToList.setFecha_nacimiento(HelpersMethods.formatStringToDate(row[9].toString()));
                insertToList.setEdad(Double.parseDouble(row[10].toString()));
                insertToList.setEstado_civil(row[11].toString());
                insertToList.setDomicilio(row[12].toString());
                insertToList.setProfesion(row[13].toString());
                insertToList.setBeneficiario(row[14].toString());
                insertToList.setPorcentaje(Double.parseDouble(row[15].toString()));
                insertToList.setParentesco(row[16].toString());
                insertToList.setGestor(row[17].toString());
                insertToList.setFecha_inicio_vigencia(HelpersMethods.formatStringToDate(row[18].toString()));
                insertToList.setFecha_fin_vigencia(HelpersMethods.formatStringToDate(row[19].toString()));
                insertToList.setCapital_asegurado_bs(Double.parseDouble(row[20].toString()));
                insertToList.setPrima(Double.parseDouble(row[21].toString()));
                insertToList.setMes(Integer.parseInt(row[22].toString()));
                insertToList.setDuplicados(row[23].toString());
                insertToList.setExclusion(row[24].toString());

                reportDTOS.add(insertToList);
            }
        }
        return reportDTOS;
    }

    @Override
    public boolean deleteUploadReport(Long deleteId) {
        try {
            FabolousUploadReportJpaEntity tmpDelete = fabolousUploadReportRepository.findById(deleteId).get();
            tmpDelete.setStatus(0);
            FabolousUploadReportJpaEntity isDelete = this.update(tmpDelete);
            if (isDelete.getStatus().equals(0)) {
                fabolousInsurancePersistenceAdapter.deleteInsertReportUpload(isDelete.getId());
                fabolousBeneficiaryPersistenceAdapter.deleteInsertInsuranceReportUpload(isDelete.getId().toString());
                return true;
            } else {
                System.out.println("Error al intentar eliminar uploadReportId: " + deleteId);
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public FabolusResultResponseClient searchClient(FabolousSearchCltDTO client, Integer page, Integer size) {
        String valSearch = "WHERE ";
        if (client.getName() != null && !client.getName().isEmpty() && client.getName().trim().length() > 0) {
            valSearch += "i.fullName like '%" + client.getName() + "%'";
            if (client.getDocumentNumber() != null && !client.getDocumentNumber().isEmpty() && client.getDocumentNumber().trim().length() > 0) {
                valSearch += "\nOR i.identification ='" + client.getDocumentNumber() + "'\n";
            }
        } else if (client.getDocumentNumber() != null && !client.getDocumentNumber().isEmpty() && client.getDocumentNumber().trim().length() > 0) {
            valSearch += "i.identification ='" + client.getDocumentNumber() + "'\n";
        }
        int initRange = HelpersMethods.getPageInitRange(page, size);
        List<Object[]> objDt = em.createNativeQuery(fabolousUploadReportRepository.getClientInFbs(valSearch)).setFirstResult(initRange).setMaxResults(size).getResultList();
        em.close();

        String countD = fabolousUploadReportRepository.getClientCountInFbs(valSearch);

        Integer count = (Integer) em.createNativeQuery(countD).getSingleResult();
        em.close();

        return FabolusResultResponseClient.builder()
                .clientList(objDt.stream().map(FabolousFindClient::new).collect(Collectors.toList()))
                .clientListQuantity(count)
                .build();
    }

    public synchronized FabolousUploadReportJpaEntity insert(FabolousUploadReportJpaEntity mg){
        try {
            return fabolousUploadReportRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public synchronized FabolousUploadReportJpaEntity update (FabolousUploadReportJpaEntity mg){
        try {
            return fabolousUploadReportRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
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

    public static Date changeTimeToZero(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date from = calendar.getTime();
        return from;
    }
}
