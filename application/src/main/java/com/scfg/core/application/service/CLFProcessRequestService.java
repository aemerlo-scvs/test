package com.scfg.core.application.service;

import com.scfg.core.application.port.in.CLFProcessRequestUseCase;
import com.scfg.core.application.port.in.GeneratePdfUseCase;
import com.scfg.core.application.port.out.*;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoveragePolicyItemPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.CoverageProductPlanPort;
import com.scfg.core.application.port.out.mortgageReliefValidations.PolicyItemPort;
import com.scfg.core.common.UseCase;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.*;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.common.User;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.*;
import com.scfg.core.domain.dto.credicasas.groupthefont.*;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.AnswerDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.RequestFontDTO;
import com.scfg.core.domain.person.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@UseCase
@Slf4j
@RequiredArgsConstructor
public class CLFProcessRequestService implements CLFProcessRequestUseCase {

    private final ClassifierPort classifierPort;
    private final PersonPort personPort;
    private final DirectionPort directionPort;
    private final GeneralRequestPort generalRequestPort;
    private final PolicyItemPort policyItemPort;
    private final PolicyPort policyPort;
    private final BeneficiaryPort beneficiaryPort;
    private final PolicyFileDocumentPort policyFileDocumentPort;
    private final FileDocumentPort fileDocumentPort;
    private final GeneratePdfUseCase generatePdfUseCase;
    private final AnswerQuestionnaireRequestPort answerQuestionnaireRequestPort;
    private final SequencePolicyPort sequencePolicyPort;
    private final DocumentPort documentPort;
    private final CoverageProductPlanPort coverageProductPlanPort;
    private final CoveragePolicyItemPort coveragePolicyItemPort;
    private final RequirementsTablePort requirementsTablePort;
    private final RelationRequirementsListPort relationRequirementsListPort;
    private final RequirementControlPort requirementControlPort;
    private final BrokerPort brokerPort;
    private final QuestionPort questionPort;
    private final UserPort userPort;
    private final SequenceCitePort sequenceCitePort;
    private final AlertPort alertPort;
    private final EmailService emailService;
    private final ProductCalculationsService productCalculationsService;

