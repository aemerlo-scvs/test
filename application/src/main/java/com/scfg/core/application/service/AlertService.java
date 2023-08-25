package com.scfg.core.application.service;

import com.scfg.core.application.port.out.AlertPort;
import com.scfg.core.common.enums.AlertEnum;
import com.scfg.core.domain.Alert;
import com.scfg.core.domain.Beneficiary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scfg.core.common.enums.AlertEnum.*;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertPort alertPort;

    public Alert getAlertById(Integer alertId) {
        return alertPort.findByAlert(alertId);
    }

    public Alert getAlertByEnum(AlertEnum alertEnum) {
        return alertPort.findByAlert(alertEnum.getValue());
    }

    public Alert getAlertByEnumReplacingContent(AlertEnum alertEnum, List<String> valuesToReplace) {
        Alert alert = alertPort.findByAlert(alertEnum.getValue());
        alert.setMail_body(getContent(alertEnum, alert.getMail_body(), valuesToReplace));
        return alert;
    }

    public Alert getAlertByEnumReplacingContent(AlertEnum alertEnum, List<String> valuesBodyToReplace, List<String> valuesSubjectToReplace) {
        Alert alert = alertPort.findByAlert(alertEnum.getValue());
        alert.setMail_body(getContent(alertEnum, alert.getMail_body(), valuesBodyToReplace));
        alert.setMail_subject(getContentSubject(alertEnum, alert.getMail_subject(), valuesSubjectToReplace));
        return alert;
    }

    public List<Alert> getAlertsByListId(List<Integer> idList) {
        return alertPort.findByIdList(idList);
    }

    public Alert getAlertByEnumReplacingContent(AlertEnum alertEnum, Alert alertData, List<String> valuesToReplace) {
        Alert alert = alertData;
        alert.setMail_body(getContent(alertEnum, alert.getMail_body(), valuesToReplace));
        return alert;
    }

    public String getContent(AlertEnum alertEnum, String content, List<String> valuesToReplace) {

        String contentReplaced = content;

        if (valuesToReplace == null || valuesToReplace.isEmpty()) {
            return content;
        }

        switch (alertEnum) {
            case WELCOME:
                contentReplaced = contentReplaced.replace("{urlRedirection}", valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("{name}", valuesToReplace.get(1));
                contentReplaced = contentReplaced.replace("{activationCode}", valuesToReplace.get(2));
                break;
            case ACTIVATION:
                contentReplaced = contentReplaced.replace("{name}", valuesToReplace.get(0));
                break;
            case VIN_ACTIVATION_CONFIRM_PROPOSAL:
                contentReplaced = contentReplaced.replace("{fullName}",valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("{newList}",valuesToReplace.get(1));
                contentReplaced = contentReplaced.replace("{years}",valuesToReplace.get(2));
                contentReplaced = contentReplaced.replace("{amount}",valuesToReplace.get(3));
                contentReplaced = contentReplaced.replace("{companyName}",valuesToReplace.get(4));
                contentReplaced = contentReplaced.replace("{beneficiaryList}",valuesToReplace.get(5));
                contentReplaced = contentReplaced.replace("{url}",valuesToReplace.get(6));
                contentReplaced = contentReplaced.replace("{urlFalse}",valuesToReplace.get(7));
                contentReplaced = contentReplaced.replace("{apsCode}",valuesToReplace.get(8));
                contentReplaced = contentReplaced.replace("{holderName}",valuesToReplace.get(9));
                contentReplaced = contentReplaced.replace("{numberAccount}",valuesToReplace.get(10));
                contentReplaced = contentReplaced.replace("{date}",valuesToReplace.get(11));
                contentReplaced = contentReplaced.replace("{hours}",valuesToReplace.get(12));
                contentReplaced = contentReplaced.replace("{identificationNumber}",valuesToReplace.get(13));
                contentReplaced = contentReplaced.replace("{ext}",valuesToReplace.get(14));
                contentReplaced = contentReplaced.replace("{mediaType}",valuesToReplace.get(15));
                contentReplaced = contentReplaced.replace("{mail}",valuesToReplace.get(16));
                contentReplaced = contentReplaced.replace("{intermediary}",valuesToReplace.get(17));
                break;
            case VIN_ACCEPT_PROP:
                contentReplaced = content.replace("{name}",valuesToReplace.get(0));
                break;
            case VIN_REJECT_PROP:
                contentReplaced = content.replace("{name}",valuesToReplace.get(0));
                break;
            case VIN_ACTIVATION_CONFIRM_PROPOSAL_SMS:
                contentReplaced = content.replace("{url}",valuesToReplace.get(0));
                break;
            case VIN_REQUESTANNEXE_ACEPTED:
                contentReplaced = contentReplaced.replace("{name}",valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("{observation}",valuesToReplace.get(1));
            case VIN_REQUESTANNEXE_OBSERVED:
                contentReplaced = contentReplaced.replace("{name}",valuesToReplace.get(0));
            case VIN_REQUESTANNEXE_REJECTED:
                contentReplaced = contentReplaced.replace("{name}",valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("{observation}",valuesToReplace.get(1));
            case VIN_ANNEXE_CONFIRM_PAYMENT_VOUCHER:
                contentReplaced = contentReplaced.replace("{policyNumber}", valuesToReplace.get(0));
            case VIRH_SCH_1:
                contentReplaced = contentReplaced.replace("#sr",valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("#apellido",valuesToReplace.get(1));
                contentReplaced = contentReplaced.replace("#fecha",valuesToReplace.get(2));
                contentReplaced = contentReplaced.replace("#seguro",valuesToReplace.get(3));
                contentReplaced = contentReplaced.replace("#poliza",valuesToReplace.get(4));
                contentReplaced = contentReplaced.replace("#link",valuesToReplace.get(5));
            case VIRH_SCH_2:
                contentReplaced = contentReplaced.replace("#sr",valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("#apellido",valuesToReplace.get(1));
                contentReplaced = contentReplaced.replace("#fecha",valuesToReplace.get(2));
                contentReplaced = contentReplaced.replace("#seguro",valuesToReplace.get(3));
                contentReplaced = contentReplaced.replace("#poliza",valuesToReplace.get(4));
                contentReplaced = contentReplaced.replace("#link",valuesToReplace.get(5));
            case VIRH_SCH_3:
                contentReplaced = contentReplaced.replace("#seguro",valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("#link",valuesToReplace.get(1));
            case VIRH_SCH_4:
                contentReplaced = contentReplaced.replace("{{1}}",valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("{{2}}",valuesToReplace.get(1));
                contentReplaced = contentReplaced.replace("{{3}}",valuesToReplace.get(2));
                contentReplaced = contentReplaced.replace("{{4}}",valuesToReplace.get(3));
            case VIRH_WELCOME:
                contentReplaced = contentReplaced.replace("#name",valuesToReplace.get(0));
                contentReplaced = contentReplaced.replace("#seguro",valuesToReplace.get(1));
        }
        return contentReplaced;
    }
    public String getContentSubject(AlertEnum alertEnum, String content, List<String> valuesSubjectToReplace) {
        String contentReplaced = content;
        if (valuesSubjectToReplace == null || valuesSubjectToReplace.isEmpty()) {
            return content;
        }
        switch (alertEnum) {
            case VIN_REQUESTANNEXE_ACEPTED:
                contentReplaced = contentReplaced.replace("{nombreAsegurado}",valuesSubjectToReplace.get(0));
            case VIN_REQUESTANNEXE_OBSERVED:
                contentReplaced = contentReplaced.replace("{nombreAsegurado}",valuesSubjectToReplace.get(0));
            case VIN_REQUESTANNEXE_REJECTED:
                contentReplaced = contentReplaced.replace("{nombreAsegurado}",valuesSubjectToReplace.get(0));
            case VIN_ANNEXE_CONFIRM_PAYMENT_VOUCHER:
                contentReplaced = contentReplaced.replace("{nombreAsegurado}", valuesSubjectToReplace.get(0));
            case VIN_REQUESTANNEXE_ACEPTED_ACCOUTING:
                contentReplaced = contentReplaced.replace("{nombreAsegurado}",valuesSubjectToReplace.get(0));
        }
        return contentReplaced;
    }

}
