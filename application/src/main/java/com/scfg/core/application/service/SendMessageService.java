package com.scfg.core.application.service;

import com.scfg.core.application.port.in.SendMessageUseCase;
import com.scfg.core.common.credentials.TwilioConfig;
import com.scfg.core.common.util.ReCaptcha;
import com.scfg.core.domain.SendSmsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class SendMessageService implements SendMessageUseCase {

    private final TwilioConfig twilioConfig;
    private final String whatsAppBaseUrl = "http://ec2-54-174-169-231.compute-1.amazonaws.com:8080/v1";

    @Override
    public Boolean sendSMS(SendSmsDTO smsDTO) {
        Boolean response = false;
        try {
            String wppText = "";
            String fromNumber = twilioConfig.getCellphoneNumber();
            if (smsDTO.getSendToWhatsApp()) {
                wppText = "whatsapp:";
                fromNumber = twilioConfig.getWhatsAppNumber();
            }
            Twilio.init(twilioConfig.getAccountSID(), twilioConfig.getAuthToken());
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber(wppText + "+591" + smsDTO.getNumber()),
                            new com.twilio.type.PhoneNumber(wppText + fromNumber),
                            smsDTO.getMessage())
                    .create();

            response = message.getErrorMessage() == null;

        } catch (Exception e) {
            String errorMsg = "Ocurrió un error al enviar el mensaje de Twilio: "+ smsDTO.getNumber() + " ";
            log.error(errorMsg + e.getMessage());
        }
        return response;
    }

    @Override
    public Boolean sendWhatsApp(SendSmsDTO smsDTO) {

        Boolean response;
        String urlParameters = "";

        String url = whatsAppBaseUrl + "/enviarmensaje";

        HashMap<String, String> params = new HashMap<>();
        params.put("number", "591" + smsDTO.getNumber());
        params.put("message", smsDTO.getMessage());

        if (smsDTO.getBase64Data() != null || smsDTO.getFilename() != null) {
            params.put("filename", smsDTO.getFilename());
            params.put("base64Data", smsDTO.getBase64Data());
        }

        try {
            urlParameters = this.getDataString(params);
        } catch (UnsupportedEncodingException e) {
            String errorMsg = "Ocurrió un error al codificar los parámetros para el mensaje de Whatsapp: "+ smsDTO.getNumber() + " ";
            log.error( errorMsg + e.getMessage());
        }

        response = doPostFormUrlEncoded(url, urlParameters);

        return response;
    }

    //#region Helpers

    public static synchronized boolean doPostFormUrlEncoded(String url, String params) {
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setDoOutput(true);
            http.setInstanceFollowRedirects(false);
            http.setRequestMethod("POST");
            http.setConnectTimeout(30000);
            http.setReadTimeout(30000);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setRequestProperty("charset", "UTF_8");
            http.setUseCaches(false);
            OutputStream out = http.getOutputStream();
            out.write(params.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();

            InputStream res = http.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(res, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            JSONObject json = new JSONObject(sb.toString());
            res.close();


            return json.toString().contains("Enviado Correctamente");

        } catch (Exception e) {
            String errorMsg = "Ocurrió un error al enviar el mensaje de Whatsapp: " + params + " ";
            log.error(errorMsg + e.getMessage());
        }
        return false;
    }

    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    //#endregion

}
