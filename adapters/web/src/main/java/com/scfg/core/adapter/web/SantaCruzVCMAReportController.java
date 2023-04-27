package com.scfg.core.adapter.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.scfg.core.application.port.in.Knowledge_HistoryPortIn;
import com.scfg.core.application.port.in.PolicyManagerBs;
import com.scfg.core.application.port.in.ReportRequestpolicyBs;
import com.scfg.core.application.port.in.SantaCruzVCMAReport;
import com.scfg.core.domain.dto.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = SantaCruzVCMAReportEndPoint.BASE)
@Api(value = "Esta api sirve para vcma")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class SantaCruzVCMAReportController implements SantaCruzVCMAReportEndPoint {
    private static final Logger logger = Logger.getLogger(SantaCruzVCMAReportController.class.getName());
    private final SantaCruzVCMAReport vcmaReport;
    private final ReportRequestpolicyBs reportRequestpolicyBsService;
    private final PolicyManagerBs managerBsService;
    private  final Knowledge_HistoryPortIn knowledge_historBsService;

    @PostMapping(value = SantaCruzVCMAReportEndPoint.GET_DATA_FROM_FILE, consumes = {"application/json"}, produces = {"application/json"})
    public Boolean getAllManagerDataCsv(@RequestBody String data) throws Exception {
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
            Type listType = new TypeToken<ArrayList<VCMAManagerDTO>>(){}.getType();
            List<VCMAManagerDTO> realManagers = gson.fromJson(jsonObject.get("managers"), listType);
            return vcmaReport.insertManagers(realManagers);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @PostMapping(
            value = {SantaCruzVCMAReportEndPoint.REPORTREQUESTPOLICY},
            produces = {"application/json"}
    )
    public FileDocumentDTO generateExcel(@RequestBody ReportRequestPolicyDTO reportRequestPolicyDTO) {
        logger.info("posiblemente haya entrao aqui");
        return reportRequestpolicyBsService.getReport(reportRequestPolicyDTO);
    }

    @PostMapping(
            value = {SantaCruzVCMAReportEndPoint.SENDREPORTEMAIL},
            produces = {"application/json"}
    )
    public boolean sendReportforMail(@RequestBody FileDocumentDTOMail fileDocumentDTOMail) throws MessagingException {
        logger.info("posiblemente haya entrao aqui");
        return reportRequestpolicyBsService.sendReportforMail(fileDocumentDTOMail);
    }


    @GetMapping(
            value = {SantaCruzVCMAReportEndPoint.REPORTPOLICY},
            produces = {"application/json"}
    )
    public FileDocumentDTO generateExcels() {
        return reportRequestpolicyBsService.getReports();
    }

    @PostMapping(
            value = {SantaCruzVCMAReportEndPoint.REPORTRESQUESTPOLICYFORDATES},
            produces = {"application/json"}
    )
    public List<VCMAReportDTO> getReportForDates(@RequestBody ReportRequestPolicyDTO reportRequestPolicyDTO) {
        return reportRequestpolicyBsService.getReportDates(reportRequestPolicyDTO);
    }

    @PostMapping(
            value = {SantaCruzVCMAReportEndPoint.LOADPOLICYMANAGER},
            produces = {"application/json"}
    )
    public boolean loadPolicyManager(@RequestBody String data) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
        Type listType = new TypeToken<ArrayList<PolicyDto>>(){}.getType();
        List<PolicyDto> loads = gson.fromJson(jsonObject.get("load"), listType);
        return managerBsService.loadPolicyManagers(loads);
    }

    @GetMapping(
            value = {SantaCruzVCMAReportEndPoint.GETPOLICYMANAGERCODE},
            produces = {"application/json"}
    )
    public List<PolicyDto> getlistpolicys(@PathVariable long code) {
        return managerBsService.findByManager_Code(code);
    }
    @GetMapping(
            value = {SantaCruzVCMAReportEndPoint.GETEXCELSINPOLIZASCONSLIDAS},
            produces = {"application/json"}
    )
    public FileDocumentDTOInf getExcelSinGestoresConsolidados() {
        return reportRequestpolicyBsService.getExcelSinGestoresConsolidados();
    }
    @GetMapping(
            value = {SantaCruzVCMAReportEndPoint.GETSINPOLIZASCONSLIDAS},
            produces = {"application/json"}
    )
    public List<ResultPolicyDtO> getSinGestoresConsolidados() {
        return reportRequestpolicyBsService.getSinGestoresConsolidados();
    }
    @PostMapping(
            value = {SantaCruzVCMAReportEndPoint.LOADEXCELCONSILDMANAGERPOLIZAS},
            produces = {"application/json"}
    )
    public boolean loadExcelConsolidManagerPolicys(@RequestBody String data) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
        Type listType = new TypeToken<ArrayList<ResultPolicyDtO>>(){}.getType();
        List<ResultPolicyDtO> loads = gson.fromJson(jsonObject.get("load"), listType);
        return knowledge_historBsService.processExcelConsolidManagerPolicy(loads);
    }
    @GetMapping(
            value = {SantaCruzVCMAReportEndPoint.UPDATEPOLIZASMANAGERCONSOLDIT},
            produces = {"application/json"}
    )
    public int updatepolicymamanagerconsolidate () {
        return reportRequestpolicyBsService.updatepolicymanager();
    }
}
