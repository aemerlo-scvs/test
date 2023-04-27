package com.scfg.core.application.service;

import com.scfg.core.application.port.in.Knowledge_HistoryPortIn;
import com.scfg.core.application.port.in.PolicyManagerBs;
import com.scfg.core.application.port.in.ReportRequestpolicyBs;
import com.scfg.core.application.port.in.SantaCruzVCMAReport;
import com.scfg.core.application.port.out.Knowledge_HistoryPort;
import com.scfg.core.application.port.out.PolicyManagerPort;
import com.scfg.core.application.port.out.ReportRequestPolicyPort;
import com.scfg.core.application.port.out.SantaCruzVCMAReportPort;
import com.scfg.core.domain.Knowledge_History;
import com.scfg.core.domain.PolicyManager;
import com.scfg.core.domain.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SantaCruzVCMAReportService implements SantaCruzVCMAReport, PolicyManagerBs, ReportRequestpolicyBs, Knowledge_HistoryPortIn {

    private final SantaCruzVCMAReportPort vcmaReportPort;
    private final ReportRequestPolicyPort reportRequestPolicyPort;
    private final PolicyManagerPort policyManagerPort;
    private  final Knowledge_HistoryPort knowledge_historyPort;

    @Override
    public Boolean insertManagers(List<VCMAManagerDTO> data) throws Exception {
        return vcmaReportPort.insert(data);
    }

    @Override
    public List<PolicyManager> getAllPolicyManager() {
        return policyManagerPort.getAllPolicyManager();
    }

    @Override
    public PolicyManager getPolycyManagerByPolicyNumber(String number) {
        return policyManagerPort.getPolycyManagerByPolicyNumber(number);
    }

    @Override
    public PolicyManager updateProdcuto(PolicyManager policyManager) {
        return policyManagerPort.updateProdcuto(policyManager);
    }

    @Override
    public boolean loadPolicyManagers(List<PolicyDto> dtoList) {
        return policyManagerPort.loadPolicyManagers(dtoList);
    }

    @Override
    public List<PolicyDto> findByManager_Code(long cod) {
        return policyManagerPort.findByManager_Code(cod);
    }

    @Override
    public FileDocumentDTOInf getReport(ReportRequestPolicyDTO reportRequestPolicyDTO) {
        return reportRequestPolicyPort.getReport(reportRequestPolicyDTO);
    }


    public FileDocumentDTOInf getExcelSinGestoresConsolidados() {
        return reportRequestPolicyPort.getExcelSinGestoresConsolidados();
    }

    @Override
    public List<ResultPolicyDtO> getSinGestoresConsolidados() {
        return reportRequestPolicyPort.getConsolidado();
    }

    @Override
    public int updatepolicymanager() {
        return reportRequestPolicyPort.updatepolicymanager() ;
    }

    @Override
    public boolean sendReportforMail(FileDocumentDTOMail fileDocumentDTOMail) throws MessagingException {
        return reportRequestPolicyPort.sendReportforMail(fileDocumentDTOMail);
    }


    @Override
    public FileDocumentDTOInf getReports() {
        return reportRequestPolicyPort.getReports();
    }

    @Override
    public List<VCMAReportDTO> getReportDates(ReportRequestPolicyDTO requestPolicyDTO) {
        return reportRequestPolicyPort.getReportDates(requestPolicyDTO);
    }

    @Override
    public boolean processExcelConsolidManagerPolicy(List<ResultPolicyDtO> list) {
        boolean menssage=false;
        List<Knowledge_History> knowledge_historyList=convertResultPolicyDTOTOKowledge(list);
        if (knowledge_historyPort.guardar(knowledge_historyList)){

            menssage=true;

        }else{
            menssage=false;
        }
        return menssage;
    }

    private List<Knowledge_History> convertResultPolicyDTOTOKowledge(List<ResultPolicyDtO> list) {
        List<Knowledge_History> knowledge_historyList =new ArrayList<>();
        list.stream().forEach(dat ->{
            Knowledge_History obj=new Knowledge_History();
            obj.setPolicy_number(dat.getPolicy_number());
            obj.setManager_code(dat.getManager_code());
            obj.setNames_correct(dat.getNames_manager_correct());
            obj.setNames_incorrect(dat.getNames_manager_incorrect());
            knowledge_historyList.add(obj);
        });
        return knowledge_historyList;
    }
}
