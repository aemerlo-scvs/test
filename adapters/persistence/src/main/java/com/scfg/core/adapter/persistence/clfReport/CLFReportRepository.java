package com.scfg.core.adapter.persistence.clfReport;

import com.scfg.core.adapter.persistence.naturalPerson.NaturalPersonJpaEntity;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.dto.credicasas.groupthefont.SearchReportParamDTO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CLFReportRepository extends JpaRepository<NaturalPersonJpaEntity,Long> {

    default String getReport(SearchReportParamDTO searchReportParamDTO) {

        String filterGroupAndTaker = "where p.assignedGroupIdc = {groupId}";
        filterGroupAndTaker = filterGroupAndTaker.replace("{groupId}", searchReportParamDTO.getGroupId()+"");

        if (searchReportParamDTO.getTakerId() > 0) {
            filterGroupAndTaker = filterGroupAndTaker + " and p.id = {takerId}";
            filterGroupAndTaker = filterGroupAndTaker.replace("{takerId}", searchReportParamDTO.getTakerId()+"");
        }

        String finalSearch = "WHERE gr.createdAt BETWEEN '{fromDate}' and '{toDate}'";

        searchReportParamDTO.setFromDate(DateUtils.changeHourInDateMorningAndNight(searchReportParamDTO.getFromDate(),true,false));
        searchReportParamDTO.setToDate(DateUtils.changeHourInDateMorningAndNight(searchReportParamDTO.getToDate(),false,true));
        finalSearch = finalSearch.replace("{fromDate}", HelpersMethods.formatStringOnlyDateAndHour(searchReportParamDTO.getFromDate()));
        finalSearch = finalSearch.replace("{toDate}", HelpersMethods.formatStringOnlyDateAndHour(searchReportParamDTO.getToDate()));

        if (searchReportParamDTO.getRequestStatusIdc() > 0) {
            finalSearch = finalSearch + " and gr.requestStatusIdc = {requestId}";
            finalSearch = finalSearch.replace("{requestId}", searchReportParamDTO.getRequestStatusIdc()+"");
        }

        if (searchReportParamDTO.getPlanId() > 0) {
            finalSearch = finalSearch + " and pln.id = {planId}";
            finalSearch = finalSearch.replace("{planId}", searchReportParamDTO.getPlanId()+"");
        }

        finalSearch = finalSearch + " and gr.status = {rstatus}";
        finalSearch = finalSearch.replace("{rstatus}", PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()+"");

    return  "DROP TABLE IF EXISTS #tempDjsFiles;   \n" +
            "DROP TABLE IF EXISTS #tempNtaRecFiles;   \n" +
            "DROP TABLE IF EXISTS #tempNtaPendFiles;   \n" +
            "DROP TABLE IF EXISTS #tempCertFiles;   \n" +
            "DROP TABLE IF EXISTS #tempPepFiles;  \n" +
            "DROP TABLE IF EXISTS #tempExtPrim;  \n" +
            "DROP TABLE IF EXISTS #PregRespDJS;  \n" +
            "DROP TABLE IF EXISTS #tempCobertAsig;  \n" +
            "DROP TABLE IF EXISTS #tempFirmType;  \n" +
            "              \n" +
            "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
            "into #tempDjsFiles from FileDocument fl   \n" +
            "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
            "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId in (1,3)   \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
            "order by typeDocument asc   \n" +
            "              \n" +
            "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
            "into #tempCertFiles from FileDocument fl   \n" +
            "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
            "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId = 4   \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
            "order by typeDocument asc   \n" +
            "              \n" +
            "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
            "into #tempNtaRecFiles from FileDocument fl   \n" +
            "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
            "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId in (6,11)  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
            "order by typeDocument asc   \n" +
            "              \n" +
            "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
            "into #tempNtaPendFiles from FileDocument fl   \n" +
            "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
            "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId = 5  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
            "order by typeDocument asc   \n" +
            "              \n" +
            "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
            "into #tempPepFiles from FileDocument fl   \n" +
            "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
            "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId IN (7,10)   \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
            "order by typeDocument asc   \n" +
            "              \n" +
            "select policyItemId,SUM(additionalPremiumPerPercentage) as primaPort,   \n" +
            "SUM(additionalPremiumPerThousand) as primaMil,   \n" +
            "STRING_AGG(cvppi.comment, ' | ') AS comment into #tempExtPrim from CoveragePolicyItem cvppi   \n" +
            "group by policyItemId   \n" +
            " \n" +
            "select STRING_AGG(cv.name, ' | ') AS 'coberturas', grs.id as requestId, pois.id as policyItemId  \n" +
            "into #tempCobertAsig from CoveragePolicyItem cvppi   \n" +
            "join CoverageProductPlan cvpp on cvpp.id = cvppi.coverageProductPlanId   \n" +
            "join CoverageProduct cvp on cvp.id = cvpp.coverageProductId   \n" +
            "join Coverage cv on cv.id = cvp.coverageId   \n" +
            "join PolicyItem pois on pois.id = cvppi.policyItemId   \n" +
            "join GeneralRequest grs on grs.id = pois.generalRequestId   \n" +
            "join (select * from Policy where numberPolicy like '%DHN%') pol on pol.id = pois.policyId \n" +
            "group by grs.id, pois.id \n" +
            " \n" +
            "select IIF(pld.isSigned = 1, 'FIRMA ELECTRÓNICA','FIRMA MANUAL') as tipoFirma, pld.policyItemId \n" +
            "into #tempFirmType from FileDocument fl   \n" +
            "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
            "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId in (1,3)   \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26 \n" +
            "order by policyItemId,uploadDate asc \n" +
            "             \n" +
            "select prod.name AS cartera, pl.numberPolicy as poliza,   \n" +
            "gr.createdAt as 'fechaLlenadoDjs',   \n" +
            "gr.creditNumber as 'noOperacion',   \n" +
            "'NUEVO' as 'tipoOperacion', --Este campo se debe actualizar con el desembolso mensual  \n" +
            "gr.requestNumber as 'noSolicitud',  \n" +
            "certN.documentNumber as 'noCertificado', \n" +
            "CONCAT(ntp.name,' ',ntp.lastName,' ', ntp.motherLastName,' ', ntp.marriedLastName) as asegurado,   \n" +
            "genre.description as genero, ntp.identificationNumber as ci, ext.description as ext, convert(date, ntp.birthDate) as 'fechaNac',   \n" +
            "CONVERT(DECIMAL(10,0), ROUND(datediff(DD,ntp.birthDate,GETDATE())/365.25,0,1)) as edad, natio.description as nacionalidad, CONVERT(DECIMAL(10,0),gr.weight) as peso,  \n" +
            "CONVERT(DECIMAL(10,0),gr.height) as estatura,    \n" +
            "workType.description as 'tipoAsegurado',moneyType.description as moneda, gr.currentAmount as 'montoVigente', gr.requestedAmount as 'montoSolicitado',   \n" +
            "gr.accumulatedAmount as 'montoAcumulado', gr.creditTerm as plazaCredito,requestStatus.description as estado,   \n" +
            "IIF(requestStatus.referenceId = 1, gr.pendingReason,'') as 'motivoPendiente', \n" +
            "IIF(requestStatus.referenceId = 6, gr.inactiveComment,'') as 'comentarioInactivacion', \n" +
            "acceptanceReason.description as 'tipoAceptacion', \n" +
            "iif(covpi.primaPort >= 0, covpi.primaPort, 0) as 'porcentExtPrim',   \n" +
            "iif(covpi.comment is not null, covpi.comment, '') as 'motExtPrim',   \n" +
            "IIF(gr.exclusionComment is not null, gr.exclusionComment, '') as 'motExc',   \n" +
            "iif(rejectTp.description is not null, rejectTp.description, '') as 'motRec',   \n" +
            "IIF(gr.rejectedComment is not null, gr.rejectedComment, '') as 'comentRec',   \n" +
            "IIF((LEN(cobert.coberturas)-LEN(REPLACE(cobert.coberturas,'|','')))>1,'COBERTURA TOTAL',cobert.coberturas) as 'cobOtorgada',   \n" +
            "convert(date,IIF(gr.lastModifiedAt <> null, gr.lastModifiedAt, gr.createdAt)) as 'fechaPronunciamiento',   \n" +
            "convert(date,(select TOP 1 tdf.uploadDate from #tempDjsFiles tdf where tdf.policyItemId = poi.id and tdf.isSigned = 1 order by tdf.uploadDate asc)) as 'fechaCargaDjs',   \n" +
            "convert(date,(select TOP 1 tdf.uploadDate from #tempCertFiles tdf where tdf.policyItemId = poi.id order by tdf.uploadDate asc)) as 'fechaCargaCert',   \n" +
            "convert(date,(select TOP 1 tdf.uploadDate from #tempNtaRecFiles tdf where tdf.policyItemId = poi.id order by tdf.uploadDate asc)) as 'fechaCargaNotRec',  \n" +
            "IIF(pep.status = 1, 'SI', 'NO') as 'clientePep',IIF(pep.status = 1, 'ACTIVO', 'INACTIVO') as 'estadoPep',   \n" +
            "IIF(pep.status = 1, convert(date,(select TOP 1 tdf.uploadDate from #tempPepFiles tdf where tdf.policyItemId = poi.id order by tdf.uploadDate asc)), null) as 'fechaPronunciamientoPep',  \n" +
            "regional.description as regional, CONCAT(usr.name, ' ', usr.surName) as 'userCreation',  \n" +
            "convert(date,(gr.lastModifiedAt)) as 'fechaOperacion',  \n" +
            "IIF((select TOP 1 COUNT(tdf.uploadDate) from #tempNtaPendFiles tdf where tdf.policyItemId = poi.id) > 0, 'MANUAL', 'AUTOMÁTICA') as 'subsType', \n" +
            "(select top 1 tft.tipoFirma from #tempFirmType tft \n" +
            "where tft.policyItemId = poi.id) as tipoFirma \n" +
            "from NaturalPerson ntp   \n" +
            "join Person p on p.naturalPersonId = ntp.id   \n" +
            "join GeneralRequest gr on gr.personId = p.id   \n" +
            "join PolicyItem poi on poi.generalRequestId = gr.id  \n" +
            "left join #tempCobertAsig cobert on cobert.requestId = gr.id and cobert.policyItemId = poi.id \n" +
            "left join #tempExtPrim covpi on covpi.policyItemId = poi.id   \n" +
            "join Policy pl on pl.id = poi.policyId   \n" +
            "left join (select pl.id as policyId, gr.planId from Classifier cl   \n" +
            "join ClassifierType clt on clt.id = cl.classifierTypeId and clt.referenceId = 41   \n" +
            "join Person p on p.assignedGroupIdc = cl.referenceId   \n" +
            "join GeneralRequest gr on gr.personId = p.id  \n" +
            "join Policy pl on pl.generalRequestId = gr.id  \n" +
            filterGroupAndTaker +
            ") x on x.policyId = pl.id  \n" +
            "left join (select cl.referenceId, cl.description from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 18) ext on ext.referenceId = ntp.extIdc  \n" +
            "left join (select cl.* from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 7) genre on genre.referenceId = ntp.genderIdc  \n" +
            "join [Plan] pln on pln.id = x.planId  \n" +
            "join Product prod on prod.id = pln.productId  \n" +
            "left join (select cl.* from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 15) natio on natio.referenceId = p.nationalityIdc  \n" +
            "left join (select cl.* from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 51) rejectTp on rejectTp.referenceId = gr.rejectedReasonIdc  \n" +
            "left join (select cl.* from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 44) workType on workType.referenceId = ntp.workTypeIdc  \n" +
            "left join (select cl.* from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 11) moneyType on moneyType.referenceId = pl.currencyTypeIdc  \n" +
            "left join (select cl.* from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 17) requestStatus on gr.requestStatusIdc = requestStatus.referenceId  \n" +
            "left join Pep pep on pep.identificationNumber = ntp.identificationNumber   \n" +
            "join Users usr on usr.id = gr.createdBy  \n" +
            "left join (select cl.* from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 9) regional on usr.regionalIdc = regional.referenceId\n" +
            "left join (select p.* from PolicyDocument p\n" +
            "join PolicyItem pi on pi.id = p.policyItemId\n" +
            "where p.documentNumber > 0) certN on certN.policyItemId = poi.id\n" +
            "left join (select cl.* from Classifier cl  \n" +
            "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 52) acceptanceReason on gr.acceptanceReasonIdc = acceptanceReason.referenceId\n" +
            finalSearch + " order by [poliza], [noSolicitud] asc";
        }

    default String getCommercialReport(SearchReportParamDTO searchReportParamDTO) {

        String filterGroupAndTaker = "where p.assignedGroupIdc = {groupId}";
        filterGroupAndTaker = filterGroupAndTaker.replace("{groupId}", searchReportParamDTO.getGroupId()+"");

        if (searchReportParamDTO.getTakerId() > 0) {
            filterGroupAndTaker = filterGroupAndTaker + " and p.id = {takerId}";
            filterGroupAndTaker = filterGroupAndTaker.replace("{takerId}", searchReportParamDTO.getTakerId()+"");
        }

        String finalSearch = "WHERE gr.createdAt BETWEEN '{fromDate}' and '{toDate}'";

        searchReportParamDTO.setFromDate(DateUtils.changeHourInDateMorningAndNight(searchReportParamDTO.getFromDate(),true,false));
        searchReportParamDTO.setToDate(DateUtils.changeHourInDateMorningAndNight(searchReportParamDTO.getToDate(),false,true));
        finalSearch = finalSearch.replace("{fromDate}", HelpersMethods.formatStringOnlyDateAndHour(searchReportParamDTO.getFromDate()));
        finalSearch = finalSearch.replace("{toDate}", HelpersMethods.formatStringOnlyDateAndHour(searchReportParamDTO.getToDate()));

        if (searchReportParamDTO.getRequestStatusIdc() > 0) {
            finalSearch = finalSearch + " and gr.requestStatusIdc = {requestId}";
            finalSearch = finalSearch.replace("{requestId}", searchReportParamDTO.getRequestStatusIdc()+"");
        }

        if (searchReportParamDTO.getPlanId() > 0) {
            finalSearch = finalSearch + " and pln.id = {planId}";
            finalSearch = finalSearch.replace("{planId}", searchReportParamDTO.getPlanId()+"");
        }

        finalSearch = finalSearch + " and gr.status = {rstatus}";
        finalSearch = finalSearch.replace("{rstatus}", PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()+"");

        return  "DROP TABLE IF EXISTS #tempDjsFiles;   \n" +
                "DROP TABLE IF EXISTS #tempNtaRecFiles;   \n" +
                "DROP TABLE IF EXISTS #tempNtaPendFiles;   \n" +
                "DROP TABLE IF EXISTS #tempCertFiles;   \n" +
                "DROP TABLE IF EXISTS #tempPepFiles;  \n" +
                "DROP TABLE IF EXISTS #tempExtPrim;  \n" +
                "DROP TABLE IF EXISTS #PregRespDJS;  \n" +
                "DROP TABLE IF EXISTS #tempCobertAsig;  \n" +
                "DROP TABLE IF EXISTS #tempFirmType;  \n" +
                "              \n" +
                "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
                "into #tempDjsFiles from FileDocument fl   \n" +
                "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
                "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId in (1,3)   \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
                "order by typeDocument asc   \n" +
                "              \n" +
                "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
                "into #tempCertFiles from FileDocument fl   \n" +
                "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
                "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId = 4   \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
                "order by typeDocument asc   \n" +
                "              \n" +
                "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
                "into #tempNtaRecFiles from FileDocument fl   \n" +
                "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
                "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId in (6,11)  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
                "order by typeDocument asc   \n" +
                "              \n" +
                "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
                "into #tempNtaPendFiles from FileDocument fl   \n" +
                "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
                "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId = 5  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
                "order by typeDocument asc   \n" +
                "              \n" +
                "select pld.policyItemId, fl.description, pld.uploadDate, pld.isSigned   \n" +
                "into #tempPepFiles from FileDocument fl   \n" +
                "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
                "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId IN (7,10)   \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26   \n" +
                "order by typeDocument asc   \n" +
                "              \n" +
                "select policyItemId,SUM(additionalPremiumPerPercentage) as primaPort,   \n" +
                "SUM(additionalPremiumPerThousand) as primaMil,   \n" +
                "STRING_AGG(cvppi.comment, ' | ') AS comment into #tempExtPrim from CoveragePolicyItem cvppi   \n" +
                "group by policyItemId   \n" +
                " \n" +
                "select STRING_AGG(cv.name, ' | ') AS 'coberturas', grs.id as requestId, pois.id as policyItemId  \n" +
                "into #tempCobertAsig from CoveragePolicyItem cvppi   \n" +
                "join CoverageProductPlan cvpp on cvpp.id = cvppi.coverageProductPlanId   \n" +
                "join CoverageProduct cvp on cvp.id = cvpp.coverageProductId   \n" +
                "join Coverage cv on cv.id = cvp.coverageId   \n" +
                "join PolicyItem pois on pois.id = cvppi.policyItemId   \n" +
                "join GeneralRequest grs on grs.id = pois.generalRequestId   \n" +
                "join (select * from Policy where numberPolicy like '%DHN%') pol on pol.id = pois.policyId \n" +
                "group by grs.id, pois.id \n" +
                " \n" +
                "select IIF(pld.isSigned = 1, 'FIRMA ELECTRÓNICA','FIRMA MANUAL') as tipoFirma, pld.policyItemId \n" +
                "into #tempFirmType from FileDocument fl   \n" +
                "Join PolicyDocument pld on pld.fileDocumentId = fl.id   \n" +
                "join Classifier cl on cl.referenceId = fl.typeDocument and cl.referenceId in (1,3)   \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 26 \n" +
                "order by policyItemId,uploadDate asc \n" +
                "             \n" +
                "select prod.name AS cartera, pl.numberPolicy as poliza,   \n" +
                "gr.createdAt as 'fechaLlenadoDjs',   \n" +
                "gr.creditNumber as 'noOperacion',   \n" +
                "'NUEVO' as 'tipoOperacion', --Este campo se debe actualizar con el desembolso mensual  \n" +
                "gr.requestNumber as 'noSolicitud',  \n" +
                "certN.documentNumber as 'noCertificado', \n" +
                "CONCAT(ntp.name,' ',ntp.lastName,' ', ntp.motherLastName,' ', ntp.marriedLastName) as asegurado,   \n" +
                "genre.description as genero, ntp.identificationNumber as ci, ext.description as ext, convert(date, ntp.birthDate) as 'fechaNac',   \n" +
                "CONVERT(DECIMAL(10,0), ROUND(datediff(DD,ntp.birthDate,GETDATE())/365.25,0,1)) as edad, natio.description as nacionalidad, CONVERT(DECIMAL(10,0),gr.weight) as peso,  \n" +
                "CONVERT(DECIMAL(10,0),gr.height) as estatura,    \n" +
                "workType.description as 'tipoAsegurado',moneyType.description as moneda, gr.currentAmount as 'montoVigente', gr.requestedAmount as 'montoSolicitado',   \n" +
                "gr.accumulatedAmount as 'montoAcumulado', gr.creditTerm as plazaCredito,requestStatus.description as estado,   \n" +
                "IIF(requestStatus.referenceId = 1, gr.pendingReason,'') as 'motivoPendiente', \n" +
                "IIF(requestStatus.referenceId = 6, gr.inactiveComment,'') as 'comentarioInactivacion', \n" +
                "acceptanceReason.description as 'tipoAceptacion', \n" +
                "iif(covpi.primaPort >= 0, covpi.primaPort, 0) as 'porcentExtPrim',   \n" +
                "iif(covpi.comment is not null, covpi.comment, '') as 'motExtPrim',   \n" +
                "IIF(gr.exclusionComment is not null, gr.exclusionComment, '') as 'motExc',   \n" +
                "iif(rejectTp.description is not null, rejectTp.description, '') as 'motRec',   \n" +
                "IIF(gr.rejectedComment is not null, gr.rejectedComment, '') as 'comentRec',   \n" +
                "IIF((LEN(cobert.coberturas)-LEN(REPLACE(cobert.coberturas,'|','')))>1,'COBERTURA TOTAL',cobert.coberturas) as 'cobOtorgada',   \n" +
                "convert(date,IIF(gr.lastModifiedAt <> null, gr.lastModifiedAt, gr.createdAt)) as 'fechaPronunciamiento',   \n" +
                "convert(date,(select TOP 1 tdf.uploadDate from #tempDjsFiles tdf where tdf.policyItemId = poi.id and tdf.isSigned = 1 order by tdf.uploadDate asc)) as 'fechaCargaDjs',   \n" +
                "convert(date,(select TOP 1 tdf.uploadDate from #tempCertFiles tdf where tdf.policyItemId = poi.id order by tdf.uploadDate asc)) as 'fechaCargaCert',   \n" +
                "convert(date,(select TOP 1 tdf.uploadDate from #tempNtaRecFiles tdf where tdf.policyItemId = poi.id order by tdf.uploadDate asc)) as 'fechaCargaNotRec',  \n" +
                "--IIF(pep.status = 1, 'SI', 'NO') as 'clientePep',IIF(pep.status = 1, 'ACTIVO', 'INACTIVO') as 'estadoPep',   \n" +
                "--IIF(pep.status = 1, convert(date,(select TOP 1 tdf.uploadDate from #tempPepFiles tdf where tdf.policyItemId = poi.id order by tdf.uploadDate asc)), null) as 'fechaPronunciamientoPep',  \n" +
                "regional.description as regional, CONCAT(usr.name, ' ', usr.surName) as 'userCreation',  \n" +
                "convert(date,(gr.lastModifiedAt)) as 'fechaOperacion',  \n" +
                "IIF((select TOP 1 COUNT(tdf.uploadDate) from #tempNtaPendFiles tdf where tdf.policyItemId = poi.id) > 0, 'MANUAL', 'AUTOMÁTICA') as 'subsType', \n" +
                "(select top 1 tft.tipoFirma from #tempFirmType tft \n" +
                "where tft.policyItemId = poi.id) as tipoFirma \n" +
                "from NaturalPerson ntp   \n" +
                "join Person p on p.naturalPersonId = ntp.id   \n" +
                "join GeneralRequest gr on gr.personId = p.id   \n" +
                "join PolicyItem poi on poi.generalRequestId = gr.id  \n" +
                "left join #tempCobertAsig cobert on cobert.requestId = gr.id and cobert.policyItemId = poi.id \n" +
                "left join #tempExtPrim covpi on covpi.policyItemId = poi.id   \n" +
                "join Policy pl on pl.id = poi.policyId   \n" +
                "left join (select pl.id as policyId, gr.planId from Classifier cl   \n" +
                "join ClassifierType clt on clt.id = cl.classifierTypeId and clt.referenceId = 41   \n" +
                "join Person p on p.assignedGroupIdc = cl.referenceId   \n" +
                "join GeneralRequest gr on gr.personId = p.id  \n" +
                "join Policy pl on pl.generalRequestId = gr.id  \n" +
                filterGroupAndTaker +
                ") x on x.policyId = pl.id  \n" +
                "left join (select cl.referenceId, cl.description from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 18) ext on ext.referenceId = ntp.extIdc  \n" +
                "left join (select cl.* from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 7) genre on genre.referenceId = ntp.genderIdc  \n" +
                "join [Plan] pln on pln.id = x.planId  \n" +
                "join Product prod on prod.id = pln.productId  \n" +
                "left join (select cl.* from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 15) natio on natio.referenceId = p.nationalityIdc  \n" +
                "left join (select cl.* from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 51) rejectTp on rejectTp.referenceId = gr.rejectedReasonIdc  \n" +
                "left join (select cl.* from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 44) workType on workType.referenceId = ntp.workTypeIdc  \n" +
                "left join (select cl.* from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 11) moneyType on moneyType.referenceId = pl.currencyTypeIdc  \n" +
                "left join (select cl.* from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 17) requestStatus on gr.requestStatusIdc = requestStatus.referenceId  \n" +
                "left join Pep pep on pep.identificationNumber = ntp.identificationNumber   \n" +
                "join Users usr on usr.id = gr.createdBy  \n" +
                "left join (select cl.* from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 9) regional on usr.regionalIdc = regional.referenceId\n" +
                "left join (select p.* from PolicyDocument p\n" +
                "join PolicyItem pi on pi.id = p.policyItemId\n" +
                "where p.documentNumber > 0) certN on certN.policyItemId = poi.id\n" +
                "left join (select cl.* from Classifier cl  \n" +
                "join ClassifierType ct on ct.id = cl.classifierTypeId and ct.referenceId = 52) acceptanceReason on gr.acceptanceReasonIdc = acceptanceReason.referenceId\n" +
                finalSearch + " order by [poliza], [noSolicitud] asc";
    }
}
