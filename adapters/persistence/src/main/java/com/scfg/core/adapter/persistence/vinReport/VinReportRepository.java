package com.scfg.core.adapter.persistence.vinReport;

import com.scfg.core.adapter.persistence.policy.PolicyJpaEntity;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.dto.vin.VinReportFilterDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VinReportRepository extends JpaRepository<PolicyJpaEntity, String> {

    default String getProductionReport(VinReportFilterDTO filterDTO) {

        String finalSearch = " AND p.issuanceDate BETWEEN '{fromDate}' AND '{toDate}'";
        filterDTO.setFromDate(DateUtils.changeHourInDateMorningAndNight(filterDTO.getFromDate(), true, false));
        filterDTO.setToDate(DateUtils.changeHourInDateMorningAndNight(filterDTO.getToDate(), false, true));
        finalSearch = finalSearch.replace("{fromDate}", HelpersMethods.formatStringOnlyDateAndHour(filterDTO.getFromDate()));
        finalSearch = finalSearch.replace("{toDate}", HelpersMethods.formatStringOnlyDateAndHour(filterDTO.getToDate()));

        String filterPolicyStatus = "";
        if (filterDTO.getPolicyStatusIdc() > 0) {
            filterPolicyStatus = " AND p.policyStatusIdc = {policyStatusIdc}";
            filterPolicyStatus = filterPolicyStatus.replace("{policyStatusIdc}", filterDTO.getPolicyStatusIdc() + "");
        }

        return "DECLARE \n" +
                "@CESTADOSOLICITUD AS INT = 17,\n" +
                "@CESTADOPOLIZA AS INT = 25, \n" +
                "@CREG AS INT = 9, \n" +
                "@CMONEDAS AS INT = 11, \n" +
                "@CEXT AS INT = 18, \n" +
                "@CNAC AS INT = 15, \n" +
                "@CGEN AS INT = 7, \n" +
                "@CEC AS INT = 14, \n" +
                "@CPAR AS INT = 13,\n" +
                "@CDIR AS INT = 22,\n" +
                "@CPAG AS INT = 27,\n" +
                "@NITBF AS INT = 1028423022,\n" +
                "@BVIN AS INT = 3,\n" +
                "@PVIN AS INT = 4,\n" +
                "@MAXVY AS INT = 5;\n" +
                "\n" +
                "SELECT @PVIN = p.id FROM [Plan] p INNER JOIN Product p3 ON p3.id = p.productId WHERE p3.agreementCode = 1083;\n" +
                "SELECT @BVIN = b.id FROM Branch b INNER JOIN Product p3 ON b.id = p3.branchId WHERE p3.agreementCode = 1083;\n" +
                "\n" +
                "\n" +
                "DROP TABLE IF EXISTS #regional\n" +
                "SELECT c.description INTO #regional\n" +
                "FROM Classifier c \n" +
                "JOIN ClassifierType ct ON ct.id = c.classifierTypeId\n" +
                "WHERE c.referenceId = 8 AND ct.referenceId = @CREG;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #holderName\n" +
                "SELECT jp.name INTO #holderName\n" +
                "FROM JuridicalPerson jp \n" +
                "JOIN Person pe ON pe.juridicalPersonId = jp.id\n" +
                "WHERE pe.nit = @NITBF; --Obtenido del backend\n" +
                "\n" +
                "DROP TABLE IF EXISTS #direction\n" +
                "SELECT d.personId, d.description AS 'domicilio' INTO #direction\n" +
                "FROM Classifier c \n" +
                "JOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CDIR\n" +
                "JOIN Direction d ON d.directionTypeIdc = c.referenceId\n" +
                "WHERE c.referenceId = 1\n" +
                "\n" +
                "DROP TABLE IF EXISTS #allBeneficiaries\n" +
                "SELECT PolicyItemId, STRING_AGG(x.ben,' | ') AS listBeneficiaries INTO #allBeneficiaries\n" +
                "FROM (SELECT DISTINCT CONCAT('Nombre: ',b.name,' ',b.lastName,' Parentesco: ',c.description,' Porcentaje: ',b.percentage)\n" +
                "AS ben, b.PolicyItemId FROM Beneficiary b\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c \n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CPAR\n" +
                ") c ON c.referenceId = b.relationShipIdc\n" +
                "WHERE b.PolicyItemId IS NOT NULL) AS x\n" +
                "GROUP BY PolicyItemId;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #coveragePlan\n" +
                "SELECT pi2.id, c1.cob1 AS 'c1', c2.cob2 AS 'c2', c3.cob3 AS 'c3' INTO #coveragePlan\n" +
                "FROM PolicyItem pi2 \n" +
                "JOIN (SELECT poi.id as id, cpi.insuredCapital as cob1\n" +
                "FROM CoveragePolicyItem cpi\n" +
                "JOIN PolicyItem poi on poi.id = cpi.policyItemId\n" +
                "JOIN CoverageProductPlan cpl on cpl.id = cpi.coverageProductPlanId\n" +
                "JOIN [Plan] pla on pla.id = cpl.planId\n" +
                "WHERE pla.agreementCode = 1 and cpl.[order] = 1\n" +
                ") as c1 ON c1.id = pi2.id\n" +
                "JOIN (SELECT poi.id as id, cpi.insuredCapital as cob2\n" +
                "FROM CoveragePolicyItem cpi\n" +
                "JOIN PolicyItem poi on poi.id = cpi.policyItemId\n" +
                "JOIN CoverageProductPlan cpl on cpl.id = cpi.coverageProductPlanId\n" +
                "JOIN [Plan] pla on pla.id = cpl.planId\n" +
                "WHERE pla.agreementCode = 1 and cpl.[order] = 2\n" +
                ") as c2 ON c2.id = pi2.id\n" +
                "JOIN (SELECT poi.id as id, cpi.insuredCapital as cob3\n" +
                "FROM CoveragePolicyItem cpi\n" +
                "JOIN PolicyItem poi on poi.id = cpi.policyItemId\n" +
                "JOIN CoverageProductPlan cpl on cpl.id = cpi.coverageProductPlanId\n" +
                "JOIN [Plan] pla on pla.id = cpl.planId\n" +
                "WHERE pla.agreementCode = 1 and cpl.[order] = 3\n" +
                ") as c3 ON c3.id = pi2.id\n" +
                "WHERE pi2.status = 1;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #cedVida\n" +
                "SELECT cp.id, (cp.c1*0.5) AS 'c11',\n" +
                "((cp.c1*0.5)*2.58/1000) * gr.creditTermInYears AS 'c12',\n" +
                "(((cp.c1*0.5)*2.58/1000)*0) AS 'c13' INTO #cedVida\n" +
                "FROM #coveragePlan cp\n" +
                "JOIN PolicyItem pi2 ON pi2.id = cp.id\n" +
                "JOIN Policy p ON p.id = pi2.policyId\n" +
                "JOIN GeneralRequest gr ON gr.id = p.generalRequestId;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #cedIndem\n" +
                "SELECT cp.id, (cp.c2*0.5) AS 'c21',\n" +
                "((cp.c2*0.5)*0.5/1000) * gr.creditTermInYears AS 'c22',\n" +
                "(((cp.c2*0.5)*0.5/1000)*0) AS 'c23' INTO #cedIndem\n" +
                "FROM #coveragePlan cp\n" +
                "JOIN PolicyItem pi2 ON pi2.id = cp.id\n" +
                "JOIN Policy p ON p.id = pi2.policyId\n" +
                "JOIN GeneralRequest gr ON gr.id = p.generalRequestId;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #gastMed\n" +
                "SELECT cp.id, (cp.c3*0.5) AS 'c31',\n" +
                "((cp.c3*0.5)*5.15/1000) * gr.creditTermInYears AS 'c32',\n" +
                "(((cp.c3*0.5)*5.15/1000)*0) AS 'c33' INTO #gastMed\n" +
                "FROM #coveragePlan cp\n" +
                "JOIN PolicyItem pi2 ON pi2.id = cp.id\n" +
                "JOIN Policy p ON p.id = pi2.policyId\n" +
                "JOIN GeneralRequest gr ON gr.id = p.generalRequestId;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #ct\n" +
                "SELECT pi2.id, (c1.c11+c2.c21+c3.c31) AS 'ct1',\n" +
                "(c1.c12+c2.c22+c3.c32) AS 'ct2',\n" +
                "(c1.c13+c2.c23+c3.c33) AS 'ct3' INTO #ct\n" +
                "FROM PolicyItem pi2\n" +
                "JOIN (SELECT * FROM #cedVida) AS c1 ON c1.id = pi2.id\n" +
                "JOIN (SELECT * FROM #cedIndem) AS c2 ON c2.id = pi2.id\n" +
                "JOIN (SELECT * FROM #gastMed) AS c3 ON c3.id = pi2.id;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #paymentType\n" +
                "SELECT c.description INTO #paymentType\n" +
                "FROM Classifier c \n" +
                "JOIN ClassifierType ct ON ct.id = c.classifierTypeId\n" +
                "WHERE c.referenceId = 1 AND ct.referenceId = @CPAG;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #age\n" +
                "SELECT np.id, (DATEDIFF(YEAR, np.birthDate, GETDATE()) - \n" +
                "CASE WHEN DATEADD (YEAR, DATEDIFF(YEAR, np.birthDate, GETDATE()), np.birthDate)> GETDATE()\n" +
                "THEN 1 ELSE 0 END) AS 'age' INTO #age\n" +
                "FROM NaturalPerson np;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #ageActuarial\n" +
                "SELECT p.id, (DATEDIFF(YEAR, np.birthDate, p.issuanceDate) - \n" +
                "CASE WHEN DATEADD (YEAR, DATEDIFF(YEAR, np.birthDate, p.issuanceDate), np.birthDate)> p.issuanceDate\n" +
                "THEN 1 ELSE 0 END) AS 'age' INTO #ageActuarial\n" +
                "FROM Policy p \n" +
                "JOIN PolicyItem pi2 ON p.id = pi2.policyId\n" +
                "JOIN Person p2 ON p2.id = pi2.personId\n" +
                "JOIN NaturalPerson np ON np.id = p2.naturalPersonId;\n" +
                "\n" +
                "DROP TABLE IF EXISTS #mathResv\n" +
                "SELECT pi2.id,\n" +
                "IIF(\n" +
                "(SELECT TOP 1 pmr.value\n" +
                " FROM PolicyItemMathReserve pmr \n" +
                " WHERE pmr.status = 1 AND pmr.policyItemId = pi2.id AND pmr.[year] = YEAR(GETDATE()) - 1) IS NULL, 0, \n" +
                "\t(SELECT TOP 1 pmr.value\n" +
                "\t FROM PolicyItemMathReserve pmr \n" +
                "\t WHERE pmr.status = 1 AND pmr.policyItemId = pi2.id AND pmr.[year] = YEAR(GETDATE()) - 1)\n" +
                ") AS  'gestAnt',\n" +
                "IIF(\n" +
                "(SELECT TOP 1 pmr.value\n" +
                " FROM PolicyItemMathReserve pmr \n" +
                " WHERE pmr.status = 1 AND pmr.policyItemId = pi2.id AND pmr.[year] = YEAR(GETDATE())) IS NULL, 0, \n" +
                "\t(SELECT TOP 1 pmr.value\n" +
                "\t FROM PolicyItemMathReserve pmr \n" +
                "\t WHERE pmr.status = 1 AND pmr.policyItemId = pi2.id AND pmr.[year] = YEAR(GETDATE()))\n" +
                ") AS  'gestAct' INTO #mathResv\n" +
                "FROM PolicyItem pi2 \n" +
                "WHERE pi2.status = 1\n" +
                "GROUP BY pi2.id\n" +
                "ORDER BY pi2.id DESC;\n" +
                "\n" +
                "\n" +
                "SELECT p.numberPolicy AS 'poliza', YEAR(p.issuanceDate) AS 'año', MONTH(p.issuanceDate) AS 'mes', CAST(p.issuanceDate AS DATE) AS 'fecha',\n" +
                "IIF(t.amount <= 0, 'ANULACION/RESCATADA', 'PRODUCCION') AS 'Tipo de Movimiento',\n" +
                "IIF(GETDATE() BETWEEN p.fromDate AND p.toDate, 'NUEVA', 'VENCIDA') AS 'SubTipo de Movimiento',\n" +
                "clm.description AS 'moneda', (SELECT * FROM #regional) AS 'regional', b.name AS 'ramo', \n" +
                "pr.name AS 'producto', pl.name AS 'plan', \n" +
                "IIF(np.complement != '', CONCAT_WS('-', np.identificationNumber, np.complement), np.identificationNumber) AS 'codCliente',\n" +
                "(SELECT * FROM #holderName) AS 'tomador', \n" +
                "CONCAT_WS(' ', np.name, np.lastName, np.motherLastName) AS 'asegurado', np.profession AS 'actividad', np.identificationNumber AS 'nroIdentificacion', \n" +
                "cle.description AS 'extension', cln.description AS 'nacionalidad', CAST(np.birthDate AS DATE) AS 'fechaNacimiento', \n" +
                "(SELECT a.age FROM #age a WHERE a.id = np.id) AS 'edad',\n" +
                "clg.description AS 'genero', clec.description AS 'estadoCivil',\n" +
                "(SELECT TOP 1 dr.domicilio FROM #direction dr WHERE dr.personId = pe.id) AS 'direccion',\n" +
                "(SELECT ben.listBeneficiaries FROM #allBeneficiaries ben WHERE ben.PolicyItemId = poi.id) AS 'beneficiario', \n" +
                "CAST(p.fromDate AS DATE) AS 'fechaInicioVigencia',\n" +
                "CAST(p.toDate AS DATE) AS 'fechaFinVigencia', gr.creditTermInYears AS 'años', \n" +
                "(SELECT c1 FROM #coveragePlan WHERE poi.id = id) AS 'cbCapitalVida', (SELECT c2 FROM #coveragePlan WHERE poi.id = id) AS 'cbIndemnizacionMuerte', (SELECT c3 FROM #coveragePlan WHERE poi.id = id) AS 'cbGastosMedicos', \n" +
                "poi.individualInsuredCapital AS 'capTotal', ROUND(p.totalPremium/gr.creditTermInYears, 2) AS 'prmAnual', p.totalPremium AS 'prmTotal', \n" +
                "poi.individualNetPremium AS 'prmNeta', poi.individualAdditionalPremium AS 'prmAdicional', \n" +
                "poi.individualRiskPremium AS 'prmPuraRiesgo', poi.APS AS 'APS', poi.FPA AS 'FPA', \n" +
                "(SELECT * FROM #holderName) AS 'servicioCobranza', poi.individualCollectionServiceCommission AS 'comisionIndividual$',\n" +
                "'50%' AS 'comisionIndividual%',\n" +
                "br.businessName AS 'Intermediario', poi.individualIntermediaryCommission AS 'comisionIndividual$Br', \n" +
                "CONCAT((p.intermediaryCommissionPercentage * 100),'%') AS 'comisionIndividual%Br',\n" +
                "'' AS 'comprobante', clep.description AS 'estado',\n" +
                "IIF(pp.annexeId IS NULL AND t.amount <= 0, 'ERROR DE SISTEMA', '') AS 'Observacion',\n" +
                "(SELECT c11 FROM #cedVida WHERE poi.id = id) AS 'capAsegCedidoVida', (SELECT c12 FROM #cedVida WHERE poi.id = id) AS 'prmCedidaVida', (SELECT c13 FROM #cedVida WHERE poi.id = id) AS 'impRemesasVida',\n" +
                "(SELECT c21 FROM #cedIndem WHERE poi.id = id) AS 'capAsegCedidoIndem', (SELECT c22 FROM #cedIndem WHERE poi.id = id) AS 'prmCedidaIndem', (SELECT c23 FROM #cedIndem WHERE poi.id = id) AS 'impRemesasIndem',\n" +
                "(SELECT c31 FROM #gastMed WHERE poi.id = id) AS 'CACedidoGastMed', (SELECT c32 FROM #gastMed WHERE poi.id = id) AS 'prmCedidaGM', (SELECT c33 FROM #gastMed WHERE poi.id = id) AS 'impRemesasGM',\n" +
                "(SELECT ct1 FROM #ct WHERE poi.id = id) AS 'capCedidoTotal', (SELECT ct2 FROM #ct WHERE poi.id = id) AS 'prmCedidaTotal', (SELECT ct3 FROM #ct WHERE poi.id = id) AS 'IREtotal',\n" +
                "(poi.individualNetPremium - (SELECT ct2 FROM #ct WHERE poi.id = id)) AS 'prmNetaRetenida', \n" +
                "(SELECT aact.age FROM #ageActuarial aact WHERE aact.id = p.id) AS 'edadActFechaCorte', 0 AS 'edadMancomFechaCorte', '' AS 'fumador', '' AS 'capSaldado', \n" +
                "(SELECT * FROM #paymentType) AS 'formaPago', (SELECT DATEDIFF(MONTH, p.issuanceDate, GETDATE())) AS 'tiempoIncurridoVigencia',\n" +
                "(SELECT TOP 1 mr.gestAnt FROM #mathResv mr WHERE mr.id = poi.id) AS 'reservaMatGestAnt', \n" +
                "(SELECT TOP 1 mr.gestAct FROM #mathResv mr WHERE mr.id = poi.id) AS 'reservaMatGestAct', '' AS 'reservaMatSegurosCompl',\n" +
                "'' AS 'saldoCuentaIndv', 0 AS '%deAhorro', CONCAT((SELECT TOP 1 mrev.percentageRate FROM MathReserve mrev),'%') AS 'tasaInteresFinanciera', \n" +
                "(0+0+(SELECT TOP 1 mr.gestAct FROM #mathResv mr WHERE mr.id = poi.id)) AS 'reservaMatFinanciera'\n" +
                "\n" +
                "\n" +
                "FROM Policy p\n" +
                "LEFT JOIN PolicyItem poi ON poi.policyId = p.id\n" +
                "JOIN GeneralRequest gr ON gr.id = p.generalRequestId\n" +
                "JOIN Person pe ON pe.id = gr.personId\n" +
                "JOIN NaturalPerson np ON np.id = pe.naturalPersonId\n" +
                "JOIN Product pr ON pr.id = p.productId\n" +
                "JOIN Branch b ON b.id = pr.branchId AND b.id = @BVIN\n" +
                "JOIN [Plan] pl ON pl.id = gr.planId AND pl.id = @PVIN\n" +
                "LEFT JOIN Broker br ON br.id = p.brokerId\n" +
                "INNER JOIN Payment py ON py.generalRequestId = gr.id\n" +
                "INNER JOIN PaymentPlan pp ON pp.paymentId  = py.id\n" +
                "INNER JOIN [Transaction] t ON t.paymentPlanId = pp.id\n" +
                "\n" +
                "\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c \n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CESTADOSOLICITUD\n" +
                ") AS cles ON cles.referenceId = gr.requestStatusIdc\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c \n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CESTADOPOLIZA\n" +
                ") AS clep ON clep.referenceId = p.policyStatusIdc\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c\n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CMONEDAS\n" +
                ") AS clm ON clm.referenceId = p.currencyTypeIdc\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c\n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CEXT\n" +
                ") AS cle ON cle.referenceId = np.extIdc\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c \n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CNAC\n" +
                ") AS cln ON cln.referenceId = pe.nationalityIdc\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c \n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CGEN\n" +
                ") AS clg ON clg.referenceId = np.genderIdc\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c \n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = @CEC\n" +
                ") AS clec ON clec.referenceId = np.maritalStatusIdc\n" +
                "WHERE p.policyStatusIdc != 0 AND p.status != 0 AND gr.status != 0 AND poi.status != 0\n" +
                "AND py.status != 0 AND pp.status != 0 AND t.status != 0\n" +
                finalSearch + filterPolicyStatus +
                "\nORDER BY p.numberPolicy ASC";
    }


    default String getCommercialReport(VinReportFilterDTO filterDTO) {

        String finalSearch = " AND p.issuanceDate BETWEEN '{fromDate}' and '{toDate}'";
        filterDTO.setFromDate(DateUtils.changeHourInDateMorningAndNight(filterDTO.getFromDate(), true, false));
        filterDTO.setToDate(DateUtils.changeHourInDateMorningAndNight(filterDTO.getToDate(), false, true));
        finalSearch = finalSearch.replace("{fromDate}", HelpersMethods.formatStringOnlyDateAndHour(filterDTO.getFromDate()));
        finalSearch = finalSearch.replace("{toDate}", HelpersMethods.formatStringOnlyDateAndHour(filterDTO.getToDate()));

        String filterPolicyStatus = "";
        if (filterDTO.getPolicyStatusIdc() > 0) {
            filterPolicyStatus = " AND p.policyStatusIdc = {policyStatusIdc}";
            filterPolicyStatus = filterPolicyStatus.replace("{policyStatusIdc}", filterDTO.getPolicyStatusIdc() + "");
        }

        return "DECLARE\n" +
                "@bancoFassilNit as int = 1028423022,\n" +
                "@agreementCode as int = 1,\n" +
                "@productAgreementCode as int = 1083,\n" +
                "@ext as int = 18,\n" +
                "@nacionality as int = 15,\n" +
                "@gender as int = 7,\n" +
                "@direction as int = 22,\n" +
                "@maritalStatus as int = 14,\n" +
                "@statusPolicy as int = 25\n" +
                "\n" +
                "drop table if exists #tempCurrency\n" +
                "select description as moneda, referenceId\n" +
                "into #tempCurrency\n" +
                "from Classifier where classifierTypeId=(select id from ClassifierType where referenceId=11)\n" +
                "\n" +
                "drop table if exists #tempAcceptMsg\n" +
                "select gr.id as requestId, mr.createdAt as fechaCreacion \n" +
                "into #tempAcceptMsg\n" +
                "from GeneralRequest gr \n" +
                "join MessageSent ms on ms.referenceId = gr.id and ms.referenceTableIdc = 1\n" +
                "join MessageResponse mr on mr.messageSentId = ms.id\n" +
                "\n" +
                "SELECT gr.requestNumber AS 'nroPropuesta', CAST(gr.requestDate AS DATE) AS 'fechaPropuesta',\n" +
                "CAST(messa.fechaCreacion AS DATE) AS 'fechaAceptPropuesta',\n" +
                "IIF(messa.fechaCreacion IS NOT NULL, DATEDIFF(DAY, gr.requestDate, messa.fechaCreacion), DATEDIFF(DAY, gr.requestDate, GETDATE())) AS 'diasTranscPropuesta', \n" +
                "cles.description AS 'estado',\n" +
                "IIF(p.policyStatusIdc <> 0, p.numberPolicy, '') AS poliza,  \n" +
                "IIF(p.policyStatusIdc <> 0, DATEPART(MM, fromDate), NULL) AS mes,\n" +
                "IIF(p.policyStatusIdc <> 0, IIF(t.amount <= 0, 'ANULACION/RESCATADA', 'PRODUCCION'), '') AS 'Tipo de Movimiento',\n" +
                "IIF(p.policyStatusIdc <> 0, CAST(p.fromDate AS DATE), NULL) AS fecha,\n" +
                "IIF(p.policyStatusIdc <> 0, moneda.moneda, '') AS moneda,\n" +
                "'Santa Cruz' as regionalSCVS,\n" +
                "bbrof.description AS sucursalBFS, bzo.description AS zonaBFS, bagen.description AS agenciaBFS, \n" +
                "CONCAT(u.name,' ',u.surName) AS gestorNegBFS, u.bfsUserCode AS codGestNegBFS,\n" +
                "b.name as ramo, prod.name as producto, pl.name as 'plan',\n" +
                "IIF(np.complement != '', CONCAT_WS('-', np.identificationNumber, np.complement), np.identificationNumber) AS 'codCliente',\n" +
                "CONCAT(np.name,' ',np.lastName,' ',np.motherLastName) as TomadorAs,\n" +
                "np.profession as actividad, CONCAT(np.name,' ',np.lastName,' ',np.motherLastName) as asegurado,\n" +
                "IIF(np.complement != '', CONCAT_WS('-', np.identificationNumber, np.complement), np.identificationNumber) AS NroIdentificacion,\n" +
                "nac.description as nacionalidad,\n" +
                "CAST(np.birthDate AS DATE) as fechNacimiento,\n" +
                "CONVERT(DECIMAL(10,0),ROUND(datediff(DD,np.birthDate, p.issuanceDate)/365.25,0,1)) as edad,\n" +
                "gen.description as genero,\n" +
                "(\n" +
                "select top 1 d.description  from Classifier c\n" +
                "join ClassifierType ct on ct.id = c.classifierTypeId and ct.referenceId = @direction\n" +
                "join Direction d on d.directionTypeIdc = c.referenceId\n" +
                "where d.personId = per.id\n" +
                "order by d.personId desc,d.id desc\n" +
                ")\n" +
                "as direccion,\n" +
                "ms.description as estadoCivil, per.email as correoElectronico, per.telephone as numTelefono,\n" +
                "CAST(p.issuanceDate AS DATE) as fechaEmision,\n" +
                "CAST(p.fromDate AS DATE) as vigenciaInicial,\n" +
                "CAST(p.toDate AS DATE) as vigenciaFinal,\n" +
                "gr.creditTermInYears as cantidadAnhos, icapital.insuredCapital as capitalVida, imuerte.insuredCapital as idemnizacionMuerteAccidental,\n" +
                "gmedicos.insuredCapital as gastosMedicos, pitem.individualInsuredCapital as capitalAseguradoTotal, p.totalPremium as primaTotal,\n" +
                "(select jp.name from Person p join JuridicalPerson jp on jp.id = p.juridicalPersonId\n" +
                "where p.nit = @bancoFassilNit\n" +
                ") as Tomador,\n" +
                "--pitem.individualCollectionServiceCommission as servicioCobranza,\n" +
                "--(p.totalPremium-pitem.individualCollectionServiceCommission) as primaCompanhia, \n" +
                "spolicy.description as estadoPoliza\n" +
                "from Policy p\n" +
                "join #tempCurrency as moneda on moneda.referenceId = p.currencyTypeIdc\n" +
                "join GeneralRequest gr on gr.id = p.generalRequestId\n" +
                "join [Plan] pl on pl.id = gr.planId\n" +
                "join Product prod on prod.id = pl.productId\n" +
                "join Branch b on b.id = prod.branchId\n" +
                "join PolicyItem pitem on pitem.policyId = p.id\n" +
                "join Person per on per.id = pitem.personId\n" +
                "join NaturalPerson np on np.id = per.naturalPersonId\n" +
                "LEFT JOIN users u ON p.createdBy = u.id \n" +
                "LEFT JOIN bankAgency bagen ON u.bfsAgencyCode = bagen.agencyId \n" +
                "LEFT JOIN bankBranchOffice bbrof ON bbrof.branchOfficeId = bagen.branchOfficeId \n" +
                "LEFT JOIN bankZones bzo ON bzo.id = bagen.zonesId\n" +
                "LEFT JOIN Payment py ON py.generalRequestId = gr.id\n" +
                "LEFT JOIN PaymentPlan pp ON pp.paymentId  = py.id\n" +
                "LEFT JOIN [Transaction] t ON t.paymentPlanId = pp.id\n" +
                "left join (\n" +
                "select c.abbreviation, c.referenceId from Classifier c\n" +
                "join ClassifierType ct on ct.id = c.classifierTypeId and ct.referenceId = @ext\n" +
                ") as ext on ext.referenceId = np.extIdc\n" +
                "left join (\n" +
                "select c.description, c.referenceId from Classifier c\n" +
                "join ClassifierType ct on ct.id = c.classifierTypeId and ct.referenceId = @nacionality\n" +
                ") as nac on nac.referenceId = per.nationalityIdc\n" +
                "left join (\n" +
                "select c.description, c.referenceId from Classifier c\n" +
                "join ClassifierType ct on ct.id = c.classifierTypeId and ct.referenceId = @gender\n" +
                ") as gen on gen.referenceId = np.genderIdc\n" +
                "left join (\n" +
                "select c.description, c.referenceId from Classifier c\n" +
                "join ClassifierType ct on ct.id = c.classifierTypeId and ct.referenceId = @maritalStatus\n" +
                ") as ms on ms.referenceId = np.maritalStatusIdc\n" +
                "left join (\n" +
                "SELECT c.name, cpi.insuredCapital, pl.id as policyItemId, cpl.[order]\n" +
                "FROM CoveragePolicyItem cpi\n" +
                "JOIN PolicyItem pl on pl.id = cpi.policyItemId\n" +
                "JOIN CoverageProductPlan cpl on cpl.id = cpi.coverageProductPlanId\n" +
                "JOIN [Plan] pla on pla.id = cpl.planId\n" +
                "JOIN CoverageProduct cp on cp.id = cpl.coverageProductId\n" +
                "JOIN Coverage c on c.id = cp.coverageId\n" +
                "WHERE pla.agreementCode = @agreementCode and cpl.[order] = 1\n" +
                ") as icapital on icapital.policyItemId = pitem.id\n" +
                "left join (\n" +
                "SELECT c.name, cpi.insuredCapital, pl.id as policyItemId, cpl.[order]\n" +
                "FROM CoveragePolicyItem cpi\n" +
                "JOIN PolicyItem pl on pl.id = cpi.policyItemId\n" +
                "JOIN CoverageProductPlan cpl on cpl.id = cpi.coverageProductPlanId\n" +
                "JOIN [Plan] pla on pla.id = cpl.planId\n" +
                "JOIN CoverageProduct cp on cp.id = cpl.coverageProductId\n" +
                "JOIN Coverage c on c.id = cp.coverageId\n" +
                "WHERE pla.agreementCode = @agreementCode and cpl.[order] = 2\n" +
                ") as imuerte on imuerte.policyItemId = pitem.id\n" +
                "left join (\n" +
                "SELECT c.name, cpi.insuredCapital, pl.id as policyItemId, cpl.[order]\n" +
                "FROM CoveragePolicyItem cpi\n" +
                "JOIN PolicyItem pl on pl.id = cpi.policyItemId\n" +
                "JOIN CoverageProductPlan cpl on cpl.id = cpi.coverageProductPlanId\n" +
                "JOIN [Plan] pla on pla.id = cpl.planId\n" +
                "JOIN CoverageProduct cp on cp.id = cpl.coverageProductId\n" +
                "JOIN Coverage c on c.id = cp.coverageId\n" +
                "WHERE pla.agreementCode = @agreementCode and cpl.[order] = 3\n" +
                ") as gmedicos on gmedicos.policyItemId = pitem.id\n" +
                "left join (\n" +
                "select c.description, c.referenceId from Classifier c\n" +
                "join ClassifierType ct  on ct.id = c.classifierTypeId\n" +
                "where ct.referenceId = @statusPolicy\n" +
                ") as spolicy on spolicy.referenceId = p.policyStatusIdc\n" +
                "LEFT JOIN (\n" +
                "\tSELECT c.* FROM Classifier c \n" +
                "\tJOIN ClassifierType ct ON ct.id = c.classifierTypeId AND ct.referenceId = 17\n" +
                ") AS cles ON cles.referenceId = gr.requestStatusIdc\n" +
                "LEFT JOIN #tempAcceptMsg messa ON messa.requestId = gr.id \n" +
                "where pl.agreementCode = @agreementCode and prod.agreementCode = @productAgreementCode \n" +
                "AND p.status != 0 AND pitem.status != 0 AND gr.status != 0\n" +
                "AND (py.status != 0 AND pp.status != 0 AND t.status != 0\n" +
                "OR py.status IS NULL AND pp.status IS NULL AND t.status IS NULL) \n" +
                finalSearch + filterPolicyStatus +
                "\nORDER BY gr.requestNumber ASC";
    }
}