    //#region Settings de campos requeridos
    double pepClientCumulusLimit = 12000; //This is in dollars ($)
    double limitStartAge = 17.99; //First Year
    double limitFinishAge = 65.00; //Maximum Year
    double minimumImc = 17.0;
    double maxImc = 35.00;
    double limitImc = 39.90; //PENDING STATUS; -- MORE THAN 40 EQUALS REJECT
    int currencyType = 2; //USD
    double totalPremiumRate = 0; // TASA PARA COBERTURA PRINCIPAL
    double pendingCumulusAmount = 100000; // MONTO ACUMULADO A SUPERAR PARA ESTAR EN PENDIENTE
    //#endregion

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, OperationException.class})
    @Override
    public ClfProcessRequestDTO processRequest(RequestFontDTO requestFontDTO) {
        try {
            double individualPremium = 0.0;
            int enumStatus = RequestStatusEnum.FINALIZED.getValue();
            String messageResponse = "";
            String rejectionComment;
            int rejectionIdc = 0;
            int acceptanceIdc = 0;
            int legalHeirs = requestFontDTO.getLegalHeirs() == null ? 0 : requestFontDTO.getLegalHeirs();
            String answersQuestions = getQuestionsWithAnswers(requestFontDTO.getAnswers());
            double insuredAge = requestFontDTO.getPerson().getAge();
            double insuredRequestAmount = requestFontDTO.getCredit().getAccumulatedAmount();
            int lastDJSQuestion = 33;
            long policyId = requestFontDTO.getCompany().getPolicyDto().get(0).getId();
            User userSess = userPort.findById(requestFontDTO.getUserId());
            String userInSessionName = userSess.getCompleteName();
            GeneralRequest getRequest = generalRequestPort.getGeneralRequest(requestFontDTO.getCompany().getPolicyDto().get(0).getGeneralRequestId());
            List<RelationRequirements> relationRequirements = new ArrayList<>();
            List<String> rejectList = new ArrayList<>();
            List<String> pendingList = new ArrayList<>();
            List<FilesRegisterDTO> files = new ArrayList<>();
            List<RequirementControlDTO> requirementControlDTOSAux = new ArrayList<>();
            List<QuestionDTO> questionDTOList = questionPort.getAllQuestion();
            List<Classifier> relationshipClassifierList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Relationship.getReferenceId());
            List<Classifier> maritalStatusClassifierList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.MaritalStatus.getReferenceId());
            List<Classifier> documentTypeExtensionsClassifierList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.ExtensionsDocumentType.getReferenceId());
            List<Classifier> activitiesClassifierList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Activity.getReferenceId());
            List<Classifier> nationalitiesClassifierList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Country.getReferenceId());
            List<Classifier> regionalsClassifierList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Regional.getReferenceId());
            List<Classifier> rejectionTypesClassifierList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.rejectType.getReferenceId());
            List<Classifier> requirementsListClassifiers = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.requirementType.getReferenceId());
            List<CoverageClf> coverageProductPlanList = coverageProductPlanPort
                    .getAllCoverageClf(requestFontDTO.getCompany().getPolicyDto().get(0).getGeneralRequestId());
            List<AnswerDTO> questionListNotNull = requestFontDTO.getAnswers().stream().filter(x -> (x.getAffirmativeAnswer() != null)).collect(Collectors.toList());
            List<AnswerDTO> questionList = questionListNotNull.stream().filter(x -> (x.getAffirmativeAnswer() && x.getQuestionId() != lastDJSQuestion)).collect(Collectors.toList());
            List<AnswerDTO> questionListOnlyLastQuestion = questionListNotNull.stream().filter(x -> x.getQuestionId() == lastDJSQuestion && !x.getAffirmativeAnswer()).collect(Collectors.toList());
            questionList.addAll(questionListOnlyLastQuestion);

            // All validations are made in the following "if´s" to determinate the request status
            if (insuredAge <= limitStartAge && insuredAge > limitFinishAge) {
                rejectionComment = rejectionTypesClassifierList.stream()
                        .filter(x -> x.getReferenceId().equals(ClassifierEnum.REJECT_LIMITAGE.getReferenceCode())).findFirst().get().getDescription();
                rejectionIdc = (int) ClassifierEnum.REJECT_LIMITAGE.getReferenceCode();
                rejectList.add(rejectionComment);
                enumStatus = RequestStatusEnum.REJECTED.getValue();
            }

            if (requestFontDTO.getAcceptanceCriteria().getImc() > maxImc && requestFontDTO.getAcceptanceCriteria().getImc() <= limitImc) {
                enumStatus = RequestStatusEnum.PENDING.getValue();
                String pendingImc = PendingTypeEnum.PENDING_FOR_IMC.getAction();
                pendingList.add(pendingImc.toUpperCase());
            }

            if (requestFontDTO.getAcceptanceCriteria().getImc() < minimumImc) {
                rejectionComment = rejectionTypesClassifierList.stream()
                        .filter(x -> x.getReferenceId().equals(ClassifierEnum.REJECT_IMC_LESS.getReferenceCode())).findFirst().get().getDescription();
                rejectionIdc = (int) ClassifierEnum.REJECT_IMC_LESS.getReferenceCode();
                rejectList.add(rejectionComment);
                enumStatus = RequestStatusEnum.REJECTED.getValue();
            }

            if (requestFontDTO.getAcceptanceCriteria().getImc() > limitImc) {
                rejectionComment = rejectionTypesClassifierList.stream().filter(x -> x.getReferenceId().equals(ClassifierEnum.REJECT_IMC.getReferenceCode())).findFirst().get().getDescription();
                rejectionIdc = (int) ClassifierEnum.REJECT_IMC.getReferenceCode();
                rejectList.add(rejectionComment);
                enumStatus = RequestStatusEnum.REJECTED.getValue();
            }

            if (insuredRequestAmount > pendingCumulusAmount && enumStatus != RequestStatusEnum.REJECTED.getValue()) {
                enumStatus = RequestStatusEnum.PENDING.getValue();
                pendingList.add(PendingTypeEnum.PENDING_FOR_CUMULUS.getAction());
            }

            if (enumStatus != RequestStatusEnum.REJECTED.getValue() && requestFontDTO.getAcceptanceCriteria().getWasRejected()) {
                enumStatus = RequestStatusEnum.PENDING.getValue();
                pendingList.add(PendingTypeEnum.PENDING_FOR_HAVE_OBS_COMPANY_REQUEST.getAction());
            }

            if (requestFontDTO.getAcceptanceCriteria().getIsPEP() && enumStatus == RequestStatusEnum.FINALIZED.getValue()
                    && insuredRequestAmount > pepClientCumulusLimit) {
                enumStatus = RequestStatusEnum.PENDING.getValue();
                pendingList.add(PendingTypeEnum.PENDING_FOR_CUMULUS_PEP.getAction());
            }

            if (questionList.size() > 0 && enumStatus != CLFResponseEnum.REJECTED.getValue()) {
                enumStatus = RequestStatusEnum.PENDING.getValue();
                pendingList.add(PendingTypeEnum.PENDING_FOR_AFFIRMATIVE_DJS.getAction());
            }

            // If the request isn't rejected then the system proceed to get the requirements table
            if (enumStatus != RequestStatusEnum.REJECTED.getValue()) {
                List<RequirementsTable> requirementsTables = requirementsTablePort
                        .getCoverageByGeneralRequestId(requestFontDTO.getCompany().getPolicyDto().get(0).getGeneralRequestId());

                requirementsTables = requirementsTables.stream().filter(x -> (insuredAge >= x.getStartAge() && x.getFinishAge() >= insuredAge)
                        && (x.getInitialAmount() <= insuredRequestAmount && x.getFinalAmount() > insuredRequestAmount)).collect(Collectors.toList());

                relationRequirements = relationRequirementsListPort.getAllRelationRequirementListByRequirementTableId(requirementsTables.get(0).getId());
            }

            // Insured direction and sign registration
            Person insured = requestFontDTO.getPerson();
            insured.setId(personPort.saveOrUpdate(insured));
            requestFontDTO.getPerson().getDirections().forEach(direction -> {
                direction.setPersonId(insured.getId());
            });
            directionPort.deletePersonalAndWorkDirectionByPersonId(insured.getId());
            directionPort.saveAllDirection(requestFontDTO.getPerson().getDirections());

            if (requestFontDTO.getSign() != null) {
                documentPort.saveOrUpdate(requestFontDTO.getSign(), insured.getId());
            }

            // Setting the request number to the request
            SequencePolicy sequencePolicy = sequencePolicyPort.getSequence(policyId);
            GeneralRequest generalRequest = new GeneralRequest(requestFontDTO, insured, enumStatus,
                    getRequest.getPlanId(), acceptanceIdc, rejectionIdc, String.join(" | ", pendingList), "",
                    String.join(" | ", rejectList), legalHeirs);

            if (sequencePolicy == null) {
                sequencePolicy = SequencePolicy.builder()
                        .requestNumber(1L)
                        .policyId(policyId)
                        .coverageCertificateNumber(0L)
                        .build();
                sequencePolicy.setId(sequencePolicyPort.saveOrUpdate(sequencePolicy));
            } else {
                sequencePolicy.setRequestNumber(sequencePolicy.getRequestNumber() + 1);
            }
            generalRequest.setRequestNumber(sequencePolicy.getRequestNumber().intValue());

            // Calculating the coverages total rate if the request isn't pending or rejected
            if (generalRequest.getRequestStatusIdc().equals(RequestStatusEnum.FINALIZED.getValue())) {
                sequencePolicy.setCoverageCertificateNumber(sequencePolicy.
                        getCoverageCertificateNumber() == null ? 1 : sequencePolicy.getCoverageCertificateNumber() + 1);
                List<CoverageClf> coverageProductPlanListFiltered = new ArrayList<>(coverageProductPlanList);
                totalPremiumRate = getCoverageRate(coverageProductPlanListFiltered, ClassifierEnum.COV_ALTERN.getReferenceCode());
                generalRequest.setAcceptanceReasonIdc((int) ClassifierEnum.ACCEPT_NORMAL.getReferenceCode());
            }
            // Saving the request, the sequence policy and the policy item
            long requestId = generalRequestPort.saveOrUpdate(generalRequest);
            sequencePolicyPort.saveOrUpdate(sequencePolicy);

            PolicyItem policyItem = new PolicyItem(requestFontDTO, insured.getId(), requestId, totalPremiumRate);
            PolicyItem policyItemSaved = policyItemPort.saveOrUpdate(productCalculationsService.calculateVariables(policyItem));

            // Obtaining the requirements. If there are more than 1 requirement the request is set to the pending status
            if (enumStatus == RequestStatusEnum.FINALIZED.getValue() || enumStatus == RequestStatusEnum.PENDING.getValue()) {
                List<RequirementControlDTO> requirementControlDTOS = new ArrayList<>();
                relationRequirements.forEach(x -> {
                    requirementControlDTOS.add(new RequirementControlDTO(x, requirementsListClassifiers, policyItemSaved.getId()));
                });
                requirementControlDTOSAux = requirementControlPort.saveOrUpdateAll(requirementControlDTOS);
                if (requirementControlDTOSAux.size() > 1) {
                    enumStatus = RequestStatusEnum.PENDING.getValue();
                    pendingList.add(PendingTypeEnum.PENDING_FOR_MEDIC_REQUIREMENTS.getAction());
                    generalRequest.setRequestStatusIdc(enumStatus);
                    generalRequest.setId(requestId);
                    generalRequest.setPendingReason(String.join(" | ", pendingList));
                    generalRequestPort.saveOrUpdate(generalRequest);
                }
            }

            // If the legar heirs option isn't selected the beneficiaries are saved
            if (legalHeirs == 0) {
                requestFontDTO.getBeneficiaries().forEach(e -> {
                    e.setPolicyItemId(policyItemSaved.getId());
                    e.setPolicyId(policyId);
                });
                beneficiaryPort.saveAllOrUpdateCLF(requestFontDTO.getBeneficiaries());
            }

            // Saving the answers from the DJS
            requestFontDTO.getAnswers().forEach(e -> {
                e.setGeneralRequestId(requestId);
            });
            answerQuestionnaireRequestPort.saveAllOrUpdateAnswers(requestFontDTO.getAnswers());

            //Defining the insured identification number to show in all the documents to download
            FileDocumentDTO djsFileDocument = new FileDocumentDTO();
            String complementCi = insured.getNaturalPerson().getComplement();
            String CINumber = (complementCi == null || complementCi.trim().isEmpty()) ?
                    insured.getNaturalPerson().getIdentificationNumber() :
                    insured.getNaturalPerson().getIdentificationNumber() + "-" + insured.getNaturalPerson().getComplement();
            Classifier agentRegional = regionalsClassifierList.stream().filter(x -> x.getReferenceId().equals(requestFontDTO.getUserRegionalIdc()))
                    .findFirst().orElse(null);

            // Generating the pending letter
            if (enumStatus == RequestStatusEnum.PENDING.getValue()) {
                // Obtaining or creating the SequenceCite register
                SequenceCite citeCompany = sequenceCitePort.getSequence(userSess.getCompanyIdc(), HelpersMethods.getActualYear());
                if (citeCompany == null) {
                    citeCompany = SequenceCite.builder()
                            .companyIdc(userSess.getCompanyIdc())
                            .year(HelpersMethods.getActualYear())
                            .citeNumber(1L)
                            .build();
                    citeCompany.setId(sequenceCitePort.saveOrUpdate(citeCompany));
                } else {
                    citeCompany.setCiteNumber(citeCompany.getCiteNumber() + 1);
                }

                int citeNumber = Math.toIntExact(citeCompany.getCiteNumber());
                FileDocumentDTO pendingFileDocument = new FileDocumentDTO();
                PendingNoteFileDTO pendingFile = new PendingNoteFileDTO();
                pendingFile.setCiteNumber(citeNumber);
                pendingFile.setGroupName(requestFontDTO.getCompany().getName());
                pendingFile.setRequestNumber(Long.valueOf(generalRequest.getRequestNumber()));
                pendingFile.setPolicyGroupNumber(requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy());
                pendingFile.setFullNameInsured(insured.getNaturalPerson().getCompleteName());
                pendingFile.setDocumentNumber(CINumber);
                pendingFile.setCurrentCreditAmount(requestFontDTO.getCredit().getValidAmount());
                pendingFile.setNewRequestCreditAmount(requestFontDTO.getCredit().getRequestedAmount());
                pendingFile.setTotalCumulusAmount(requestFontDTO.getCredit().getAccumulatedAmount());
                pendingFile.setCreditPeriodRequested(requestFontDTO.getCredit().getCreditTerm());
                pendingFile.setCurrencyType(currencyType);
                pendingFile.setUserRegional(agentRegional != null ? agentRegional.getDescription() : "Sin regional");

                byte[] citeDebitAuthorizationBytes = this.generatePdfUseCase
                        .generateTheFountPendingNote(pendingFile);

                String base64 = Base64.getEncoder().encodeToString(citeDebitAuthorizationBytes);
                String fileName = "NTA-PEN-SS-DJS-" + generalRequest.getRequestNumber() + "-" + requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy();
                pendingFileDocument.setMime("application/pdf");
                pendingFileDocument.setName(fileName);
                pendingFileDocument.setContent(base64);
                pendingFileDocument.setCite(citeNumber);
                pendingFileDocument.setTypeId(TypesAttachmentsEnum.PENDING_NOTE.getValue());
                int isSigned = 0;
                FilesRegisterDTO pending = new FilesRegisterDTO(pendingFileDocument, isSigned);
                files.add(pending);
                messageResponse = GelResponseEnum.PENDING_MESSAGE.getValue();
                sequenceCitePort.saveOrUpdate(citeCompany);
            }

            // Generating the rejection letter
            if (enumStatus == RequestStatusEnum.REJECTED.getValue()) {
                // Obtaining or creating the SequenceCite register
                SequenceCite citeCompany = sequenceCitePort.getSequence(userSess.getCompanyIdc(), HelpersMethods.getActualYear());
                if (citeCompany == null) {
                    citeCompany = SequenceCite.builder()
                            .companyIdc(userSess.getCompanyIdc())
                            .year(HelpersMethods.getActualYear())
                            .citeNumber(1L)
                            .build();
                    citeCompany.setId(sequenceCitePort.saveOrUpdate(citeCompany));
                } else {
                    citeCompany.setCiteNumber(citeCompany.getCiteNumber() + 1);
                }

                FileDocumentDTO rejectFileDto = new FileDocumentDTO();
                int citeNumber = Math.toIntExact(citeCompany.getCiteNumber());
                RejectNoteFileDTO rejectNoteFileDTO = RejectNoteFileDTO.builder()
                        .citeNumber(citeNumber)
                        .groupName(requestFontDTO.getCompany().getName().toUpperCase(Locale.ROOT))
                        .requestNumber(Long.valueOf(generalRequest.getRequestNumber()))
                        .policyName(requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy())
                        .fullNameInsured(requestFontDTO.getPerson().getNaturalPerson().getCompleteName())
                        .documentNumber(CINumber)
                        .currentCreditAmount(requestFontDTO.getCredit().getValidAmount())
                        .newRequestCreditAmount(requestFontDTO.getCredit().getRequestedAmount())
                        .totalCumulusAmount(requestFontDTO.getCredit().getValidAmount())
                        .creditPeriodRequested(requestFontDTO.getCredit().getCreditTerm())
                        .currencyType(currencyType)
                        .rejectionReason(rejectList)
                        .pathologyMedic(false)
                        .rejectComment("")
                        .userRegional(agentRegional != null ? agentRegional.getDescription() : "Sin regional")
                        .build();

                byte[] citeDebitAuthorizationBytes = this.generatePdfUseCase
                        .generateTheFountRejectNote(rejectNoteFileDTO);

                String base64 = Base64.getEncoder().encodeToString(citeDebitAuthorizationBytes);
                String fileName = "NTA-REC-SS-DJS-" + generalRequest.getRequestNumber() + "-" + requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy();
                rejectFileDto.setMime("application/pdf");
                rejectFileDto.setName(fileName);
                rejectFileDto.setTypeId(TypesAttachmentsEnum.REJECTED_NOTE.getValue());
                rejectFileDto.setContent(base64);
                rejectFileDto.setCite(citeNumber);
                int isSigned = 0;
                FilesRegisterDTO rejected = new FilesRegisterDTO(rejectFileDto, isSigned);
                files.add(rejected);
                messageResponse = GelResponseEnum.REJECTED_MESSAGE.getValue();
                sequenceCitePort.saveOrUpdate(citeCompany);
            }

            // Generating the coverage certificate
            if (enumStatus == RequestStatusEnum.FINALIZED.getValue()) {
                List<CoveragePolicyItem> coveragePolicyItems = new ArrayList<>();
                coverageProductPlanList.removeIf(c -> c.getOrder() > 1 && c.getCoverageTypeIdc() == ClassifierEnum.COV_ALTERN.getReferenceCode());
                coverageProductPlanList.forEach(x -> {
                    coveragePolicyItems.add(new CoveragePolicyItem(x, policyItemSaved.getId(), requestFontDTO.getCredit().getRequestedAmount()));
                });
                List<CoveragePolicyItem> coveragePolicyItemList = coveragePolicyItemPort.saveOrUpdateAll(coveragePolicyItems);
                List<CoveragePair> coveragePairs = new ArrayList<>();
                coveragePolicyItemList.forEach(x -> {
                    coveragePairs.add(new CoveragePair(coverageProductPlanList, x, requestFontDTO.getCredit().getCreditNumber(),
                            policyItem.getIndividualInsuredCapital()));
                });

                Broker broker = brokerPort.getBrokerById(requestFontDTO.getCompany().getPolicyDto().get(0).getBrokerId());
                GeneralRequest generalRequestHolder = generalRequestPort.getGeneralRequest(requestFontDTO.getCompany().getPolicyDto().get(0)
                        .getGeneralRequestId());
                Person personHolder = personPort.findById(generalRequestHolder.getPersonId());
                List<Direction> directions = directionPort.findAllByPersonId(personHolder.getId());

                FileDocumentDTO certificateFileDocument = new FileDocumentDTO();
                CoverageFileDTO coverageFileDTO = CoverageFileDTO.builder()
                        .policyNumber(requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy())
                        .certificateNumber(sequencePolicy.getCoverageCertificateNumber().intValue())
                        .policyHolderName(requestFontDTO.getCompany().getName().toUpperCase(Locale.ROOT))
                        .policyHolderTelephone(personHolder.getTelephone())
                        .policyHolderAddress(directions.get(0).getDescription())
                        .insuredName(requestFontDTO.getPerson().getNaturalPerson().getCompleteName())
                        .beneficiaryList(requestFontDTO.getBeneficiaries())
                        .coverageList(coveragePairs)
                        .insuredStartDateStr(HelpersMethods.formatStringOnlyDate(requestFontDTO.getCompany().getPolicyDto().get(0).getFromDate()))
                        .insuredFinishDateStr(HelpersMethods.formatStringOnlyDate(policyItemSaved.getTermValidity()))
                        .premiumMonthly(individualPremium)
                        .extraPremiumMonthly(0.0)
                        .totalRate(policyItem.getIndividualPremiumRate())
                        .totalPremiumMonthly(individualPremium)
                        .extraPremiumAnswer("")
                        .coverageLimitAgeList(coveragePairs)
                        .userRegionalIdc(requestFontDTO.getUserRegionalIdc())
                        .digitalFirm(requestFontDTO.getSign())
                        .brokerName(broker.getBusinessName())
                        .brokerDirectionAndNumber(broker.getAddress() + " Telf. " + broker.getTelephone())
                        .djsNumber(Long.valueOf(generalRequest.getRequestNumber()))
                        .legalHeirs(legalHeirs)
                        .insuredTypeIdc(requestFontDTO.getCredit().getInsuredTypeIdc())
                        .build();

                byte[] citeDebitAuthorizationBytes = this.generatePdfUseCase
                        .generateCoverageCertificate(coverageFileDTO);

                String base64 = Base64.getEncoder().encodeToString(citeDebitAuthorizationBytes);
                String fileName = requestFontDTO.getSign() == null ?
                        "CERT-" + sequencePolicy.getCoverageCertificateNumber().intValue() + "-SS-DJS-" + generalRequest.getRequestNumber() + "-" +
                                requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy() :
                        "CERT-" + sequencePolicy.getCoverageCertificateNumber().intValue() + "-SS-DJS-" + generalRequest.getRequestNumber() + "-" +
                                requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy() + "-(Firmado)";

                int certType = requestFontDTO.getSign() == null ?
                        TypesAttachmentsEnum.COVERAGECERTIFICATE.getValue() :
                        TypesAttachmentsEnum.SIGNED_COVERAGECERTIFICATE.getValue();
                certificateFileDocument.setMime("application/pdf");
                certificateFileDocument.setName(fileName);
                certificateFileDocument.setTypeId(certType);
                certificateFileDocument.setContent(base64);
                int isSigned = requestFontDTO.getSign() == null ? 0 : 1;
                FilesRegisterDTO certificateCoverage = new FilesRegisterDTO(certificateFileDocument, isSigned);
                files.add(certificateCoverage);
                messageResponse = GelResponseEnum.ACCEPTED_MESSAGE.getValue();
            }

            // Generating the DJS file
            long maritalStatusRequest = requestFontDTO.getPerson().getNaturalPerson().getMaritalStatusIdc();
            DjsFileDTO djsFileDTO = DjsFileDTO.builder()
                    .beneficiaryList(requestFontDTO.getBeneficiaries())
                    .insuredValues(requestFontDTO.getPerson())
                    .companyName(requestFontDTO.getCompany().getName().toUpperCase(Locale.ROOT))
                    .insuredAge(requestFontDTO.getPerson().getAge())
                    .answers(requestFontDTO.getAnswers().stream().filter(x -> (x.getAffirmativeAnswer() != null)).collect(Collectors.toList()))
                    .insuredCriteria(requestFontDTO.getAcceptanceCriteria())
                    .answerFieldsConcat(answersQuestions)
                    .credit(requestFontDTO.getCredit())
                    .userOfficeIdc(requestFontDTO.getUserOfficeIdc())
                    .userRegionalIdc(requestFontDTO.getUserRegionalIdc())
                    .userName(userInSessionName)
                    .currencyType(currencyType)
                    .digitalFirm(requestFontDTO.getSign())
                    .classifierListForRelationTypes(relationshipClassifierList)
                    .classifierListForExtensionCi(documentTypeExtensionsClassifierList)
                    .maritalStatus(maritalStatusClassifierList.stream().filter(x ->
                            (x.getReferenceId() == maritalStatusRequest)).findFirst().get().getDescription())
                    .listActivity(activitiesClassifierList)
                    .nationalityList(nationalitiesClassifierList)
                    .regionalList(regionalsClassifierList)
                    .questionDTOList(questionDTOList)
                    .djsNumber(Long.valueOf(generalRequest.getRequestNumber()))
                    .legalHeirs(legalHeirs)
                    .build();
            byte[] citeDebitAuthorizationBytes = this.generatePdfUseCase
                    .generateDJSDocument(djsFileDTO);

            String base64 = Base64.getEncoder().encodeToString(citeDebitAuthorizationBytes);
            String fileName = requestFontDTO.getSign() == null ?
                    "SS-DJS-" + generalRequest.getRequestNumber() + "-" + requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy() :
                    "SS-DJS-" + generalRequest.getRequestNumber() + "-" + requestFontDTO.getCompany().getPolicyDto().get(0).getNumberPolicy() + "-(Firmado)";
            int djsType = requestFontDTO.getSign() == null ? TypesAttachmentsEnum.DJS.getValue() : TypesAttachmentsEnum.SIGNED_DJS.getValue();
            djsFileDocument.setMime("application/pdf");
            djsFileDocument.setName(fileName);
            djsFileDocument.setContent(base64);
            djsFileDocument.setTypeId(djsType);
            int isSigned = requestFontDTO.getSign() == null ? 0 : 1;
            FilesRegisterDTO djsFile = new FilesRegisterDTO(djsFileDocument, isSigned);
            files.add(djsFile);

            // Saving all the generated files
            List<FileDocumentDTO> fileInsertsGet = new ArrayList<>();
            files.forEach(x -> {
                fileInsertsGet.add(x.getFile());
            });
            List<FileDocumentDTO> filesSaved = fileDocumentPort.SaveOrUpdateAll(fileInsertsGet);

            // If the DJS is signed the requirement control assigned to it is registered as uploaded
            if (requestFontDTO.getSign() != null) {
                RequirementControlDTO requirementControlDTO = requirementControlDTOSAux.stream()
                        .filter(x -> x.getDescription().contains("Declaración de Salud")).findFirst().orElse(null);
                if (requirementControlDTO != null) {
                    FileDocumentDTO file = filesSaved.stream().filter(x -> x.getName().startsWith("SS-DJS-")).findFirst().orElse(null);
                    requirementControlDTO.setFileDocument(file != null ? FileDocument.builder().id(file.getId()).build() : FileDocument.builder().id(null).build());
                    requirementControlDTO.setReceptionDate(LocalDate.now());
                    requirementControlPort.saveOrUpdate(requirementControlDTO);
                }
            }

            // Saving al the Policy Documents
            List<PolicyFileDocument> policyFileDocuments = new ArrayList<>();
            String documentNumber = sequencePolicy.getCoverageCertificateNumber() == null ? "" : sequencePolicy.getCoverageCertificateNumber().toString();
            filesSaved.forEach(x -> {
                String documentNumberAux = x.getName().contains("CERT-") ? documentNumber : null;
                int signed = files.stream().filter(o -> o.getFile().getName() == x.getName()).findFirst().get().getIsSigned();
                PolicyFileDocument policyFileDocument = PolicyFileDocument.builder()
                        .fileDocumentId(x.getId())
                        .policyItemId(policyItemSaved.getId())
                        .uploadDate(new Date())
                        .isSigned(signed)
                        .documentNumber(documentNumberAux)
                        .build();
                policyFileDocuments.add(policyFileDocument);
            });
            policyFileDocumentPort.saveAllOrUpdate(policyFileDocuments);

            ClfProcessRequestDTO clfProcessRequestDTO = ClfProcessRequestDTO.builder()
                    .files(filesSaved)
                    .response(enumStatus)
                    .message(messageResponse)
                    .build();

            FileDocumentDTO fileData;
            byte[] decoderFile;
            Emailbody emailbody;

            // If the insured is a PEP the system sends an email to:  consultas.seguros@santacruzfg.com;rcmorales@santacruzfg.com
            if (requestFontDTO.getAcceptanceCriteria().getIsPEP()) {
                fileData = filesSaved.stream().filter(x -> x.getName().startsWith("SS-DJS-")).findFirst().get();
                decoderFile = Base64.getDecoder().decode(fileData.getContent());
                Alert alert = alertPort.findByAlert(getAlertType(requestFontDTO.getCompany().getName(), GelAlertEnum.PEP_REQUEST));
                setGelSubjectAndBody(alert, generalRequest.getRequestNumber().toString(),
                        generalRequest.getRequestDate(), requestFontDTO.getPerson().getNaturalPerson().getCompleteName());
                emailbody = new Emailbody(alert, requestFontDTO.getCompany().getName(), requestId);
                emailbody.setBytes(decoderFile);
                emailbody.setName_attachment(fileData.getName() + ".pdf");

                emailService.saveEmail(emailbody);
            }

            // If the request is rejected the system sends an email to:  consultas.seguros@santacruzfg.com
            if (enumStatus == RequestStatusEnum.REJECTED.getValue()) {
                fileData = filesSaved.stream().filter(x -> x.getName().startsWith("NTA-REC")).findFirst().get();
                decoderFile = Base64.getDecoder().decode(fileData.getContent());
                Alert alert = alertPort.findByAlert(getAlertType(requestFontDTO.getCompany().getName(), GelAlertEnum.REJECTED_REQUEST));
                alert.setMail_to(alert.getMail_to().replace("{userEmail}", userSess.getEmail()));
                setGelSubjectAndBody(alert, GelAlertEnum.REJECTED_REQUEST, generalRequest.getRequestNumber().toString(),
                        generalRequest.getRequestDate(), requestFontDTO.getPerson().getNaturalPerson().getCompleteName(),
                        requestFontDTO.getCredit().getRequestedAmount(), requestFontDTO.getCredit().getAccumulatedAmount(),
                        "Rechazado");
                emailbody = new Emailbody(alert, requestFontDTO.getCompany().getName(), requestId);
                emailbody.setBytes(decoderFile);
                emailbody.setName_attachment(fileData.getName() + ".pdf");

                emailService.saveEmail(emailbody);
            }

            // If the request is pending the system sends an email to:  consultas.seguros@santacruzfg.com
            if (enumStatus == RequestStatusEnum.PENDING.getValue()) {
                fileData = filesSaved.stream().filter(x -> x.getName().startsWith("SS-DJS-")).findFirst().get();
                decoderFile = Base64.getDecoder().decode(fileData.getContent());
                Alert alert = alertPort.findByAlert(getAlertType(requestFontDTO.getCompany().getName(), GelAlertEnum.PENDING_REQUEST));
                setGelSubjectAndBody(alert, generalRequest.getRequestNumber().toString(),
                        generalRequest.getRequestDate(), requestFontDTO.getPerson().getNaturalPerson().getCompleteName());
                emailbody = new Emailbody(alert, requestFontDTO.getCompany().getName(), requestId);
                emailbody.setBytes(decoderFile);
                emailbody.setName_attachment(fileData.getName() + ".pdf");

                emailService.saveEmail(emailbody);
            }

            // If the request is accepted the system sends an email to:  consultas.seguros@santacruzfg.com
            if (enumStatus == RequestStatusEnum.FINALIZED.getValue()){
                fileData = filesSaved.stream().filter(x -> x.getName().startsWith("CERT-")).findFirst().get();
                decoderFile = Base64.getDecoder().decode(fileData.getContent());
                Alert alert = alertPort.findByAlert(getAlertType(requestFontDTO.getCompany().getName(), GelAlertEnum.APPROVED_REQUEST));
                alert.setMail_to(alert.getMail_to().replace("{userEmail}", userSess.getEmail()));
                setGelSubjectAndBody(alert, GelAlertEnum.APPROVED_REQUEST, generalRequest.getRequestNumber().toString(),
                        generalRequest.getRequestDate(), requestFontDTO.getPerson().getNaturalPerson().getCompleteName(),
                        requestFontDTO.getCredit().getRequestedAmount(), requestFontDTO.getCredit().getAccumulatedAmount(),
                        "condiciones normales");
                emailbody = new Emailbody(alert, requestFontDTO.getCompany().getName(), requestId);
                emailbody.setBytes(decoderFile);
                emailbody.setName_attachment(fileData.getName() + ".pdf");

                emailService.saveEmail(emailbody);
            }
            return clfProcessRequestDTO;
        } catch (Exception e) {
            log.error("Ocurrió un error al querer procesar la solicitud: [{}]", e.getMessage());
            System.out.println(e.getMessage());
            throw new OperationException("Falló al querer realizar la operación");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, OperationException.class})
    @Override
    public ClfProcessRequestDTO processSubscriptionRequest(ProcessExistRequestDTO processExistRequest) {
        try {
            double individualPremium = 0.0;
            double extraPremiumVale = 0.0;
            String extraAnswer = "";
            String messageResponse = "";
            String approveText = "";
            int enumStatus = RequestStatusEnum.FINALIZED.getValue();

            List<String> rejectionList = new ArrayList<>();
            List<FilesRegisterDTO> filesToSend = new ArrayList<>();
            List<Classifier> rejectionListCls = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.rejectType.getReferenceId());
            List<Classifier> approveList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.acceptType.getReferenceId());
            User user = userPort.findById(processExistRequest.getRequestDetail().getCreatedBy());

            // Getting the GeneralRequest, Policy, PolicyItem and SequencePolicy of the policy
            GeneralRequest generalRequest = generalRequestPort.getGeneralRequest(processExistRequest.getRequestDetail().getId());
            Policy policy = policyPort.findById(processExistRequest.getRequestDetail().getPolicyItemId());
            PolicyItem policyItem = policyItemPort.findById(processExistRequest.getRequestDetail().getPolicyItemId());
            SequencePolicy sequencePolicy = sequencePolicyPort.getSequence(policy.getId());

            // Building all the objects to generate if the request is accepted
            if (processExistRequest.getRequestDetail().getRequestStatusIdc().equals(RequestStatusEnum.FINALIZED.getValue())) {
                generalRequest.setRequestStatusIdc(processExistRequest.getRequestDetail().getRequestStatusIdc());
                generalRequest.setAcceptanceReasonIdc(processExistRequest.getRequestDetail().getAcceptanceReasonIdc());
                generalRequest.setExclusionComment(processExistRequest.getRequestDetail().getExclusionComment());
                List<CoveragePolicyItem> coveragePolicyItemsTemp = coveragePolicyItemPort.findByPolicyItemId(policyItem.getId());
                List<CoverageClf> coverageProductPlanList = coverageProductPlanPort.getAllCoverageClf(policy.getGeneralRequestId());

                // Calculating the coverages rate and setting the comments from the extra prime
                double individualTotalPremiumRate = 0;
                List<CoverageClf> coverageProductPlanListAux = new ArrayList<>();
                for (CoveragePolicyItem coveragePolicyItem : coveragePolicyItemsTemp) {
                    if (coveragePolicyItem.getAdditionalPremiumPerPercentage() >= 0 || coveragePolicyItem.getAdditionalPremiumPerThousand() > 0) {
                        CoverageClf aux = coverageProductPlanList.stream().filter(c ->
                                c.getCoverageProductPlanId().equals(coveragePolicyItem.getCoverageProductPlanId())).findFirst().get();
                        coverageProductPlanListAux.add(aux);
                        double coverageRate = aux.getRate();
                        double extraPerRate = coverageRate * coveragePolicyItem.getAdditionalPremiumPerPercentage();
                        individualTotalPremiumRate += coverageRate + extraPerRate;
                    }
                    if (coveragePolicyItem.getComment() != null && coveragePolicyItem.getComment().trim() != "") {
                        extraAnswer += extraAnswer.isEmpty() ? coveragePolicyItem.getComment() : " | " + coveragePolicyItem.getComment();
                    }
                }

                // Setting policyItem attributes for its creation
                policyItem.setIndividualPremium(0.0);
                policyItem.setIndividualPremiumRate(individualTotalPremiumRate);
                policyItem.setValidityStart(new Date());
                policyItem.setTermValidity(policy.getToDate());
                PolicyItem policyItemAux = policyItemPort.saveOrUpdate(productCalculationsService.calculateVariables(policyItem));

                // Defining the coverage certificate number
                long counterPlus = sequencePolicy.getCoverageCertificateNumber() == null ? 1 : sequencePolicy.getCoverageCertificateNumber() + 1;
                sequencePolicy.setCoverageCertificateNumber(counterPlus);
                sequencePolicyPort.saveOrUpdate(sequencePolicy);

                // Getting the beneficiary list for the coverage certificate document
                List<Beneficiary> beneficiaryList = beneficiaryPort.findAllByPolicyItemId(policyItem.getId());

                // The coverage list to display in the coverage certificate document
                List<CoveragePolicyItem> coveragePolicyItemList = coveragePolicyItemPort.findByPolicyItemId(policyItem.getId());
                List<CoveragePair> coveragePairs = new ArrayList<>();
                for (CoveragePolicyItem coveragePolicyItem : coveragePolicyItemList) {
                    coveragePairs.add(new CoveragePair(coverageProductPlanListAux, coveragePolicyItem, generalRequest.getCreditNumber(),
                            policyItem.getIndividualInsuredCapital()));
                }

                // Getting the broker for the coverage certificate document
                Broker broker = brokerPort.getBrokerById(policy.getBrokerId());

                // Obtaining the acceptance type description
                approveText = approveList.stream().filter(x ->
                        x.getReferenceId().equals(processExistRequest.getRequestDetail().getAcceptanceReasonIdc().longValue())).findFirst().get().getDescription();

                //Getting the holder's addresses (directions) registered
                GeneralRequest generalRequestHolder = generalRequestPort.getGeneralRequest(policy.getGeneralRequestId());
                Person personHolder = personPort.findById(generalRequestHolder.getPersonId());
                List<Direction> directions = directionPort.findAllByPersonId(personHolder.getId());

                // Building the CoverageFile object to generate the coverage certificate document
                CoverageFileDTO coverageFileDTO = CoverageFileDTO.builder()
                        .policyNumber(policy.getNumberPolicy())
                        .certificateNumber(sequencePolicy.getCoverageCertificateNumber().intValue())
                        .policyHolderName(processExistRequest.getRequestDetail().getOrganizationName().toUpperCase(Locale.ROOT))
                        .policyHolderTelephone(personHolder.getTelephone())
                        .policyHolderAddress(directions.get(0).getDescription())
                        .insuredName(fullNameProcessExistRequest(processExistRequest.getRequestDetail()))
                        .beneficiaryList(beneficiaryList)
                        .coverageList(coveragePairs)
                        .insuredStartDateStr(HelpersMethods.formatStringOnlyDate(policy.getFromDate()))
                        .insuredFinishDateStr(HelpersMethods.formatStringOnlyDate(policyItemAux.getTermValidity()))
                        .premiumMonthly(individualPremium)
                        .extraPremiumMonthly(extraPremiumVale)
                        .totalRate(policyItem.getIndividualPremiumRate())
                        .totalPremiumMonthly(individualPremium + extraPremiumVale)
                        .extraPremiumAnswer(extraAnswer)
                        .coverageLimitAgeList(coveragePairs)
                        .userRegionalIdc(user.getRegionalIdc())
                        .digitalFirm(null)
                        .brokerName(broker.getBusinessName())
                        .brokerDirectionAndNumber(broker.getAddress() + " Telf. " + broker.getTelephone())
                        .djsNumber(Long.valueOf(generalRequest.getRequestNumber()))
                        .legalHeirs(generalRequest.getLegalHeirs())
                        .exclusionComment(generalRequest.getExclusionComment())
                        .insuredTypeIdc(generalRequest.getInsuredTypeIdc().longValue())
                        .build();

                generalRequestPort.saveOrUpdate(generalRequest);

                byte[] citeDebitAuthorizationBytes = this.generatePdfUseCase
                        .generateCoverageCertificate(coverageFileDTO);

                String base64 = Base64.getEncoder().encodeToString(citeDebitAuthorizationBytes);
                String fileName = "CERT-" + sequencePolicy.getCoverageCertificateNumber().intValue() + "-SS-DJS-" + generalRequest.getRequestNumber()
                        + "-" + policy.getNumberPolicy();
                FileDocumentDTO certificateFileDocument = new FileDocumentDTO();
                certificateFileDocument.setMime("application/pdf");
                certificateFileDocument.setName(fileName);
                certificateFileDocument.setTypeId(TypesAttachmentsEnum.COVERAGECERTIFICATE.getValue());
                certificateFileDocument.setContent(base64);
                FilesRegisterDTO certificateCoverage = new FilesRegisterDTO(certificateFileDocument, 0);
                filesToSend.add(certificateCoverage);
                messageResponse = "Solicitud aceptada. Revisar el Certificado de Cobertura y la Declaración Jurada de Salud que se descargaron automáticamente.";
            }
            // If the status isn't accepted the only option left is that the request was rejected
            else {
                // Defining the cite number for the rejection letter
                SequenceCite citeCompany = sequenceCitePort.getSequence(user.getCompanyIdc(), HelpersMethods.getActualYear());
                if (citeCompany == null) {
                    citeCompany = SequenceCite.builder()
                            .companyIdc(user.getCompanyIdc())
                            .year(HelpersMethods.getActualYear())
                            .citeNumber(1L)
                            .build();
                    citeCompany.setId(sequenceCitePort.saveOrUpdate(citeCompany));
                } else {
                    citeCompany.setCiteNumber(citeCompany.getCiteNumber() + 1);
                }

                generalRequest.setRequestStatusIdc(processExistRequest.getRequestDetail().getRequestStatusIdc());
                generalRequest.setRejectedComment(processExistRequest.getRequestDetail().getRejectedComment());
                generalRequest.setRejectedReasonIdc(processExistRequest.getRequestDetail().getRejectedReasonIdc());
                generalRequestPort.saveOrUpdate(generalRequest);

                enumStatus = RequestStatusEnum.REJECTED.getValue();

                // Getting the rejection reasons
                Classifier clsStatus = rejectionListCls.stream()
                        .filter(x -> x.getReferenceId().equals(generalRequest.getRejectedReasonIdc().longValue()))
                        .findFirst()
                        .orElse(null);
                String rejectionType = clsStatus != null ? clsStatus.getDescription() : "";
                rejectionList.add(rejectionType);

                // Building the Reject File object to generate the Rejection Letter document
                FileDocumentDTO rejectFileDto = new FileDocumentDTO();
                int citeNumber = Math.toIntExact(citeCompany.getCiteNumber());
                double creditAmount = processExistRequest.getRequestDetail().getAccumulatedAmount() - processExistRequest.getRequestDetail().getRequestedAmount();
                List<Classifier> regionalList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Regional.getReferenceId());
                User agentUser = userPort.findById(policyItem.getCreatedBy());
                Classifier agentRegional = regionalList.stream().filter(x -> x.getReferenceId().equals(agentUser.getRegionalIdc()))
                        .findFirst().orElse(null);

                RejectNoteFileDTO rejectNoteFileDTO = RejectNoteFileDTO.builder()
                        .citeNumber(citeNumber)
                        .groupName(processExistRequest.getRequestDetail().getOrganizationName())
                        .requestNumber(Long.valueOf(generalRequest.getRequestNumber()))
                        .policyName(policy.getNumberPolicy())
                        .fullNameInsured(fullNameProcessExistRequest(processExistRequest.getRequestDetail()))
                        .documentNumber(processExistRequest.getRequestDetail().getIdentificationNumber())
                        .currentCreditAmount(creditAmount > 0 ? creditAmount : 0.0)
                        .newRequestCreditAmount(processExistRequest.getRequestDetail().getRequestedAmount())
                        .totalCumulusAmount(creditAmount > 0 ? creditAmount : 0.0)
                        .creditPeriodRequested(processExistRequest.getRequestDetail().getCreditTerm())
                        .currencyType(currencyType)
                        .rejectionReason(rejectionList)
                        .pathologyMedic(clsStatus.getReferenceId().equals(ClassifierEnum.REJECT_PATOLOGY.getReferenceCode()))
                        .rejectComment(processExistRequest.getRequestDetail().getRejectedComment())
                        .userRegional(agentRegional != null ? agentRegional.getDescription() : "Sin regional")
                        .build();

                byte[] citeDebitAuthorizationBytes = this.generatePdfUseCase
                        .generateTheFountRejectNote(rejectNoteFileDTO);

                String base64 = Base64.getEncoder().encodeToString(citeDebitAuthorizationBytes);
                String fileName = "NTA-REC-SS-DJS-" + generalRequest.getRequestNumber() + "-" + policy.getNumberPolicy();
                rejectFileDto.setMime("application/pdf");
                rejectFileDto.setName(fileName);
                rejectFileDto.setTypeId(TypesAttachmentsEnum.REJECTED_NOTE.getValue());
                rejectFileDto.setContent(base64);
                int isSigned = 0;
                FilesRegisterDTO rejected = new FilesRegisterDTO(rejectFileDto, isSigned);
                filesToSend.add(rejected);
                messageResponse = "Solicitud rechazada por " + processExistRequest.getRequestDetail().getRejectedComment();
                sequenceCitePort.saveOrUpdate(citeCompany);
            }

            long generalRequestId = generalRequestPort.saveOrUpdate(generalRequest);

            // Validating that if the request belongs to a PEP client, the validation document should exist
            if (processExistRequest.getRequestDetail().getIsPep()) {
                if (processExistRequest.getPepValidation() != null) {
                    FileDocument fileDocument = FileDocument.builder()
                            .description(processExistRequest.getPepValidation().getFileDocument().getName())
                            .typeDocument(processExistRequest.getPepValidation().getFileDocument().getTypeId())
                            .mime(processExistRequest.getPepValidation().getFileDocument().getMime())
                            .content(processExistRequest.getPepValidation().getFileDocument().getContent())
                            .build();
                    fileDocument = fileDocumentPort.SaveOrUpdate(fileDocument);

                    ZoneId systemTimeZone = ZoneId.systemDefault();
                    ZonedDateTime zonedDateTime = processExistRequest.getPepValidation().getPepValidationDate().atStartOfDay(systemTimeZone);
                    Date date = Date.from(zonedDateTime.toInstant());
                    Date uploadDate = processExistRequest.getPepValidation() != null ? date : new Date();
                    PolicyFileDocument policyFileDocument = PolicyFileDocument.builder()
                            .fileDocumentId(fileDocument.getId())
                            .policyItemId(processExistRequest.getRequestDetail().getPolicyItemId())
                            .uploadDate(uploadDate)
                            .isSigned(0)
                            .documentNumber(null)
                            .build();
                    policyFileDocumentPort.saveOrUpdate(policyFileDocument);
                }
            }

            // Saving the generated documents
            List<FileDocumentDTO> fileInsertsGet = new ArrayList<>();
            filesToSend.forEach(x -> {
                fileInsertsGet.add(x.getFile());
            });
            List<FileDocumentDTO> fileInserts = fileDocumentPort.SaveOrUpdateAll(fileInsertsGet);
            List<PolicyFileDocument> policyFileDocuments = new ArrayList<>();
            String documentNumber = sequencePolicy.getCoverageCertificateNumber() == null ? "" : sequencePolicy.getCoverageCertificateNumber().toString();
            fileInserts.forEach(x -> {
                String documentNumberAux = x.getName().contains("CERT") ? documentNumber : null;
                int signed = filesToSend.stream().filter(o -> o.getFile().getName() == x.getName()).findFirst().get().getIsSigned();
                PolicyFileDocument policyFileDocument = PolicyFileDocument.builder()
                        .fileDocumentId(x.getId())
                        .policyItemId(policyItem.getId())
                        .uploadDate(new Date())
                        .isSigned(signed)
                        .documentNumber(documentNumberAux)
                        .build();
                policyFileDocuments.add(policyFileDocument);
            });
            policyFileDocumentPort.saveAllOrUpdate(policyFileDocuments);

            // Building the response
            ClfProcessRequestDTO clfProcessRequestDTO = ClfProcessRequestDTO.builder()
                    .files(fileInserts)
                    .response(enumStatus)
                    .message(messageResponse)
                    .build();

            // Building the email response
            String search = enumStatus == RequestStatusEnum.REJECTED.getValue() ? "NTA-REC" : "CERT";
            FileDocumentDTO djsFileData = fileInserts.stream().filter(x -> x.getName().startsWith(search)).findFirst().get();
            byte[] decoderFile = Base64.getDecoder().decode(djsFileData.getContent());
            Emailbody emailbody;

            GelAlertEnum enumId = enumStatus == CLFResponseEnum.REJECTED.getValue() ? GelAlertEnum.REJECTED_REQUEST : GelAlertEnum.APPROVED_REQUEST;
            String reason = enumStatus == CLFResponseEnum.REJECTED.getValue() ? "Rechazado" : approveText.toLowerCase(Locale.ROOT);

            Alert alert = alertPort.findByAlert(getAlertType(processExistRequest.getRequestDetail().getOrganizationName(), enumId));
            alert.setMail_to(alert.getMail_to().replace("{userEmail}", user.getEmail()));
            setGelSubjectAndBody(alert, enumId, generalRequest.getRequestNumber().toString(),
                    generalRequest.getRequestDate(), fullNameProcessExistRequest(processExistRequest.getRequestDetail()),
                    processExistRequest.getRequestDetail().getRequestedAmount(), processExistRequest.getRequestDetail().getAccumulatedAmount(),
                    reason);
            emailbody = new Emailbody(alert, "TODO", generalRequestId);
            emailbody.setBytes(decoderFile);
            emailbody.setName_attachment(djsFileData.getName() + ".pdf");

            emailService.saveEmail(emailbody);

            return clfProcessRequestDTO;
        } catch (Exception e) {
            log.error("Ocurrio un error al querer procesar la solicitud: [{}]", e.getMessage());
            System.out.println(e.getMessage());
            throw new OperationException("Falló al querer realizar la operación");
        }
    }

    public String getQuestionsWithAnswers(List<AnswerDTO> answers) {
        String value = "";
        List<String> auxValue = new ArrayList<>();

        List<AnswerDTO> questionsList = new ArrayList<>();
        List<AnswerDTO> answersList = new ArrayList<>();
        List<AnswerDTO> questionList2 = new ArrayList<>();
        answers.forEach(o -> {
            if (o.getQuestionOrder() != null) {
                questionsList.add(o);
            } else {
                answersList.add(o);
                if (o.getAffirmativeAnswer() != null && o.getAffirmativeAnswer() == true) {
                    questionList2.add(o);
                }
            }
        });

        questionsList.addAll(questionList2);

        for (int i = 0; i < questionsList.size(); i++) {
            AnswerDTO question = questionsList.get(i);
            String quesAnswer = "";
            String answerTxt = "";
            String SEPARATOR = "";
            if (question.getQuestionOrder() == null) {
                quesAnswer = questionsList.get(i - 1).getQuestionOrder() + ".1. ";
            } else {
                quesAnswer = question.getQuestionOrder() + ". ";
            }
            for (AnswerDTO answer : answersList) {
                if (question.getQuestionId() == answer.getParentQuestionId()) {
                    quesAnswer += SEPARATOR + answer.getAnswer();
                    SEPARATOR = ", ";
                }
            }
            if (quesAnswer.length() > 3) auxValue.add(quesAnswer);
        }
        value = String.join("\n", auxValue);
        return value;
    }

    private String fullNameProcessExistRequest(RequestDetailDTO requestDetailDTO) {
        String name = requestDetailDTO.getName();
        String lastname = requestDetailDTO.getLastName();
        String motherLastname = requestDetailDTO.getMotherLastName().isEmpty() ? "" : " " + requestDetailDTO.getMotherLastName();
        String marriedLastname = (requestDetailDTO.getMarriedLastName() == null || requestDetailDTO.getMarriedLastName().isEmpty() || requestDetailDTO.getMarriedLastName().trim().isEmpty()) ?
                "" : (ClassifierEnum.WIDOWED_STATUS.getReferenceCode() == requestDetailDTO.getMaritalStatusIdc()) ? " VIUDA DE " + requestDetailDTO.getMarriedLastName().trim() : " DE " + requestDetailDTO.getMarriedLastName().trim();

        return name + " " + lastname + motherLastname + marriedLastname;
    }


    private double getCoverageRate(List<CoverageClf> coverages, long coverageType) {

        CoverageClf mainCoverage = coverages.stream().filter(x -> x.getCoverageTypeIdc() == coverageType).findFirst().orElse(null);
        double totalRate = 0;

        if (mainCoverage != null) {
            totalRate = getCoverageRate(coverages, coverageType, mainCoverage);
        }
        return totalRate;
    }

    private double getCoverageRate(List<CoverageClf> coverages, long coverageType, CoverageClf mainCoverage) {
        double totalRate = mainCoverage.getRate();

        for (CoverageClf coverageClf : coverages) {
            if (!coverageClf.getCoverageProductId().equals(mainCoverage.getCoverageProductId()) &&
                    mainCoverage.getCoverageProductId().equals(coverageClf.getParentCoverageProductId())) {
                coverages.remove(coverageClf);
                totalRate += getCoverageRate(coverages, coverageType, coverageClf);
            }
        }
        return totalRate;
    }

    private void setGelSubjectAndBody(Alert alert, String requestNumber, LocalDateTime date, String completeName) {
        String subject = alert.getMail_subject();
        String body = alert.getMail_body();
        subject = subject.replace("{nroSolicitud}", requestNumber);
        subject = subject.replace("{completeName}", completeName);
        body = body.replace("{requestDate}", HelpersMethods.formatStringOnlyLocalDateTime(date));

        alert.setSubjectAndBody(subject, body);
    }

    private void setGelSubjectAndBody(Alert alert, GelAlertEnum alertEnum, String requestNumber, LocalDateTime date,
                                      String completeName, double requestAmount, double cumulusAmount, String reason) {
        String subject = alert.getMail_subject();
        String body = alert.getMail_body();
        subject = subject.replace("{nroSolicitud}", requestNumber);
        subject = subject.replace("{completeName}", completeName);

        body = body.replace("{nroSolicitud}", requestNumber);
        body = body.replace("{completeName}", completeName);
        body = body.replace("{requestAmount}", requestAmount + "");

        switch (alertEnum) {
            case APPROVED_REQUEST:
                body = body.replace("{cumulusAmount}", cumulusAmount + "");
                body = body.replace("{acceptanceMessage}", reason);
                break;
            case REJECTED_REQUEST:
                body = body.replace("{requestStatus}", reason);
                break;
            default:
                break;
        }

        alert.setSubjectAndBody(subject, body);
    }

    private int getAlertType(String companyName, GelAlertEnum alertType) {
        int alert = 0;
        switch (alertType) {
            case PENDING_REQUEST:
                alert = companyName.toUpperCase().trim().contains(GelCompanyEnum.PAHUICHI.getValue()) ? AlertEnum.GEL_PAHUICHI_PENDING_REQUEST.getValue() : AlertEnum.GEL_CASAMIA_PENDING_REQUEST.getValue();
                break;
            case PEP_REQUEST:
                alert = companyName.toUpperCase().trim().contains(GelCompanyEnum.PAHUICHI.getValue()) ? AlertEnum.GEL_PAHUICHI_PEP_REQUEST.getValue() : AlertEnum.GEL_CASAMIA_PEP_REQUEST.getValue();
                break;
            case REJECTED_REQUEST:
                alert = companyName.toUpperCase().trim().contains(GelCompanyEnum.PAHUICHI.getValue()) ? AlertEnum.GEL_PAHUICHI_REJECTED_REQUEST.getValue() : AlertEnum.GEL_CASAMIA_REJECTED_REQUEST.getValue();
                break;
            case APPROVED_REQUEST:
                alert = companyName.toUpperCase().trim().contains(GelCompanyEnum.PAHUICHI.getValue()) ? AlertEnum.GEL_PAHUICHI_APPROVED_REQUEST.getValue() : AlertEnum.GEL_CASAMIA_APPROVED_REQUEST.getValue();
                break;
            default:
                break;
        }
        return alert;
    }

}
