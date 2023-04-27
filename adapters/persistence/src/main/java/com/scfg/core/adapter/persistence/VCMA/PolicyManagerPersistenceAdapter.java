package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.models.PolicyManagerJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.repository.PolicyManagerRepository;
import com.scfg.core.adapter.persistence.VCMA.utils.UtilScfg;
import com.scfg.core.application.port.out.PolicyManagerPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.PolicyManager;
import com.scfg.core.domain.dto.PolicyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class PolicyManagerPersistenceAdapter implements PolicyManagerPort {

    @Autowired
    PolicyManagerRepository policyManegerRespository;
    Logger log = Logger.getLogger(PolicyManagerPersistenceAdapter.class.getName());

    public PolicyManagerRepository getPolicyManegerRespository() {
        return policyManegerRespository;
    }

    public void setPolicyManegerRespository(PolicyManagerRepository policyManegerRespository) {
        this.policyManegerRespository = policyManegerRespository;
    }

    @Override
    public List<PolicyManager> getAllPolicyManager() {
        try {
            Object policyList = policyManegerRespository.findAll();
            return (List<PolicyManager>) policyList;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public PolicyManager getPolycyManagerByPolicyNumber(String number) {
        try {
            Object data = policyManegerRespository.findById(number).get();
            return (PolicyManager) data;
        } catch (Exception ex) {
            return null;
        }
    }


    @Override
    public PolicyManager updateProdcuto(PolicyManager policyManager) {
        return null;
    }

    @Override
    public boolean loadPolicyManagers(List<PolicyDto> dtoList) {
        try {
            for (PolicyDto items : dtoList) {
                PolicyManagerJpaEntity policyManager=convertToModelPolicyManager(items);
                addPolicyManager2(policyManager);
            }
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean addPolicyManager2(PolicyManagerJpaEntity policyManager) {
        try {

            Optional<PolicyManagerJpaEntity> list = findById2(policyManager.getPolicy_number());
            if (list.isPresent()) {
                return false;
            } else {
                policyManegerRespository.save(policyManager);
                return true;
            }
        } catch (Exception ex) {
            log.info(() -> ex.getMessage());
            return false;
        }
    }


    public Optional<PolicyManagerJpaEntity> findById2(String id) {
        return policyManegerRespository.findById(id);
    }

    @Override
    public List<PolicyDto> findByManager_Code(long cod) {
        Object dtoListNew=policyManegerRespository.findAll();
        List<PolicyManagerJpaEntity> dtoList= (List<PolicyManagerJpaEntity>) dtoListNew;
        List<PolicyManagerJpaEntity> auxlist= dtoList.stream().filter(a -> a.getManager_code() ==cod).collect(Collectors.toList());
        return ListEntityToPolicysDto(auxlist);
    }



    private List<PolicyDto> ListEntityToPolicysDto(List<PolicyManagerJpaEntity> byManager_code) {
        List<PolicyDto> ls=new ArrayList<>();
        byManager_code.stream().forEach(d ->{
            ls.add(convertEntityToPolicysDto(d));
        });
        return ls;
    }

    private PolicyDto convertEntityToPolicysDto(PolicyManagerJpaEntity d) {
        try {
            PolicyDto pol = new PolicyDto();
            pol.setRequest_id(d.getRequest_id());
            pol.setRequest_date(UtilScfg.convertTimesStampToString(d.getRequest_date()));
            pol.setManager_bank(d.getManager_bank());
            pol.setNames_manager(d.getNames_manager());
            pol.setStatus(d.getRequest_status());
            pol.setPolicy_number(d.getPolicy_number());
            pol.setEmission_date(UtilScfg.convertTimesStampToStringHHMMSS(d.getEmission_date()));
            pol.setDate_from(UtilScfg.convertTimesStampToString(d.getDate_from()));
            pol.setDate_to(UtilScfg.convertTimesStampToString(d.getDate_to()));
            pol.setName_plan(d.getName_plan());
            pol.setValue_am(d.getValue_am());
            pol.setValue_ac(d.getValue_ac());
            pol.setPremiun_total(d.getPremiun_total());
            pol.setNames_manager(d.getNames_manager());
            return pol;
        }catch (Exception ex){
            return null;
        }
    }

    private PolicyManagerJpaEntity convertToModelPolicyManager(PolicyDto items) {
        PolicyManagerJpaEntity policyManager= new PolicyManagerJpaEntity();
        policyManager.setRequest_id(items.getRequest_id());
        policyManager.setRequest_date(UtilScfg.convertToTimesStampHHMMSS(items.getRequest_date()));
        policyManager.setManager_bank(items.getManager_bank().trim());
        policyManager.setNames_manager(items.getNames_manager().trim());
        policyManager.setRequest_status(items.getStatus());
        policyManager.setPolicy_number(items.getPolicy_number().trim());
        policyManager.setEmission_date(UtilScfg.convertToTimesStampHHMMSS(items.getEmission_date()));
        policyManager.setDate_from(UtilScfg.convertToTimesStamp(items.getDate_from()));
        policyManager.setDate_to(UtilScfg.convertToTimesStamp(items.getDate_to()));
        policyManager.setName_plan(items.getName_plan().trim());
        policyManager.setValue_am(items.getValue_am());
        policyManager.setValue_ac(items.getValue_ac());
        policyManager.setPremiun_total(items.getPremiun_total());
        return policyManager;
    }
}
