package com.scfg.core.application.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
import com.scfg.core.application.port.in.GeneratePdfUseCase;
import com.scfg.core.application.port.out.ClassifierPort;
import com.scfg.core.common.enums.*;
import com.scfg.core.common.templates.pdf.GenericHeaderPdfTemplate;
import com.scfg.core.common.templates.pdf.HeaderFooterPdfTemplate;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.common.util.MyProperties;
import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.dto.credicasas.QuestionDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.*;
import com.scfg.core.domain.dto.vin.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GeneratePdfService implements GeneratePdfUseCase {

    private static Logger logger = LoggerFactory.getLogger(GeneratePdfService.class);
    private final MyProperties properties;
    private final ClassifierPort classifierPort;

    private final Environment environment;
    private static String accountName = "Santa Cruz Vida y Salud Seguros y Reaseguros Personales S.A.";
    private static String ccFile = "C.c: Archivo";

    private static Rectangle GOVERMENT_LEGAL = new RectangleReadOnly(612, 936); // TAMAÑO OFICIO

    @Override
    public byte[] getPdf() {
        Document document = new Document(PageSize.LETTER);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
        try {
            PdfWriter.getInstance(document, out);
//            document.open();
            Font font = FontFactory.getFont("ARIAL");
            font.setSize(10);
            Font font2 = FontFactory.getFont("ARIAL_BOLD");
            font2.setSize(10);
            Font font3 = FontFactory.getFont(FontFactory.COURIER);
            font3.setSize(6);

            Paragraph test = new Paragraph("Santa Cruz de la Sierra, xx de xxxx de xxxx\n" +
                    "Cite: SCS xxx /202x\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Señores\n" +
                    "GRUPO EMPRESARIAL LA FUENTE\n" +
                    "Presente.-\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "REF.:\tSOLICITUD DE SEGURO NO. xxxxx, Sr(a). xxxxxxxxxxxxxx – RECHAZADA\n" +
                    "\tPÓLIZA DE SEGURO DE VIDA EN GRUPO No. xxxxxxxxxxxx\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "De nuestra consideración:\n" +
                    "\n" +
                    "\n" +
                    "Por medio de la presente, habiendo recibido la solicitud de seguro no. xxxxx correspondiente al Sr(a). xxxxxxxxxxxxx, con Cédula de Identidad / Pasaporte no. xxxxxxx, cliente del Grupo Empresarial La Fuente, tras haber finalizado la evaluación o suscripción correspondiente, comunicamos lo siguiente:\n" +
                    "\n" +
                    "Monto créditos vigentes:\t\tBs./USD______________\n" +
                    "Monto nuevo crédito solicitado:\t\tBs./USD______________\n" +
                    "Total monto acumulado:\t\tBs./USD______________\n" +
                    "Plazo crédito solicitado:\t\t\txx MESES\n" +
                    "\n" +
                    "•\tEl riesgo No es Asegurable, en razón a que el cliente señalado presenta EDAD FUERA DE LOS LÍMITES PARA INGRESO / ÍNDICE DE MASA CORPORAL ELEVADO / PATOLOGÍAS MÉDICAS ASOCIADAS, por tanto se encuentran fuera de nuestros parámetros de aseguramiento.\n" +
                    "\n" +
                    "\n" +
                    "Para acceder a la información confidencial sobre las patologías médicas asociadas el (la) Sr(a). xxxxxxxxxxxxx deberá pasar personalmente a nuestras oficinas de Santa Cruz Vida y Salud Seguros y Reaseguros Personales S.A., ubicadas en Av. San Martin, esq. calle Hugo Wast, Santa Cruz de la Sierra, a objeto de recoger las mismas en sobre cerrado.\n" +
                    "\n" +
                    "\n" +
                    "En este sentido la solicitud No. xxxxx, no cuenta con cobertura bajo la Póliza de Seguro de Vida en Grupo No. xxxxxxxxxxxx.\n" +
                    "\n" +
                    "\n" +
                    "Sin otro particular, aprovechamos para enviar nuestros cordiales saludos. \n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Santa Cruz Vida y Salud Seguros y Reaseguros Personales S.A.\n" +
                    "\n" +
                    "C.c.: Archivo\n");
//            document.add(test);
//            document.close();
            String HTML = "<html>\n" +
                    "<head><meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">\n" +
                    "<style type=\"text/css\">\n" +
                    "<!--\n" +
                    "span.cls_003{font-family:Arial,serif;font-size:10.4px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}\n" +
                    "div.cls_003{font-family:Arial,serif;font-size:10.4px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}\n" +
                    "span.cls_004{font-family:Arial,serif;font-size:8.9px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}\n" +
                    "div.cls_004{font-family:Arial,serif;font-size:8.9px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}\n" +
                    "span.cls_005{font-family:Arial,serif;font-size:7.2px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}\n" +
                    "div.cls_005{font-family:Arial,serif;font-size:7.2px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}\n" +
                    "span.cls_007{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:normal;font-style:italic;text-decoration: none}\n" +
                    "div.cls_007{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:normal;font-style:italic;text-decoration: none}\n" +
                    "span.cls_008{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}\n" +
                    "div.cls_008{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:normal;font-style:normal;text-decoration: none}\n" +
                    "span.cls_006{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}\n" +
                    "div.cls_006{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}\n" +
                    "span.cls_012{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: underline}\n" +
                    "div.cls_012{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:bold;font-style:normal;text-decoration: none}\n" +
                    "span.cls_009{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:bold;font-style:italic;text-decoration: none}\n" +
                    "div.cls_009{font-family:Arial,serif;font-size:8.1px;color:rgb(0,0,0);font-weight:bold;font-style:italic;text-decoration: none}\n" +
                    "-->\n" +
                    "</style>\n" +
                    "<script type=\"text/javascript\" src=\"46384e2e-6363-11ec-a980-0cc47a792c0a_id_46384e2e-6363-11ec-a980-0cc47a792c0a_files/wz_jsgraphics.js\"></script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div style=\"position:absolute;left:50%;margin-left:-306px;top:0px;width:612px;height:792px;border-style:outset;overflow:hidden\">\n" +
                    "<div style=\"position:absolute;left:0px;top:0px\">\n" +
                    "<img src=\"46384e2e-6363-11ec-a980-0cc47a792c0a_id_46384e2e-6363-11ec-a980-0cc47a792c0a_files/background1.jpg\" width=612 height=792></div>\n" +
                    "<div style=\"position:absolute;left:157.00px;top:55.27px\" class=\"cls_003\"><span class=\"cls_003\">DECLARACIÓN JURADA DE SALUD - BENEFICIARIO MIXTO</span></div>\n" +
                    "<div style=\"position:absolute;left:217.08px;top:66.48px\" class=\"cls_004\"><span class=\"cls_004\">PÓLIZA DE    SEGURO  DE    VIDA EN GRUPO</span></div>\n" +
                    "<div style=\"position:absolute;left:210.67px;top:76.88px\" class=\"cls_005\"><span class=\"cls_005\">CÓDIGO DE REGISTRO: 212-934663-2020 10 010 3003</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:93.70px\" class=\"cls_007\"><span class=\"cls_007\">ESTIMADO CLIENTE, FAVOR COMPLETAR LA INFORMACIÓN  QUE SE REQUIERE A CONTINUACIÓN  EN LETRA MAYÚSCULA</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:101.70px\" class=\"cls_007\"><span class=\"cls_007\">IMPRENTA:</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:120.92px\" class=\"cls_008\"><span class=\"cls_008\">Tomador o Contratante: ______________________________________________________________________________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:140.15px\" class=\"cls_006\"><span class=\"cls_006\">1. </span><span class=\"cls_012\">INFORMACIÓN DEL PROPUESTO ASEGURADO</span><span class=\"cls_006\">:</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:157.78px\" class=\"cls_008\"><span class=\"cls_008\">Nombre Completo: _________________________________________________________________________________________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:176.17px\" class=\"cls_008\"><span class=\"cls_008\">Número de C.I.: ___________________ Exp.: _______ Edad: ___________ Peso (kg.): ____________ Estatura (cm.): _________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:185.80px\" class=\"cls_008\"><span class=\"cls_008\">Dirección Domicilio: ________________________________________________________________________________________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:194.60px\" class=\"cls_008\"><span class=\"cls_008\">Telf. Domicilio: _________________________ Celular: __________________________ Telf. Oficina: ______________________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:204.22px\" class=\"cls_008\"><span class=\"cls_008\">Ocupación (actividad principal): _______________________________________________________________________________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:223.42px\" class=\"cls_006\"><span class=\"cls_006\">2. </span><span class=\"cls_012\">BENEFICIARIOS</span><span class=\"cls_006\">:</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:241.05px\" class=\"cls_006\"><span class=\"cls_006\">A TITULO ONEROSO</span><span class=\"cls_008\">: ____________________________________________________, el saldo insoluto de la deuda.</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:249.85px\" class=\"cls_006\"><span class=\"cls_006\">A TITULO GRATUITO</span><span class=\"cls_009\">:</span><span class=\"cls_008\"> Los beneficiarios nominados líneas abajo, solo por la diferencia entre el Capital Asegurado y el Saldo insoluto de la</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:259.47px\" class=\"cls_008\"><span class=\"cls_008\">deuda.</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:268.28px\" class=\"cls_008\"><span class=\"cls_008\">Nombre Completo: _____________________________________ Parentesco: __________ _____ Porcentaje (%):________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:277.87px\" class=\"cls_008\"><span class=\"cls_008\">Nombre Completo: _____________________________________ Parentesco: _______________ Porcentaje (%):________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:297.10px\" class=\"cls_006\"><span class=\"cls_006\">3. </span><span class=\"cls_012\">CUESTIONARIO</span><span class=\"cls_006\">:</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:314.73px\" class=\"cls_008\"><span class=\"cls_008\">Declaración Jurada que el Asegurado fórmula sobre su estado de salud con el fin de que pueda ser tom ado en consideración dentro del</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:323.53px\" class=\"cls_008\"><span class=\"cls_008\">Seguro de Vida en Grupo contratado por el Tomador señalado.</span></div>\n" +
                    "<div style=\"position:absolute;left:482.23px;top:333.95px\" class=\"cls_006\"><span class=\"cls_006\">SI</span></div>\n" +
                    "<div style=\"position:absolute;left:504.57px;top:333.95px\" class=\"cls_006\"><span class=\"cls_006\">NO</span></div>\n" +
                    "<div style=\"position:absolute;left:71.30px;top:341.95px\" class=\"cls_008\"><span class=\"cls_008\">1.</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:341.95px\" class=\"cls_008\"><span class=\"cls_008\">¿Actualmente padece de alguna enfermedad diagnosticada, cuál?</span></div>\n" +
                    "<div style=\"position:absolute;left:71.30px;top:351.55px\" class=\"cls_008\"><span class=\"cls_008\">2.</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:351.55px\" class=\"cls_008\"><span class=\"cls_008\">¿Se encuentra usted actualmente con algún tratamiento médico o e sta tomando algún medicamento?</span></div>\n" +
                    "<div style=\"position:absolute;left:71.30px;top:360.37px\" class=\"cls_008\"><span class=\"cls_008\">3.</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:360.37px\" class=\"cls_008\"><span class=\"cls_008\">¿Ha sufrido algún accidente o tiene algún impedimento físico?</span></div>\n" +
                    "<div style=\"position:absolute;left:71.30px;top:369.98px\" class=\"cls_008\"><span class=\"cls_008\">4.</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:369.98px\" class=\"cls_008\"><span class=\"cls_008\">Durante los últimos cinco (5) años, ¿ha sido intervenido o le han recomendado someterse</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:378.80px\" class=\"cls_008\"><span class=\"cls_008\">a cirugías quirúrgicas?, en su caso detallar.</span></div>\n" +
                    "<div style=\"position:absolute;left:71.30px;top:388.40px\" class=\"cls_008\"><span class=\"cls_008\">5.</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:388.40px\" class=\"cls_008\"><span class=\"cls_008\">¿Tiene Sida o es portador del virus de inmunodeficiencia humana - VIH?</span></div>\n" +
                    "<div style=\"position:absolute;left:71.30px;top:397.20px\" class=\"cls_008\"><span class=\"cls_008\">6.</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:397.20px\" class=\"cls_008\"><span class=\"cls_008\">¿Ha sido diagnosticado, tratado o va recibir tratamiento contra el Cáncer?</span></div>\n" +
                    "<div style=\"position:absolute;left:71.30px;top:406.83px\" class=\"cls_008\"><span class=\"cls_008\">7.</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:406.83px\" class=\"cls_008\"><span class=\"cls_008\">¿Realiza alguna actividad, deporte o pasatiempo considerado riesgoso, es decir, que conlleve un inusual</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:415.62px\" class=\"cls_008\"><span class=\"cls_008\">riesgo para la vida humana?, en su caso detallar.</span></div>\n" +
                    "<div style=\"position:absolute;left:71.30px;top:425.23px\" class=\"cls_008\"><span class=\"cls_008\">8.</span></div>\n" +
                    "<div style=\"position:absolute;left:92.13px;top:425.23px\" class=\"cls_008\"><span class=\"cls_008\">¿Se encuentra usted actualmente en buen estado de salud, en ausencia de afecciones y/o enfermedades?</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:443.65px\" class=\"cls_012\"><span class=\"cls_012\">NOTA</span><span class=\"cls_006\">: En caso de haber contestado alguna respuesta Afirmativa (SI), favor detallar las mismas (</span><span class=\"cls_009\">diagnostico, fecha, tratamiento,</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:451.65px\" class=\"cls_009\"><span class=\"cls_009\">centro médico o médico tratante, estado de salud actual</span><span class=\"cls_006\">):</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:460.47px\" class=\"cls_008\"><span class=\"cls_008\">_______________________________________________________________________________________________________________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:469.27px\" class=\"cls_008\"><span class=\"cls_008\">_______________________________________________________________________________________________________________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:478.10px\" class=\"cls_008\"><span class=\"cls_008\">_______________________________________________________________________________________________________________</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:498.10px\" class=\"cls_008\"><span class=\"cls_008\">Declaro haber contestado con total veracidad y de máxima buena fe las preguntas del presente cuestionario, así como no haber omitido ni</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:507.72px\" class=\"cls_008\"><span class=\"cls_008\">ocultado hechos y/o circunstancias que hubieran podido influir en la aceptación o no aceptación del seguro por parte de la Co mpañía. Es de</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:516.53px\" class=\"cls_008\"><span class=\"cls_008\">mi conocimiento que cualquier declaración falsa, reticencia o mala fe, me hará perder todos los beneficios del seguro.</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:534.95px\" class=\"cls_008\"><span class=\"cls_008\">Relevo expresamente del secreto profesional y legal a cualquier médico y/o centro de salud que me hubiese asistido y/o tratado y le autorizo</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:543.75px\" class=\"cls_008\"><span class=\"cls_008\">a revelar a Santa Cruz Vida y Salud Seguros y Reaseguros Personales S.A. todos los datos y antecedentes patológicos que pudie ra tener o</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:553.37px\" class=\"cls_008\"><span class=\"cls_008\">de los que hubiera adquirido conocimiento al prestarme sus servicios. Entiendo que de presentarse alguna eventualidad contemplada bajo</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:562.17px\" class=\"cls_008\"><span class=\"cls_008\">la póliza de seguro como consecuencia de alguna enfermedad existente a la fecha de la firma de ese documento o cuando haya alcanzado</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:571.77px\" class=\"cls_008\"><span class=\"cls_008\">la edad límite estipulada en la póliza, la Compañía quedará liberada de toda responsabilidad en lo que respecta a mi seguro.</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:590.20px\" class=\"cls_008\"><span class=\"cls_008\">En conformidad con la información que antecede, solicito a Santa Cruz Vida y Salud Seguros y Reaseguros Personales S.A., me o torgue el</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:599.00px\" class=\"cls_008\"><span class=\"cls_008\">Seguro de Vida en Grupo, para lo cual doy mi absoluta conformidad a todas las condiciones establecidas por la Compañía, sobre coberturas,</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:608.62px\" class=\"cls_008\"><span class=\"cls_008\">vigencia, exclusiones y caducidad del citado seguro obligándome a pagar las primas del seguro solicitado.</span></div>\n" +
                    "<div style=\"position:absolute;left:64.88px;top:621.42px\" class=\"cls_012\"><span class=\"cls_012\">DATOS DEL CRÉDITO:</span></div>\n" +
                    "<div style=\"position:absolute;left:340.45px;top:621.42px\" class=\"cls_008\"><span class=\"cls_008\">TITULAR (</span></div>\n" +
                    "<div style=\"position:absolute;left:387.59px;top:621.42px\" class=\"cls_008\"><span class=\"cls_008\">)</span></div>\n" +
                    "<div style=\"position:absolute;left:410.92px;top:621.42px\" class=\"cls_008\"><span class=\"cls_008\">CONYUGUE/CODEUDOR (</span></div>\n" +
                    "<div style=\"position:absolute;left:519.62px;top:621.42px\" class=\"cls_008\"><span class=\"cls_008\">)</span></div>\n" +
                    "<div style=\"position:absolute;left:64.88px;top:635.05px\" class=\"cls_008\"><span class=\"cls_008\">Capital asegurado vigente:………………………………………</span></div>\n" +
                    "<div style=\"position:absolute;left:64.88px;top:648.65px\" class=\"cls_008\"><span class=\"cls_008\">Capital actual Solicitado: ………………………………………..</span></div>\n" +
                    "<div style=\"position:absolute;left:64.88px;top:663.88px\" class=\"cls_008\"><span class=\"cls_008\">Plazo del crédito:………………………………………</span></div>\n" +
                    "<div style=\"position:absolute;left:64.88px;top:677.50px\" class=\"cls_008\"><span class=\"cls_008\">Capital asegurado Acumulado: …………………………………</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:694.30px\" class=\"cls_008\"><span class=\"cls_008\">Lugar: _____________________    Fecha: _______ / _______ / _______</span></div>\n" +
                    "<div style=\"position:absolute;left:410.92px;top:694.30px\" class=\"cls_008\"><span class=\"cls_008\">___________________________</span></div>\n" +
                    "<div style=\"position:absolute;left:434.17px;top:703.92px\" class=\"cls_008\"><span class=\"cls_008\">FIRMA ASEGURADO</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:712.72px\" class=\"cls_008\"><span class=\"cls_008\">Número del Crédito: --------------------------------------------</span></div>\n" +
                    "<div style=\"position:absolute;left:56.88px;top:722.33px\" class=\"cls_008\"><span class=\"cls_008\">Tipo de Trabajo: (Dependiente/Independiente)</span></div>\n" +
                    "</div>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n";
            HtmlConverter.convertToPdf(HTML, out);
        } catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateTheFountPendingNote(PendingNoteFileDTO pendingNoteFileDTO) {
        Document document = new Document(GOVERMENT_LEGAL);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            Font arialFontSize10 = FontFactory.getFont("ARIAL");
            arialFontSize10.setSize(8);
            Font arialFontBoldSize10 = FontFactory.getFont("ARIAL_BOLD");
            arialFontBoldSize10.setSize(8);
            HeaderFooterPdfTemplate event = new HeaderFooterPdfTemplate(properties, 0l, pendingNoteFileDTO.getRequestNumber());
            writer.setPageEvent(event);
            document.open();

            Calendar dateNow = DateUtils.getDateNowByGregorianCalendar();
            int actualYear = dateNow.get(Calendar.YEAR);
            int actualMonth = dateNow.get(Calendar.MONTH);
            String actualMonthStr = dateNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.forLanguageTag("es-BO"));
            int actualDay = dateNow.get(Calendar.DAY_OF_MONTH);

            Paragraph headerLocationDate = new Paragraph(pendingNoteFileDTO.getUserRegional() + ", "
                    + actualDay + " de " + actualMonthStr + " de " + actualYear, arialFontSize10);
            headerLocationDate.setAlignment(Element.ALIGN_LEFT);
            document.add(headerLocationDate);

            Paragraph headerCite = new Paragraph("Cite: SCVS/GEL/DHN/" + pendingNoteFileDTO.getCiteNumber()
                    + "/" + actualYear, arialFontSize10);
            headerCite.setAlignment(Element.ALIGN_LEFT);
            document.add(headerCite);

            Paragraph title = new Paragraph("\nSeñores", arialFontSize10);
            title.setAlignment(Element.ALIGN_LEFT);
            document.add(title);

            Paragraph groupCompany = new Paragraph(pendingNoteFileDTO.getGroupName(), arialFontBoldSize10);
            groupCompany.setAlignment(Element.ALIGN_LEFT);
            document.add(groupCompany);

            Chunk underLinePresent = new Chunk("Presente.-");
            underLinePresent.setUnderline(0.2f, -2f);
            Paragraph present = new Paragraph("", arialFontSize10);
            present.add(underLinePresent);
            present.setAlignment(Element.ALIGN_LEFT);
            document.add(present);

            Paragraph blankSpace = new Paragraph("\n", arialFontSize10);
            document.add(blankSpace);

            PdfPTable referenceTable = new PdfPTable(10);
            PdfPCell refCell = new PdfPCell();
            refCell.setPhrase(new Phrase("REF.:", arialFontSize10));
            refCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            refCell.setBorder(Rectangle.NO_BORDER);
            refCell.setColspan(1);
            referenceTable.addCell(refCell);

            PdfPCell refDescriptionCell = new PdfPCell();
            refDescriptionCell.setPhrase(new Phrase("SOLICITUD DE SEGURO NO." + pendingNoteFileDTO.getRequestNumber() + ", Sr(a)." +
                    pendingNoteFileDTO.getFullNameInsured() + " - PENDIENTE " + "PÓLIZA DE SEGURO DE DESGRAVAMEN No. " +
                    pendingNoteFileDTO.getPolicyGroupNumber(), arialFontSize10));
            refDescriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            refDescriptionCell.setBorder(Rectangle.NO_BORDER);
            refDescriptionCell.setColspan(8);
            referenceTable.addCell(refDescriptionCell);

            PdfPCell fillSpaceCell = new PdfPCell();
            fillSpaceCell.setBorder(Rectangle.NO_BORDER);
            fillSpaceCell.setColspan(1);
            referenceTable.addCell(fillSpaceCell);

            referenceTable.setWidthPercentage(100);
            document.add(referenceTable);

            Paragraph ourConsideration = new Paragraph("\n" +
                    "De nuestra consideración:", arialFontSize10);
            ourConsideration.setAlignment(Element.ALIGN_LEFT);
            document.add(ourConsideration);

            Chunk datClt = new Chunk("\n" +
                    "Por medio de la presente, habiendo recibido la solicitud de " +
                    "seguro No. ", arialFontSize10);
            Chunk datCltName = new Chunk(pendingNoteFileDTO.getRequestNumber().toString(), arialFontSize10);
            Chunk datCltN = new Chunk(" correspondiente al(a) Sr(a). ", arialFontSize10);
            Chunk datCltNam = new Chunk(pendingNoteFileDTO.getFullNameInsured(), arialFontSize10);
            Chunk datCltC = new Chunk(", con Cédula de Identidad / Pasaporte No. ", arialFontSize10);
            Chunk datCltF = new Chunk(pendingNoteFileDTO.getDocumentNumber(), arialFontSize10);
            Chunk datCltG = new Chunk(", cliente de los Sres. ", arialFontSize10);
            Chunk datCltH = new Chunk(pendingNoteFileDTO.getGroupName(), arialFontSize10);
            Chunk datCltJ = new Chunk(", tras haber revisado la Declaración Jurada de Salud, comunicamos lo siguiente:\n\n\n", arialFontSize10);
            Paragraph dataClient = new Paragraph("", arialFontSize10);
            dataClient.add(datClt);
            dataClient.add(datCltName);
            dataClient.add(datCltN);
            dataClient.add(datCltNam);
            dataClient.add(datCltC);
            dataClient.add(datCltF);
            dataClient.add(datCltG);
            dataClient.add(datCltH);
            dataClient.add(datCltJ);
            dataClient.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(dataClient);

            Chunk vigentCredit = new Chunk(HelpersMethods.formatNumberWithThousandsSeparators(pendingNoteFileDTO.getCurrentCreditAmount()), arialFontSize10);
//            vigentCredit.setUnderline(0.2f, -2f);
            Chunk newCredit = new Chunk(HelpersMethods.formatNumberWithThousandsSeparators(pendingNoteFileDTO.getNewRequestCreditAmount()), arialFontSize10);
//            newCredit.setUnderline(0.2f, -2f);
            Chunk totalAcumulated = new Chunk(HelpersMethods.formatNumberWithThousandsSeparators(pendingNoteFileDTO.getTotalCumulusAmount()), arialFontSize10);
//            totalAcumulated.setUnderline(0.2f, -2f);
            Chunk creditLimit = new Chunk(String.valueOf(pendingNoteFileDTO.getCreditPeriodRequested()), arialFontSize10);
//            creditLimit.setUnderline(0.2f, -2f);

            PdfPTable tableOfAmounts = new PdfPTable(4);

            PdfPCell cellNewCreditTitle = new PdfPCell();
            cellNewCreditTitle.setPhrase(new Phrase("Monto nuevo crédito solicitado:", arialFontSize10));
            cellNewCreditTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellNewCreditTitle.setBorder(Rectangle.NO_BORDER);
            cellNewCreditTitle.setColspan(2);
            tableOfAmounts.addCell(cellNewCreditTitle);
            PdfPCell cellNewCredit = new PdfPCell();
            Phrase newCreditCell = new Phrase(pendingNoteFileDTO.getCurrencyType() == 1 ? "BS " : "USD ", arialFontSize10);
            newCreditCell.add(newCredit);
            cellNewCredit.setPhrase(newCreditCell);
            cellNewCredit.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellNewCredit.setBorder(Rectangle.NO_BORDER);
            tableOfAmounts.addCell(cellNewCredit);
            tableOfAmounts.addCell(fillSpaceCell);

            PdfPCell cellTotalCreditTitle = new PdfPCell();
            cellTotalCreditTitle.setPhrase(new Phrase("Total monto acumulado:", arialFontSize10));
            cellTotalCreditTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellTotalCreditTitle.setBorder(Rectangle.NO_BORDER);
            cellTotalCreditTitle.setColspan(2);
            tableOfAmounts.addCell(cellTotalCreditTitle);
            PdfPCell cellTotalCredit = new PdfPCell();
            Phrase totalAccumulatedCell = new Phrase(pendingNoteFileDTO.getCurrencyType() == 1 ? "BS " : "USD ", arialFontSize10);
            totalAccumulatedCell.add(totalAcumulated);
            cellTotalCredit.setPhrase(totalAccumulatedCell);
            cellTotalCredit.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellTotalCredit.setBorder(Rectangle.NO_BORDER);
            tableOfAmounts.addCell(cellTotalCredit);
            tableOfAmounts.addCell(fillSpaceCell);

            PdfPCell cellCreditRequestTitle = new PdfPCell();
            cellCreditRequestTitle.setPhrase(new Phrase("Plazo crédito solicitado:", arialFontSize10));
            cellCreditRequestTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellCreditRequestTitle.setBorder(Rectangle.NO_BORDER);
            cellCreditRequestTitle.setColspan(2);
            tableOfAmounts.addCell(cellCreditRequestTitle);
            PdfPCell cellCreditRequest = new PdfPCell();
            cellCreditRequest.setPhrase(new Phrase(creditLimit + " MESES", arialFontSize10));
            cellCreditRequest.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellCreditRequest.setBorder(Rectangle.TOP);
            tableOfAmounts.addCell(cellCreditRequest);
            tableOfAmounts.addCell(fillSpaceCell);

            document.add(tableOfAmounts);

            Paragraph conclusion = new Paragraph("\n\n" +
                    "Una vez concluida la evaluación del riesgo, se comunicará al funcionario del Tomador/Contratante, " +
                    "el pronunciamiento sobre el propuesto Asegurado.",
                    arialFontSize10);
            conclusion.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(conclusion);

            Chunk startFinish = new Chunk("En este sentido la solicitud No. ", arialFontSize10);
            Chunk startFinishName = new Chunk(pendingNoteFileDTO.getRequestNumber().toString(), arialFontSize10);
            Chunk startFinishF = new Chunk(", queda PENDIENTE", arialFontSize10);
            Chunk startFinishG = new Chunk(" hasta que se emita el pronunciamiento por parte de nuestra Compañía.\n\n" +
                    "Sin otro particular, aprovechamos para enviar nuestros cordiales saludos.", arialFontSize10);
            Paragraph finalization = new Paragraph();
            finalization.add(startFinish);
            finalization.add(startFinishName);
            finalization.add(startFinishF);
            finalization.add(startFinishG);
            finalization.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(finalization);

            PdfPTable firmsFinish = new PdfPTable(2);
            Image imgFirst = Image.getInstance(properties.getPathImages() + "mauricioFrancoFirma.png");
            imgFirst.scaleAbsolute(200, 50);
            PdfPCell firstNameAuthFirm = new PdfPCell();
            firstNameAuthFirm.setBorder(Rectangle.NO_BORDER);
            firstNameAuthFirm.setHorizontalAlignment(Element.ALIGN_CENTER);
            firstNameAuthFirm.addElement(imgFirst);
            firmsFinish.addCell(firstNameAuthFirm);

            Image imgSecond = Image.getInstance(properties.getPathImages() + "marioAguirreFirma.jpg");
            imgSecond.scaleAbsolute(200, 50);
            PdfPCell secondNameAuthFirm = new PdfPCell();
            secondNameAuthFirm.setBorder(Rectangle.NO_BORDER);
            secondNameAuthFirm.setHorizontalAlignment(Element.ALIGN_CENTER);
            secondNameAuthFirm.addElement(imgSecond);
            firmsFinish.addCell(secondNameAuthFirm);

            Phrase cellNamePhrase = new Phrase("Mauricio Franco Melazzini\nGerente Técnico", arialFontSize10);
            PdfPCell firstNameAuth = new PdfPCell();
            firstNameAuth.setBorder(Rectangle.NO_BORDER);
            firstNameAuth.setPhrase(cellNamePhrase);
            firstNameAuth.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(firstNameAuth);

            Phrase cellName2Phrase = new Phrase("Mario Aguirre Durán\nGerente General", arialFontSize10);
            PdfPCell secondNameAuth = new PdfPCell();
            secondNameAuth.setBorder(Rectangle.NO_BORDER);
            secondNameAuth.setPhrase(cellName2Phrase);
            secondNameAuth.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(secondNameAuth);

            firmsFinish.addCell(fillSpaceCell);
            firmsFinish.addCell(fillSpaceCell);
            firmsFinish.addCell(fillSpaceCell);
            firmsFinish.addCell(fillSpaceCell);

            PdfPCell authFirms = new PdfPCell();
            authFirms.setBorder(Rectangle.NO_BORDER);
            authFirms.setColspan(2);
            authFirms.setPhrase(new Phrase("FIRMAS AUTORIZADAS", arialFontSize10));
            authFirms.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(authFirms);

            PdfPCell companyName = new PdfPCell();
            companyName.setBorder(Rectangle.NO_BORDER);
            companyName.setColspan(2);
            companyName.setPhrase(new Phrase("SANTA CRUZ VIDA Y SALUD SEGUROS Y REASEGUROS PERSONALES S.A.", arialFontSize10));
            companyName.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(companyName);

            document.add(firmsFinish);

            Font arialFontSizeCc = FontFactory.getFont("ARIAL");
            arialFontSizeCc.setSize(6);

//            Paragraph ccArchive = new Paragraph("\n" + ccFile, arialFontBoldSize10);
//            ccArchive.setAlignment(Element.ALIGN_LEFT);
//            document.add(ccArchive);

            document.close();

        } catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateTheFountRejectNote(RejectNoteFileDTO rejectNoteFileDTO) {
        Document document = new Document(GOVERMENT_LEGAL);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            Font arialFontSize10 = FontFactory.getFont("ARIAL");
            arialFontSize10.setSize(9);
            Font arialFontBoldSize10 = FontFactory.getFont("ARIAL_BOLD");
            arialFontBoldSize10.setSize(9);
            HeaderFooterPdfTemplate event = new HeaderFooterPdfTemplate(properties, 0L, rejectNoteFileDTO.getRequestNumber());
            writer.setPageEvent(event);
            document.open();

            Calendar dateNow = DateUtils.getDateNowByGregorianCalendar();
            int actualYear = dateNow.get(Calendar.YEAR);
            int actualMonth = dateNow.get(Calendar.MONTH);
            String actualMonthStr = dateNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.forLanguageTag("es-BO"));
            int actualDay = dateNow.get(Calendar.DAY_OF_MONTH);

            Paragraph headerLocationDate = new Paragraph(rejectNoteFileDTO.getUserRegional() + ", "
                    + actualDay + " de " + actualMonthStr + " de " + actualYear, arialFontSize10);
            headerLocationDate.setAlignment(Element.ALIGN_LEFT);
            document.add(headerLocationDate);

            Paragraph headerCite = new Paragraph("Cite: SCVS/GEL/DHN/" + rejectNoteFileDTO.getCiteNumber()
                    + "/" + actualYear, arialFontSize10);
            headerCite.setAlignment(Element.ALIGN_LEFT);
            document.add(headerCite);

            Paragraph title = new Paragraph("\nSeñores", arialFontSize10);
            title.setAlignment(Element.ALIGN_LEFT);
            document.add(title);

            Paragraph groupCompany = new Paragraph(rejectNoteFileDTO.getGroupName(), arialFontBoldSize10);
            groupCompany.setAlignment(Element.ALIGN_LEFT);
            document.add(groupCompany);

            Chunk underLinePresent = new Chunk("Presente.-");
            underLinePresent.setUnderline(0.2f, -2f);
            Paragraph present = new Paragraph("", arialFontSize10);
            present.add(underLinePresent);
            present.setAlignment(Element.ALIGN_LEFT);
            document.add(present);

            Paragraph blankSpace = new Paragraph("\n", arialFontSize10);
            document.add(blankSpace);

            PdfPTable referenceTable = new PdfPTable(10);
            PdfPCell refCell = new PdfPCell();
            refCell.setPhrase(new Phrase("REF.:", arialFontSize10));
            refCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            refCell.setBorder(Rectangle.NO_BORDER);
            refCell.setColspan(1);
            referenceTable.addCell(refCell);

            PdfPCell refDescriptionCell = new PdfPCell();
            refDescriptionCell.setPhrase(new Phrase("SOLICITUD DE SEGURO NO." + rejectNoteFileDTO.getRequestNumber() + ", Sr(a)." +
                    rejectNoteFileDTO.getFullNameInsured() + " - RECHAZADA " + "PÓLIZA DE SEGURO DE DESGRAVAMEN No. " +
                    rejectNoteFileDTO.getPolicyName(), arialFontSize10));
            refDescriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            refDescriptionCell.setBorder(Rectangle.NO_BORDER);
            refDescriptionCell.setColspan(8);
            referenceTable.addCell(refDescriptionCell);

            PdfPCell fillSpaceCell = new PdfPCell();
            fillSpaceCell.setBorder(Rectangle.NO_BORDER);
            fillSpaceCell.setColspan(1);
            referenceTable.addCell(fillSpaceCell);

            referenceTable.setWidthPercentage(100);
            document.add(referenceTable);

            Paragraph ourConsideration = new Paragraph("\n" +
                    "De nuestra consideración:", arialFontSize10);
            ourConsideration.setAlignment(Element.ALIGN_LEFT);
            document.add(ourConsideration);

            Chunk datClt = new Chunk("\n" +
                    "Por medio de la presente, habiendo recibido la solicitud de " +
                    "seguro No. ", arialFontSize10);
            Chunk datClta = new Chunk(rejectNoteFileDTO.getRequestNumber().toString(), arialFontSize10);
            Chunk datClts = new Chunk(" correspondiente al(a) Sr(a). ", arialFontSize10);
            Chunk datCltd = new Chunk(rejectNoteFileDTO.getFullNameInsured(), arialFontSize10);
            Chunk datCltf = new Chunk(", con Cédula de Identidad / Pasaporte No. ", arialFontSize10);
            Chunk datCltg = new Chunk(rejectNoteFileDTO.getDocumentNumber(), arialFontSize10);
            Chunk datClth = new Chunk(", cliente de los Sres. ", arialFontSize10);
            Chunk datCltj = new Chunk(rejectNoteFileDTO.getGroupName(), arialFontSize10);
            Chunk datCltk = new Chunk(", tras haber finalizado la evaluación o suscripción correspondiente, comunicamos lo siguiente:\n", arialFontSize10);
            Paragraph dataClient = new Paragraph("", arialFontSize10);
            dataClient.add(datClt);
            dataClient.add(datClta);
            dataClient.add(datClts);
            dataClient.add(datCltd);
            dataClient.add(datCltf);
            dataClient.add(datCltg);
            dataClient.add(datClth);
            dataClient.add(datCltj);
            dataClient.add(datCltk);
            dataClient.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(dataClient);


            String rejectionList = "";

            for (String data : rejectNoteFileDTO.getRejectionReason()) {
                rejectionList += (rejectionList == "") ? data : " | " + data;
            }

            Chunk riskP = new Chunk("• El riesgo No es Asegurable, en razón a que el cliente señalado presenta ", arialFontSize10);
            Chunk riskPa = new Chunk(rejectionList, arialFontSize10);
            Chunk riskPs = new Chunk(", por tanto se encuentra fuera de nuestros parámetros de aseguramiento.", arialFontSize10);
            Paragraph riskPending = new Paragraph();
            riskPending.add(riskP);
            riskPending.add(riskPa);
            riskPending.add(riskPs);
            riskPending.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(riskPending);

            if (rejectNoteFileDTO.getPathologyMedic()) {
                Chunk concluF = new Chunk("Para acceder a la información confidencial sobre las patologías médicas asociadas el (la) Sr(a). ", arialFontSize10);
                Chunk concluG = new Chunk(rejectNoteFileDTO.getFullNameInsured(), arialFontSize10);
                Chunk concluH = new Chunk(" deberá pasar personalmente a nuestras oficinas de " + accountName + ", ubicadas en Av. San Martin, " +
                        "esquina Calle Hugo Wast (Calle 5 Oeste), Edif. Santa Cruz Vida y Salud, Piso PB, Santa Cruz de la Sierra, a objeto de recoger las mismas en " +
                        "sobre cerrado.", arialFontSize10);
                Paragraph conclusion = new Paragraph();
                conclusion.add(concluF);
                conclusion.add(concluG);
                conclusion.add(concluH);
                conclusion.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(conclusion);
            }

            Chunk finalizationF = new Chunk("" +
                    "En este sentido la solicitud No. ", arialFontSize10);
            Chunk finalizationG = new Chunk(rejectNoteFileDTO.getRequestNumber().toString(), arialFontSize10);
            Chunk finalizationH = new Chunk(", no cuenta con cobertura bajo la Póliza de Seguro de Desgravamen No. ", arialFontSize10);
            Chunk finalizationJ = new Chunk(rejectNoteFileDTO.getPolicyName() + ".", arialFontSize10);
            Chunk finalizationK = new Chunk("\n\n" +
                    "Sin otro particular, aprovechamos para enviar nuestros cordiales saludos. ", arialFontSize10);
            Paragraph finalization = new Paragraph();
            finalization.add(finalizationF);
            finalization.add(finalizationG);
            finalization.add(finalizationH);
            finalization.add(finalizationJ);
            finalization.add(finalizationK);
            finalization.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(finalization);

            PdfPTable firmsFinish = new PdfPTable(2);
            Image imgFirst = Image.getInstance(properties.getPathImages() + "mauricioFrancoFirma.png");
            imgFirst.scaleAbsolute(200, 50);
            PdfPCell firstNameAuthFirm = new PdfPCell();
            firstNameAuthFirm.setBorder(Rectangle.NO_BORDER);
            firstNameAuthFirm.setHorizontalAlignment(Element.ALIGN_CENTER);
            firstNameAuthFirm.addElement(imgFirst);
            firmsFinish.addCell(firstNameAuthFirm);

            Image imgSecond = Image.getInstance(properties.getPathImages() + "marioAguirreFirma.jpg");
            imgSecond.scaleAbsolute(200, 50);
            PdfPCell secondNameAuthFirm = new PdfPCell();
            secondNameAuthFirm.setBorder(Rectangle.NO_BORDER);
            secondNameAuthFirm.setHorizontalAlignment(Element.ALIGN_CENTER);
            secondNameAuthFirm.addElement(imgSecond);
            firmsFinish.addCell(secondNameAuthFirm);

            Phrase cellNamePhrase = new Phrase("Mauricio Franco Melazzini\nGerente Técnico", arialFontSize10);
            PdfPCell firstNameAuth = new PdfPCell();
            firstNameAuth.setBorder(Rectangle.NO_BORDER);
            firstNameAuth.setPhrase(cellNamePhrase);
            firstNameAuth.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(firstNameAuth);

            Phrase cellName2Phrase = new Phrase("Mario Aguirre Durán\nGerente General", arialFontSize10);
            PdfPCell secondNameAuth = new PdfPCell();
            secondNameAuth.setBorder(Rectangle.NO_BORDER);
            secondNameAuth.setPhrase(cellName2Phrase);
            secondNameAuth.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(secondNameAuth);

            firmsFinish.addCell(fillSpaceCell);
            firmsFinish.addCell(fillSpaceCell);
            firmsFinish.addCell(fillSpaceCell);
            firmsFinish.addCell(fillSpaceCell);

            PdfPCell authFirms = new PdfPCell();
            authFirms.setBorder(Rectangle.NO_BORDER);
            authFirms.setColspan(2);
            authFirms.setPhrase(new Phrase("FIRMAS AUTORIZADAS", arialFontSize10));
            authFirms.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(authFirms);

            PdfPCell companyName = new PdfPCell();
            companyName.setBorder(Rectangle.NO_BORDER);
            companyName.setColspan(2);
            companyName.setPhrase(new Phrase("SANTA CRUZ VIDA Y SALUD SEGUROS Y REASEGUROS PERSONALES S.A.", arialFontSize10));
            companyName.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(companyName);

            document.add(firmsFinish);

            document.add(new Paragraph("\n\n", arialFontSize10));

            PdfPTable rejectionDataTable = new PdfPTable(10);
            rejectionDataTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            rejectionDataTable.setWidthPercentage(100);

            PdfPCell aux = new PdfPCell();
            aux.setColspan(10);
            aux.setPhrase(new Phrase("", arialFontSize10));
            aux.setBorder(Rectangle.NO_BORDER);

            PdfPCell rejectionFirmFullName = new PdfPCell();
            rejectionFirmFullName.setPhrase(new Phrase("Nombre completo:", arialFontSize10));
            rejectionFirmFullName.setHorizontalAlignment(Element.ALIGN_LEFT);
            rejectionFirmFullName.setBorder(Rectangle.NO_BORDER);
            rejectionFirmFullName.setColspan(3);
            rejectionDataTable.addCell(rejectionFirmFullName);

            PdfPCell rejectionFirmFullNameText = new PdfPCell();
            rejectionFirmFullNameText.setPhrase(new Phrase("", arialFontSize10));
            rejectionFirmFullNameText.setHorizontalAlignment(Element.ALIGN_LEFT);
            rejectionFirmFullNameText.setBorder(Rectangle.BOTTOM);
            rejectionFirmFullNameText.setColspan(5);
            rejectionDataTable.addCell(rejectionFirmFullNameText);

            rejectionDataTable.addCell(fillSpaceCell);
            rejectionDataTable.addCell(fillSpaceCell);

            rejectionDataTable.addCell(aux);
            rejectionDataTable.addCell(aux);

            PdfPCell rejectionFirmIdentificationNumber = new PdfPCell();
            rejectionFirmIdentificationNumber.setPhrase(new Phrase("Cédula de identidad:", arialFontSize10));
            rejectionFirmIdentificationNumber.setHorizontalAlignment(Element.ALIGN_LEFT);
            rejectionFirmIdentificationNumber.setBorder(Rectangle.NO_BORDER);
            rejectionFirmIdentificationNumber.setColspan(3);
            rejectionDataTable.addCell(rejectionFirmIdentificationNumber);

            PdfPCell rejectionFirmIdentificationNumberText = new PdfPCell();
            rejectionFirmIdentificationNumberText.setPhrase(new Phrase("", arialFontSize10));
            rejectionFirmIdentificationNumberText.setHorizontalAlignment(Element.ALIGN_LEFT);
            rejectionFirmIdentificationNumberText.setBorder(Rectangle.BOTTOM);
            rejectionFirmIdentificationNumberText.setColspan(5);
            rejectionDataTable.addCell(rejectionFirmIdentificationNumberText);

            rejectionDataTable.addCell(fillSpaceCell);
            rejectionDataTable.addCell(fillSpaceCell);

            rejectionDataTable.addCell(aux);
            rejectionDataTable.addCell(aux);

            PdfPCell rejectionFirmReceptionDate = new PdfPCell();
            rejectionFirmReceptionDate.setPhrase(new Phrase("Fecha de recepción:", arialFontSize10));
            rejectionFirmReceptionDate.setHorizontalAlignment(Element.ALIGN_LEFT);
            rejectionFirmReceptionDate.setBorder(Rectangle.NO_BORDER);
            rejectionFirmReceptionDate.setColspan(3);
            rejectionDataTable.addCell(rejectionFirmReceptionDate);

            PdfPCell rejectionFirmReceptionDateText = new PdfPCell();
            rejectionFirmReceptionDateText.setPhrase(new Phrase("", arialFontSize10));
            rejectionFirmReceptionDateText.setHorizontalAlignment(Element.ALIGN_LEFT);
            rejectionFirmReceptionDateText.setBorder(Rectangle.BOTTOM);
            rejectionFirmReceptionDateText.setColspan(5);
            rejectionDataTable.addCell(rejectionFirmReceptionDateText);

            rejectionDataTable.addCell(fillSpaceCell);
            rejectionDataTable.addCell(fillSpaceCell);

            rejectionDataTable.addCell(aux);
            rejectionDataTable.addCell(aux);
            rejectionDataTable.addCell(aux);
            rejectionDataTable.addCell(aux);
            rejectionDataTable.addCell(aux);
            rejectionDataTable.addCell(aux);
            rejectionDataTable.addCell(aux);

            PdfPCell rejectionFirm = new PdfPCell();
            rejectionFirm.setPhrase(new Phrase("Firma:", arialFontSize10));
            rejectionFirm.setHorizontalAlignment(Element.ALIGN_LEFT);
            rejectionFirm.setBorder(Rectangle.NO_BORDER);
            rejectionFirm.setColspan(3);
            rejectionDataTable.addCell(rejectionFirm);

            PdfPCell rejectionFirmText = new PdfPCell();
            rejectionFirmText.setPhrase(new Phrase("", arialFontSize10));
            rejectionFirmText.setHorizontalAlignment(Element.ALIGN_LEFT);
            rejectionFirmText.setBorder(Rectangle.BOTTOM);
            rejectionFirmText.setColspan(5);
            rejectionDataTable.addCell(rejectionFirmText);

            rejectionDataTable.addCell(fillSpaceCell);
            rejectionDataTable.addCell(fillSpaceCell);

            document.add(rejectionDataTable);

            document.close();

        } catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateCoverageCertificate(CoverageFileDTO coverageFileDTO) {
        Document document = new Document(GOVERMENT_LEGAL);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "times.ttf", "TIMES");
        FontFactory.register(properties.getPathFonts() + "timesbd.ttf", "TIMES_BOLD");
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            Font timesFontBoldSize8 = FontFactory.getFont("TIMES_BOLD");
            timesFontBoldSize8.setSize(7);
            Font timesFontSize8 = FontFactory.getFont("TIMES");
            timesFontSize8.setSize(7);
            Font timesFontBoldSize7 = FontFactory.getFont("TIMES_BOLD");
            timesFontBoldSize7.setSize(6);
            Font timesFontSize7 = FontFactory.getFont("TIMES");
            timesFontSize7.setSize(6);
            Font timesFontBoldSize6 = FontFactory.getFont("TIMES_BOLD");
            timesFontBoldSize6.setSize(5);
            Font timesFontSize6 = FontFactory.getFont("TIMES");
            timesFontSize6.setSize(5);
            HeaderFooterPdfTemplate event = new HeaderFooterPdfTemplate(properties, coverageFileDTO.getCertificateNumber().longValue(), coverageFileDTO.getDjsNumber());
            writer.setPageEvent(event);
            document.open();

            Paragraph title = new Paragraph("", timesFontBoldSize8);
            title.setAlignment(Element.ALIGN_CENTER);
            Chunk firstTitle = new Chunk("CERTIFICADO DE COBERTURA INDIVIDUAL No. " + coverageFileDTO.getCertificateNumber());
            firstTitle.setUnderline(0.2f, -2f);
            title.add(firstTitle);
            document.add(title);

            // #TITLES SECTION
            Paragraph secondTitle = new Paragraph("", timesFontBoldSize8);
            secondTitle.setAlignment(Element.ALIGN_CENTER);
            Chunk secTitle = new Chunk("PÓLIZA DE SEGURO COLECTIVO DE DESGRAVAMEN No. " + coverageFileDTO.getPolicyNumber());
            secTitle.setUnderline(0.2f, -2f);
            secondTitle.add(secTitle);
            document.add(secondTitle);
            Paragraph subSubTitle = new Paragraph("CÓDIGO DE REGISTRO: 212-934926-2019 02 001 4001", timesFontSize6);
            subSubTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subSubTitle);

            Paragraph presentCont = new Paragraph("\n" +
                    "Por el presente Certificado de Cobertura Individual se hace constar que la persona nominada en la " +
                    "Declaración Jurada de Salud y Solicitud de Seguro, conforme las declaraciones contenidas en ella, su firma como aceptación " +
                    "expresa del seguro, con sujeción a las Condiciones Generales, Particulares, Cláusulas Adicionales y Anexos de la póliza " +
                    "principal, se encuentra cubierto bajo la Póliza de Seguro de Desgravamen, de acuerdo a las siguientes condiciones.", timesFontSize6);
            presentCont.setAlignment(Element.ALIGN_JUSTIFIED);
            presentCont.setMultipliedLeading(1.0f);
            document.add(presentCont);

            Paragraph presentCont2 = new Paragraph("");
            Chunk insuranceObjectBoldText = new Chunk("OBJETO DEL SEGURO: ", timesFontBoldSize6);
            Chunk insuranceObjectText = new Chunk("La Póliza de Seguro de Desgravamen, representada en este certificado, tiene por objeto " +
                    "cubrir en caso de la Muerte del Asegurado, el saldo insoluto que este debía pagar a consecuencia del contrato de préstamo " +
                    "previamente celebrado con el Tomador. Beneficiándose de esta manera a los herederos del Asegurado, quienes verán liberada " +
                    "su herencia de la obligación de pago del préstamo contraído por el Asegurado. En ningún caso, el presente seguro cubrirá cuotas " +
                    "de amortización en mora o pendientes de pago antes de la Muerte del Asegurado.", timesFontSize6);
            presentCont2.add(insuranceObjectBoldText);
            presentCont2.add(insuranceObjectText);
            presentCont2.setAlignment(Element.ALIGN_JUSTIFIED);
            presentCont2.setMultipliedLeading(0.5f);
            document.add(presentCont2);

            Paragraph jumpLine = new Paragraph("\n", timesFontSize6);
            // #END TITLES SECTION

            // #COMPANY, POLICY HOLDER, INTERMEDIARY INFO SECTION
            PdfPTable insuranceInfoTable = new PdfPTable(10);
            insuranceInfoTable.setWidthPercentage(100);

            PdfPCell spaceCell = new PdfPCell();
            spaceCell.setBorder(Rectangle.NO_BORDER);
            spaceCell.setColspan(10);

            int burialValidation = (int) coverageFileDTO.getCoverageList().stream().filter(x -> x.getCoverageName().equals("SEPELIO")).count();

            PdfPCell companyNameTitle = new PdfPCell();
            companyNameTitle.setPhrase(new Phrase("COMPAÑÍA ASEGURADORA:", timesFontBoldSize6));
            companyNameTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            companyNameTitle.setBorder(Rectangle.NO_BORDER);
            companyNameTitle.setColspan(3);
            insuranceInfoTable.addCell(companyNameTitle);
            PdfPCell companyName = new PdfPCell();
            companyName.setPhrase(new Phrase("SANTA CRUZ VIDA Y SALUD SEGUROS Y REASEGUROS PERSONALES S.A.", timesFontBoldSize6));
            companyName.setHorizontalAlignment(Element.ALIGN_LEFT);
            companyName.setBorder(Rectangle.NO_BORDER);
            companyName.setColspan(7);
            insuranceInfoTable.addCell(companyName);

            PdfPCell contactTitle = new PdfPCell();
            contactTitle.setPhrase(new Phrase("DIRECCIÓN Y TELÉFONO:", timesFontBoldSize6));
            contactTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            contactTitle.setBorder(Rectangle.NO_BORDER);
            contactTitle.setColspan(3);
            insuranceInfoTable.addCell(contactTitle);
            PdfPCell companyContact = new PdfPCell();
            companyContact.setPhrase(new Phrase("Av. San Martin esquina Calle Hugo Wast (Calle 5 Oeste), Edif. Santa Cruz Vida y Salud Telf. " +
                    "(+591) 3-3158525", timesFontSize6));
            companyContact.setHorizontalAlignment(Element.ALIGN_LEFT);
            companyContact.setBorder(Rectangle.NO_BORDER);
            companyContact.setColspan(7);
            insuranceInfoTable.addCell(companyContact);

//            insuranceInfoTable.addCell(spaceCell);

            PdfPCell holderAndBeneficiaryTitle = new PdfPCell();
            holderAndBeneficiaryTitle.setPhrase(new Phrase("TOMADOR Y BENEFICIARIO:", timesFontBoldSize6));
            holderAndBeneficiaryTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            holderAndBeneficiaryTitle.setBorder(Rectangle.NO_BORDER);
            holderAndBeneficiaryTitle.setColspan(3);
            insuranceInfoTable.addCell(holderAndBeneficiaryTitle);
            PdfPCell holderAndBeneficiary = new PdfPCell();
            holderAndBeneficiary.setPhrase(new Phrase("BENEF. ONEROSO: " + coverageFileDTO.getPolicyHolderName(), timesFontBoldSize6));
            holderAndBeneficiary.setHorizontalAlignment(Element.ALIGN_LEFT);
            holderAndBeneficiary.setBorder(Rectangle.NO_BORDER);
            holderAndBeneficiary.setColspan(7);
            insuranceInfoTable.addCell(holderAndBeneficiary);

            insuranceInfoTable.addCell(contactTitle);
            PdfPCell holderContact = new PdfPCell();
            holderContact.setPhrase(new Phrase(coverageFileDTO.getPolicyHolderAddress() + " Telf. " +
                    coverageFileDTO.getPolicyHolderTelephone(), timesFontSize6));
            holderContact.setHorizontalAlignment(Element.ALIGN_LEFT);
            holderContact.setBorder(Rectangle.NO_BORDER);
            holderContact.setColspan(7);
            insuranceInfoTable.addCell(holderContact);

            PdfPCell beneficiariesSpace = new PdfPCell();
            beneficiariesSpace.setHorizontalAlignment(Element.ALIGN_LEFT);
            beneficiariesSpace.setBorder(Rectangle.NO_BORDER);
            beneficiariesSpace.setColspan(3);
            if (burialValidation > 0) {
                String beneficiariesContent = "";
                for (Beneficiary beneficiary : coverageFileDTO.getBeneficiaryList()) {
                    beneficiariesContent += beneficiariesContent.isEmpty() ?
                            beneficiary.getName() + " " + beneficiary.getLastName() + ", " +
                                    Objects.requireNonNull(classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Relationship.getReferenceId())
                                            .stream().filter(x -> x.getReferenceId().longValue() == beneficiary.getRelationshipIdc().longValue())
                                            .findFirst().orElse(null)).getDescription() + ", " + beneficiary.getPercentage() + "%"
                            : " - " + beneficiary.getName() + " " + beneficiary.getLastName() + ", " +
                            Objects.requireNonNull(classifierPort.getAllClassifiersByClassifierTypeReferenceId(ClassifierTypeEnum.Relationship.getReferenceId())
                                    .stream().filter(x -> x.getReferenceId().longValue() == beneficiary.getRelationshipIdc().longValue())
                                    .findFirst().orElse(null)).getDescription() + ", " + beneficiary.getPercentage() + "%";
                }

                insuranceInfoTable.addCell(beneficiariesSpace);

                PdfPCell beneficiariesCell = new PdfPCell();
                beneficiariesCell.setPhrase(new Phrase("BENEF. GRATUITO (Para Sepelio): " + beneficiariesContent, timesFontBoldSize6));
                beneficiariesCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                beneficiariesCell.setBorder(Rectangle.NO_BORDER);
                beneficiariesCell.setColspan(7);
                insuranceInfoTable.addCell(beneficiariesCell);

            }
            insuranceInfoTable.addCell(beneficiariesSpace);

            PdfPCell beneficiariesCell = new PdfPCell();
            Phrase insuredTitle = new Phrase("ASEGURADO: ", timesFontBoldSize6);
            Phrase insuredDesc = new Phrase(coverageFileDTO.getInsuredName(), timesFontSize6);
            Paragraph insuredText = new Paragraph("", timesFontBoldSize6);
            insuredText.setAlignment(Element.ALIGN_LEFT);
            insuredText.setMultipliedLeading(1.0f);
            insuredText.add(insuredTitle);
            insuredText.add(insuredDesc);
            beneficiariesCell.addElement(insuredText);
            beneficiariesCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            beneficiariesCell.setBorder(Rectangle.NO_BORDER);
            beneficiariesCell.setColspan(7);
            insuranceInfoTable.addCell(beneficiariesCell);
//            insuranceInfoTable.addCell(spaceCell);

            PdfPCell intermediaryTitle = new PdfPCell();
            intermediaryTitle.setPhrase(new Phrase("INTERMEDIARIO:", timesFontBoldSize6));
            intermediaryTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            intermediaryTitle.setBorder(Rectangle.NO_BORDER);
            intermediaryTitle.setColspan(3);
            insuranceInfoTable.addCell(intermediaryTitle);
            PdfPCell intermediary = new PdfPCell();
            intermediary.setPhrase(new Phrase("" + coverageFileDTO.getBrokerName(), timesFontBoldSize6));
            intermediary.setHorizontalAlignment(Element.ALIGN_LEFT);
            intermediary.setBorder(Rectangle.NO_BORDER);
            intermediary.setColspan(7);
            insuranceInfoTable.addCell(intermediary);

            insuranceInfoTable.addCell(contactTitle);
            PdfPCell intermediaryContact = new PdfPCell();
            intermediaryContact.setPhrase(new Phrase("" + coverageFileDTO.getBrokerDirectionAndNumber(), timesFontSize6));
            intermediaryContact.setHorizontalAlignment(Element.ALIGN_LEFT);
            intermediaryContact.setBorder(Rectangle.NO_BORDER);
            intermediaryContact.setColspan(7);
            insuranceInfoTable.addCell(intermediaryContact);

            document.add(insuranceInfoTable);
            // #END COMPANY, POLICY HOLDER, INTERMEDIARY INFO SECTION
//            document.add(jumpLine);

            // #COVERAGES AND INSURED CAPITALS SECTION
            Paragraph coverage = new Paragraph("COBERTURAS Y CAPITALES ASEGURADOS*:", timesFontBoldSize6);
            coverage.setAlignment(Element.ALIGN_LEFT);
            document.add(coverage);

            document.add(jumpLine);

            PdfPTable coverageContent = new PdfPTable(2);

            Stream.of("COBERTURA", "CAPITAL ASEGURADO").forEach(headerTitle -> {
                PdfPCell headerTable = new PdfPCell();
                headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTable.setPhrase(new Phrase(headerTitle, timesFontBoldSize6));
                coverageContent.addCell(headerTable);
            });

            for (CoveragePair cov : coverageFileDTO.getCoverageList()) {
                PdfPCell coverageDesc = new PdfPCell();
                coverageDesc.setPhrase(new Phrase(cov.getCoverageName().equalsIgnoreCase("INVALIDEZ TOTAL Y PERMANENTE")
                        ? "PAGO ANTICIPADO DEL CAPITAL EN CASO DE INVALIDEZ TOTAL Y PERMANENTE A POR CONSECUENCIA DE ENFERMEDAD O ACCIDENTE"
                        : cov.getCoverageName(), timesFontSize6));
                coverageDesc.setHorizontalAlignment(Element.ALIGN_LEFT);
                coverageContent.addCell(coverageDesc);

                String insuredCapital = cov.getCommentAdditional().isEmpty() ?
                        HelpersMethods.formatNumberWithThousandsSeparators(cov.getInsuredAmount()) : cov.getCommentAdditional();
                PdfPCell capitalInsuredUsd = new PdfPCell();
                capitalInsuredUsd.setPhrase(new Phrase(insuredCapital, timesFontSize6));
                capitalInsuredUsd.setHorizontalAlignment(Element.ALIGN_LEFT);
                coverageContent.addCell(capitalInsuredUsd);
            }
            document.add(coverageContent);
//            document.add(jumpLine);

            for (CoveragePair cov : coverageFileDTO.getCoverageList()) {
                Paragraph coverageDetail = new Paragraph("");
                Chunk coverageName = new Chunk("", timesFontBoldSize6);
                Chunk coverageDescription = new Chunk("", timesFontSize6);
                switch (cov.getCoverageName()) {
                    case "MUERTE POR CUALQUIER CAUSA":
                        coverageName.append("COBERTURA PRINCIPAL – MUERTE: ");
                        coverageDescription.append("Este seguro cubre en caso de Muerte del Asegurado, sea por causa natural, accidental o enfermedad " +
                                "no excluida en la póliza, el Saldo Insoluto a partir de la fecha de fallecimiento del Asegurado, siempre que se " +
                                "produzca durante la vigencia de la póliza, ésta se encuentre en vigor, se hayan cancelado las primas convenidas " +
                                "dentro los plazos estipulados y se cumplan con las condiciones y/o requisitos establecidos en la presente póliza.\n");
                        break;

                    case "INVALIDEZ TOTAL Y PERMANENTE":
                        coverageName.append("PAGO ANTICIPADO DEL CAPITAL EN CASO DE INVALIDEZ TOTAL Y PERMANENTE A POR CONSECUENCIA DE ENFERMEDAD " +
                                "O ACCIDENTE: ");
                        coverageDescription.append("Si por accidente o enfermedad el asegurado sufriere incapacidad total y permanente que lo obligue " +
                                "a abandonar su empleo u ocupación, impidiéndole ejercer otra actividad conforme a su posición social, a sus " +
                                "conocimientos y sus aptitudes y a juicio de los médicos forenses proporcionados por la compañía aseguradora.\n");
                        break;

                    case "SEPELIO":
                        coverageName.append("SEPELIO: ");
                        coverageDescription.append("USD 300,00 por asegurado (Aplicable a los beneficiarios a título gratuito).\n");
                        break;

                    case "MUERTE ACCIDENTAL":
                        coverageName.append("COBERTURA ALTERNA MUERTE ACCIDENTAL: ");
                        coverageDescription.append("Aplicable únicamente a los casos que no sean aceptados por la compañía para las coberturas de " +
                                "Muerte e Invalidez Total y Permanente por cualquier causa.\n");
                        break;

                    default:
                        coverageName.append("");
                        coverageDescription.append("\n");
                }

                coverageDetail.add(coverageName);
                coverageDetail.add(coverageDescription);
                coverageDetail.setAlignment(Element.ALIGN_JUSTIFIED);
                coverageDetail.setMultipliedLeading(0.5f);
                document.add(coverageDetail);
            }
//            document.add(jumpLine);
            // #END COVERAGES AND INSURED CAPITALS SECTION

            // #AGE LIMIT
            Paragraph limit = new Paragraph("LÍMITES DE EDAD:", timesFontBoldSize6);
            limit.setAlignment(Element.ALIGN_LEFT);
            document.add(limit);

            PdfPTable ageLimitContentTable = new PdfPTable(3);
            ageLimitContentTable.setWidthPercentage(60);

            Stream.of("Cobertura", "Ingreso", "Permanencia").forEach(headerTitle -> {
                PdfPCell headerTable = new PdfPCell();
                headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTable.setBorder(Rectangle.NO_BORDER);
                headerTable.setPhrase(new Phrase(headerTitle, timesFontBoldSize6));
                ageLimitContentTable.addCell(headerTable);
            });

            if (burialValidation > 0) {
                for (CoveragePair cov : coverageFileDTO.getCoverageList()) {
                    if (!cov.getCoverageName().equals("SEPELIO")) {
                        PdfPCell coverageDesc = new PdfPCell();
                        if (cov.getCoverageName().equals("MUERTE POR CUALQUIER CAUSA")) {
                            coverageDesc.setPhrase(new Phrase("MUERTE/SEPELIO", timesFontSize6));
                        } else {
                            coverageDesc.setPhrase(new Phrase(cov.getCoverageName(), timesFontSize6));
                        }
                        coverageDesc.setHorizontalAlignment(Element.ALIGN_LEFT);
                        coverageDesc.setBorder(Rectangle.NO_BORDER);
                        ageLimitContentTable.addCell(coverageDesc);

                        PdfPCell entryAges = new PdfPCell();
                        entryAges.setPhrase(new Phrase(cov.getEntryAgeDescription(), timesFontSize6));
                        entryAges.setHorizontalAlignment(Element.ALIGN_LEFT);
                        entryAges.setBorder(Rectangle.NO_BORDER);
                        ageLimitContentTable.addCell(entryAges);

                        PdfPCell permanencyAges = new PdfPCell();
                        permanencyAges.setPhrase(new Phrase(cov.getPermanencyAgeDescription(), timesFontSize6));
                        permanencyAges.setHorizontalAlignment(Element.ALIGN_LEFT);
                        permanencyAges.setBorder(Rectangle.NO_BORDER);
                        ageLimitContentTable.addCell(permanencyAges);
                    }
                }
            } else {

                for (CoveragePair cov : coverageFileDTO.getCoverageList()) {
                    PdfPCell coverageDesc = new PdfPCell();
                    coverageDesc.setPhrase(new Phrase(cov.getCoverageName(), timesFontSize6));
                    coverageDesc.setHorizontalAlignment(Element.ALIGN_LEFT);
                    coverageDesc.setBorder(Rectangle.NO_BORDER);
                    ageLimitContentTable.addCell(coverageDesc);

                    PdfPCell entryAges = new PdfPCell();
                    entryAges.setPhrase(new Phrase(cov.getEntryAgeDescription(), timesFontSize6));
                    entryAges.setHorizontalAlignment(Element.ALIGN_LEFT);
                    entryAges.setBorder(Rectangle.NO_BORDER);
                    ageLimitContentTable.addCell(entryAges);

                    PdfPCell permanencyAges = new PdfPCell();
                    permanencyAges.setPhrase(new Phrase(cov.getPermanencyAgeDescription(), timesFontSize6));
                    permanencyAges.setHorizontalAlignment(Element.ALIGN_LEFT);
                    permanencyAges.setBorder(Rectangle.NO_BORDER);
                    ageLimitContentTable.addCell(permanencyAges);
                }

            }
            document.add(ageLimitContentTable);
//            document.add(jumpLine);

            // #END AGE LIMIT

            // #INSURANCE INFO
            PdfPTable insuranceRateTable = new PdfPTable(4);
            insuranceRateTable.setWidthPercentage(100);

            PdfPCell insuranceRateTitle = new PdfPCell();
            insuranceRateTitle.setPhrase(new Phrase("TASA DEL SEGURO: ", timesFontBoldSize6));
            insuranceRateTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            insuranceRateTitle.setBorder(Rectangle.NO_BORDER);
            insuranceRateTable.addCell(insuranceRateTitle);

            PdfPCell oneSpaceCell = new PdfPCell();
            oneSpaceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            oneSpaceCell.setBorder(Rectangle.NO_BORDER);

            int deathDisabilityValidation = (int) coverageFileDTO.getCoverageList().stream().filter(x -> x.getCoverageName()
                            .equals("MUERTE POR CUALQUIER CAUSA") || x.getCoverageName().equals("INVALIDEZ TOTAL Y PERMANENTE"))
                    .count();

            List<CoveragePair> extraPremiumCoveragesList = coverageFileDTO.getCoverageList().stream()
                    .filter(x -> x.getAdditionalPremiumPercentage() != null && x.getAdditionalPremiumPercentage() > 0)
                    .collect(Collectors.toList());

            if (deathDisabilityValidation >= 2) { // MUERTE POR CUALQUIER CAUSA E INVALIDEZ
                PdfPCell insuranceRate = new PdfPCell();
                insuranceRate.setPhrase(new Phrase("Muerte, Invalidez: " +
                        HelpersMethods.formatNumberWithThousandsSeparatorsThreeDigits(coverageFileDTO.getTotalRate()) + "% (por ciento mensual)",
                        timesFontSize6));
                insuranceRate.setHorizontalAlignment(Element.ALIGN_LEFT);
                insuranceRate.setBorder(Rectangle.NO_BORDER);
                insuranceRate.setColspan(2);
                insuranceRateTable.addCell(insuranceRate);
            } else if (deathDisabilityValidation >= 1) { // MUERTE POR CUALQUIER CAUSA
                PdfPCell insuranceRate = new PdfPCell();
                insuranceRate.setPhrase(new Phrase("Muerte: " +
                        HelpersMethods.formatNumberWithThousandsSeparatorsThreeDigits(coverageFileDTO.getTotalRate()) + "% (por ciento mensual)",
                        timesFontSize6));
                insuranceRate.setHorizontalAlignment(Element.ALIGN_LEFT);
                insuranceRate.setBorder(Rectangle.NO_BORDER);
                insuranceRate.setColspan(2);
                insuranceRateTable.addCell(insuranceRate);
            } else { // MUERTE ACCIDENTAL
                PdfPCell insuranceRate = new PdfPCell();
                insuranceRate.setPhrase(new Phrase("Muerte Accidental: " +
                        HelpersMethods.formatNumberWithThousandsSeparatorsThreeDigits(coverageFileDTO.getTotalRate()) + "% (por ciento mensual)",
                        timesFontSize6));
                insuranceRate.setHorizontalAlignment(Element.ALIGN_LEFT);
                insuranceRate.setBorder(Rectangle.NO_BORDER);
                insuranceRate.setColspan(2);
                insuranceRateTable.addCell(insuranceRate);
            }

            insuranceRateTable.addCell(oneSpaceCell);

            PdfPCell acceptanceConditionTitle = new PdfPCell();
            acceptanceConditionTitle.setPhrase(new Phrase("CONDICIÓN DE ACEPTACIÓN: ", timesFontBoldSize6));
            acceptanceConditionTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            acceptanceConditionTitle.setBorder(Rectangle.NO_BORDER);
            insuranceRateTable.addCell(acceptanceConditionTitle);

            String coveragesNames = "";
            for (CoveragePair coveragePair : extraPremiumCoveragesList) {
                coveragesNames += coveragesNames.isEmpty() ? coveragePair.getCoverageName() : ", " + coveragePair.getCoverageName();
            }

            if (coverageFileDTO.getExtraPremiumAnswer() != null && !coverageFileDTO.getExtraPremiumAnswer().isEmpty()) {
                PdfPCell insuranceRate = new PdfPCell();
                insuranceRate.setPhrase(new Phrase("Cobertura con Extra Prima para " + coveragesNames + ", por contar con: " +
                        coverageFileDTO.getExtraPremiumAnswer(), timesFontSize6));
                insuranceRate.setHorizontalAlignment(Element.ALIGN_LEFT);
                insuranceRate.setBorder(Rectangle.NO_BORDER);
                insuranceRate.setColspan(3);
                insuranceRateTable.addCell(insuranceRate);

                if (coverageFileDTO.getExclusionComment() != null && !coverageFileDTO.getExclusionComment().isEmpty()) {
                    insuranceRateTable.addCell(oneSpaceCell);
                    PdfPCell exclusionComment = new PdfPCell();
                    exclusionComment.setPhrase(new Phrase("Cobertura con exclusión de " + coverageFileDTO.getExclusionComment(), timesFontSize6));
                    exclusionComment.setHorizontalAlignment(Element.ALIGN_LEFT);
                    exclusionComment.setBorder(Rectangle.NO_BORDER);
                    exclusionComment.setColspan(3);
                    insuranceRateTable.addCell(exclusionComment);
                }

            } else {
                if (coverageFileDTO.getExclusionComment() != null && !coverageFileDTO.getExclusionComment().isEmpty()) {
                    PdfPCell exclusionComment = new PdfPCell();
                    exclusionComment.setPhrase(new Phrase("Cobertura con exclusión de " + coverageFileDTO.getExclusionComment(), timesFontSize6));
                    exclusionComment.setHorizontalAlignment(Element.ALIGN_LEFT);
                    exclusionComment.setBorder(Rectangle.NO_BORDER);
                    exclusionComment.setColspan(3);
                    insuranceRateTable.addCell(exclusionComment);
                } else {
                    if (deathDisabilityValidation >= 2) { // MUERTE POR CUALQUIER CAUSA E INVALIDEZ
                        PdfPCell insuranceRate = new PdfPCell();
                        insuranceRate.setPhrase(new Phrase("Cobertura Total para Muerte, Invalidez", timesFontSize6));
                        insuranceRate.setHorizontalAlignment(Element.ALIGN_LEFT);
                        insuranceRate.setBorder(Rectangle.NO_BORDER);
                        insuranceRate.setColspan(2);
                        insuranceRateTable.addCell(insuranceRate);
                    } else if (deathDisabilityValidation >= 1) { // MUERTE POR CUALQUIER CAUSA
                        PdfPCell insuranceRate = new PdfPCell();
                        insuranceRate.setPhrase(new Phrase("Cobertura Total para Muerte", timesFontSize6));
                        insuranceRate.setHorizontalAlignment(Element.ALIGN_LEFT);
                        insuranceRate.setBorder(Rectangle.NO_BORDER);
                        insuranceRate.setColspan(2);
                        insuranceRateTable.addCell(insuranceRate);
                    } else { // MUERTE ACCIDENTAL
                        PdfPCell insuranceRate = new PdfPCell();
                        insuranceRate.setPhrase(new Phrase("Cobertura Total para Muerte Accidental", timesFontSize6));
                        insuranceRate.setHorizontalAlignment(Element.ALIGN_LEFT);
                        insuranceRate.setBorder(Rectangle.NO_BORDER);
                        insuranceRate.setColspan(2);
                        insuranceRateTable.addCell(insuranceRate);
                    }
                    insuranceRateTable.addCell(oneSpaceCell);
                }
            }

            insuranceRateTable.addCell(oneSpaceCell);
            PdfPCell clarification = new PdfPCell();
            clarification.setPhrase(new Phrase("Aclaración: Las tasas pueden variar, sujetas a las condiciones convenidas entre " +
                    "la Compañía y el Tomador del seguro.", timesFontSize6));
            clarification.setHorizontalAlignment(Element.ALIGN_LEFT);
            clarification.setBorder(Rectangle.NO_BORDER);
            clarification.setColspan(3);
            insuranceRateTable.addCell(clarification);

            document.add(insuranceRateTable);
//            document.add(jumpLine);

            PdfPTable premiumTable = new PdfPTable(5);
            premiumTable.setWidthPercentage(100);

            PdfPCell insurancePremiumTitle = new PdfPCell();
            insurancePremiumTitle.setPhrase(new Phrase("TASA DEL SEGURO:", timesFontBoldSize6));
            insurancePremiumTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            insurancePremiumTitle.setBorder(Rectangle.NO_BORDER);
            insurancePremiumTitle.setColspan(1);
            premiumTable.addCell(insurancePremiumTitle);
            PdfPCell insurancePremiumText = new PdfPCell();
            insurancePremiumText.setPhrase(new Phrase("El monto resultante entre multiplicar la tasa con el saldo insoluto del período.",
                    timesFontSize6));
            insurancePremiumText.setHorizontalAlignment(Element.ALIGN_LEFT);
            insurancePremiumText.setBorder(Rectangle.NO_BORDER);
            insurancePremiumText.setColspan(4);
            premiumTable.addCell(insurancePremiumText);

            PdfPCell paymentMethodTitle = new PdfPCell();
            paymentMethodTitle.setPhrase(new Phrase("FORMA DE PAGO:", timesFontBoldSize6));
            paymentMethodTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentMethodTitle.setBorder(Rectangle.NO_BORDER);
            paymentMethodTitle.setColspan(1);
            premiumTable.addCell(paymentMethodTitle);
            PdfPCell paymentMethodText = new PdfPCell();
            paymentMethodText.setPhrase(new Phrase("Al contado y por anticipado (a través del Tomador del Seguro).",
                    timesFontSize6));
            paymentMethodText.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentMethodText.setBorder(Rectangle.NO_BORDER);
            paymentMethodText.setColspan(4);
            premiumTable.addCell(paymentMethodText);

            document.add(premiumTable);
//            document.add(jumpLine);

            // #END INSURANCE INFO

            // #INSURANCE INFO RISKS AND EXCLUSIONS LARGE TEXT
            if (deathDisabilityValidation >= 2) { // MUERTE POR CUALQUIER CAUSA E INVALIDEZ
                Paragraph anyCauseDeathRisksAndExclusions = new Paragraph("EXCLUSIONES Y RIESGOS NO CUBIERTOS – COBERTURA DE MUERTE:",
                        timesFontBoldSize7);
                anyCauseDeathRisksAndExclusions.setAlignment(Element.ALIGN_LEFT);
//                document.add(anyCauseDeathRisksAndExclusions);
                Paragraph disabilityExclusions = new Paragraph("EXCLUSIONES – COBERTURA DE INVALIDEZ TOTAL Y PERMANENTE:", timesFontBoldSize7);
                disabilityExclusions.setAlignment(Element.ALIGN_LEFT);

                PdfPTable anyCauseDeathRisksAndExclusionsDetailTable = new PdfPTable(2);
                anyCauseDeathRisksAndExclusionsDetailTable.setWidthPercentage(95);

                PdfPCell detail = new PdfPCell();
                detail.setBorder(Rectangle.NO_BORDER);
                detail.setHorizontalAlignment(Element.ALIGN_LEFT);

                detail.addElement(anyCauseDeathRisksAndExclusions);
                anyCauseDeathRisksAndExclusionsDetailTable.addCell(detail);
                detail.setPhrase(new Phrase());
                detail.addElement(disabilityExclusions);
                anyCauseDeathRisksAndExclusionsDetailTable.addCell(detail);

                Phrase anyDeathDetail = new Phrase("a)\tIntervención directa o indirecta del Asegurado en actos delictivos que le ocasionen la " +
                        "muerte;\n" +
                        "b)\tSi el Asegurado participa como conductor o acompañante, profesionalmente o como aficionado, en " +
                        "competencias o ensayos de velocidad so resistencia, en cualquier clase de vehículo, terrestre, acuático o aéreo, a motor o no, " +
                        "prácticas de paracaídas, montañismo, ala delta, parapente, artes marciales, boxeo u otros deportes o disciplinas de peleas, " +
                        "equitación, carreras de caballo y/o práctica de deportes extremos, salvo que se hayan declarado previamente y que la Compañía " +
                        "haya aceptado de forma expresa;\n" +
                        "c)\tSi el Asegurado realiza operaciones o viajes submarinos o en transportes aéreos no autorizados " +
                        "para transporte de pasajeros;\n" +
                        "d)\tLa participación del Asegurado en cualquier maniobra, experimento, exhibición, desafío o actividad " +
                        "notoriamente peligrosa, entendiéndose por “acto notoriamente peligroso” aquellas donde se pone en grave peligro la vida e " +
                        "integridad física de las personas;\n" +
                        "e)\tGuerra internacional o civil (declarada o no), invasión, actos de enemigos extranjeros, " +
                        "hostilidades u operaciones bélicas, insurrección, sublevación, rebelión, sedición, motín, huelga, guerrilla, revolución o " +
                        "hechos que las leyes u autoridades califiquen como delitos contra la seguridad del Estado;\n" +
                        "f)\tFisión, fusión nuclear o contaminación radioactiva;\n" +
                        "g)\tEnfermedad grave congénita y enfermedad preexistente conocida contraída y/o diagnosticada con " +
                        "anterioridad al ingreso al seguro;\n" +
                        "h)\tSuicidio suscitado dentro de los dos (2) primeros años a partir del desembolso del " +
                        "préstamo;\n" +
                        "i)\tVIH/SIDA;\n" +
                        "j)\tDaños auto infligidos y cuando el Asegurado esté bajo los efectos de drogas ilícitas;\n" +
                        "k)\tEpidemias declaradas como tal por las entidades gubernamentales competentes;\n" +
                        "l)\tFraude al seguro o su intento.", timesFontSize7);

                detail.setPhrase(anyDeathDetail);
                anyCauseDeathRisksAndExclusionsDetailTable.addCell(detail);

                Phrase disabilityDetail = new Phrase("a)\tRiña, pelea o agresión, salvo que sea en defensa de persona, propia o de un bien;\n" +
                        "b)\tLa utilización por el Asegurado de medios de transporte aéreo, salvo en calidad de pasajero de " +
                        "líneas aéreas debidamente autorizadas para el transporte público;\n" +
                        "c)\tParticipación del Asegurado en carreras de velocidad o resistencia, concursos, desafíos o todo " +
                        "acto notoriamente peligroso, entendido por “acto notoriamente peligroso” aquellas donde se pone en grave peligro la vida e " +
                        "integridad física de las personas, siempre que no haya sido declarado y aceptado por la Compañía;\n" +
                        "d)\tIntento de suicidio, heridas o lesiones corporales inferidas al Asegurado por sí mismo o por " +
                        "el beneficiario de la póliza o por terceros con su consentimiento;\n" +
                        "e)\tTodo hecho ilegal que el Asegurado cometa o trate de cometer.\n" +
                        "f)\tQue el asegurado se encuentre en estado de ebriedad o bajo los efectos de drogas o alucinógenos.\n" +
                        "g)\tFalsas declaraciones, omisión o reticencia del Asegurado que puedan influir en la comprobación " +
                        "de su estado de invalidez.\n" +
                        "h)\tLas exclusiones estipuladas para la cobertura principal.", timesFontSize7);

                detail.setPhrase(disabilityDetail);
                anyCauseDeathRisksAndExclusionsDetailTable.addCell(detail);

                document.add(anyCauseDeathRisksAndExclusionsDetailTable);

            } else if (deathDisabilityValidation >= 1) { // MUERTE POR CUALQUIER CAUSA
                Paragraph risksAndExclusionsAnyCauseDeath = new Paragraph("EXCLUSIONES Y RIESGOS NO CUBIERTOS – COBERTURA DE MUERTE:",
                        timesFontBoldSize7);
                risksAndExclusionsAnyCauseDeath.setAlignment(Element.ALIGN_LEFT);
                document.add(risksAndExclusionsAnyCauseDeath);

                PdfPTable anyCauseDeathRisksAndExclusionsDetailTable = new PdfPTable(1);
                anyCauseDeathRisksAndExclusionsDetailTable.setWidthPercentage(95);

                PdfPCell detail = new PdfPCell();
                detail.setBorder(Rectangle.NO_BORDER);
                detail.setHorizontalAlignment(Element.ALIGN_LEFT);

                Phrase anyDeathDetail = new Phrase("a)\tIntervención directa o indirecta del Asegurado en actos delictivos que le ocasionen la " +
                        "muerte;\n" +
                        "b)\tSi el Asegurado participa como conductor o acompañante, profesionalmente o como aficionado, en " +
                        "competencias o ensayos de velocidad so resistencia, en cualquier clase de vehículo, terrestre, acuático o aéreo, a motor o no, " +
                        "prácticas de paracaídas, montañismo, ala delta, parapente, artes marciales, boxeo u otros deportes o disciplinas de peleas, " +
                        "equitación, carreras de caballo y/o práctica de deportes extremos, salvo que se hayan declarado previamente y que la Compañía " +
                        "haya aceptado de forma expresa;\n" +
                        "c)\tSi el Asegurado realiza operaciones o viajes submarinos o en transportes aéreos no autorizados " +
                        "para transporte de pasajeros;\n" +
                        "d)\tLa participación del Asegurado en cualquier maniobra, experimento, exhibición, desafío o actividad " +
                        "notoriamente peligrosa, entendiéndose por “acto notoriamente peligroso” aquellas donde se pone en grave peligro la vida e " +
                        "integridad física de las personas;\n" +
                        "e)\tGuerra internacional o civil (declarada o no), invasión, actos de enemigos extranjeros, " +
                        "hostilidades u operaciones bélicas, insurrección, sublevación, rebelión, sedición, motín, huelga, guerrilla, revolución o " +
                        "hechos que las leyes u autoridades califiquen como delitos contra la seguridad del Estado;\n" +
                        "f)\tFisión, fusión nuclear o contaminación radioactiva;\n" +
                        "g)\tEnfermedad grave congénita y enfermedad preexistente conocida contraída y/o diagnosticada con " +
                        "anterioridad al ingreso al seguro;\n" +
                        "h)\tSuicidio suscitado dentro de los dos (2) primeros años a partir del desembolso del " +
                        "préstamo;\n" +
                        "i)\tVIH/SIDA;\n" +
                        "j)\tDaños auto infligidos y cuando el Asegurado esté bajo los efectos de drogas ilícitas;\n" +
                        "k)\tEpidemias declaradas como tal por las entidades gubernamentales competentes;\n" +
                        "l)\tFraude al seguro o su intento.", timesFontSize7);

                detail.setPhrase(anyDeathDetail);
                anyCauseDeathRisksAndExclusionsDetailTable.addCell(detail);
                document.add(anyCauseDeathRisksAndExclusionsDetailTable);
            } else { // MUERTE ACCIDENTAL
                Paragraph risksAndExclusionsAnyCauseDeath = new Paragraph("EXCLUSIONES Y RIESGOS NO CUBIERTOS – COBERTURA DE MUERTE ACCIDENTAL:",
                        timesFontBoldSize7);
                risksAndExclusionsAnyCauseDeath.setAlignment(Element.ALIGN_LEFT);
                document.add(risksAndExclusionsAnyCauseDeath);

                PdfPTable anyCauseDeathRisksAndExclusionsDetailTable = new PdfPTable(1);
                anyCauseDeathRisksAndExclusionsDetailTable.setWidthPercentage(95);

                PdfPCell detail = new PdfPCell();
                detail.setBorder(Rectangle.NO_BORDER);
                detail.setHorizontalAlignment(Element.ALIGN_LEFT);

                Phrase anyDeathDetail = new Phrase("a)\tIntervención directa o indirecta del Asegurado en actos delictivos que le ocasionen la " +
                        "muerte;\n" +
                        "b)\tSi el Asegurado participa como conductor o acompañante, profesionalmente o como aficionado, en " +
                        "competencias o ensayos de velocidad so resistencia, en cualquier clase de vehículo, terrestre, acuático o aéreo, a motor o no, " +
                        "prácticas de paracaídas, montañismo, ala delta, parapente, artes marciales, boxeo u otros deportes o disciplinas de peleas, " +
                        "equitación, carreras de caballo y/o práctica de deportes extremos, salvo que se hayan declarado previamente y que la Compañía " +
                        "haya aceptado de forma expresa;\n" +
                        "c)\tSi el Asegurado realiza operaciones o viajes submarinos o en transportes aéreos no autorizados " +
                        "para transporte de pasajeros;\n" +
                        "d)\tLa participación del Asegurado en cualquier maniobra, experimento, exhibición, desafío o actividad " +
                        "notoriamente peligrosa, entendiéndose por “acto notoriamente peligroso” aquellas donde se pone en grave peligro la vida e " +
                        "integridad física de las personas;\n" +
                        "e)\tGuerra internacional o civil (declarada o no), invasión, actos de enemigos extranjeros, " +
                        "hostilidades u operaciones bélicas, insurrección, sublevación, rebelión, sedición, motín, huelga, guerrilla, revolución o " +
                        "hechos que las leyes u autoridades califiquen como delitos contra la seguridad del Estado;\n" +
                        "f)\tFisión, fusión nuclear o contaminación radioactiva;\n" +
                        "g)\tEnfermedad grave congénita y enfermedad preexistente conocida contraída y/o diagnosticada con " +
                        "anterioridad al ingreso al seguro;\n" +
                        "h)\tSuicidio suscitado dentro de los dos (2) primeros años a partir del desembolso del " +
                        "préstamo;\n" +
                        "i)\tVIH/SIDA;\n" +
                        "j)\tDaños auto infligidos y cuando el Asegurado esté bajo los efectos de drogas ilícitas;\n" +
                        "k)\tEpidemias declaradas como tal por las entidades gubernamentales competentes;\n" +
                        "l)\tFraude al seguro o su intento.", timesFontSize7);

                detail.setPhrase(anyDeathDetail);
                anyCauseDeathRisksAndExclusionsDetailTable.addCell(detail);
                document.add(anyCauseDeathRisksAndExclusionsDetailTable);
            }
            // #END INSURANCE INFO RISKS AND EXCLUSIONS LARGE TEXT

            // #SINISTER PROCEDURES
            Paragraph sinisterProceduresDetail = new Paragraph("", timesFontBoldSize6);
            Phrase sinisterProceduresTitle = new Phrase("PROCEDIMIENTO EN CASO DE SINIESTRO:\n", timesFontBoldSize6);
            Phrase sinisterProceduresText = new Phrase("El Asegurado o Beneficiario, tan pronto y a más tardar dentro de los tres (3) días " +
                    "de tener conocimiento del siniestro, deben comunicar tal hecho a la Compañía, salvo fuerza mayor o impedimento justificado. Para " +
                    "establecer la procedencia del siniestro la Compañía solicitará lo siguiente:", timesFontSize6);
            sinisterProceduresDetail.add(sinisterProceduresTitle);
            sinisterProceduresDetail.add(sinisterProceduresText);
            sinisterProceduresDetail.setAlignment(Element.ALIGN_LEFT);
            sinisterProceduresDetail.setMultipliedLeading(1.0f);
            document.add(sinisterProceduresDetail);

            document.add(jumpLine);

            if (deathDisabilityValidation >= 2) { // MUERTE POR CUALQUIER CAUSA E INVALIDEZ
                PdfPTable sinisterProceduresTable = new PdfPTable(2);
                sinisterProceduresTable.setWidthPercentage(95);

                PdfPCell deathTitleCell = new PdfPCell();
                deathTitleCell.setPhrase(new Phrase("Para el caso de Muerte", timesFontBoldSize6));
                deathTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sinisterProceduresTable.addCell(deathTitleCell);
                PdfPCell disabilityTitleCell = new PdfPCell();
                disabilityTitleCell.setPhrase(new Phrase("Para el caso de Invalidez Total y Permanente", timesFontBoldSize6));
                disabilityTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sinisterProceduresTable.addCell(disabilityTitleCell);

                PdfPCell detail = new PdfPCell();
                detail.setHorizontalAlignment(Element.ALIGN_LEFT);
                if (burialValidation > 0) {
                    Paragraph detailBurial = new Paragraph("");
                    Phrase points = new Phrase("a)\tNota de denuncia del siniestro, aclarando la fecha en la cual el Tomador toma conocimiento del " +
                            "siniestro.\n" +
                            "b)\tCertificado de Cobertura Individual a nombre del Asegurado\n" +
                            "c)\tDeclaración Jurada de Salud (documento original)\n" +
                            "d)\tCertificado de Defunción (original) expedido en Oficialía de Registro Civil,\n" +
                            "e)\tCertificado Único de Defunción (original o copia legalizada), indicando la causa primaria, secundaria y la causa agravante del Muerte del Asegurado,\n" +
                            "f)\tCertificado de nacimiento y/o fotocopia de cédula de identidad del Asegurado (copia simple).\n" +
                            "g)\tInforme FELCC o Transito, en caso de muerte accidental (original o copia legalizada),\n" +
                            "h)\tHistorial Clínico foliado y en su totalidad, en caso de muerte por enfermedad o natural (copia legalizada o simple),\n" +
                            "i)\tContrato de compra de terreno (copia simple),\n" +
                            "j)\tExtracto o Liquidación emitido por el Tomador.\n", timesFontSize6);
                    Phrase burialClarification = new Phrase("Para la Cobertura de Sepelio: \n", timesFontBoldSize6);
                    Phrase burialClarificationSecondSection = new Phrase("Aplican los incisos a), b), d) y Fotocopia del Carnet de " +
                            "Identidad del (los) Beneficiarios.\n", timesFontSize6);
                    detailBurial.add(points);
                    detailBurial.add(burialClarification);
                    detailBurial.add(burialClarificationSecondSection);
                    detailBurial.setMultipliedLeading(1.0f);
                    detailBurial.setAlignment(Element.ALIGN_LEFT);
                    detail.addElement(detailBurial);
                } else {
                    detail.setPhrase(new Phrase("a)\tNota de denuncia del siniestro, aclarando la fecha en la cual el Tomador toma conocimiento del " +
                            "siniestro.\n" +
                            "b)\tCertificado de Cobertura Individual a nombre del Asegurado\n" +
                            "c)\tDeclaración Jurada de Salud (documento original)\n" +
                            "d)\tCertificado de Defunción (original) expedido en Oficialía de Registro Civil,\n" +
                            "e)\tCertificado Único de Defunción (original o copia legalizada), indicando la causa primaria, secundaria y la causa " +
                            "agravante del Muerte del Asegurado,\n" +
                            "f)\tCertificado de nacimiento y/o fotocopia de cédula de identidad del Asegurado (copia simple).\n" +
                            "g)\tInforme FELCC o Transito, en caso de muerte accidental (original o copia legalizada),\n" +
                            "h)\tHistorial Clínico foliado y en su totalidad, en caso de muerte por enfermedad o natural (copia legalizada o simple),\n" +
                            "i)\tContrato de compra de terreno (copia simple),\n" +
                            "j)\tExtracto o Liquidación emitido por el Tomador.\n", timesFontSize6));
                }
                sinisterProceduresTable.addCell(detail);

                detail.setPhrase(new Phrase("a)\tNota de denuncia del siniestro, aclarando la fecha en la cual el Tomador toma conocimiento del " +
                        "siniestro.\n" +
                        "b)\tCertificado de nacimiento y/o fotocopia de cédula de identidad del Asegurado,\n" +
                        "c)\tCertificado de Cobertura Individual a nombre del Asegurado\n" +
                        "d)\tDeclaración Jurada de Salud (documento original)\n" +
                        "e)\tInforme FELCC o Tránsito (en caso accidente),\n" +
                        "f)\tInforme Médico detallado del médico o médicos que hayan tratado al Asegurado, con indicación del origen, de la naturaleza, " +
                        "del desarrollo y de las consecuencias de la enfermedad o de las lesiones causantes de la invalidez, así como de la probable " +
                        "duración de la misma,\n" +
                        "g)\tDeclaración o Dictamen de Calificación Médica de Invalidez, emitida por un médico calificador debidamente registrado en la " +
                        "APS, o el Instituto Nacional de Salud Ocupacional (INSO) o la Entidad Encargada de Calificar (EEC),\n" +
                        "h)\tContrato de compra de terreno (copia simple),\n" +
                        "i)\tExtracto o Liquidación emitido por el Tomador.", timesFontSize6));
                sinisterProceduresTable.addCell(detail);

                document.add(sinisterProceduresTable);

            } else if (deathDisabilityValidation >= 1) { // MUERTE POR CUALQUIER CAUSA
                PdfPTable sinisterProceduresTable = new PdfPTable(1);
                sinisterProceduresTable.setWidthPercentage(95);

                PdfPCell deathTitleCell = new PdfPCell();
                deathTitleCell.setPhrase(new Phrase("Para el caso de Muerte", timesFontBoldSize6));
                deathTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sinisterProceduresTable.addCell(deathTitleCell);

                PdfPCell detail = new PdfPCell();
                detail.setHorizontalAlignment(Element.ALIGN_LEFT);
                detail.setPhrase(new Phrase("a)\tNota de denuncia del siniestro, aclarando la fecha en la cual el Tomador toma conocimiento del " +
                        "siniestro.\n" +
                        "b)\tCertificado de Cobertura Individual a nombre del Asegurado\n" +
                        "c)\tDeclaración Jurada de Salud (documento original)\n" +
                        "d)\tCertificado de Defunción (original) expedido en Oficialía de Registro Civil,\n" +
                        "e)\tCertificado Único de Defunción (original o copia legalizada), indicando la causa primaria, secundaria y la causa " +
                        "agravante del Muerte del Asegurado,\n" +
                        "f)\tCertificado de nacimiento y/o fotocopia de cédula de identidad del Asegurado (copia simple).\n" +
                        "g)\tInforme FELCC o Transito, en caso de muerte accidental (original o copia legalizada),\n" +
                        "h)\tHistorial Clínico foliado y en su totalidad, en caso de muerte por enfermedad o natural (copia legalizada o simple),\n" +
                        "i)\tContrato de compra de terreno (copia simple),\n" +
                        "j)\tExtracto o Liquidación emitido por el Tomador.\n", timesFontSize6));

                sinisterProceduresTable.addCell(detail);
                document.add(sinisterProceduresTable);

            } else { // MUERTE ACCIDENTAL
                PdfPTable sinisterProceduresTable = new PdfPTable(1);
                sinisterProceduresTable.setWidthPercentage(95);

                PdfPCell deathTitleCell = new PdfPCell();
                deathTitleCell.setPhrase(new Phrase("Para el caso de Muerte Accidental", timesFontBoldSize6));
                deathTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sinisterProceduresTable.addCell(deathTitleCell);

                PdfPCell detail = new PdfPCell();
                detail.setHorizontalAlignment(Element.ALIGN_LEFT);
                detail.setPhrase(new Phrase("a)\tNota de denuncia del siniestro, aclarando la fecha en la cual el Tomador toma conocimiento del " +
                        "siniestro.\n" +
                        "b)\tCertificado de Cobertura Individual a nombre del Asegurado\n" +
                        "c)\tDeclaración Jurada de Salud (documento original)\n" +
                        "d)\tCertificado de Defunción (original) expedido en Oficialía de Registro Civil,\n" +
                        "e)\tCertificado Único de Defunción (original o copia legalizada), indicando la causa primaria, secundaria y la causa agravante " +
                        "del Muerte del Asegurado,\n" +
                        "f)\tCertificado de nacimiento y/o fotocopia de cédula de identidad del Asegurado (copia simple).\n" +
                        "g)\tInforme FELCC o Transito, en caso de muerte accidental (original o copia legalizada),\n" +
                        "h)\tHistorial Clínico foliado y en su totalidad, (copia legalizada o simple),\n" +
                        "i)\tContrato de compra de terreno (copia simple),\n" +
                        "j)\tExtracto o Liquidación emitido por el Tomador.", timesFontSize6));

                sinisterProceduresTable.addCell(detail);
                document.add(sinisterProceduresTable);
            }
            document.add(jumpLine);
            // #END SINISTER PROCEDURES

            // #SINISTER PROCEDURES LARGE TEXT

            PdfPTable testTable = new PdfPTable(1);
            testTable.setWidthPercentage(100);

            Paragraph sinisterProceduresLargeText = new Paragraph("");
            Phrase voluntaryAdherenceTitle = new Phrase("ADHESIÓN VOLUNTARIA DEL ASEGURADO: ", timesFontBoldSize6);
            Phrase voluntaryAdherenceText = new Phrase("El Asegurado se adhiere voluntariamente al presente seguro, cuya indemnización en caso " +
                    "de siniestro será a favor del Tomador del seguro. Para la cancelación del presente seguro el Asegurado únicamente debe presentar " +
                    "una carta firmada dirigida al Tomador o a la Compañía solicitando su baja del seguro, misma que surtirá efecto inmediato desde la " +
                    "fecha de su recepción.\n", timesFontSize6);
            Phrase coverageActivationTitle = new Phrase("ACTIVACIÓN DE LA COBERTURA: ", timesFontBoldSize6);
            Phrase coverageActivationText = new Phrase("Para los casos sin requisitos médicos la cobertura de la póliza se activará a partir " +
                    "del momento del desembolso del préstamo o de la habilitación de la tarjeta de crédito, y para los casos en los que corresponda " +
                    "el cumplimiento de requisitos médicos, bajo aceptación expresa por parte de la Compañía.\n", timesFontSize6);
            Phrase paymentSlipTitle = new Phrase("INCUMPLIMIENTO AL PAGO DE PRIMAS: ", timesFontBoldSize6);
            Phrase paymentSlipText = new Phrase("Se otorgará cobertura mientras las primas se paguen de forma continua, el incumplimiento en el " +
                    "pago de las primas, dentro de los plazos fijados, suspende la vigencia de la póliza de seguro.\n", timesFontSize6);
            Phrase premiumPaymentTitle = new Phrase("PAGO DE PRIMAS: ", timesFontBoldSize6);
            Phrase premiumPaymentText = new Phrase("El pago de la prima de seguro deberá ser efectuado por el Asegurado al Tomador otorgante del " +
                    "crédito, en las fechas según cronograma de amortización del contrato de préstamo. No incurre en mora el Asegurado, si el lugar " +
                    "del pago o el domicilio han sido cambiados sin su conocimiento. Una vez cobradas las primas de los Asegurados es obligación del " +
                    "Tomador, por cuenta de estos, pagar las primas del presente seguro a la Compañía, conforme plazos establecidos en las Condiciones " +
                    "Particulares. La cobertura individual del seguro caduca automáticamente por falta de pago de las primas y vencido el plazo de " +
                    "treinta (30) días, quedando la Compañía libre de cualquier responsabilidad.\n", timesFontSize6);
            Phrase nonContestableTitle = new Phrase("NO IMPUGNABILIDAD: ", timesFontBoldSize6);
            Phrase nonContestableText = new Phrase("Esta póliza no es impugnable después de dos (2) años de vigencia ininterrumpida de la " +
                    "cobertura individual del Asegurado excepto en lo referente a la falta de pago de primas, de acuerdo a lo dispuesto en el Art. " +
                    "1138 del Código de Comercio. En virtud a esto, la Compañía podrá, dentro de los primeros dos (2) años, impugnar la validez de " +
                    "este contrato por omisión o inexactitud de las declaraciones del Asegurado hechas en la solicitud, en su declaración de estado de " +
                    "salud o con motivo del examen médico. En la renovación anual, la Compañía podrá requerir nuevos exámenes médicos u otras pruebas " +
                    "de asegurabilidad para establecer las condiciones de la renovación del seguro. En todo caso podrá demandar su nulidad por dolo " +
                    "conforme a Ley.\n", timesFontSize6);
            Phrase compensationPaymentTitle = new Phrase("PAGO DE INDEMNIZACIONES: ", timesFontBoldSize6);
            Phrase compensationPaymentText = new Phrase("La Compañía debe pronunciarse sobre el derecho del Tomador o Beneficiario a cobrar la " +
                    "indemnización dentro de los treinta (30) días de recibir la información y evidencias que le fueran solicitadas. Se dejará " +
                    "constancia escrita de la fecha del recibo de la información y evidencias a efectos del cómputo del plazo. La solicitud de " +
                    "complementos por parte de la Compañía no podrá extenderse por más de dos (2) veces a partir de la primera solicitud de informes " +
                    "y evidencia, debiendo pronunciarse dentro el plazo establecido y de manera definitiva sobre el derecho del asegurado, después de " +
                    "la entrega por parte del asegurado del último requerimiento de información.\n" +
                    "El plazo de treinta (30) días mencionado, fenece con la aceptación o rechazo del siniestro o con la solicitud de la Compañía al " +
                    "Tomador que se complemente la información y evidencia del hecho, y no vuelven a correr hasta que el Tomador o Beneficiario haya " +
                    "cumplido con tales requerimientos. En caso de demora u omisión del Tomador o Beneficiario en proporcionar la información y " +
                    "evidencias sobre el fallecimiento del Asegurado, el término señalado no corre hasta el cumplimiento de estas obligaciones. " +
                    "El silencio de la Compañía, vencido el término para pronunciarse o vencidas las solicitudes de complementación, importa la " +
                    "aceptación del reclamo.\n" +
                    "En caso de conformidad, la Compañía realizará el pago de la indemnización por mediación del Tomador, dentro de los quince (15) días " +
                    "siguientes al término del plazo anterior y contra entrega del finiquito correspondiente.\n", timesFontSize6);
            Phrase conciliationTitle = new Phrase("CONCILIACIÓN Y/O ARBITRAJE: ", timesFontBoldSize6);
            Phrase conciliationText = new Phrase("Las controversias de hecho suscitadas entre las partes, sobre la determinación de las causas del " +
                    "siniestro, serán resueltas mediante Peritaje, nombrando cada parte los peritos de su parte que correspondan. De no resolverse la " +
                    "controversia, las partes podrán acudir a un tercer perito dirimidor, nombrado de común acuerdo, dando por valido su " +
                    "pronunciamiento. Los costos de los peritos de parte serán asumidos por cada una de las partes y el costo del perito dirimidor será " +
                    "asumido en montos iguales por ambas partes. Las partes podrán acordar de principio, nombrar un solo perito dirimidor dando por " +
                    "válido su pronunciamiento.\n" +
                    "Las controversias de derecho suscitadas entre las partes, sobre la naturaleza y alcance del contrato de seguro, serán resueltas " +
                    "en única e inapelable instancia de Arbitraje, de acuerdo a lo previsto en la Ley de Conciliación y Arbitraje Nº 708 de fecha 25 de " +
                    "junio de 2015. La Autoridad de Fiscalización y Control de Pensiones y Seguros – APS podrá fungir como instancia de conciliación, " +
                    "para todo siniestro cuya cuantía no supere el monto de UFV 100.000 (Cien Mil 00/100 Unidades de Fomento de Vivienda). Si por " +
                    "esta vía no existiera un acuerdo, la APS podrá conocer y resolver la controversia por resolución administrativa debidamente " +
                    "motivada.\n", timesFontSize6);
            Phrase mainPolicyTitle = new Phrase("PÓLIZA PRINCIPAL: ", timesFontBoldSize6);
            Phrase mainPolicyText = new Phrase("El presente documento resume los términos y condiciones de la póliza principal suscrita por " +
                    "Santa Cruz Vida y Salud Seguros y Reaseguros Personales S.A. y el Tomador del seguro, el mismo que respalda este certificado y " +
                    "prevalece sobre cualquier otro texto informativo. Las partes reconocen que las condiciones del Seguro y la póliza, pueden cambiar " +
                    "y que en todos los casos el Asegurado debe informarse sobre los cambios o modificaciones en la cobertura de los seguros " +
                    "contratados por el Tomador.", timesFontSize6);
            sinisterProceduresLargeText.add(voluntaryAdherenceTitle);
            sinisterProceduresLargeText.add(voluntaryAdherenceText);
            sinisterProceduresLargeText.add(coverageActivationTitle);
            sinisterProceduresLargeText.add(coverageActivationText);
            sinisterProceduresLargeText.add(paymentSlipTitle);
            sinisterProceduresLargeText.add(paymentSlipText);
            sinisterProceduresLargeText.add(premiumPaymentTitle);
            sinisterProceduresLargeText.add(premiumPaymentText);
            sinisterProceduresLargeText.add(nonContestableTitle);
            sinisterProceduresLargeText.add(nonContestableText);
            sinisterProceduresLargeText.add(compensationPaymentTitle);
            sinisterProceduresLargeText.add(compensationPaymentText);
            sinisterProceduresLargeText.add(conciliationTitle);
            sinisterProceduresLargeText.add(conciliationText);
            sinisterProceduresLargeText.add(mainPolicyTitle);
            sinisterProceduresLargeText.add(mainPolicyText);
            sinisterProceduresLargeText.setAlignment(Element.ALIGN_JUSTIFIED);
            sinisterProceduresLargeText.setMultipliedLeading(1f);
            PdfPCell testCell = new PdfPCell();
            testCell.setBorder(Rectangle.NO_BORDER);
            testCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            testCell.addElement(sinisterProceduresLargeText);
            testTable.addCell(testCell);
            document.add(testTable);

            // #END SINISTER PROCEDURES LARGE TEXT

            // #SIGNS
            document.add(jumpLine);

            PdfPTable firmsFinish = new PdfPTable(4);
            firmsFinish.setWidthPercentage(100);
            firmsFinish.setHorizontalAlignment(Element.ALIGN_CENTER);

            String cellName = "Mauricio Franco Melazzini\nGerente Técnico";
            Paragraph cellNamePhrase = new Paragraph(cellName.toUpperCase(Locale.ROOT), timesFontBoldSize6);
            cellNamePhrase.setAlignment(Element.ALIGN_CENTER);
            Image imgFirst = Image.getInstance(properties.getPathImages() + "mauricioFrancoFirma.png");
            imgFirst.scaleAbsolute(150, 30);
            imgFirst.setAlignment(Image.ALIGN_CENTER);
            PdfPCell firstNameAuthFirm = new PdfPCell();
            firstNameAuthFirm.setBorder(Rectangle.NO_BORDER);
            firstNameAuthFirm.setHorizontalAlignment(Element.ALIGN_CENTER);
            firstNameAuthFirm.addElement(imgFirst);
            firstNameAuthFirm.addElement(cellNamePhrase);
            firmsFinish.addCell(firstNameAuthFirm);

            String cellName2 = "Mario Aguirre Durán\nGerente General";
            Paragraph cellName2Phrase = new Paragraph(cellName2.toUpperCase(Locale.ROOT), timesFontBoldSize6);
            cellName2Phrase.setAlignment(Element.ALIGN_CENTER);
            Image imgSecond = Image.getInstance(properties.getPathImages() + "marioAguirreFirma.jpg");
            imgSecond.scaleAbsolute(150, 30);
            imgSecond.setAlignment(Image.ALIGN_CENTER);
            PdfPCell secondNameAuthFirm = new PdfPCell();
            secondNameAuthFirm.setBorder(Rectangle.NO_BORDER);
            secondNameAuthFirm.setHorizontalAlignment(Element.ALIGN_CENTER);
            secondNameAuthFirm.addElement(imgSecond);
            secondNameAuthFirm.addElement(cellName2Phrase);
            firmsFinish.addCell(secondNameAuthFirm);

            firmsFinish.addCell(oneSpaceCell);

            Paragraph cellName3Phrase = new Paragraph("FIRMA ASEGURADO\n", timesFontBoldSize6);
            Paragraph cellName4Phrase = new Paragraph(coverageFileDTO.getInsuredName() + "\n", timesFontSize6);
            Calendar dateNow = DateUtils.getDateNowByGregorianCalendar();
            int actualYear = dateNow.get(Calendar.YEAR);
            int actualMonth = dateNow.get(Calendar.MONTH) + 1;
            int actualDay = dateNow.get(Calendar.DAY_OF_MONTH);
            String monthDate = actualMonth > 9 ? actualMonth + "" : "0" + actualMonth;
            String dayDateStr = actualDay > 9 ? actualDay + "" : "0" + actualDay;
            String actualDateStr = dayDateStr + " / " + monthDate + " / " + actualYear;
            Paragraph cellName5Phrase = new Paragraph("Fecha Emisión: " + actualDateStr, timesFontSize6);
            cellName3Phrase.setAlignment(Element.ALIGN_LEFT);
            if (coverageFileDTO.getDigitalFirm() != null) {
                byte[] imageByte;
                BASE64Decoder decoder = new BASE64Decoder();
                imageByte = decoder.decodeBuffer(coverageFileDTO.getDigitalFirm().getContent());
                Image img = Image.getInstance(imageByte);
                img.setAlignment(Image.ALIGN_LEFT);
                img.scaleAbsolute(150, 30);

                PdfPCell insuredNameAuthFirm = new PdfPCell();
                insuredNameAuthFirm.setBorder(Rectangle.NO_BORDER);
                insuredNameAuthFirm.setHorizontalAlignment(Element.ALIGN_LEFT);
                insuredNameAuthFirm.addElement(img);
                insuredNameAuthFirm.addElement(cellName3Phrase);
                insuredNameAuthFirm.addElement(cellName4Phrase);
                insuredNameAuthFirm.addElement(cellName5Phrase);
                firmsFinish.addCell(insuredNameAuthFirm);
            } else {
                Paragraph linesToAdd = new Paragraph("___________________________", timesFontSize6);
                linesToAdd.setAlignment(Element.ALIGN_LEFT);
                PdfPCell insuredNameAuthFirm = new PdfPCell();
                insuredNameAuthFirm.setBorder(Rectangle.NO_BORDER);
                insuredNameAuthFirm.setHorizontalAlignment(Element.ALIGN_LEFT);
                insuredNameAuthFirm.setVerticalAlignment(Element.ALIGN_BOTTOM);
                insuredNameAuthFirm.addElement(linesToAdd);
                insuredNameAuthFirm.addElement(cellName3Phrase);
                insuredNameAuthFirm.addElement(cellName4Phrase);
                insuredNameAuthFirm.addElement(cellName5Phrase);
                firmsFinish.addCell(insuredNameAuthFirm);
            }

            PdfPCell authFirms = new PdfPCell();
            authFirms.setBorder(Rectangle.NO_BORDER);
            authFirms.setColspan(2);
            if (coverageFileDTO.getDigitalFirm() != null) {
                authFirms.setPaddingTop(-10);
            }
            authFirms.setPhrase(new Phrase("FIRMAS AUTORIZADAS\n" +
                    "SANTA CRUZ VIDA Y SALUD SEGUROS Y REASEGUROS PERSONALES S.A.", timesFontBoldSize6));
            authFirms.setHorizontalAlignment(Element.ALIGN_CENTER);
            firmsFinish.addCell(authFirms);

            firmsFinish.addCell(oneSpaceCell);
            firmsFinish.addCell(oneSpaceCell);

            document.add(firmsFinish);

            // #END SIGNS

            document.close();

        } catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateDJSDocument(DjsFileDTO djsFileDTO) {
        Document document = new Document(GOVERMENT_LEGAL);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "times.ttf", "TIMES_NEW_ROMAN");
        FontFactory.register(properties.getPathFonts() + "timesbd.ttf", "TIMES_NEW_ROMAN_BOLD");

        List<Classifier> classifierListForRelationTypes = djsFileDTO.getClassifierListForRelationTypes();
        List<Classifier> classifierListForExtensionCi = djsFileDTO.getClassifierListForExtensionCi();
        List<Classifier> listActivity = djsFileDTO.getListActivity();
        List<Classifier> nationalityList = djsFileDTO.getNationalityList();
        List<Classifier> regionalList = djsFileDTO.getRegionalList();
        List<QuestionDTO> questionDTOList = djsFileDTO.getQuestionDTOList();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            Font arialFontItalicSize8 = FontFactory.getFont("TIMES_NEW_ROMAN", 5, Font.ITALIC, Color.BLACK); // ANTES 8 // ANTES 6
            Font arialFontSize10 = FontFactory.getFont("TIMES_NEW_ROMAN");
            arialFontSize10.setSize(6); // ANTES 9 // ANTES 7
            Font arialFontSize8 = FontFactory.getFont("TIMES_NEW_ROMAN");
            arialFontSize8.setSize(5); // ANTES 8 // ANTES 6
            Font arialFontBoldSize10 = FontFactory.getFont("TIMES_NEW_ROMAN_BOLD");
            arialFontBoldSize10.setSize(6); // ANTES 9 // ANTES 7
            Font arialFontBoldSize8 = FontFactory.getFont("TIMES_NEW_ROMAN_BOLD");
            arialFontBoldSize8.setSize(5); // ANTES 8 // ANTES 6
            Font arialFontBoldSize7 = FontFactory.getFont("TIMES_NEW_ROMAN_BOLD");
            arialFontBoldSize8.setSize(4); // ANTES 7 // ANTES 5
            Font arialFontSize7 = FontFactory.getFont("TIMES_NEW_ROMAN");
            arialFontSize7.setSize(4); // ANTES 7 // ANTES 5
            arialFontSize7.setColor(Color.BLUE);
            Font arialFontSize7v2 = FontFactory.getFont("TIMES_NEW_ROMAN");
            arialFontSize7v2.setSize(4); // ANTES 7 // ANTES 5
            Font arialFontSizeAct10 = FontFactory.getFont("TIMES_NEW_ROMAN");
            arialFontSizeAct10.setSize(7); // ANTES 10 // ANTES 8
            Font arialFontSize12 = FontFactory.getFont("TIMES_NEW_ROMAN");
            arialFontSize12.setSize(9); // ANTES 12 // ANTES 10
            Font arialFontSize11 = FontFactory.getFont("TIMES_NEW_ROMAN");
            arialFontSize11.setSize(8); // ANTES 11 // ANTES 9
            Font arialFontBoldSize12 = FontFactory.getFont("TIMES_NEW_ROMAN_BOLD");
            arialFontBoldSize12.setSize(9); // ANTES 12 // ANTES 10
            Font arialFontBoldSizeAct10 = FontFactory.getFont("TIMES_NEW_ROMAN_BOLD");
            arialFontBoldSizeAct10.setSize(7); // ANTES 10 // ANTES 8
            HeaderFooterPdfTemplate event = new HeaderFooterPdfTemplate(properties, 0l, djsFileDTO.getDjsNumber());
            writer.setPageEvent(event);
            document.open();

            Paragraph title = new Paragraph("", arialFontBoldSize10);
            title.setAlignment(Element.ALIGN_CENTER);
            Chunk firstTitle = new Chunk("SOLICITUD DE SEGURO COLECTIVO DE DESGRAVAMEN -");
            title.add(firstTitle);
            document.add(title);

            Paragraph secondTitle = new Paragraph("", arialFontBoldSizeAct10);
            secondTitle.setAlignment(Element.ALIGN_CENTER);
            Chunk secTitle = new Chunk("DECLARACIÓN JURADA DE SALUD No. SCS/" + djsFileDTO.getDjsNumber());
            secondTitle.add(secTitle);
            document.add(secondTitle);

            Paragraph subSubSTitle = new Paragraph("Formato aprobado y registrado mediante Resolución Administrativa APS/DS/ N° 155/2019 del 04/02/2019", arialFontSize8);
            subSubSTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subSubSTitle);

            Paragraph subSubTitle = new Paragraph("CÓDIGO DE REGISTRO: 212-934663-2020 10 010 3003", arialFontSizeAct10);
            subSubTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subSubTitle);

            Paragraph breakJump = new Paragraph("\n", arialFontSize7);
            document.add(breakJump);
            PdfPTable delimiterTable = new PdfPTable(1);
            delimiterTable.setWidthPercentage(100);
            PdfPCell delimiterCellNew = new PdfPCell();
            delimiterCellNew.setColspan(1);
            delimiterCellNew.setBorder(Rectangle.TOP);
            delimiterTable.addCell(delimiterCellNew);
            document.add(delimiterTable);

            Paragraph italicMessage = new Paragraph("ESTIMADO CLIENTE, FAVOR COMPLETAR LA INFORMACIÓN DETALLADA A CONTINUACIÓN UTILIZANDO LETRA LEGIBLE.", arialFontSize10);
            document.add(italicMessage);

            Chunk takerUser = new Chunk(djsFileDTO.getCompanyName(), arialFontBoldSizeAct10); //Verificar

            Paragraph takerOrCont = new Paragraph("\n\n\nTOMADOR/CONTRATANTE: ", arialFontSizeAct10);
            takerOrCont.add(takerUser);
            takerOrCont.setLeading(Element.ALIGN_JUSTIFIED);
            document.add(takerOrCont);

            //#region Insured Information

            Chunk stepOneText = new Chunk("DATOS PERSONALES:", arialFontBoldSizeAct10);
            stepOneText.setUnderline(0.2f, -2f);
            Paragraph stepOne = new Paragraph("", arialFontBoldSizeAct10);
            stepOne.add(stepOneText);
            document.add(stepOne);
            stepOne.setAlignment(Element.ALIGN_LEFT);

            //#region Insurer Data 2

            PdfPTable insuredData = new PdfPTable(48);
            insuredData.setTotalWidth(Utilities.millimetersToPoints(190));
            insuredData.setLockedWidth(true);
            insuredData.setHorizontalAlignment(Element.ALIGN_LEFT);

            insuredData.addCell(getCell("", 48, Element.ALIGN_BOTTOM));

            insuredData.addCell(getCell("Nombre Completo:", 9, Element.ALIGN_LEFT, true));
            insuredData.addCell(getCell(djsFileDTO.getInsuredValues().getNaturalPerson().getCompleteName(), 39, Element.ALIGN_JUSTIFIED));

            String genderValue = (djsFileDTO.getInsuredValues().getNaturalPerson().getGenderIdc() == 1)
                    ? "Hombre ( X ) / Mujer (  )" : "Hombre (  ) / Mujer ( X )";
            insuredData.addCell(getCell(genderValue, 12, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Fecha de Nacimiento:", 9, Element.ALIGN_LEFT, true));
            insuredData.addCell(getCell(HelpersMethods.formatStringOnlyLocalDateTime(djsFileDTO.getInsuredValues().getNaturalPerson().getBirthDate()), 5, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Nacionalidad:", 7, Element.ALIGN_LEFT, true));
            long nationalReferenceId = djsFileDTO.getInsuredValues().getNationalityIdc();
            String nationalityTest = nationalityList.stream().filter(x -> x.getReferenceId().equals(nationalReferenceId)).findFirst().get().getDescription();
            insuredData.addCell(getCell(nationalityTest, 15, Element.ALIGN_JUSTIFIED));


            String complementCi = djsFileDTO.getInsuredValues().getNaturalPerson().getComplement();
            String CINumber = (complementCi == null || complementCi.isEmpty() || complementCi.trim().isEmpty()) ?
                    djsFileDTO.getInsuredValues().getNaturalPerson().getIdentificationNumber() : djsFileDTO.getInsuredValues().getNaturalPerson().getIdentificationNumber() + "-" + complementCi;
            insuredData.addCell(getCell("Nº de C.I. o Pasaporte:", 9, Element.ALIGN_LEFT, true));
            insuredData.addCell(getCell(CINumber, 7, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Exp.:", 3, Element.ALIGN_LEFT, true));
            long extNaturalPersonId = djsFileDTO.getInsuredValues().getNaturalPerson().getExtIdc();
            String extStr = (extNaturalPersonId > 0) ? classifierListForExtensionCi.stream().filter(x -> x.getReferenceId().equals(extNaturalPersonId)).findFirst().get().getDescription() : "S/N";
            insuredData.addCell(getCell(extStr, 3, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Edad:", 3, Element.ALIGN_LEFT, true));
            insuredData.addCell(getCell(String.valueOf(djsFileDTO.getInsuredAge().intValue()), 3, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Peso (kg.):", 5, Element.ALIGN_LEFT, true));
            insuredData.addCell(getCell(HelpersMethods.formatNumberWithThousandsSeparators(djsFileDTO.getInsuredCriteria().getWeight()), 4, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Estatura (cm.):", 7, Element.ALIGN_LEFT, true));
            insuredData.addCell(getCell(HelpersMethods.formatNumberWithThousandsSeparators(djsFileDTO.getInsuredCriteria().getHeight()), 4, Element.ALIGN_JUSTIFIED));

            insuredData.completeRow();

            Direction djsDirectionInsuredPersonal = djsFileDTO.getInsuredValues().getDirections().stream()
                    .filter(x -> x.getDirectionTypeIdc() == DirectionTypeEnum.PERSONAL.getValue()).findAny().get();
            Direction djsDirectionInsuredWork = djsFileDTO.getInsuredValues().getDirections().stream()
                    .filter(x -> x.getDirectionTypeIdc() == DirectionTypeEnum.WORK.getValue()).findAny().get();

            insuredData.addCell(getCell("Nº Celular:", 9, Element.ALIGN_LEFT, true));
            String insuredCelf = (djsFileDTO.getInsuredValues().getTelephone() == null || djsFileDTO.getInsuredValues().getTelephone() == "") ? "S/N" : djsFileDTO.getInsuredValues().getTelephone();
            insuredData.addCell(getCell(insuredCelf, 8, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Telf. Domicilio:", 7, Element.ALIGN_LEFT, true));
            String insuredTelf = (djsDirectionInsuredPersonal.getCellPhone() == null || djsDirectionInsuredPersonal.getCellPhone() == "") ? "S/N" : djsDirectionInsuredPersonal.getCellPhone();
            insuredData.addCell(getCell(insuredTelf, 7, Element.ALIGN_JUSTIFIED));


            insuredData.addCell(getCell("Telf. Oficina:", 6, Element.ALIGN_LEFT, true));
            String insuredOffTelf = (djsDirectionInsuredWork.getCellPhone() == null || djsDirectionInsuredWork.getCellPhone() == "") ? "S/N" : djsDirectionInsuredWork.getCellPhone();
            insuredData.addCell(getCell(insuredOffTelf, 13, Element.ALIGN_JUSTIFIED));


            insuredData.addCell(getCell("Estado Civil:", 9, Element.ALIGN_LEFT, true));
            String civilStatus = djsFileDTO.getMaritalStatus().toUpperCase();
            insuredData.addCell(getCell(civilStatus, 9, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Profesión (estudios):", 12, Element.ALIGN_LEFT, true));
            String profetionD = (djsFileDTO.getInsuredValues().getNaturalPerson().getProfession() == null ||
                    djsFileDTO.getInsuredValues().getNaturalPerson().getProfession() == "") ?
                    "S/N" : djsFileDTO.getInsuredValues().getNaturalPerson().getProfession();
            insuredData.addCell(getCell(profetionD, 18, Element.ALIGN_JUSTIFIED));

            insuredData.completeRow(); //falta llenar mas cuadros

            insuredData.addCell(getCell("Lugar de Trabajo:", 9, Element.ALIGN_LEFT, true));
            String insuredWorkPlace = (djsFileDTO.getInsuredValues().getNaturalPerson().getWorkPlace() == null ||
                    djsFileDTO.getInsuredValues().getNaturalPerson().getWorkPlace() == "") ?
                    "S/N" : djsFileDTO.getInsuredValues().getNaturalPerson().getWorkPlace();
            insuredData.addCell(getCell(insuredWorkPlace, 39, Element.ALIGN_JUSTIFIED));

            insuredData.addCell(getCell("Actividad/Ocupación:", 9, Element.ALIGN_LEFT, true));
            long activityIdc = djsFileDTO.getInsuredValues().getActivityIdc();
            insuredData.addCell(getCell(listActivity.stream().filter(x -> x.getReferenceId().equals(activityIdc)).findFirst().get().getDescription().toUpperCase(), 39, Element.ALIGN_JUSTIFIED));

            document.add(insuredData);

            //#endregion

            //#endregion

            //#region Beneficiary

            Chunk stepTwoText = new Chunk("Beneficiario Cobertura de Sepelio", arialFontBoldSizeAct10);
            Paragraph stepTwo = new Paragraph("", arialFontBoldSizeAct10);
            stepTwo.add(stepTwoText);
            stepTwo.setAlignment(Element.ALIGN_LEFT);
            document.add(stepTwo);

            //#region Beneficiary List 2

            boolean beneficiaryUnderAge = false;
            List<String> beneficiareUnderNames = new ArrayList<>();
            List<String> beneficiareUnderRecip = new ArrayList<>();
            PdfPTable beneciaryTable = new PdfPTable(48);
            beneciaryTable.setTotalWidth(Utilities.millimetersToPoints(190));
            beneciaryTable.setLockedWidth(true);
            beneciaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            beneciaryTable.addCell(getCell("", 48, Element.ALIGN_BOTTOM));
            if (djsFileDTO.getLegalHeirs() == 0) {
                beneficiaryUnderAge = djsFileDTO.getBeneficiaryList().stream().filter(x -> x.getIsUnderAge() > 1).collect(Collectors.toList()).size() > 0
                        ? true : false;
                djsFileDTO.getBeneficiaryList().forEach(beneficiary -> {
                    String beneficiaryNames = HelpersMethods.fullName(beneficiary.getName(), beneficiary.getLastName(), beneficiary.getMotherLastName(), beneficiary.getMarriedLastName());
                    if (beneficiary.getIsUnderAge() > 1) {
                        beneficiareUnderNames.add(beneficiaryNames);
                        String benExt = beneficiary.getLegalExt() != null ? classifierListForExtensionCi.stream().filter(x -> x.getReferenceId().equals(beneficiary.getLegalExt().longValue())).findFirst().get().getDescription() : "";
                        beneficiareUnderRecip.add(beneficiary.getRepresentativeLegalName() + " - CI " + beneficiary.getLegalIdentification() + " "
                                + benExt);
                    }
                    beneciaryTable.addCell(getCell("Nombre Completo:", 9, Element.ALIGN_LEFT, true));
                    beneciaryTable.addCell(getCell(beneficiaryNames.toUpperCase(Locale.ROOT), 16, Element.ALIGN_JUSTIFIED));

                    beneciaryTable.addCell(getCell("Parentesco:", 5, Element.ALIGN_LEFT, true));
                    long relationIdc = beneficiary.getRelationshipIdc();
                    beneciaryTable.addCell(getCell(classifierListForRelationTypes.stream().filter(x -> x.getReferenceId().equals(relationIdc)).findFirst().get().getDescription().toUpperCase(), 7, Element.ALIGN_JUSTIFIED));

                    beneciaryTable.addCell(getCell("Porcentaje (%):", 7, Element.ALIGN_LEFT, true));
                    beneciaryTable.addCell(getCell(beneficiary.getPercentage().toString(), 4, Element.ALIGN_JUSTIFIED));
                });
            } else {
                beneciaryTable.addCell(getCell("", 2, Element.ALIGN_LEFT, true));
                beneciaryTable.addCell(getCell("Nombre Completo:", 7, Element.ALIGN_LEFT, true));
                beneciaryTable.addCell(getCell("HEREDERO(S) LEGAL(ES)", 19, Element.ALIGN_JUSTIFIED));

                beneciaryTable.addCell(getCell("Porcentaje (%):", 5, Element.ALIGN_LEFT, true));
                beneciaryTable.addCell(getCell("100", 15, Element.ALIGN_JUSTIFIED));
            }

            document.add(beneciaryTable);

            //#endregion

            //#endregion

            document.add(delimiterTable);

            //#region Questionary

            Chunk stepThreeText = new Chunk("(Favor marcar las respuestas con X)", arialFontBoldSizeAct10);
            Paragraph stepThree = new Paragraph("CUESTIONARIO ", arialFontBoldSizeAct10);
            stepThree.add(stepThreeText);
            stepThree.setAlignment(Element.ALIGN_LEFT);
            document.add(stepThree);

            float[] columnWidths = new float[]{10f, 100f, 100f, 30f, 20f};

            PdfPTable questionarieTable = new PdfPTable(5);
            questionarieTable.setWidths(columnWidths);
            questionarieTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            Stream.of("", "", "", "SI", "NO").forEach(headerTitle -> {
                arialFontSizeAct10.setSize(7); // ANTES 8
                PdfPCell headerTable = new PdfPCell();
                headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTable.setPhrase(new Phrase(headerTitle, arialFontSizeAct10));
                headerTable.setBorder(Rectangle.NO_BORDER);
                questionarieTable.addCell(headerTable);
            });

            djsFileDTO.getAnswers().forEach(answer -> {
                if (answer.getQuestionOrder() != null) {
                    arialFontSizeAct10.setSize(7); // ANTES 8
                    PdfPCell number = new PdfPCell();
                    number.setPhrase(new Phrase(answer.getQuestionOrder() + ". ", arialFontSizeAct10));
                    number.setBorder(Rectangle.NO_BORDER);
                    questionarieTable.addCell(number);

                    String questionFromList = questionDTOList.stream().filter(x -> x.getId().equals(answer.getQuestionId())).findFirst().get().getContent();
                    PdfPCell question = new PdfPCell();
                    question.setColspan(2);
                    question.setPhrase(new Phrase(questionFromList, arialFontSizeAct10));
                    question.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    question.setBorder(Rectangle.NO_BORDER);
                    questionarieTable.addCell(question);

                    PdfPCell yesCell = new PdfPCell();
                    yesCell.setPhrase(new Phrase(answer.getAffirmativeAnswer() ? "( X )" : "(    )", arialFontSizeAct10));
                    yesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    yesCell.setBorder(Rectangle.NO_BORDER);
                    questionarieTable.addCell(yesCell);

                    PdfPCell noCell = new PdfPCell();
                    noCell.setPhrase(new Phrase(answer.getAffirmativeAnswer() ? "(    )" : "( X )", arialFontSizeAct10));
                    noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    noCell.setBorder(Rectangle.NO_BORDER);
                    questionarieTable.addCell(noCell);
                }
            });
            document.add(questionarieTable);

            Chunk noteQuestionaryChunk = new Chunk("NOTA", arialFontBoldSizeAct10);
            Chunk noteQuestionary2Chunk = new Chunk(": En caso de haber contestado alguna respuesta (de la Nº 1 " +
                    "a 8) Afirmativa (SI), y la 9 de forma negativa (NO), favor detallar/aclarar las mismas:", arialFontBoldSizeAct10);
            Paragraph noteQuestionaryPara = new Paragraph("", arialFontSizeAct10);
            noteQuestionaryPara.add(noteQuestionaryChunk);
            noteQuestionaryPara.add(noteQuestionary2Chunk);
            noteQuestionaryPara.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(noteQuestionaryPara);

            int concatLenght = djsFileDTO.getAnswerFieldsConcat().length();
            String defaultValue = "_______________________________________________________________________________________________________________________________________\n" +
                    "_______________________________________________________________________________________________________________________________________";
            if (concatLenght > 0) {
                defaultValue = djsFileDTO.getAnswerFieldsConcat();
                String spaces = "";
                for (int i = concatLenght; i < defaultValue.length() - 1; i++) {
                    spaces = spaces + "_";
                }
                defaultValue = defaultValue + spaces;
            }
            Chunk answerTrueChunk = new Chunk(defaultValue, arialFontSizeAct10);

            Paragraph answerData = new Paragraph("", arialFontSizeAct10);
            answerData.add(answerTrueChunk);
            answerData.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(answerData);

            //#endregion

            Paragraph semiFinishPara = new Paragraph("Declaro haber contestado con total veracidad y de máxima buena " +
                    "fe las preguntas del presente cuestionario, así como no haber omitido ni ocultado hechos y/o " +
                    "circunstancias que hubieran podido influir en la aceptación o no aceptación del seguro por parte de " +
                    "la Compañía. Es de mi conocimiento que cualquier declaración falsa, reticencia o mala fe, me hará " +
                    "perder todos los beneficios del seguro.\n" +
                    "Relevo expresamente del secreto profesional y legal a cualquier médico y/o centro de salud que me " +
                    "hubiese asistido y/o tratado y le autorizo a revelar a " + accountName + " todos los datos y " +
                    "antecedentes patológicos que pudiera tener o de los que hubiera adquirido conocimiento al " +
                    "prestarme sus servicios. Entiendo que de presentarse alguna eventualidad contemplada bajo la " +
                    "póliza de seguro como consecuencia de alguna enfermedad existente a la fecha de la firma de ese " +
                    "documento o cuando haya alcanzado la edad límite estipulada en la póliza, la Compañía quedará " +
                    "liberada de toda responsabilidad en lo que respecta a mi seguro.\n" +
                    "En conformidad con la información que antecede, solicito a " + accountName + ", me otorgue el Seguro " +
                    "de Desgravamen, para lo cual doy mi absoluta conformidad a todas las condiciones establecidas por " +
                    "la Compañía, sobre coberturas, vigencia, exclusiones y caducidad del citado seguro obligándome a " +
                    "pagar las primas del seguro solicitado.", arialFontSize10);
            semiFinishPara.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(semiFinishPara);

            Chunk placeChunk = new Chunk(regionalList.stream().filter(x -> x.getReferenceId().equals(djsFileDTO.getUserRegionalIdc())).findFirst().get().getDescription(), arialFontSizeAct10);

            Calendar dateNow = DateUtils.getDateNowByGregorianCalendar();
            int actualYear = dateNow.get(Calendar.YEAR);
            int actualMonthVal = dateNow.get(Calendar.MONTH) + 1;
            String actualMonth = actualMonthVal < 10 ? "0" + actualMonthVal : "" + actualMonthVal;
            String actualDay = dateNow.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + dateNow.get(Calendar.DAY_OF_MONTH) : "" + dateNow.get(Calendar.DAY_OF_MONTH);


            Chunk typeWorkChunk = new Chunk(djsFileDTO.getInsuredValues().getNaturalPerson().getWorkTypeIdc() == ClassifierEnum.DEPENDENT.getReferenceCode() ? "Dependiente" : "Independiente", arialFontSize8);

            //#region CreditInfo and firm

            String typeMoney = djsFileDTO.getCurrencyType() == 1 ? "BS ( X )   USD (  )" : "BS (  )   USD ( X )";
            Chunk glue2 = new Chunk(new VerticalPositionMark());
            Chunk dataCreditChunk2 = new Chunk("DATOS DEL CRÉDITO: ", arialFontBoldSize10);
            dataCreditChunk2.setUnderline(0.2f, -2f);
            String titOrCony2 = djsFileDTO.getCredit().getInsuredTypeIdc() == ClassifierEnum.TITULAR.getReferenceCode() ?
                    "TITULAR (  X  )      CONYUGUE/CODEUDOR (    )" : "TITULAR (    )      CONYUGUE/CODEUDOR (  X  )";
            Paragraph dataCreditPara2 = new Paragraph();
            dataCreditPara2.add(dataCreditChunk2);
            dataCreditPara2.add(glue2);
            dataCreditPara2.add(new Chunk(titOrCony2, arialFontBoldSize10)); //Esto será un IF


            PdfPTable finishTable = new PdfPTable(48);
            finishTable.setTotalWidth(Utilities.millimetersToPoints(190));
            finishTable.setLockedWidth(true);
            finishTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            finishTable.addCell(getCell("", 48, Element.ALIGN_BOTTOM));

            String placementStr = regionalList.stream().filter(x -> x.getReferenceId().equals(djsFileDTO.getUserRegionalIdc())).findFirst().get().getDescription();
            String dateStr = actualDay + " / " + actualMonth + " / " + actualYear;
            Paragraph planceDate = new Paragraph("Lugar y fecha: " + placementStr + ", " + dateStr, arialFontSizeAct10);
            planceDate.setAlignment(Element.ALIGN_JUSTIFIED);
            PdfPCell placeAndDateCell = new PdfPCell();
            placeAndDateCell.addElement(planceDate);
            placeAndDateCell.setColspan(25);
            placeAndDateCell.setBorder(Rectangle.NO_BORDER);
            placeAndDateCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            finishTable.addCell(placeAndDateCell); //ANTES 10


            PdfPCell firmDataCell = new PdfPCell();
            firmDataCell.setColspan(5);
            firmDataCell.setBorder(Rectangle.NO_BORDER);

            Paragraph firmText = new Paragraph("Firma:", arialFontSizeAct10);
            firmText.setAlignment(Element.ALIGN_CENTER);
            firmDataCell.addElement(firmText);
            firmDataCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            finishTable.addCell(firmDataCell);

            PdfPCell imageDataCell = new PdfPCell();
            imageDataCell.setColspan(18);
            imageDataCell.setBorder(Rectangle.NO_BORDER);

            if (djsFileDTO.getDigitalFirm() != null) {
                byte[] imageByte;
                BASE64Decoder decoder = new BASE64Decoder();
                imageByte = decoder.decodeBuffer(djsFileDTO.getDigitalFirm().getContent());
                Image img = Image.getInstance(imageByte);
                img.setAlignment(Image.ALIGN_CENTER);
                img.scaleAbsolute(150, 50);

                imageDataCell.addElement(img);
                finishTable.addCell(imageDataCell); // Para la tabla
                finishTable.addCell(getCell(" ", 30, Element.ALIGN_LEFT));

                PdfPCell firmDataNameCell = new PdfPCell();
                firmDataNameCell.setColspan(18);
                firmDataNameCell.setBorder(Rectangle.TOP);
                Paragraph authFirmName = new Paragraph(djsFileDTO.getInsuredValues().getNaturalPerson().getCompleteName(), arialFontBoldSize8);
                authFirmName.setAlignment(Element.ALIGN_CENTER);
                firmDataNameCell.addElement(authFirmName);
                finishTable.addCell(firmDataNameCell);
            } else {
                Paragraph needFirm = new Paragraph(" \n\n\n", arialFontSize8);
                needFirm.setAlignment(Element.ALIGN_CENTER);


                imageDataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                imageDataCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                imageDataCell.addElement(needFirm);
                finishTable.addCell(imageDataCell); // Para la tabla
                finishTable.addCell(getCell(" ", 30, Element.ALIGN_LEFT));

                PdfPCell firmDataNameCell = new PdfPCell();
                firmDataNameCell.setColspan(18);
                firmDataNameCell.setBorder(Rectangle.TOP);
                Paragraph authFirmName = new Paragraph(djsFileDTO.getInsuredValues().getNaturalPerson().getCompleteName(), arialFontBoldSize8);
                authFirmName.setAlignment(Element.ALIGN_CENTER);
                firmDataNameCell.addElement(authFirmName);
                finishTable.addCell(firmDataNameCell);
            }

            if (beneficiaryUnderAge) {
                PdfPCell delimiterCell = new PdfPCell();
                delimiterCell.setColspan(48); //antes 5
                delimiterCell.setBorder(Rectangle.NO_BORDER);
                finishTable.addCell(delimiterCell);
                finishTable.addCell(getCell("Nota Aclarativa:", 48, Element.ALIGN_LEFT, true)); //antes 5
                finishTable.addCell(getCell("En caso de que al fallecimiento del asegurado,  el/los " +
                                "beneficiario(s) sea(n) menor(es) de edad, el/los responsable(s) de Cobro del porcentaje que le(s) " +
                                "corresponde es/son:",
                        48, Element.ALIGN_JUSTIFIED)); //antes 5
                int beneficiaryNumber = 0;
                for (int i = 0; i < beneficiareUnderNames.size(); i++) {
                    beneficiaryNumber++;
                    Chunk benTitle = new Chunk("Beneficiario " + beneficiaryNumber + ": ", arialFontSize10);
                    Chunk resCopTitle = new Chunk(" - Responsable de cobro: ", arialFontSize10);
                    Chunk benefiaciaryName = new Chunk(beneficiareUnderNames.get(i), arialFontSize10);
                    Chunk resCopName = new Chunk(beneficiareUnderRecip.get(i), arialFontSize10);

                    Phrase resTotalCR = new Phrase();
                    resTotalCR.add(benTitle);
                    resTotalCR.add(benefiaciaryName);
                    resTotalCR.add(resCopTitle);
                    resTotalCR.add(resCopName);

                    PdfPCell respCobCell = new PdfPCell();
                    respCobCell.setPhrase(resTotalCR);
                    respCobCell.setBorder(Rectangle.NO_BORDER);
                    respCobCell.setColspan(48); //antes 5
                    respCobCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    finishTable.addCell(respCobCell);
                }
            }

            finishTable.addCell(getCell("", 48, Element.ALIGN_JUSTIFIED));
            finishTable.addCell(getCell("", 48, Element.ALIGN_JUSTIFIED));

            document.add(finishTable);

            //#endregion

            document.add(delimiterTable);

            //#region Table of credit data 2

            document.add(dataCreditPara2);
            PdfPTable creditTable = new PdfPTable(48);
            creditTable.setTotalWidth(Utilities.millimetersToPoints(190));
            creditTable.setLockedWidth(true);
            creditTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            creditTable.addCell(getCell("", 48, Element.ALIGN_BOTTOM));

            creditTable.addCell(getCell("Plazo del presente crédito:", 12, Element.ALIGN_JUSTIFIED, true));
            creditTable.addCell(getCell(djsFileDTO.getCredit().getCreditTerm() + " Meses", 17, Element.ALIGN_JUSTIFIED));

            creditTable.addCell(getCell("No.  de Operación:", 10, Element.ALIGN_JUSTIFIED, true));
            creditTable.addCell(getCell(djsFileDTO.getCredit().getCreditNumber(), 13, Element.ALIGN_LEFT));

            creditTable.addCell(getCell("Monto actual solicitado:", 12, Element.ALIGN_JUSTIFIED, true));
            creditTable.addCell(getCell(HelpersMethods.formatNumberWithThousandsSeparators(djsFileDTO.getCredit().getRequestedAmount()),
                    8, Element.ALIGN_JUSTIFIED));
            creditTable.addCell(getCell(typeMoney, 9, Element.ALIGN_LEFT));

            creditTable.addCell(getCell("Tipo de Operación:", 10, Element.ALIGN_JUSTIFIED, true));
            creditTable.addCell(getCell("NUEVO", 9, Element.ALIGN_LEFT));//DATO EN DURO, SE DEBE CAMBIAR

            creditTable.addCell(getCell("Monto total acumulado:", 12, Element.ALIGN_JUSTIFIED, true));
            creditTable.addCell(getCell(HelpersMethods.formatNumberWithThousandsSeparators(djsFileDTO.getCredit().getAccumulatedAmount()),
                    8, Element.ALIGN_JUSTIFIED));
            creditTable.addCell(getCell(typeMoney, 28, Element.ALIGN_LEFT));

            document.add(creditTable);

            //#endregion

            //#region Credit Oficial and user

            PdfPTable creditOficialUserTable = new PdfPTable(48);
            creditOficialUserTable.setTotalWidth(Utilities.millimetersToPoints(190));
            creditOficialUserTable.setLockedWidth(true);
            creditOficialUserTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            creditOficialUserTable.addCell(getCell("", 48, Element.ALIGN_BOTTOM));
            Paragraph textCreditSum = new Paragraph("(Sumatoria de créditos vigentes con saldo adeudado con seguro " +
                    "más el actual solicitado)", arialFontItalicSize8);
            textCreditSum.setAlignment(Element.ALIGN_LEFT);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(30);
            cell.setUseAscender(true);
            cell.setUseDescender(true);
            cell.addElement(textCreditSum);
            creditOficialUserTable.addCell(cell);

            PdfPCell firmDataUserCreditCell = new PdfPCell();
            firmDataUserCreditCell.setColspan(18);
            firmDataUserCreditCell.setBorder(Rectangle.NO_BORDER);
            Paragraph authUserFirmName = new Paragraph("Usuario: " + djsFileDTO.getUserName(), arialFontSize8);
            firmDataUserCreditCell.setUseAscender(true);
            firmDataUserCreditCell.setUseDescender(true);
            authUserFirmName.setAlignment(Element.ALIGN_RIGHT);
            firmDataUserCreditCell.addElement(authUserFirmName);
            creditOficialUserTable.addCell(firmDataUserCreditCell); // Para la tabla
            document.add(creditOficialUserTable);

            //#endregion

            document.close();

        } catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateVINCoverageCertificate(GenerateCertificateVin generateCertificateVin) {
        Document document = new Document(GOVERMENT_LEGAL);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
        FontFactory.register(properties.getPathFonts() + "Arial_Italic.ttf", "ARIAL_ITALIC");
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            Font arialSize9 = FontFactory.getFont("ARIAL");
            arialSize9.setSize(7);//9
            Font arialBoldSize9 = FontFactory.getFont("ARIAL_BOLD");
            arialBoldSize9.setSize(7);//9
            Font arialSize8 = FontFactory.getFont("ARIAL");
            arialSize8.setSize(6);//8
            Font arialBoldSize8 = FontFactory.getFont("ARIAL_BOLD");
            arialBoldSize8.setSize(6);//8
            Font arialItalicSize8 = FontFactory.getFont("ARIAL_ITALIC");
            arialItalicSize8.setSize(6);//8
            Font arialSize7 = FontFactory.getFont("ARIAL");
            arialSize7.setSize(5);//7
            Font arialBoldSize7 = FontFactory.getFont("ARIAL_BOLD");
            arialBoldSize7.setSize(5);//7
            GenericHeaderPdfTemplate event = new GenericHeaderPdfTemplate(properties);
            writer.setPageEvent(event);
            document.open();

            String insurerName = generateCertificateVin.getInsurer().getNaturalPerson().getCompleteName().toUpperCase();
            String insurerCompanyName = generateCertificateVin.getInsurerCompany().getJuridicalPerson().getName().toUpperCase();
            String holderCompanyName = generateCertificateVin.getHolderCompany().getJuridicalPerson().getName().toUpperCase();
            String intermediary = generateCertificateVin.getBrokerDTO().getBusinessName();

            Calendar dateNow = DateUtils.getDateNowByGregorianCalendar();
            int actualYear = dateNow.get(Calendar.YEAR);
            int actualMonth = dateNow.get(Calendar.MONTH);
            String actualMonthStr = dateNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.forLanguageTag("es-BO"));
            int actualDay = dateNow.get(Calendar.DAY_OF_MONTH);
            int actualHour = dateNow.get(Calendar.HOUR);
            int actualMin = dateNow.get(Calendar.MINUTE);
            int propossalDay = generateCertificateVin.getRequest().getRequestDate().getDayOfMonth();
            int propossalMonth = generateCertificateVin.getRequest().getRequestDate().getMonthValue();
            int propossalYear = generateCertificateVin.getRequest().getRequestDate().getYear();

            Paragraph blankJump = new Paragraph("\n", arialBoldSize9);

            //#region Propuesta contenido

            Paragraph titleFirst = new Paragraph("", arialBoldSize9);
            titleFirst.setAlignment(Element.ALIGN_CENTER);
            Chunk firstTitlteChunk = new Chunk("PROPUESTA DE");
            titleFirst.add(firstTitlteChunk);
            document.add(titleFirst);

            Paragraph titleSecond = new Paragraph("", arialBoldSize9);
            titleSecond.setAlignment(Element.ALIGN_CENTER);
            Chunk secondTitlteChunk = new Chunk("SEGURO DE VIDA INDIVIDUAL TEMPORAL INCLUSIVO");
            titleSecond.add(secondTitlteChunk);
            document.add(titleSecond);

            String registerCode = APSCodesEnum.APS_PROPOSAL_VIN_CODE.getValue() + ""; //AQUI DEBE IR EL CODIGO DE REGISTRO DE LA APS
            Paragraph subTitle = new Paragraph("CÓDIGO DE REGISTRO: " + registerCode, arialSize7);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitle);

            Paragraph welcomeClient = new Paragraph("Estimado(a) cliente a continuación se detalla la propuesta del " +
                    "Seguro de Vida Individual Temporal Inclusivo solicitado:", arialSize8);
            document.add(welcomeClient);

            PdfPTable proposalDataTable = new PdfPTable(48);
            proposalDataTable.setTotalWidth(Utilities.millimetersToPoints(190));
            proposalDataTable.setLockedWidth(true);
            proposalDataTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            proposalDataTable.addCell(getCellVinSize8("", 48, Element.ALIGN_LEFT, true, false));

            proposalDataTable.addCell(getCellVinSize8("PRODUCTO:", 10, Element.ALIGN_LEFT, true, false));//10
            proposalDataTable.addCell(getCellVinSize8("SEGURO DE VIDA INDIVIDUAL TEMPORAL INCLUSIVO", 20,
                    Element.ALIGN_LEFT, false, false));//20 -- Verificar el texto si es dinamico o no
            proposalDataTable.addCell(getCellVinSize8("PROPUESTA NÚMERO:", 9, Element.ALIGN_LEFT, true, false));//9
            proposalDataTable.addCell(getCellVinSize8(generateCertificateVin.getRequest().getRequestNumber() + "", 9, Element.ALIGN_LEFT, false, false));//9 -- NRo dinamico

            proposalDataTable.addCell(getCellVinSize8("ENTIDAD ASEGURADORA:", 10, Element.ALIGN_LEFT, true, false));//10
            proposalDataTable.addCell(getCellVinSize8(insurerCompanyName,
                    38, Element.ALIGN_LEFT, false, false));//38 -- Este texto es mejor tenerlo en una variable

            proposalDataTable.addCell(getCellVinSize8("PROPUESTO ASEGURADO:", 10, Element.ALIGN_LEFT, true, false));//10
            proposalDataTable.addCell(getCellVinSize8(insurerName, 38, Element.ALIGN_LEFT, false, false));//38 -- Nombre del asegurado

            proposalDataTable.addCell(getCellVinSize8("TOMADOR:", 10, Element.ALIGN_LEFT, true, false));//10
            proposalDataTable.addCell(getCellVinSize8(holderCompanyName, 38, Element.ALIGN_LEFT, false, false));//38 -- Nombre del tomador

            proposalDataTable.addCell(getCellVinSize8("INTERMEDIARIO:", 10, Element.ALIGN_LEFT, true, false));//12
            proposalDataTable.addCell(getCellVinSize8(intermediary, 38, Element.ALIGN_LEFT, false, false));//36 -- Nombre del intermediario

            document.add(proposalDataTable);

            Paragraph propCart = new Paragraph("CARACTERÍSTICAS:", arialBoldSize8);
            document.add(propCart);

            PdfPTable cobTableProp = new PdfPTable(40);
            cobTableProp.setHorizontalAlignment(Element.ALIGN_CENTER);
            cobTableProp.addCell(getCellVinSize8("", 40, Element.ALIGN_CENTER, true, false));
            Stream.of("COBERTURAS:", "CAPITALES ASEGURADOS:").forEach(headerTitle -> {
                arialBoldSize8.setSize(6);
                PdfPCell headerTable = new PdfPCell();
                headerTable.setColspan(20);
                headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTable.setPhrase(new Phrase(headerTitle, arialBoldSize8));
                cobTableProp.addCell(headerTable);
            });

            for (CoverageDTO s : generateCertificateVin.getCoverageDTOList()) {
                cobTableProp.addCell(getCellVinSize8(s.getCoverageName(), 20, Element.ALIGN_CENTER, true, true));
                cobTableProp.addCell(getCellVinSize8(HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                                s.getInsuredCapitalCoverage(),
                                generateCertificateVin.getMoneyType().getAbbreviation()),
                        20, Element.ALIGN_CENTER, false, true));
            }
            document.add(cobTableProp);

            PdfPTable ageLmVgTable = new PdfPTable(48);
            ageLmVgTable.setTotalWidth(Utilities.millimetersToPoints(190));
            ageLmVgTable.setLockedWidth(true);
            ageLmVgTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            ageLmVgTable.addCell(getCellVinSize8("", 48, Element.ALIGN_CENTER, true, false));

            ageLmVgTable.addCell(getCellVinSize8("LÍMITES DE EDAD:", 8, Element.ALIGN_LEFT, true, false));
            ageLmVgTable.addCell(getCellVinSize8("Ingreso: 18 a 70 años cumplidos.", 16, Element.ALIGN_LEFT, false, false));
            ageLmVgTable.addCell(getCellVinSize8("Permanencia: 71 años cumplidos.", 24, Element.ALIGN_LEFT, false, false));
            ageLmVgTable.addCell(getCellVinSize8("VIGENCIA:", 8, Element.ALIGN_LEFT, true, false));
            ageLmVgTable.addCell(getCellVinSize8(generateCertificateVin.getRequest().getCreditTermInYears() + " año(s)",
                    40, Element.ALIGN_LEFT, false, false));//Aqui va la cantidad de años

            ageLmVgTable.addCell(getCellVinSize8("", 48, Element.ALIGN_LEFT, false, false));

            ageLmVgTable.addCell(getCellVinSize8("PRIMA TOTAL:", 8, Element.ALIGN_LEFT, true, false));
            ageLmVgTable.addCell(getCellVinSize8(HelpersMethods.
                            convertNumberToCompanyFormatNumberAndCurrency(generateCertificateVin.getPolicy().getTotalPremium(),
                                    generateCertificateVin.getMoneyType().getAbbreviation()),
                    40, Element.ALIGN_LEFT, false, false));//Aqui ira el monto dinamico

            ageLmVgTable.addCell(getCellVinSize8("FORMA DE PAGO:", 8, Element.ALIGN_LEFT, true, false));
            ageLmVgTable.addCell(getCellVinSize8("Al Contado y por anticipado, a través del Tomador.", 40, Element.ALIGN_LEFT, false, false));

            ageLmVgTable.addCell(getCellVinSize8("FORMA DE PAGO PARA INDEMNIZACIONES DE SINIESTROS:", 48, Element.ALIGN_LEFT, true, false));
            ageLmVgTable.addCell(getCellVinSize8("•", 4, Element.ALIGN_CENTER, true, false));
            ageLmVgTable.addCell(getCellVinSize8("Para las coberturas de Muerte por cualquier causa e " +
                            "Indemnización Adicional por Muerte Accidental, según solicitud de los Beneficiarios.", 44,
                    Element.ALIGN_LEFT, false, false));

            Paragraph insurerDateBtn = new Paragraph("•", arialBoldSize8);
            insurerDateBtn.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cellInsurerBtn = new PdfPCell();
            cellInsurerBtn.setBorder(Rectangle.NO_BORDER);
            cellInsurerBtn.setColspan(4);
            cellInsurerBtn.setUseAscender(true);
            cellInsurerBtn.setUseDescender(true);
            cellInsurerBtn.addElement(insurerDateBtn);
            ageLmVgTable.addCell(cellInsurerBtn);
            String valToReplace = "No. " + HelpersMethods.formatBankAccountNumber(generateCertificateVin.getAccount().getAccountNumber()) + ", "; //Aqui se reemplaza por le nro de cuenta real
            String valToReplace2 = holderCompanyName + ", "; // Aqui se reemplaza por el nombre del intermediario
            String valToReplace3 = insurerName; // Aqui se reemplaza por el nombre del asegurado
            Paragraph insurerDate = new Paragraph("Para la cobertura de Reembolso de Gastos Médicos por Accidente, " +
                    "el propuesto Asegurado autoriza que las indemnizaciones se abonen a su cuenta bancaria "
                    + valToReplace + "de la Entidad de Intermediación Financiera " + valToReplace2 +
                    "que tiene como titular al mismo propuesto asegurado " + valToReplace3, arialSize8);
            insurerDate.setAlignment(Element.ALIGN_LEFT);
            PdfPCell cellInsurer = new PdfPCell();
            cellInsurer.setBorder(Rectangle.NO_BORDER);
            cellInsurer.setColspan(44);
            cellInsurer.setUseAscender(true);
            cellInsurer.setUseDescender(true);
            cellInsurer.addElement(insurerDate);
            ageLmVgTable.addCell(cellInsurer);

            document.add(ageLmVgTable);

            document.add(blankJump);

            Paragraph beneficiaryPara = new Paragraph("BENEFICIARIOS:", arialBoldSize8);
            document.add(beneficiaryPara);

            PdfPTable beneficiaryTable = new PdfPTable(48);
            beneficiaryTable.setTotalWidth(Utilities.millimetersToPoints(190));
            beneficiaryTable.setLockedWidth(true);
            beneficiaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            int benefiaryCount = 1;
            for (Beneficiary j : generateCertificateVin.getBeneficiaryList()) {
                String relation = generateCertificateVin.getRelationShipList().stream()
                        .filter(x -> x.getReferenceId().equals(j.getRelationshipIdc().longValue()))
                        .findFirst().get().getDescription();
                beneficiaryTable.addCell(getCellVinSize8("Beneficiario " + benefiaryCount + ":", 8,
                        Element.ALIGN_LEFT, false, false));
                beneficiaryTable.addCell(getCellVinSize8(j.getFullName(), 16,
                        Element.ALIGN_LEFT, false, false));//Aqui va nombre del asegurado
                beneficiaryTable.addCell(getCellVinSize8("Porcentaje:", 4, Element.ALIGN_LEFT, false, false));
                beneficiaryTable.addCell(getCellVinSize8(j.getPercentage().toString(), 3, Element.ALIGN_LEFT, false, false));//Aqui va el porcentaje
                beneficiaryTable.addCell(getCellVinSize8("Relación:", 4, Element.ALIGN_LEFT, false, false));
                beneficiaryTable.addCell(getCellVinSize8(relation, 13, Element.ALIGN_LEFT, false, false));//Aqui va el tipo de relacion
                benefiaryCount++;
            }

            document.add(beneficiaryTable);
            document.add(blankJump);

            PdfPTable proposalDateTable = new PdfPTable(48);
            proposalDateTable.setTotalWidth(Utilities.millimetersToPoints(190));
            proposalDateTable.setLockedWidth(true);
            proposalDateTable.setHorizontalAlignment(Element.ALIGN_CENTER);

            proposalDateTable.addCell(getCellVinSize8("FECHA DE LA PROPUESTA:", 12,
                    Element.ALIGN_LEFT, true, false));
            proposalDateTable.addCell(getCellVinSize8(HelpersMethods.formatStringOnlyLocalDateTime(generateCertificateVin.getRequest().getRequestDate()), 10,
                    Element.ALIGN_LEFT, false, false));

            proposalDateTable.addCell(getCellVinSize8("VALIDEZ DE LA PROPUESTA:", 11, Element.ALIGN_LEFT, true, false));
            proposalDateTable.addCell(getCellVinSize8("180 días a contar desde la fecha de envío.", 15,
                    Element.ALIGN_LEFT, false, false));

            document.add(proposalDateTable);

            Paragraph acceptance = new Paragraph("", arialBoldSize9);
            Chunk acceptanceChunk = new Chunk("Si usted acepta la presente propuesta, favor responder “SI” el presente mensaje.", arialBoldSize9);
            acceptanceChunk.setUnderline(0.2f, -2f);
            acceptance.add(acceptanceChunk);
            document.add(acceptance);

            document.add(blankJump);

            Paragraph acepSec = new Paragraph("ACEPTACIÓN DE LA PROPUESTA DEL SEGURO:", arialBoldSize8);
            document.add(acepSec);

            String identificationAndCmp = generateCertificateVin.getInsurer().getNaturalPerson().getIdentificationNumber();
            if (!generateCertificateVin.getInsurer().getNaturalPerson().getComplement().isEmpty()) {
                identificationAndCmp = identificationAndCmp + "-" + generateCertificateVin.getInsurer().getNaturalPerson().getComplement();
            }
            String ext = "N/A";
            if (generateCertificateVin.getInsurer().getNaturalPerson().getExtIdc() != null) {
                ext = generateCertificateVin.getExtensions().stream().filter(x -> x.getReferenceId().equals(
                        generateCertificateVin.getInsurer().getNaturalPerson().getExtIdc().longValue()
                )).findFirst().get().getDescription();
            }

            Paragraph textAccept = new Paragraph("Mediante la presente, yo " + insurerName + " con documento de " +
                    "identidad N° " + identificationAndCmp + ", ext.: " + ext + ", habiendo recibido y leído la propuesta del " +
                    "SEGURO DE VIDA INDIVIDUAL TEMPORAL INCLUSIVO realizada por " + insurerCompanyName + ", acepto la " +
                    "misma, dando mi absoluta conformidad a todas y cada una de las condiciones y estipulaciones establecidas en esta.", arialSize8);

            document.add(textAccept);

            String typeMessage = "";
            if (generateCertificateVin.getMessageDTO().getMessageTypeIdc() == MessageTypeEnum.EMAIL.getValue()) {
                typeMessage = "correo electrónico ";
            } else {
                typeMessage = "número de celular ";
            }

            Paragraph textMediaAccept = new Paragraph("Dicha propuesta, fue aceptada de forma digital desde el " +
                    typeMessage + generateCertificateVin.getMessageDTO().getTo() +
                    ", en fecha " + HelpersMethods.formatStringOnlyDate(generateCertificateVin.getMessageDTO().getCreatedAt()) +
                    " a horas " + HelpersMethods.formatStringOnlyHourAndMinute(generateCertificateVin.getMessageDTO().getCreatedAt()) +
                    ", con la cual autorizo a la Entidad Aseguradora a emitir el Certificado de Cobertura una " +
                    "vez realizado el pago de la prima.", arialSize8);

            document.add(textMediaAccept);
            document.add(blankJump);

            Paragraph continueMessage = new Paragraph("Emitida la póliza, esta será enviada al Asegurado en " +
                    "formato digital, pudiendo solicitar una copia física impresa, en cualquier momento, del documento " +
                    "electrónico o físico del seguro emitido a mi favor, en las oficinas de la Entidad Aseguradora o " +
                    "del Tomador, o requerir su envío o entrega por algún medio electrónico y/o digital.", arialSize8);
            document.add(continueMessage);

            //#endregion

            //#region Certificado de cobertura

            document.add(blankJump);
            Paragraph cobCert = new Paragraph("CERTIFICADO DE COBERTURA", arialBoldSize9);
            cobCert.setAlignment(Element.ALIGN_CENTER);
            document.add(cobCert);

            Paragraph cobCertitle = new Paragraph("SEGURO DE VIDA INDIVIDUAL TEMPORAL INCLUSIVO", arialBoldSize9);
            cobCertitle.setAlignment(Element.ALIGN_CENTER);
            document.add(cobCertitle);

            String registerCodeAps = APSCodesEnum.APS_CERT_VIN_CODE.getValue() + ""; //AQUI DEBE IR EL CODIGO DE REGISTRO DE LA APS
            Paragraph subTitleAps = new Paragraph("CÓDIGO DE REGISTRO APS: " + registerCodeAps, arialSize7);
            subTitleAps.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitleAps);

            PdfPTable certInsurerTable = new PdfPTable(48);
            certInsurerTable.setTotalWidth(Utilities.millimetersToPoints(190));
            certInsurerTable.setLockedWidth(true);
            certInsurerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            certInsurerTable.addCell(getCellVinSize8("", 48, Element.ALIGN_CENTER, true, false));

            certInsurerTable.addCell(getCellVinSize8("POLIZA/CERTIFICADO NUMERO:", 10, Element.ALIGN_LEFT, true, false));//10
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getPolicy().getNumberPolicy(), 38, Element.ALIGN_LEFT, false, false));//38
            certInsurerTable.addCell(getCellVinSize8("ENTIDAD ASEGURADORA:", 10, Element.ALIGN_LEFT, true, false));//10
            certInsurerTable.addCell(getCellVinSize8(insurerCompanyName, 38, Element.ALIGN_LEFT, true, false));//38

            certInsurerTable.addCell(getCellVinSize8("DIRECCIÓN Y CONTACTO:", 10, Element.ALIGN_LEFT, true, false));//10
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurerCompany().getDirections().get(0).getDescription(), 38, Element.ALIGN_LEFT, false, false));//37
            certInsurerTable.addCell(getCellVinSize8("", 10, Element.ALIGN_LEFT, true, false));//11
            certInsurerTable.addCell(getCellVinSize8("Línea Gratuita:", 6, Element.ALIGN_LEFT, true, false));//6
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurerCompany().getTelephones()
                    .stream().filter(x -> x.getNumberTypeIdc() == (int) ClassifierEnum.NUMBER_TYPE_FREE.getReferenceCode())
                    .findFirst().get().getNumber(), 6, Element.ALIGN_LEFT, false, false));//6
            certInsurerTable.addCell(getCellVinSize8("Whatsapp:", 4, Element.ALIGN_LEFT, true, false));//4
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurerCompany().getTelephones()
                    .stream().filter(x -> x.getNumberTypeIdc() == (int) ClassifierEnum.NUMBER_TYPE_WHATSAPP.getReferenceCode())
                    .findFirst().get().getNumber(), 7, Element.ALIGN_LEFT, false, false));//7
            certInsurerTable.addCell(getCellVinSize8("Teléfono:", 4, Element.ALIGN_LEFT, true, false));//4
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurerCompany().getTelephones()
                    .stream().filter(x -> x.getNumberTypeIdc() == (int) ClassifierEnum.NUMBER_TYPE_FIJO.getReferenceCode())
                    .findFirst().get().getNumber(), 11, Element.ALIGN_LEFT, false, false));//10

            certInsurerTable.addCell(getCellVinSize8("", 10, Element.ALIGN_LEFT, true, false));//11
            certInsurerTable.addCell(getCellVinSize8("Correo electrónico:", 7, Element.ALIGN_LEFT, true, false));//7
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurerCompany().getEmail(),
                    12, Element.ALIGN_LEFT, false, false));//12
            certInsurerTable.addCell(getCellVinSize8("Página web:", 5, Element.ALIGN_LEFT, true, false));//5
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurerCompany().getJuridicalPerson().getWebSite(),
                    14, Element.ALIGN_LEFT, false, false));//13

            certInsurerTable.addCell(getCellVinSize8("TOMADOR:", 8, Element.ALIGN_LEFT, true, false));//8
            certInsurerTable.addCell(getCellVinSize8(holderCompanyName, 16, Element.ALIGN_LEFT, false, false));//18
            certInsurerTable.addCell(getCellVinSize8("INTERMEDIARIO:", 8, Element.ALIGN_LEFT, true, false));//7
            certInsurerTable.addCell(getCellVinDefault(intermediary, 18, Element.ALIGN_LEFT,5,false, false, false));//15

            certInsurerTable.addCell(getCellVinSize8("ASEGURADO:", 8, Element.ALIGN_LEFT, true, false));//8
            certInsurerTable.addCell(getCellVinSize8(insurerName, 16, Element.ALIGN_LEFT, false, false));//18
            certInsurerTable.addCell(getCellVinSize8("C.I.:", 2, Element.ALIGN_LEFT, true, false));//2
            certInsurerTable.addCell(getCellVinSize8(identificationAndCmp, 6, Element.ALIGN_LEFT, false, false));//6
            certInsurerTable.addCell(getCellVinSize8("Ext.:", 2, Element.ALIGN_LEFT, true, false));//2
            certInsurerTable.addCell(getCellVinSize8(ext, 14, Element.ALIGN_LEFT, false, false));//12

            certInsurerTable.addCell(getCellVinSize8("FECHA DE NACIMIENTO:", 8, Element.ALIGN_LEFT, true, false));//8
            certInsurerTable.addCell(getCellVinSize8(HelpersMethods.formatStringOnlyLocalDateTime(generateCertificateVin.getInsurer().getNaturalPerson().getBirthDate()),
                    16, Element.ALIGN_LEFT, false, false));//18
            certInsurerTable.addCell(getCellVinSize8("LUGAR DE NACIMIENTO:", 8, Element.ALIGN_LEFT, true, false));//7
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getNationality().getDescription().toUpperCase(), 18, Element.ALIGN_LEFT, false, false));//15

            certInsurerTable.addCell(getCellVinSize8("NACIONALIDAD:", 8, Element.ALIGN_LEFT, true, false));//8
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getNationality().getDescription().toUpperCase(), 16, Element.ALIGN_LEFT, false, false));//16
            certInsurerTable.addCell(getCellVinSize8("ESTADO CIVIL:", 8, Element.ALIGN_LEFT, true, false));//8
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getMaritalStatus().getDescription().toUpperCase(), 18, Element.ALIGN_LEFT, false, false));//6

            certInsurerTable.addCell(getCellVinSize8("DIRECCIÓN DOMICILIO:", 8, Element.ALIGN_LEFT, true, false));//8
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurer().getDirection().getDescription(), 40, Element.ALIGN_LEFT, false, false));//38

            certInsurerTable.addCell(getCellVinSize8("TELÉFONO(S):", 8, Element.ALIGN_LEFT, true, false));//8
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurer().getTelephone().toString(), 16, Element.ALIGN_LEFT, false, false));//16
            certInsurerTable.addCell(getCellVinSize8("PROFESIÓN/OFICIO:", 8, Element.ALIGN_LEFT, true, false));//8
            certInsurerTable.addCell(getCellVinSize8(generateCertificateVin.getInsurer().getNaturalPerson().getProfession(), 18, Element.ALIGN_LEFT, false, false));//6

            document.add(certInsurerTable);

            PdfPTable cobCertTable = new PdfPTable(48);
            cobCertTable.setTotalWidth(Utilities.millimetersToPoints(190));
            cobCertTable.setLockedWidth(true);
            cobCertTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            cobCertTable.addCell(getCellVinSize8("", 48, Element.ALIGN_CENTER, true, false));
            Stream.of("COBERTURAS:", "CAPITALES ASEGURADOS:").forEach(headerTitle -> {
                arialBoldSize8.setSize(6);
                PdfPCell headerTable = new PdfPCell();
                headerTable.setColspan(24);
                headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTable.setPhrase(new Phrase(headerTitle, arialBoldSize8));
                headerTable.setBorder(Rectangle.NO_BORDER);
                cobCertTable.addCell(headerTable);
            });

            for (CoverageDTO s : generateCertificateVin.getCoverageDTOList()) {
                cobCertTable.addCell(getCellVinSize8(s.getCoverageName(), 24, Element.ALIGN_LEFT, false, false));
                cobCertTable.addCell(getCellVinSize8(HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                                s.getInsuredCapitalCoverage(),
                                generateCertificateVin.getMoneyType().getAbbreviation()),
                        24, Element.ALIGN_LEFT, false, false));
            }
            document.add(cobCertTable);
            document.add(blankJump);

            PdfPTable primCertTable = new PdfPTable(48);
            primCertTable.setTotalWidth(Utilities.millimetersToPoints(190));
            primCertTable.setLockedWidth(true);
            primCertTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            primCertTable.addCell(getCellVinSize8("", 48, Element.ALIGN_LEFT, true, false));
            primCertTable.addCell(getCellVinSize8("PRIMA TOTAL (incluyendo recargos):", 16, Element.ALIGN_LEFT, true, false));//16
            primCertTable.addCell(getCellVinSize8(HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    generateCertificateVin.getPolicy().getTotalPremium(),
                    generateCertificateVin.getMoneyType().getAbbreviation()), 10, Element.ALIGN_LEFT, false, false));//10 - Debe salir de un calculo o DB
            primCertTable.addCell(getCellVinSize8("FORMA DE PAGO:", 8, Element.ALIGN_LEFT, true, false));//8
            primCertTable.addCell(getCellVinSize8("Al contado y por anticipado.", 14, Element.ALIGN_LEFT, false, false));//14

            String fromDate = HelpersMethods.formatStringOnlyDate(generateCertificateVin.getPolicy().getFromDate());
            String toDate = HelpersMethods.formatStringOnlyDate(generateCertificateVin.getPolicy().getToDate());
            String fullCommentDate = "Desde el " + fromDate + " a hrs. 12:00 am (medio día) hasta el " + toDate +
                    " 12:00 am (medio día).";
            primCertTable.addCell(getCellVinSize8("VIGENCIA:", 5, Element.ALIGN_LEFT, true, false));//5
            primCertTable.addCell(getCellVinSize8(fullCommentDate, 43, Element.ALIGN_LEFT, false, false));//43

            document.add(primCertTable);
            document.add(blankJump);

            Paragraph beneficiaryParaCert = new Paragraph("BENEFICIARIOS*:", arialBoldSize8);
            document.add(beneficiaryParaCert);

            PdfPTable beneficiaryTableCert = new PdfPTable(48);
            beneficiaryTableCert.setTotalWidth(Utilities.millimetersToPoints(190));
            beneficiaryTableCert.setLockedWidth(true);
            beneficiaryTableCert.setHorizontalAlignment(Element.ALIGN_LEFT);

            int benefiaryCountCert = 1;
            boolean beneficiaryUnderAge = false;
            List<String> beneficiareUnderNames = new ArrayList<>();
            List<String> beneficiareUnderRecip = new ArrayList<>();
            beneficiaryUnderAge = generateCertificateVin.getBeneficiaryList().stream().filter(x -> x.getIsUnderAge() > 1).collect(Collectors.toList()).size() > 0
                    ? true : false;
            for (Beneficiary j : generateCertificateVin.getBeneficiaryList()) {
                if (j.getIsUnderAge() > 1) {
                    beneficiareUnderNames.add(j.getFullName());
                    String benExt = j.getLegalExt() != null ? generateCertificateVin.getExtensions().stream().filter(x -> x.getReferenceId().equals(j.getLegalExt().longValue())).findFirst().get().getDescription() : "";
                    beneficiareUnderRecip.add(j.getRepresentativeLegalName() + " - CI " + j.getLegalIdentification() + " "
                            + benExt);
                }
                String relation = generateCertificateVin.getRelationShipList().stream()
                        .filter(x -> x.getReferenceId().equals(j.getRelationshipIdc().longValue()))
                        .findFirst().get().getDescription();
                beneficiaryTableCert.addCell(getCellVinSize8("Beneficiario " + benefiaryCountCert + ":", 8,
                        Element.ALIGN_LEFT, true, false));
                beneficiaryTableCert.addCell(getCellVinSize8(j.getFullName(), 16,
                        Element.ALIGN_LEFT, false, false));//Aqui va nombre del asegurado
                beneficiaryTableCert.addCell(getCellVinSize8("Porcentaje:", 4, Element.ALIGN_LEFT, false, false));
                beneficiaryTableCert.addCell(getCellVinSize8(j.getPercentage().toString(), 3, Element.ALIGN_LEFT, false, false));//Aqui va el porcentaje
                beneficiaryTableCert.addCell(getCellVinSize8("Relación:", 4, Element.ALIGN_LEFT, false, false));
                beneficiaryTableCert.addCell(getCellVinSize8(relation, 13, Element.ALIGN_LEFT, false, false));//Aqui va el tipo de relacion
                benefiaryCountCert++;
            }
            beneficiaryTableCert.addCell(getCellVinSize8("*Los Beneficiarios podrán ser actualizados por el " +
                    "Asegurado durante la vigencia de la póliza, mediante nota expresa remitida a la Entidad " +
                    "Aseguradora.", 48, Element.ALIGN_LEFT, false, false));//48
            document.add(beneficiaryTableCert);

            Paragraph cobCertT = new Paragraph("COBERTURAS*:", arialBoldSize8);
            cobCertT.setAlignment(Element.ALIGN_LEFT);
            document.add(cobCertT);

            String cobTitle1 = "MUERTE POR CUALQUIER CAUSA: ";
            String cobCont1 = "Ante el fallecimiento del Asegurado por cualquier causa, sea natural, accidental y/o " +
                    "por enfermedad, con excepción de los riesgos descritos en las Exclusiones, se pagará a los " +
                    "beneficiarios designados el valor expuesto en el capital asegurado.";
            String cobTitle2 = "INDEMNIZACIÓN ADICIONAL POR MUERTE ACCIDENTAL: ";
            String cobCont2 = "En caso de fallecimiento del Asegurado por causa accidental se pagará un capital " +
                    "adicional equivalente al capital asegurado de muerte por cualquier causa.";
            String cobTitle3 = "REEMBOLSO DE GASTOS MÉDICOS POR ACCIDENTE: ";
            String cobCont3 = "Ante la ocurrencia de un accidente en el cual el Asegurado resulte con lesiones " +
                    "físicas, se pagará a reembolso los gastos incurridos a consecuencia directa de tal accidente, " +
                    "por honorarios médicos, gastos farmacéuticos, hospitalarios, quirúrgicos y/o prótesis dentales, " +
                    "no así aparatos ortopédicos, hasta el valor expuesto en el Capital Asegurado por cada anualidad.";

            Paragraph cob1 = new Paragraph(cobTitle1, arialBoldSize8);
            Chunk cobC1 = new Chunk(cobCont1, arialSize8);
            cob1.add(cobC1);
            PdfPCell cellCob1 = new PdfPCell();
            cellCob1.setBorder(Rectangle.NO_BORDER);
            cellCob1.setColspan(46);
            cellCob1.setUseAscender(true);
            cellCob1.setUseDescender(true);
            cellCob1.addElement(cob1);

            Paragraph cob2 = new Paragraph(cobTitle2, arialBoldSize8);
            Chunk cobC2 = new Chunk(cobCont2, arialSize8);
            cob2.add(cobC2);
            PdfPCell cellCob2 = new PdfPCell();
            cellCob2.setBorder(Rectangle.NO_BORDER);
            cellCob2.setColspan(46);
            cellCob2.setUseAscender(true);
            cellCob2.setUseDescender(true);
            cellCob2.addElement(cob2);

            Paragraph cob3 = new Paragraph(cobTitle3, arialBoldSize8);
            Chunk cobC3 = new Chunk(cobCont3, arialSize8);
            cob3.add(cobC3);
            PdfPCell cellCob3 = new PdfPCell();
            cellCob3.setBorder(Rectangle.NO_BORDER);
            cellCob3.setColspan(46);
            cellCob3.setUseAscender(true);
            cellCob3.setUseDescender(true);
            cellCob3.addElement(cob3);

            PdfPCell cellPoitn = new PdfPCell();
            Paragraph point = new Paragraph("•", arialBoldSize8);
            cellPoitn.setBorder(Rectangle.NO_BORDER);
            cellPoitn.setColspan(2);
            cellPoitn.setUseAscender(true);
            cellPoitn.setUseDescender(true);
            cellPoitn.addElement(point);

            PdfPTable coverageCertDesc = new PdfPTable(48);
            coverageCertDesc.setTotalWidth(Utilities.millimetersToPoints(190));
            coverageCertDesc.setLockedWidth(true);
            coverageCertDesc.setHorizontalAlignment(Element.ALIGN_LEFT);
            coverageCertDesc.addCell(cellPoitn); // 2
            coverageCertDesc.addCell(cellCob1); // 46
            coverageCertDesc.addCell(cellPoitn); // 2
            coverageCertDesc.addCell(cellCob2); // 46
            coverageCertDesc.addCell(cellPoitn); // 2
            coverageCertDesc.addCell(cellCob3); // 46

            Paragraph finCob = new Paragraph("*Se cubren todos los eventos, hasta el valor del capital asegurado " +
                    "estipulado para cada cobertura, siempre que se hayan producido durante la vigencia de la " +
                    "póliza.", arialItalicSize8);
            PdfPCell cellFinishMessageCob = new PdfPCell();
            cellFinishMessageCob.setBorder(Rectangle.NO_BORDER);
            cellFinishMessageCob.setColspan(48);
            cellFinishMessageCob.setUseAscender(true);
            cellFinishMessageCob.setUseDescender(true);
            cellFinishMessageCob.addElement(finCob);
            coverageCertDesc.addCell(cellFinishMessageCob);

            document.add(coverageCertDesc);

            Paragraph secureTemp = new Paragraph("SEGURO DE VIDA “TEMPORAL”: ", arialBoldSize8);
            Chunk secChunk = new Chunk("Se refiere a un seguro cuya cobertura está limita a la temporalidad o tiempo " +
                    "establecido en la vigencia del mismo, bajo las condiciones establecidas en el presente " +
                    "certificado de cobertura, cuya vigencia podrá ser de hasta cinco (5) años consecutivos.", arialSize8);
            secureTemp.add(secChunk);
            document.add(secureTemp);

            Paragraph excTemp = new Paragraph("EXCLUSIONES: ", arialBoldSize8);
            Chunk excChunk = new Chunk("La Entidad Aseguradora queda liberada de su responsabilidad, si las " +
                    "causas del evento se deben directa o indirectamente, total o parcialmente a:", arialSize8);
            excTemp.add(excChunk);
            document.add(excTemp);

            //#region Exclusiones a detalle
            PdfPTable excTable = new PdfPTable(48);
            excTable.setTotalWidth(Utilities.millimetersToPoints(190));
            excTable.setLockedWidth(true);
            excTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            excTable.addCell(getCellVinSize8("", 48, Element.ALIGN_LEFT, true, false));

            Paragraph excA = new Paragraph("a)", arialSize8);
            PdfPCell cellExcA = new PdfPCell();
            cellExcA.setBorder(Rectangle.NO_BORDER);
            cellExcA.setColspan(1);
            cellExcA.setUseAscender(true);
            cellExcA.setUseDescender(true);
            cellExcA.addElement(excA);
            excTable.addCell(cellExcA);

            Paragraph excContA = new Paragraph("Suicidio suscitado dentro del primer año de vigencia " +
                    "del seguro.", arialSize8);
            PdfPCell cellExcContA = new PdfPCell();
            cellExcContA.setBorder(Rectangle.NO_BORDER);
            cellExcContA.setColspan(47);
            cellExcContA.setUseAscender(true);
            cellExcContA.setUseDescender(true);
            cellExcContA.addElement(excContA);
            excTable.addCell(cellExcContA);

            Paragraph excB = new Paragraph("b)", arialSize8);
            PdfPCell cellExcB = new PdfPCell();
            cellExcB.setBorder(Rectangle.NO_BORDER);
            cellExcB.setColspan(1);
            cellExcB.setUseAscender(true);
            cellExcB.setUseDescender(true);
            cellExcB.addElement(excB);
            excTable.addCell(cellExcB);

            Paragraph excContB = new Paragraph("Guerra internacional o civil (declarada o no), invasión, " +
                    "actos de enemigos extranjeros, " +
                    "hostilidades u operaciones bélicas, insurrección, sublevación, rebelión, sedición, motín, huelga, " +
                    "guerrilla, revolución o hechos que las leyes u autoridades califiquen como delitos contra la " +
                    "seguridad del Estado; Intervención directa o indirecta del Asegurado en actos delictuosos o " +
                    "hechos ilegales, que le ocasionen la muerte.", arialSize8);
            PdfPCell cellExcContB = new PdfPCell();
            cellExcContB.setBorder(Rectangle.NO_BORDER);
            cellExcContB.setColspan(47);
            cellExcContB.setUseAscender(true);
            cellExcContB.setUseDescender(true);
            cellExcContB.addElement(excContB);
            excTable.addCell(cellExcContB);

            Paragraph excC = new Paragraph("c)", arialSize8);
            PdfPCell cellExcC = new PdfPCell();
            cellExcC.setBorder(Rectangle.NO_BORDER);
            cellExcC.setColspan(1);
            cellExcC.setUseAscender(true);
            cellExcC.setUseDescender(true);
            cellExcC.addElement(excC);
            excTable.addCell(cellExcC);

            Paragraph excContC = new Paragraph("Fisión, fusión o energía nuclear, contaminación radioactiva, " +
                    "terrorismo nuclear, biológico y/o químico", arialSize8);
            PdfPCell cellExcContC = new PdfPCell();
            cellExcContC.setBorder(Rectangle.NO_BORDER);
            cellExcContC.setColspan(47);
            cellExcContC.setUseAscender(true);
            cellExcContC.setUseDescender(true);
            cellExcContC.addElement(excContC);
            excTable.addCell(cellExcContC);

            Paragraph excD = new Paragraph("d)", arialSize8);
            PdfPCell cellExcD = new PdfPCell();
            cellExcD.setBorder(Rectangle.NO_BORDER);
            cellExcD.setColspan(1);
            cellExcD.setUseAscender(true);
            cellExcD.setUseDescender(true);
            cellExcD.addElement(excD);
            excTable.addCell(cellExcD);

            Paragraph excContD = new Paragraph("Fenómenos de la naturaleza de carácter catastrófico tales" +
                    " como: sismos, inundaciones, riadas, erupciones volcánicas, terremotos y/o tsunamis.", arialSize8);
            PdfPCell cellExcContD = new PdfPCell();
            cellExcContD.setBorder(Rectangle.NO_BORDER);
            cellExcContD.setColspan(47);
            cellExcContD.setUseAscender(true);
            cellExcContD.setUseDescender(true);
            cellExcContD.addElement(excContD);
            excTable.addCell(cellExcContD);

            Paragraph excE = new Paragraph("e)", arialSize8);
            PdfPCell cellExcE = new PdfPCell();
            cellExcE.setBorder(Rectangle.NO_BORDER);
            cellExcE.setColspan(1);
            cellExcE.setUseAscender(true);
            cellExcE.setUseDescender(true);
            cellExcE.addElement(excE);
            excTable.addCell(cellExcE);

            Paragraph excContE = new Paragraph("Epidemias y/o pandemias declaradas por las entidades " +
                    "gubernamentales competente. No obstante se aclara que cubre el fallecimiento a consecuencia " +
                    "de Coronavirus Covid-19 (SARS-CoV-2) y/o Dengue, siempre y cuando el fallecimiento no " +
                    "ocurra durante los primeros treinta (30) días calendario de la fecha de " +
                    "inicio de vigencia del seguro.", arialSize8);
            PdfPCell cellExcContE = new PdfPCell();
            cellExcContE.setBorder(Rectangle.NO_BORDER);
            cellExcContE.setColspan(47);
            cellExcContE.setUseAscender(true);
            cellExcContE.setUseDescender(true);
            cellExcContE.addElement(excContE);
            excTable.addCell(cellExcContE);

            Paragraph excF = new Paragraph("f)", arialSize8);
            PdfPCell cellExcF = new PdfPCell();
            cellExcF.setBorder(Rectangle.NO_BORDER);
            cellExcF.setColspan(1);
            cellExcF.setUseAscender(true);
            cellExcF.setUseDescender(true);
            cellExcF.addElement(excF);
            excTable.addCell(cellExcF);

            Paragraph excContF = new Paragraph("Síndrome de Inmuno Deficiencia Adquirida (SIDA/VIH) y/o " +
                    "cualquier síndrome o enfermedad similar o derivada de SIDA.", arialSize8);
            PdfPCell cellExcContF = new PdfPCell();
            cellExcContF.setBorder(Rectangle.NO_BORDER);
            cellExcContF.setColspan(47);
            cellExcContF.setUseAscender(true);
            cellExcContF.setUseDescender(true);
            cellExcContF.addElement(excContF);
            excTable.addCell(cellExcContF);

            Paragraph excG = new Paragraph("g)", arialSize8);
            PdfPCell cellExcG = new PdfPCell();
            cellExcG.setBorder(Rectangle.NO_BORDER);
            cellExcG.setColspan(1);
            cellExcG.setUseAscender(true);
            cellExcG.setUseDescender(true);
            cellExcG.addElement(excG);
            excTable.addCell(cellExcG);

            Paragraph excContG = new Paragraph("Enfermedades preexistentes, se excluye los hechos suscitados " +
                    "a consecuencia de enfermedades que con anterioridad a la fecha del ingreso a la póliza, hayan sido " +
                    "diagnosticadas o respecto de las cuales se haya efectuado un gasto comprobable y " +
                    "hayan sido de conocimiento del Asegurado.", arialSize8);
            PdfPCell cellExcContG = new PdfPCell();
            cellExcContG.setBorder(Rectangle.NO_BORDER);
            cellExcContG.setColspan(47);
            cellExcContG.setUseAscender(true);
            cellExcContG.setUseDescender(true);
            cellExcContG.addElement(excContG);
            excTable.addCell(cellExcContG);

            Paragraph excH = new Paragraph("h)", arialSize8);
            PdfPCell cellExcH = new PdfPCell();
            cellExcH.setBorder(Rectangle.NO_BORDER);
            cellExcH.setColspan(1);
            cellExcH.setUseAscender(true);
            cellExcH.setUseDescender(true);
            cellExcH.addElement(excH);
            excTable.addCell(cellExcH);

            Paragraph excContH = new Paragraph("Para la cobertura de Reembolso de Gastos Médicos por Accidente, " +
                    "cualquier gasto que no sea a consecuencia directa de Accidente ocurrido durante la vigencia del " +
                    "seguro, entendiéndose como tal a toda lesión corporal producida por la acción imprevista, fortuita, " +
                    "de una fuerza externa que obre súbitamente sobre la persona física del Asegurado, hecho que " +
                    "es independiente de su voluntad y que puede ser determinado por los médicos de una " +
                    "manera cierta y objetiva.", arialSize8);
            PdfPCell cellExcContH = new PdfPCell();
            cellExcContH.setBorder(Rectangle.NO_BORDER);
            cellExcContH.setColspan(47);
            cellExcContH.setUseAscender(true);
            cellExcContH.setUseDescender(true);
            cellExcContH.addElement(excContH);
            excTable.addCell(cellExcContH);

            document.add(excTable);

            //#endregion

            Paragraph prodSin = new Paragraph("PROCEDIMIENTO EN CASO DE SINIESTRO:", arialBoldSize8);
            prodSin.setAlignment(Element.ALIGN_LEFT);
            document.add(prodSin);

            //#region Procedimiento en caso de siniestro

            PdfPTable prodSinTable = new PdfPTable(48);
            prodSinTable.setTotalWidth(Utilities.millimetersToPoints(190));
            prodSinTable.setLockedWidth(true);
            prodSinTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            prodSinTable.addCell(getCellVinSize8("", 48, Element.ALIGN_LEFT, true, false));

            Paragraph sinA = new Paragraph("a)", arialBoldSize8);
            PdfPCell cellSinA = new PdfPCell();
            cellSinA.setBorder(Rectangle.NO_BORDER);
            cellSinA.setColspan(2);
            cellSinA.setUseAscender(true);
            cellSinA.setUseDescender(true);
            cellSinA.addElement(sinA);
            prodSinTable.addCell(cellSinA);

            Paragraph sinContA = new Paragraph("Aviso de Siniestro:", arialBoldSize8);
            Chunk chunkSinA = new Chunk("El Asegurado o Beneficiario, tan pronto y a más tardar dentro de los " +
                    "quince (15) días calendario de tener conocimiento del siniestro, sea en territorio nacional o en " +
                    "el extranjero, deben comunicar tal hecho a la Entidad Aseguradora y/o al Tomador, salvo fuerza " +
                    "mayor o impedimento justificado. Los interesados podrán realizar el aviso de siniestros a través " +
                    "del Tomador, Intermediario, página web, correo electrónico, Contact Center (línea gratuita o " +
                    "Whatsapp) o de forma directa a nuestra Entidad Aseguradora.", arialSize8);
            sinContA.add(chunkSinA);
            PdfPCell cellSinContA = new PdfPCell();
            cellSinContA.setBorder(Rectangle.NO_BORDER);
            cellSinContA.setColspan(46);
            cellSinContA.setUseAscender(true);
            cellSinContA.setUseDescender(true);
            cellSinContA.addElement(sinContA);
            prodSinTable.addCell(cellSinContA);

            Paragraph sinB = new Paragraph("b)", arialBoldSize8);
            PdfPCell cellSinB = new PdfPCell();
            cellSinB.setBorder(Rectangle.NO_BORDER);
            cellSinB.setColspan(2);
            cellSinB.setUseAscender(true);
            cellSinB.setUseDescender(true);
            cellSinB.addElement(sinB);
            prodSinTable.addCell(cellSinB);

            Paragraph sinContB = new Paragraph("Documentación Requerida:", arialBoldSize8);
            Chunk chunkSinB = new Chunk("Para la atención del reclamo, se deberá presentar " +
                    "los siguientes documentos:", arialSize8);
            sinContB.add(chunkSinB);
            PdfPCell cellSinContB = new PdfPCell();
            cellSinContB.setBorder(Rectangle.NO_BORDER);
            cellSinContB.setColspan(46);
            cellSinContB.setUseAscender(true);
            cellSinContB.setUseDescender(true);
            cellSinContB.addElement(sinContB);
            prodSinTable.addCell(cellSinContB);

            prodSinTable.addCell(getCellVinSize8("En caso de Muerte:", 24, Element.ALIGN_CENTER, true, true));
            prodSinTable.addCell(getCellVinSize8("En caso de Reembolso de Gastos Médicos:", 24, Element.ALIGN_CENTER, true, true));

            PdfPTable extraInfoTable = new PdfPTable(48);


            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("Fotocopia de Cédula de Identidad " +
                    "del Asegurado.", 46, Element.ALIGN_LEFT, false, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("Original Certificado de defunción expedido " +
                    "en oficialía de registro civil en original.", 46, Element.ALIGN_LEFT, false, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("Original o copia legalizada Certificado " +
                    "Médico Único de Defunción, debidamente llenado, estableciendo la causas directa, " +
                    "antecedentes, originaria y contribuyente del fallecimiento (según formato establecido por " +
                    "el Ministerio de Salud).", 46, Element.ALIGN_LEFT, false, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("Epicrisis y/o historia clínica, " +
                    "cuando corresponda.", 46, Element.ALIGN_LEFT, false, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("Original o copia legalizada del Certificado " +
                    "de la Autoridad competente (Tránsito y/o FELCC), " +
                    "cuando corresponda.", 46, Element.ALIGN_LEFT, false, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoTable.addCell(getCellWithNoMiddleVinSize8("Fotocopia de Cédula de Identidad de " +
                    "los Beneficiarios, si no existieran Beneficiarios nominados, debe presentarse la " +
                    "Declaratoria de Herederos del Asegurado conforme Ley.", 46, Element.ALIGN_LEFT, false, false));
            PdfPCell cellTable1 = new PdfPCell();
            cellTable1.setColspan(24);
            cellTable1.setUseAscender(true);
            cellTable1.setUseDescender(true);
            cellTable1.addElement(extraInfoTable);
            prodSinTable.addCell(cellTable1);

            PdfPTable extraInfoContTable = new PdfPTable(48);


            extraInfoContTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoContTable.addCell(getCellWithNoMiddleVinSize8("Fotocopia de Cédula de Identidad " +
                    "del Asegurado.", 46, Element.ALIGN_LEFT, false, false));
            extraInfoContTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoContTable.addCell(getCellWithNoMiddleVinSize8("Certificado o Informe Médico, expresando " +
                    "las causas del accidente y sus consecuencias probables, incluyendo informes, estudios, " +
                    "laboratorios y/o rayos X si hubiere.", 46, Element.ALIGN_LEFT, false, false));
            extraInfoContTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            extraInfoContTable.addCell(getCellWithNoMiddleVinSize8("Original o copia legalizada del " +
                    "Certificado de la Autoridad competente (Tránsito y/o FELCC), " +
                    "cuando corresponda.", 46, Element.ALIGN_LEFT, false, false));
            extraInfoContTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            Paragraph textCont = new Paragraph("Para el reembolso de gastos realizados en atenciones en " +
                    "centros médicos, clínicas, farmacias y/u honorarios profesionales, se debe presentar las " +
                    "facturas y recetas, detallando los servicios y productos suministrados, a " +
                    "nombre del Asegurado y/o ", arialSize8);
            Chunk textContChunk = new Chunk(insurerCompanyName + " NIT: " +
                    generateCertificateVin.getInsurerCompany().getNit(), arialBoldSize8);
            Chunk textContChunk2 = new Chunk(". Estas facturas deberán ser entregadas a la Entidad " +
                    "Aseguradora en lo posible dentro el mes de su emisión. Se aclara que no se " +
                    "realizarán descuentos por presentar facturas posteriores al mes de su emisión.", arialSize8);
            textCont.add(textContChunk);
            textCont.add(textContChunk2);
            PdfPCell cellTableContExt = new PdfPCell();
            cellTableContExt.setColspan(46);
            cellTableContExt.setBorder(Rectangle.NO_BORDER);
            cellTableContExt.setUseAscender(true);
            cellTableContExt.setUseDescender(true);
            cellTableContExt.addElement(textCont);
            extraInfoContTable.addCell(cellTableContExt);

            PdfPCell cellTable2 = new PdfPCell();
            cellTable2.setColspan(24);
            cellTable2.setUseAscender(true);
            cellTable2.setUseDescender(true);
            cellTable2.addElement(extraInfoContTable);
            prodSinTable.addCell(cellTable2);

            document.add(prodSinTable);

            //#endregion

            Paragraph pronPlace = new Paragraph("PLAZO DE PRONUNCIAMIENTO: ", arialBoldSize8);
            Chunk pronChunk = new Chunk("El plazo para el pronunciamiento de la Entidad Aseguradora es de " +
                    "veinte (20) días una vez recibida la información y evidencias del siniestro. Este plazo " +
                    "fenece con la aceptación o rechazo del siniestro o con la solicitud de información " +
                    "complementaria y no vuelven a correr hasta que el Asegurado/Beneficiario haya cumplido " +
                    "con tales requerimientos, las solicitudes adicionales no podrán extenderse por más de " +
                    "dos (2) veces. Una vez remitida la información por parte del Asegurado/Beneficiario, " +
                    "el silencio de la Entidad Aseguradora vencido el término para pronunciarse o vencidas " +
                    "las solicitudes de complementación, importa la aceptación del reclamo.", arialSize8);
            pronPlace.add(pronChunk);
            document.add(pronPlace);

            Paragraph inmPlace = new Paragraph("PLAZO DE INDEMNIZACIÓN: ", arialBoldSize8);
            Chunk inmChunk = new Chunk("Establecido el derecho del Asegurado y el monto de la indemnización, " +
                    "la Entidad Aseguradora debe pagar el reclamo dentro de los doce (12) días siguientes.", arialSize8);
            inmPlace.add(inmChunk);
            document.add(inmPlace);

            Paragraph paySin = new Paragraph("FORMA DE PAGO PARA INDEMNIZACIONES DE SINIESTROS:", arialBoldSize8);
            document.add(paySin);

            PdfPTable paySinTable = new PdfPTable(48);
            paySinTable.setTotalWidth(Utilities.millimetersToPoints(190));
            paySinTable.setLockedWidth(true);
            paySinTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            paySinTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            paySinTable.addCell(getCellWithNoMiddleVinSize8("Para las coberturas de Muerte por cualquier causa e " +
                    "Indemnización Adicional por Muerte Accidental, según solicitud de " +
                    "los Beneficiarios.", 46, Element.ALIGN_LEFT, false, false));
            String cont1 = "Para la cobertura de Reembolso de Gastos Médicos por Accidente, el " +
                    "Asegurado autoriza que las indemnizaciones se abonen a su cuenta bancaria " + valToReplace +
                    "de la Entidad de Intermediación Financiera " + holderCompanyName + ", que tiene como titular " +
                    "al asegurado " + insurerName + ". En su defecto se podrá pagar por otros medios como " +
                    "ser cheque o transferencia bancaria.";
            paySinTable.addCell(getCellWithNoMiddleVinSize8("•", 2, Element.ALIGN_LEFT, true, false));
            paySinTable.addCell(getCellWithNoMiddleVinSize8(cont1, 46, Element.ALIGN_LEFT, false, false));
            document.add(paySinTable);

            Paragraph perArt = new Paragraph("PERITAJE Y ARBITRAJE: ", arialBoldSize8);
            Chunk perArtChunk = new Chunk("Las controversias de hecho sobre las características técnicas de " +
                    "un seguro, serán resueltas a través del peritaje, para lo cual las partes nombrarán a " +
                    "un perito (médico calificado), dando como válido su pronunciamiento. Si por esta vía " +
                    "no se llegara a un acuerdo sobre dichas controversias, éstas deberán definirse por la " +
                    "vía del arbitraje. Las controversias de derecho suscitadas entre las partes sobre la " +
                    "naturaleza y alcance del contrato de seguro, serán resueltas en única e inapelable " +
                    "instancia, por la vía del arbitraje, de acuerdo a lo previsto en la Ley de Conciliación " +
                    "y Arbitraje Nº 708 de fecha 25-06-2015. La Autoridad de Fiscalización y Control de " +
                    "Pensiones y Seguros – APS podrá fungir como instancia de conciliación, para todo " +
                    "siniestro cuya cuantía no supere el monto de UFV 100.000 (Cien Mil 00/100 " +
                    "Unidades de Fomento de Vivienda). Si por esta vía no existiera un acuerdo, " +
                    "la APS podrá conocer y resolver la controversia por resolución administrativa debidamente motivada.", arialSize8);
            perArt.add(perArtChunk);
            document.add(perArt);

            Paragraph prescription = new Paragraph("PRESCRIPCIÓN: ", arialBoldSize8);
            Chunk prescriptionChunk = new Chunk("En caso de muerte, los beneficios del seguro de vida individual " +
                    "temporal inclusivo no reclamados, prescriben en favor, del Estado, en el término de " +
                    "cinco (5) años, a contar de la fecha en que el beneficiario conozca la existencia del " +
                    "beneficio en su favor.", arialSize8);
            prescription.add(prescriptionChunk);
            document.add(prescription);

            Paragraph prevCons = new Paragraph("CONSENTIMIENTO PREVIO DEL ASEGURADO: ", arialBoldSize8);
            Chunk prevConsChunk = new Chunk("El seguro para el caso de muerte, contratado por un tercero, " +
                    "es nulo si no existe el consentimiento propio del Asegurado antes de su celebración. " +
                    "Igualmente es nulo el contrato de seguro para el caso de muerte de un menor de edad " +
                    "que no haya cumplido los catorce (14) años o de persona sujeta a interdicción. " +
                    "En caso de verificarse alguno de los casos citados se devolverán las primas cobradas.", arialSize8);
            prevCons.add(prevConsChunk);
            document.add(prevCons);

            Paragraph finContract = new Paragraph("TERMINACIÓN DEL CONTRATO: ", arialBoldSize8);
            Chunk finContractChunk = new Chunk("El Asegurado podrá terminar el presente contrato por " +
                    "voluntad unilateral, en cualquier momento sin expresión de causa, la cual producirá " +
                    "sus efectos desde su notificación escrita a la Entidad Aseguradora. En caso de" +
                    " que la solicitud de terminación se encuentre dentro los primeros treinta (30) días" +
                    " continuos de vigencia de la póliza se devolverá el 100% de la prima total pagada." +
                    " Si la solicitud de terminación es posterior a los primeros treinta (30) días continuos " +
                    "de vigencia, se devolverá el valor de rescate (entendiéndose por valor de rescate a la " +
                    "suma de dinero provisionada por la Entidad Aseguradora en caso que la póliza termine antes del" +
                    " plazo establecido para la entrega al asegurado) a la fecha del requerimiento descontando el " +
                    "treinta por ciento (30%) de la prima anual correspondiente a gastos de administración.", arialSize8);
            finContract.add(finContractChunk);
            document.add(finContract);

            Paragraph emiCert = new Paragraph("EMISIÓN DEL CERTIFICADO DEL SEGURO: ", arialBoldSize8);
            Chunk emiCertChunk = new Chunk("La emisión del presente certificado de cobertura se realiza " +
                    "conforme aceptación expresa para la celebración del contrato de seguro, por parte del " +
                    "Asegurado  " + insurerName + " con documento de identidad N° " + identificationAndCmp +
                    ", realizada de forma digital desde el " + typeMessage + generateCertificateVin.getMessageDTO().getTo() + ", " +
                    "en fecha " + HelpersMethods.formatStringOnlyDate(generateCertificateVin.getMessageDTO().getCreatedAt()) +
                    " a horas " + HelpersMethods.formatStringOnlyHourAndMinute(generateCertificateVin.getMessageDTO().getCreatedAt()) +
                    " a través del Tomador: " + holderCompanyName, arialSize8);
            emiCert.add(emiCertChunk);
            document.add(emiCert);

            Paragraph dateAndPlace = new Paragraph("FECHA Y LUGAR DE EMISIÓN: ", arialBoldSize8);
            String emissionPlace = generateCertificateVin.getRegional() != null ? generateCertificateVin.getRegional().getDescription() : "SANTA CRUZ";
            Chunk dateAndPlaceChunk = new Chunk(HelpersMethods.formatStringOnlyDate(generateCertificateVin.getPolicy().getIssuanceDate()) + ", " + emissionPlace, arialSize8);//Ver que dato poner aqui
            dateAndPlace.add(dateAndPlaceChunk);
            document.add(dateAndPlace);

            document.add(blankJump);
            document.add(blankJump);

            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                String mfranco = "LUIS MAURICIO FRANCO MELAZZINI";
                String maguirre = "MARIO EDMUNDO AGUIRRE DURAN";

                Date nowDate = new Date();

                String mfrancoFirm = "Firmado digitalmente por " + mfranco + " \nFecha:" + HelpersMethods.formatStringOnlyDateAndHour(nowDate);
                String maguirreFirm = "Firmado digitalmente por " + maguirre + " \nFecha:" + HelpersMethods.formatStringOnlyDateAndHour(nowDate);
                PdfPTable firmTable = new PdfPTable(6);
                firmTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                firmTable.setLockedWidth(true);
                firmTable.setTotalWidth(150f);

                firmTable.addCell(getCellVinDefault(mfranco, 1, Element.ALIGN_LEFT, 4, false, false, true));
                firmTable.addCell(getCellVinDefault(mfrancoFirm, 2, Element.ALIGN_LEFT, 3, false, false, true));
                firmTable.addCell(getCellVinDefault(maguirre, 1, Element.ALIGN_LEFT, 4, false, false, true));
                firmTable.addCell(getCellVinDefault(maguirreFirm, 2, Element.ALIGN_LEFT, 3, false, false, true));
                document.add(firmTable);
            } else {
                String rfmolina = "RAFAEL FERNANDO MOLINA LIZARAZU";

                Date nowDate = new Date();

                String rfmolinaFirm = "Firmado digitalmente por " + rfmolina + " \nFecha:" + HelpersMethods.formatStringOnlyDateAndHour(nowDate);
                PdfPTable firmTable = new PdfPTable(2);
                firmTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                firmTable.setLockedWidth(true);
                firmTable.setTotalWidth(80f);

                firmTable.addCell(getCellVinDefault(rfmolina, 1, Element.ALIGN_LEFT, 4, false, false, true));
                firmTable.addCell(getCellVinDefault(rfmolinaFirm, 1, Element.ALIGN_LEFT, 3, false, false, true));
                document.add(firmTable);
            }

            document.add(blankJump);

            Paragraph firmAutTitle = new Paragraph("FIRMAS AUTORIZADAS\n" +
                    insurerCompanyName + "\n", arialBoldSize8);
            firmAutTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(firmAutTitle);

            //#region Beneficiarios menores de edad

            if (beneficiaryUnderAge) {
                PdfPTable benUnderTable = new PdfPTable(48);
                benUnderTable.setTotalWidth(Utilities.millimetersToPoints(190));
                benUnderTable.setLockedWidth(true);
                benUnderTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                benUnderTable.addCell(getCellVinSize8("", 48, Element.ALIGN_LEFT, true, false));

                PdfPCell cellBorder = new PdfPCell(new Paragraph("", arialBoldSize7));
                cellBorder.setBorder(Rectangle.TOP);
                cellBorder.setColspan(48);
                benUnderTable.addCell(cellBorder);

                PdfPCell delimiterCell = new PdfPCell();
                delimiterCell.setColspan(48); //antes 5
                delimiterCell.setBorder(Rectangle.NO_BORDER);
                benUnderTable.addCell(delimiterCell);
                benUnderTable.addCell(getCellWithNoMiddleVinSize8("Nota Aclarativa:", 48, Element.ALIGN_LEFT, true, false)); //antes 5
                benUnderTable.addCell(getCellWithNoMiddleVinSize8("En caso de que al fallecimiento del asegurado,  el/los " +
                                "beneficiario(s) sea(n) menor(es) de edad, el/los responsable(s) de Cobro del porcentaje que le(s) " +
                                "corresponde es/son:",
                        48, Element.ALIGN_JUSTIFIED, false, false)); //antes 5
                int beneficiaryNumber = 0;
                for (int i = 0; i < beneficiareUnderNames.size(); i++) {
                    beneficiaryNumber++;
                    Chunk benTitle = new Chunk("Beneficiario " + beneficiaryNumber + ": ", arialSize7);
                    Chunk resCopTitle = new Chunk(" - Responsable de cobro: ", arialSize7);
                    Chunk benefiaciaryName = new Chunk(beneficiareUnderNames.get(i), arialSize7);
                    Chunk resCopName = new Chunk(beneficiareUnderRecip.get(i), arialSize7);

                    Phrase resTotalCR = new Phrase();
                    resTotalCR.add(benTitle);
                    resTotalCR.add(benefiaciaryName);
                    resTotalCR.add(resCopTitle);
                    resTotalCR.add(resCopName);

                    PdfPCell respCobCell = new PdfPCell();
                    respCobCell.setPhrase(resTotalCR);
                    respCobCell.setBorder(Rectangle.NO_BORDER);
                    respCobCell.setColspan(48); //antes 5
                    respCobCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    benUnderTable.addCell(respCobCell);
                }
                document.add(benUnderTable);
            }

            //#endregion

            //#endregion

            document.close();
        } catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateVINSettlement(GenerateDocSettlement obj) {
        Document document = new Document(PageSize.LETTER);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            Font arialBoldSize12 = FontFactory.getFont("ARIAL_BOLD");
            arialBoldSize12.setSize(12);
            Font arialSize8 = FontFactory.getFont("ARIAL");
            arialSize8.setSize(8);
            Font arialBoldSize7 = FontFactory.getFont("ARIAL_BOLD");
            arialBoldSize7.setSize(8);

            Color bgGray = new Color(191, 191, 191);
            Color bgLightGray = new Color(242, 242, 242);
            PdfPCell rowBlank = getCellVinFiniquito(" ", 48, Element.ALIGN_LEFT, 6, false, null);
            int cellFontSize = 8;
            Paragraph jumpLine = new Paragraph("\n");

            document.open();

            try {
                Image img = Image.getInstance(properties.getPathImages() + "SC-Vida-y-Salud-fondo-blanco.jpg");
                img.scaleToFit(210, 316);
                img.setAbsolutePosition(25, 712);
                document.add(img);
            } catch (Exception x) {
                x.printStackTrace();
            }

            String tab = "    ";
            String nroSettlement = obj.getNroSettlement();
            String policyAssured = obj.getAssuranceName().toUpperCase();
            String policyName = obj.getPolicyName().toUpperCase();
            String policyCode = obj.getPolicyNumber().toUpperCase();
            String policyDateStart = HelpersMethods.formatStringOnlyDate(obj.getPolicyFromDate());
            String policyDateEnd = HelpersMethods.formatStringOnlyDate(obj.getPolicyToDate());
            String policyCI = obj.getAssuranceIdentificationNumber() + obj.getAssuranceIdentificationExtension();
            String policyAmountAssured = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(obj.getInsuredCapital(), "Bs");
            String policyDateRequest = HelpersMethods.formatStringOnlyDate(obj.getRequestDate());

            String paymentAccount = obj.getAccountNumber();
            String paymentCurrency = obj.getCurrencyDesc().toUpperCase();
            String paymentCurrencyAbbreviation = obj.getCurrencyAbbreviation();
            String paymentAmountAccepted = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getAmountAccepted(), paymentCurrencyAbbreviation);
            String paymentDollarExchangeRate = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getCurrencyDollarValue(), paymentCurrencyAbbreviation);

            String detailRescueValue = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getRescueValue(), "");
            String detailAdministrativeExpenses = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getAdminExpenses(), "");
            String detailDiscountPerDay = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getDiscountPerDay(), "");
            String detailValueToReturn = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getValueToReturn(), "");
            String detailCurrencyDetail = paymentCurrencyAbbreviation.toUpperCase();

            LocalDateTime date = DateUtils.asDateToLocalDateTime(obj.getDate());
            Calendar acceptanceDate = DateUtils.asCalendarLocalDateTime(date);
            String acceptanceDateDay = Integer.toString(acceptanceDate.get(Calendar.DATE));
            String acceptanceDateMonth = "enero";

            //region switch Months

            switch (acceptanceDate.get(Calendar.MONTH)) {
                case 1:
                    acceptanceDateMonth = "enero";
                    break;
                case 2:
                    acceptanceDateMonth = "febrero";
                    break;
                case 3:
                    acceptanceDateMonth = "marzo";
                    break;
                case 4:
                    acceptanceDateMonth = "abril";
                    break;

                case 5:
                    acceptanceDateMonth = "mayo";
                    break;

                case 6:
                    acceptanceDateMonth = "junio";
                    break;

                case 7:
                    acceptanceDateMonth = "julio";
                    break;

                case 8:
                    acceptanceDateMonth = "agosto";
                    break;

                case 9:
                    acceptanceDateMonth = "septiembre";
                    break;

                case 10:
                    acceptanceDateMonth = "octubre";
                    break;

                case 11:
                    acceptanceDateMonth = "noviembre";
                    break;
                case 12:
                    acceptanceDateMonth = "diciembre";
                    break;
            }

            //endregion

            String acceptanceDateYear = Integer.toString(acceptanceDate.get(Calendar.YEAR));
            String acceptancePlace = obj.getCity();
            String acceptanceInsurerCompany = obj.getInsurerCompanyName().toUpperCase();

            // Title
            Paragraph title = new Paragraph("LIQUIDACIÓN Y FINIQUITO DE RESCATE" + tab + "NRO. VIN" + nroSettlement, arialBoldSize12);
            title.setAlignment(Element.ALIGN_RIGHT);
            document.add(title);
            document.add(jumpLine);

            // Policy Data Table
            PdfPTable policyTable = new PdfPTable(48);
            policyTable.setTotalWidth(Utilities.millimetersToPoints(190));
            policyTable.setLockedWidth(true);
            policyTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            policyTable.addCell(rowBlank);
            policyTable.addCell(getCellVinFiniquito("Datos de la Póliza", 48, Element.ALIGN_LEFT, 8, true, bgGray));
            policyTable.addCell(rowBlank);

            policyTable.addCell(getCellVinFiniquito("Póliza:", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(policyName, 27, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            policyTable.addCell(getCellVinFiniquito("", 12, Element.ALIGN_LEFT, cellFontSize, false, null));
            policyTable.addCell(rowBlank);

            policyTable.addCell(getCellVinFiniquito("Tomador/Asegurado:", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(policyAssured, 20, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            policyTable.addCell(getCellVinFiniquito("No. Póliza:", 7, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(policyCode, 12, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            policyTable.addCell(rowBlank);

            policyTable.addCell(getCellVinFiniquito("No. Cédula de Identidad:", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(policyCI, 20, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            policyTable.addCell(getCellVinFiniquito("Vigencia Póliza:", 7, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(policyDateStart, 6, Element.ALIGN_RIGHT, cellFontSize, false, bgLightGray));
            policyTable.addCell(getCellVinFiniquito(policyDateEnd, 6, Element.ALIGN_RIGHT, cellFontSize, false, bgLightGray));
            policyTable.addCell(rowBlank);

            policyTable.addCell(getCellVinFiniquito("Capital Asegurado:", 11, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito("", 4, Element.ALIGN_LEFT, cellFontSize, false, null));
            policyTable.addCell(getCellVinFiniquito(policyAmountAssured, 10, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            policyTable.addCell(getCellVinFiniquito("", 4, Element.ALIGN_LEFT, cellFontSize, false, null));
            policyTable.addCell(getCellVinFiniquito("Fecha Solicitud de Rescate:", 13, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(policyDateRequest, 6, Element.ALIGN_RIGHT, cellFontSize, false, bgLightGray));
            policyTable.addCell(rowBlank);

            document.add(policyTable);


            // Payment Data Table
            PdfPTable paymentTable = new PdfPTable(48);
            paymentTable.setTotalWidth(Utilities.millimetersToPoints(190));
            paymentTable.setLockedWidth(true);
            paymentTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentTable.addCell(getCellVinFiniquito("Datos para el Pago", 48, Element.ALIGN_LEFT, cellFontSize, true, bgGray));
            paymentTable.addCell(rowBlank);

            paymentTable.addCell(getCellVinFiniquito("Forma de Pago:", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            paymentTable.addCell(getCellVinFiniquito("ABONO EN CUENTA BCO. FASSIL NO.:" + paymentAccount, 20, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            paymentTable.addCell(getCellVinFiniquito("Monto Aceptado: ", 7, Element.ALIGN_LEFT, cellFontSize, true, null));
            paymentTable.addCell(getCellVinFiniquito(paymentAmountAccepted, 6, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            paymentTable.addCell(getCellVinFiniquito("", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            paymentTable.addCell(rowBlank);

            paymentTable.addCell(getCellVinFiniquito("Páguese a la orden de:", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            paymentTable.addCell(getCellVinFiniquito(policyAssured, 20, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            paymentTable.addCell(getCellVinFiniquito(" ", 19, Element.ALIGN_LEFT, cellFontSize, false, null));

            paymentTable.addCell(rowBlank);

            paymentTable.addCell(getCellVinFiniquito("Moneda de pago:", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            paymentTable.addCell(getCellVinFiniquito(paymentCurrency, 20, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            paymentTable.addCell(getCellVinFiniquito("Tipo de cambio:", 7, Element.ALIGN_LEFT, cellFontSize, true, null));
            paymentTable.addCell(getCellVinFiniquito(paymentDollarExchangeRate, 6, Element.ALIGN_LEFT, cellFontSize, false, bgLightGray));
            paymentTable.addCell(getCellVinFiniquito("", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            paymentTable.addCell(rowBlank);

            document.add(paymentTable);

            // Detail Payment Table
            PdfPTable detailPaymentTable = new PdfPTable(48);
            detailPaymentTable.setTotalWidth(Utilities.millimetersToPoints(190));
            detailPaymentTable.setLockedWidth(true);

            detailPaymentTable.addCell(getCellVinFiniquito(" ", 9, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquitoBorder("Detalle", 14, Element.ALIGN_CENTER, cellFontSize, true, null, Rectangle.BOTTOM));
            detailPaymentTable.addCell(getCellVinFiniquito(" ", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquitoBorder(detailCurrencyDetail, 5, Element.ALIGN_CENTER, cellFontSize, true, null, Rectangle.BOTTOM));
            detailPaymentTable.addCell(getCellVinFiniquito(" ", 14, Element.ALIGN_LEFT, cellFontSize, false, null));

            detailPaymentTable.addCell(getCellVinFiniquito(" ", 9, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquito("Valor del rescate(a):", 14, Element.ALIGN_LEFT, cellFontSize, true, null));
            detailPaymentTable.addCell(getCellVinFiniquito("", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquito(detailRescueValue, 5, Element.ALIGN_RIGHT, cellFontSize, true, null));
            detailPaymentTable.addCell(getCellVinFiniquito(" ", 14, Element.ALIGN_LEFT, cellFontSize, false, null));

            detailPaymentTable.addCell(getCellVinFiniquito(" ", 9, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquito("Gastos de Administración (b):", 14, Element.ALIGN_LEFT, cellFontSize, true, null));
            detailPaymentTable.addCell(getCellVinFiniquito("", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquito(detailAdministrativeExpenses, 5, Element.ALIGN_RIGHT, cellFontSize, true, null));
            detailPaymentTable.addCell(getCellVinFiniquito(" ", 14, Element.ALIGN_LEFT, cellFontSize, false, null));


            detailPaymentTable.addCell(getCellVinFiniquito(" ", 9, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquitoBorder("Descuento del rescate a prórrata día (c):", 14, Element.ALIGN_LEFT, cellFontSize, true, null, Rectangle.BOTTOM));
            detailPaymentTable.addCell(getCellVinFiniquito("", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquitoBorder(detailDiscountPerDay, 5, Element.ALIGN_RIGHT, cellFontSize, true, null, Rectangle.BOTTOM));
            detailPaymentTable.addCell(getCellVinFiniquito(" ", 14, Element.ALIGN_LEFT, cellFontSize, false, null));

            detailPaymentTable.addCell(getCellVinFiniquito(" ", 9, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquito("Valor del rescate a Devolver (e = a-b-c):", 14, Element.ALIGN_LEFT, cellFontSize, true, null));
            detailPaymentTable.addCell(getCellVinFiniquito("", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            detailPaymentTable.addCell(getCellVinFiniquitoBorder(detailValueToReturn, 5, Element.ALIGN_RIGHT, cellFontSize, true, null, Rectangle.BOTTOM));
            detailPaymentTable.addCell(getCellVinFiniquito(" ", 14, Element.ALIGN_LEFT, cellFontSize, false, null));

            detailPaymentTable.addCell(rowBlank);
            detailPaymentTable.addCell(rowBlank);
            document.add(detailPaymentTable);

            // acceptanceTable
            PdfPTable acceptanceTable = new PdfPTable(48);
            acceptanceTable.setTotalWidth(Utilities.millimetersToPoints(190));
            acceptanceTable.setLockedWidth(true);
            acceptanceTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            acceptanceTable.addCell(getCellVinFiniquito("Conformidad y Aceptación de la Liquidación y Finiquito de Rescate", 48, Element.ALIGN_LEFT, cellFontSize, true, bgGray));
            acceptanceTable.addCell(rowBlank);

            PdfPCell cell = new PdfPCell();
            cell.setColspan(48);
            cell.setUseAscender(true);
            cell.setUseDescender(true);
            cell.setBorder(Rectangle.NO_BORDER);
            Paragraph acceptanceCont = new Paragraph("", arialSize8);
            acceptanceCont.add("Mediante la firma del presente documento, yo ");
            acceptanceCont.add(new Chunk(policyAssured, arialBoldSize7));
            acceptanceCont.add(" como Tomador de la póliza ");
            acceptanceCont.add(new Chunk(policyCode, arialBoldSize7));
            acceptanceCont.add(" recibo la presente Liquidación y Finiquito emitida por " + acceptanceInsurerCompany +
                    ", aceptando el monto del valor de rescate a devolver, asimismo declaro " +
                    "haber leído la misma en su integridad, por lo cual doy mi conformidad y no teniendo nada más que " +
                    "reclamar, renuncio  expresamente a cualquier acción de reclamo en la vía administrativa, legal y/o " +
                    "proceso judicial o extrajudicial con relación a éste seguro y/o a la liquidación entregada. Dando por " +
                    "finalizada la cobertura de la póliza de Seguro de Vida Individual Temporal Inclusivo anteriormente " +
                    "indicada, sin lugar a recurso ulterior, otorgando al presente documento la calidad de COSA JUZGADA, " +
                    "conforme el artículo 949 del Código Civil.");
            acceptanceCont.setAlignment(Element.ALIGN_JUSTIFIED);
            cell.addElement(acceptanceCont);
            acceptanceTable.addCell(cell);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(getCellVinFiniquito("Dando fe a lo anteriormente señalado firmo la presente Liquidación y Finiquito de Rescate.", 48, Element.ALIGN_LEFT, cellFontSize, false, null));
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(getCellVinFiniquito(acceptancePlace + ",", 8, Element.ALIGN_LEFT, cellFontSize, false, null));
            acceptanceTable.addCell(getCellVinFiniquito(acceptanceDateDay + " de " + acceptanceDateMonth + " de " +
                    acceptanceDateYear, 40, Element.ALIGN_LEFT, cellFontSize, false, null));
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(getCellVinFiniquito(" ", 7, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(getCellVinFiniquito("FIRMA:", 9, Element.ALIGN_RIGHT, cellFontSize, false, null));
            acceptanceTable.addCell(getCellVinFiniquito(" ", 1, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(getCellVinFiniquitoBorder("TOMADOR Y/O ASEGURADO", 18, Element.ALIGN_CENTER, cellFontSize, true, null, Rectangle.TOP));
            acceptanceTable.addCell(getCellVinFiniquito(" ", 13, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(getCellVinFiniquito(" ", 7, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(getCellVinFiniquito("NO. C.I.:", 9, Element.ALIGN_RIGHT, cellFontSize, false, null));
            acceptanceTable.addCell(getCellVinFiniquito(" ", 1, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(getCellVinFiniquitoBorder(" ", 18, Element.ALIGN_CENTER, cellFontSize, true, null, Rectangle.BOTTOM));
            acceptanceTable.addCell(getCellVinFiniquito(" ", 13, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(getCellVinFiniquito(" ", 7, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(getCellVinFiniquito("FECHA DE RECEPCIÓN:", 9, Element.ALIGN_RIGHT, cellFontSize, false, null));
            acceptanceTable.addCell(getCellVinFiniquito(" ", 1, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(getCellVinFiniquitoBorder(" ", 18, Element.ALIGN_CENTER, cellFontSize, true, null, Rectangle.BOTTOM));
            acceptanceTable.addCell(getCellVinFiniquito(" ", 13, Element.ALIGN_CENTER, cellFontSize, true, null));
            acceptanceTable.addCell(rowBlank);

            document.add(acceptanceTable);
            PdfPTable firmsFinish = new PdfPTable(1);
            Image imgFirst = Image.getInstance(properties.getPathImages() + "firmasEscaneadas.jpg");
            imgFirst.scaleAbsolute(400, 100);
            PdfPCell firstNameAuthFirm = new PdfPCell();
            firstNameAuthFirm.setBorder(Rectangle.NO_BORDER);
            firstNameAuthFirm.setHorizontalAlignment(Element.ALIGN_CENTER);
            firstNameAuthFirm.addElement(imgFirst);
            firmsFinish.addCell(firstNameAuthFirm);
            document.add(firmsFinish);
            document.close();
        } catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateVINRescission(GenerateDocSettlement obj) {
        Document document = new Document(PageSize.LETTER);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
        FontFactory.register(properties.getPathFonts() + "Arial_Italic.ttf", "ARIAL_ITALIC");

        try {

            PdfWriter writer = PdfWriter.getInstance(document, out);
            Font arialSize8 = FontFactory.getFont("ARIAL");
            arialSize8.setSize(8);
            Font arialBoldSize7 = FontFactory.getFont("ARIAL_BOLD");
            arialBoldSize7.setSize(7);
            Font arialItalic7 = FontFactory.getFont("ARIAL_ITALIC");
            arialItalic7.setSize(7);
            Font arialFontItalicSize8 = FontFactory.getFont("ARIAL", Font.ITALIC, Color.BLACK);
            arialFontItalicSize8.setSize(8);

            String city = obj.getCity();
            String date = HelpersMethods.formatStringOnlyDate(obj.getDate());
            String policyName = obj.getPolicyName().toUpperCase();
            String policyNumber = obj.getPolicyNumber().toUpperCase();
            String insuredCapital = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(obj.getInsuredCapital(), "Bs");
            String premiumPaid = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getPremiumPaid(), "Bs");
            String from = HelpersMethods.formatStringOnlyDate(obj.getPolicyFromDate());
            String toDate = HelpersMethods.formatStringOnlyDate(obj.getPolicyToDate());
            String yearsQuantity = obj.getCreditTermInYears().toString();
            String rescueRequestDate = HelpersMethods.formatStringOnlyDate(obj.getRequestDate());
            String yearlyPremium = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(obj.getPremiumPaidAnnual(), "Bs");
            String years = obj.getYearsPassed().toString();
            String days = obj.getDaysPassed().toString();
            String rescueValue = HelpersMethods.convertNumberToCompanyFormatNumber(obj.getRescueValue());
            String expenseManagement = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getAdminExpenses(), "");
            String rescueDiscount = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getDiscountPerDay(), "");
            String rescueValueToReturn = HelpersMethods.convertNumberToCompanyFormatNumberAndCurrency(
                    obj.getValueToReturn(), "");
            String nameClient = obj.getAssuranceName().toUpperCase();
            String identificationNumber = obj.getAssuranceIdentificationNumber() + obj.getAssuranceIdentificationExtension();
            String cellphone = obj.getCellphone();
            String accountNumber = obj.getAccountNumber();

            Color bgGray = new Color(244, 244, 244);
            PdfPCell rowBlank = getCellVinFiniquito(" ", 48, Element.ALIGN_LEFT, 8, false, null);
            int cellFontSize = 8;

            GenericHeaderPdfTemplate event = new GenericHeaderPdfTemplate(properties);
            writer.setPageEvent(event);
            document.open();

            // head DataTable
            PdfPTable headTable = new PdfPTable(48);
            headTable.setTotalWidth(Utilities.millimetersToPoints(190));
            headTable.setLockedWidth(true);
            headTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            headTable.addCell(getCellVinFiniquito(city, 10, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            headTable.addCell(getCellVinFiniquito(date, 6, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            headTable.addCell(getCellVinFiniquito("", 32, Element.ALIGN_CENTER, cellFontSize, false, null));
            headTable.addCell(rowBlank);
            headTable.addCell(rowBlank);

            headTable.addCell(getCellVinFiniquito("Señores", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            headTable.addCell(getCellVinFiniquito("", 38, Element.ALIGN_LEFT, cellFontSize, false, null));

            headTable.addCell(getCellVinFiniquito("Santa Cruz Vida y Salud Seguros y Reaseguros Personales S.A.", 25, Element.ALIGN_LEFT, cellFontSize, true, null));
            headTable.addCell(getCellVinFiniquito("", 23, Element.ALIGN_LEFT, cellFontSize, true, null));

            headTable.addCell(getCellVinFiniquito("Presente.-", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            headTable.addCell(getCellVinFiniquito("", 38, Element.ALIGN_LEFT, cellFontSize, false, null));
            headTable.addCell(rowBlank);
            headTable.addCell(rowBlank);

            headTable.addCell(getCellVinFiniquito("Referencia: ", 5, Element.ALIGN_LEFT, cellFontSize, false, null));
            headTable.addCell(getCellVinFiniquitoBorder("Solicitud de Rescate - Seguro de Vida Individual Temporal Inclusivo", 30, Element.ALIGN_CENTER, cellFontSize, true, null, Rectangle.BOTTOM));
            headTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));
            headTable.addCell(rowBlank);
            headTable.addCell(rowBlank);
            document.add(headTable);

            // body DataTable
            PdfPTable bodyTable = new PdfPTable(48);
            bodyTable.setTotalWidth(Utilities.millimetersToPoints(190));
            bodyTable.setLockedWidth(true);
            bodyTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Mediante la presente, tengo a bien solicitar el rescate total del Seguro de Vida Individual", 34, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("", 11, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Temporal Inclusivo", 7, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(policyNumber, 12, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("contratada por mi persona,", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("", 17, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("asimismo en consideración a las condiciones de la póliza, solicito la devolución correspondiente", 39, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("", 6, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("a mi favor, a continuación expuesta:", 16, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("", 29, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(rowBlank);

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Capital asegurado:", 7, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(insuredCapital, 10, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("", 9, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Prima pagada: ", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(premiumPaid, 9, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("", 4, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Vigencia de Póliza:", 7, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Desde: ", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(from, 9, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("Hasta: ", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(toDate, 9, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("", 4, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Cantidad de años:", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(yearsQuantity, 9, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("", 23, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(rowBlank);

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Fecha de solicitud de Rescate", 17, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(rescueRequestDate, 9, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("Prima anual:", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(yearlyPremium, 9, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(rowBlank);

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Vigencia transcurrida:", 13, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Años: ", 4, Element.ALIGN_RIGHT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(years, 9, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("Días: ", 6, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(days, 9, Element.ALIGN_CENTER, cellFontSize, false, bgGray));
            bodyTable.addCell(getCellVinFiniquito("", 8, Element.ALIGN_CENTER, cellFontSize, false, null));
            bodyTable.addCell(rowBlank);
            bodyTable.addCell(rowBlank);
            //sum

            bodyTable.addCell(getCellVinFiniquito("", 30, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquitoBorder("Bs", 5, Element.ALIGN_CENTER, cellFontSize, true, null, Rectangle.BOTTOM));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Valor de Rescate (a):", 20, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(rescueValue, 5, Element.ALIGN_RIGHT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Gastos de Administración (b):", 20, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito(expenseManagement, 5, Element.ALIGN_RIGHT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Descuento del rescate a prórrata día (c):", 20, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquitoBorder(rescueDiscount, 5, Element.ALIGN_RIGHT, cellFontSize, false, null, Rectangle.BOTTOM));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquito("Valor del rescate a Devolver (e = a-b-c):", 20, Element.ALIGN_LEFT, cellFontSize, true, null));
            bodyTable.addCell(getCellVinFiniquitoBorder(rescueValueToReturn, 5, Element.ALIGN_RIGHT, cellFontSize, true, null, Rectangle.BOTTOM));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(rowBlank);
            bodyTable.addCell(rowBlank);

            bodyTable.addCell(getCellVinFiniquito("", 3, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinRescission("Aclaración: Si la solicitud de terminación es posterior a los primeros treinta (30) días continuos de vigencia, se devolverá considerando el valor de rescate (entendiéndose por valor de rescate a la suma de dinero provisionada por la Entidad Aseguradora en caso que la póliza termine antes del plazo establecido para la entrega al asegurado).",
                    32, Element.ALIGN_JUSTIFIED, cellFontSize, false, null, Rectangle.NO_BORDER, false));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(rowBlank);

            bodyTable.addCell(getCellVinFiniquito("Firma:", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquitoBorder("", 25, Element.ALIGN_LEFT, cellFontSize, false, null, Rectangle.BOTTOM));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("Nombre Completo:", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquitoBorder(nameClient, 25, Element.ALIGN_LEFT, cellFontSize, false, bgGray, Rectangle.BOTTOM));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("No. Cédula de Identidad:", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquitoBorder(identificationNumber, 25, Element.ALIGN_LEFT, cellFontSize, false, bgGray, Rectangle.BOTTOM));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            bodyTable.addCell(getCellVinFiniquito("Teléfono(s) y Celular(es):", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(getCellVinFiniquitoBorder(cellphone, 25, Element.ALIGN_LEFT, cellFontSize, false, bgGray, Rectangle.BOTTOM));
            bodyTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));
            bodyTable.addCell(rowBlank);
            document.add(bodyTable);


            //footer DataTable
            PdfPTable footerTable = new PdfPTable(48);
            footerTable.setTotalWidth(Utilities.millimetersToPoints(190));
            footerTable.setLockedWidth(true);
            footerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            footerTable.addCell(rowBlank);
            footerTable.addCell(getCellVinRescission("Autorizo expresamente que la devolución del valor del rescate se realice mediante:", 35, Element.ALIGN_LEFT, cellFontSize, true, null, Rectangle.NO_BORDER, true));
            footerTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));
            footerTable.addCell(rowBlank);

            footerTable.addCell(getCellVinFiniquito("Forma de Pago:", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            footerTable.addCell(getCellVinFiniquitoBorder("", 25, Element.ALIGN_LEFT, cellFontSize, false, null, Rectangle.BOTTOM));
            footerTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            footerTable.addCell(getCellVinFiniquito("", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            footerTable.addCell(getCellVinRescission("Abono en Cuenta Bco. Fassil No.: " + accountNumber, 25, Element.ALIGN_LEFT, cellFontSize, false, bgGray, Rectangle.BOTTOM, false));
            footerTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            footerTable.addCell(getCellVinFiniquito("Aprobación Compañía de Seguros:", 10, Element.ALIGN_LEFT, cellFontSize, false, null));
            footerTable.addCell(getCellVinFiniquito("", 25, Element.ALIGN_LEFT, cellFontSize, false, null));
            footerTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));
            footerTable.addCell(rowBlank);

            footerTable.addCell(getCellVinFiniquito("Firma(s) Autorizada(s):", 10, Element.ALIGN_RIGHT, cellFontSize, false, null));
            footerTable.addCell(getCellVinFiniquitoBorder("", 25, Element.ALIGN_LEFT, cellFontSize, false, null, Rectangle.BOTTOM));
            footerTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            footerTable.addCell(getCellVinFiniquito("Sello(s):", 10, Element.ALIGN_RIGHT, cellFontSize, false, null));
            footerTable.addCell(getCellVinFiniquito("", 25, Element.ALIGN_LEFT, cellFontSize, false, null));
            footerTable.addCell(getCellVinFiniquito("", 13, Element.ALIGN_LEFT, cellFontSize, false, null));

            document.add(footerTable);

            document.close();
        } catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    @Override
    public byte[] generateVINRamsonSettlement(DocRescueStatement obj) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        Document document = new Document(PageSize.LETTER);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
        FontFactory.register(properties.getPathFonts() + "Arial_Italic.ttf", "ARIAL_ITALIC");
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            Font arialBoldSize12 = FontFactory.getFont("ARIAL_BOLD");
            arialBoldSize12.setSize(12);
            Font arialSize8 = FontFactory.getFont("ARIAL");
            arialSize8.setSize(8);
            Font arialBoldSize7 = FontFactory.getFont("ARIAL_BOLD");
            arialBoldSize7.setSize(7);
            Font arialItalic7 = FontFactory.getFont("ARIAL_ITALIC");
            arialItalic7.setSize(7);
            Font arialFontItalicSize8 = FontFactory.getFont("ARIAL", Font.ITALIC, Color.BLACK);
            arialFontItalicSize8.setSize(8);

            Color bgGray = new Color(191, 191, 191);
            Color bgLightGray = new Color(242, 242, 242);
            PdfPCell rowBlank = getCellVinFiniquito(" ", 48, Element.ALIGN_LEFT, 6, false, null);
            int cellFontSize = 8;
            Paragraph jumpLine = new Paragraph("\n");

            GenericHeaderPdfTemplate event = new GenericHeaderPdfTemplate(properties);
            writer.setPageEvent(event);
            document.open();

            try {
                Image img = Image.getInstance(properties.getPathImages()+"SC-Vida-y-Salud-fondo-blanco.jpg");
                img.scaleToFit(210,316);
                img.setAbsolutePosition(25,712);
                document.add(img);
            } catch (Exception x) {
                x.printStackTrace();
            }
            //            Title
            document.add(jumpLine);
            document.add(jumpLine);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            Paragraph title = new Paragraph("COMUNICADO DE DEVOLUCIÓN DE RESCATE SCS-GTE/"
                    + String.format("%03d", obj.getDocumentNumber())+"-" +currentYear,arialBoldSize12);
            title.setAlignment(Element.ALIGN_RIGHT);
            document.add(title);
            document.add(jumpLine);

            PdfPTable dateTable = new PdfPTable(48);
            dateTable.setTotalWidth(Utilities.millimetersToPoints(190));
            dateTable.setLockedWidth(true);
            dateTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            dateTable.addCell(getCellVinFiniquito("DE :", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            dateTable.addCell(getCellVinFiniquito(obj.getNameOf(), 21, Element.ALIGN_LEFT, cellFontSize, false, null));
            dateTable.addCell(getCellVinFiniquito("AREA : ", 6, Element.ALIGN_LEFT, cellFontSize, true, null));
            dateTable.addCell(getCellVinFiniquito(obj.getAreaSend(), 12, Element.ALIGN_LEFT, cellFontSize, false, null));

            dateTable.addCell(getCellVinFiniquito("PARA :", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            dateTable.addCell(getCellVinFiniquito(obj.getNameFor(), 21, Element.ALIGN_LEFT, cellFontSize, false, null));
            dateTable.addCell(getCellVinFiniquito("AREA : ", 6, Element.ALIGN_LEFT, cellFontSize, true, null));
            dateTable.addCell(getCellVinFiniquito(obj.getAreaReception(), 12, Element.ALIGN_LEFT, cellFontSize, false, null));

            dateTable.addCell(getCellVinFiniquito("FECHA :", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            dateTable.addCell(getCellVinFiniquito(obj.getRequestDate(), 39, Element.ALIGN_LEFT, cellFontSize, false, null));

            dateTable.addCell(getCellVinFiniquito("REF :", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            dateTable.addCell(getCellVinFiniquito(obj.getRef(), 39, Element.ALIGN_LEFT, cellFontSize, false, null));

            dateTable.addCell(rowBlank);
            document.add(dateTable);

            PdfPTable policyTable = new PdfPTable(48);
            policyTable.setTotalWidth(Utilities.millimetersToPoints(190));
            policyTable.setLockedWidth(true);
            policyTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            policyTable.addCell(rowBlank);
            policyTable.addCell(getCellVinFiniquito("", 48, Element.ALIGN_LEFT, 8, true, bgGray));

            policyTable.addCell(getCellVinFiniquito("Forma de Pago: ", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(obj.getPaymentMethod() +" : "+obj.getNumberPaymentMethod(), 27, Element.ALIGN_LEFT, cellFontSize, false, null));
            policyTable.addCell(getCellVinFiniquito("Moneda: ", 6, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(obj.getCurrencyPayment(), 6, Element.ALIGN_LEFT, cellFontSize, false, null));

            policyTable.addCell(getCellVinFiniquito("A nombre de: ", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(obj.getToNamePayment(), 27, Element.ALIGN_LEFT, cellFontSize, false, null));
            policyTable.addCell(getCellVinFiniquito("Nro. Póliza: ", 6, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(obj.getPolicyNumber(), 6, Element.ALIGN_LEFT, cellFontSize, false, null));

            policyTable.addCell(getCellVinFiniquito("No. Cédula de Identidad", 9, Element.ALIGN_LEFT, cellFontSize, true, null));
            policyTable.addCell(getCellVinFiniquito(obj.getIdentificationNumber()+" "+obj.getExtension() , 39, Element.ALIGN_LEFT, cellFontSize, false, null));

            policyTable.addCell(rowBlank);
            document.add(policyTable);

            PdfPTable part2 = new PdfPTable(48);
            part2.setTotalWidth(Utilities.millimetersToPoints(190));
            part2.setLockedWidth(true);
            part2.setHorizontalAlignment(Element.ALIGN_LEFT);

            part2.addCell(getCellVinFiniquito("Capital Asegurado Total", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part2.addCell(getCellVinFiniquito("Valor del Rescate a Devolver", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part2.addCell(getCellVinFiniquito("Prima Neta Rescatada", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part2.addCell(getCellVinFiniquito("Prima Adicional Rescatada", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part2.addCell(getCellVinFiniquito("APS", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part2.addCell(getCellVinFiniquito("FPA", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part2.addCell(getCellVinFiniquito("Prima de Riesgo", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part2.addCell(getCellVinFiniquito("Servicio de Cobranza Rescatado", 6, Element.ALIGN_CENTER, 8, true, bgGray));

            part2.addCell(getCellVinFiniquito(format.format(obj.getCapitalAseguradoTotal()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part2.addCell(getCellVinFiniquito(format.format(obj.getValorRescateADevolver()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part2.addCell(getCellVinFiniquito(format.format(obj.getPrimaNetaRescatada()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part2.addCell(getCellVinFiniquito(format.format(obj.getPrimaAdicionalRescatada()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part2.addCell(getCellVinFiniquito(format.format(obj.getAps()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part2.addCell(getCellVinFiniquito(format.format(obj.getFpa()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part2.addCell(getCellVinFiniquito(format.format(obj.getPrimaRiesgo()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part2.addCell(getCellVinFiniquito(format.format(obj.getServicioCobranzaRescatado()), 6, Element.ALIGN_RIGHT, 8, false, null));

            part2.addCell(rowBlank);
            document.add(part2);

            PdfPTable part3 = new PdfPTable(48);
            part3.setTotalWidth(Utilities.millimetersToPoints(190));
            part3.setLockedWidth(true);
            part3.setHorizontalAlignment(Element.ALIGN_LEFT);

            part3.addCell(getCellVinFiniquito("Prima Cedida Rescatada", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part3.addCell(getCellVinFiniquito("Capital Cedido Rescatado", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part3.addCell(getCellVinFiniquito("Reserva Matemática", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part3.addCell(getCellVinFiniquito("Inpuestos Remesas", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part3.addCell(getCellVinFiniquito("Comisión Broker Rescatada", 6, Element.ALIGN_CENTER, 8, true, bgGray));
            part3.addCell(getCellVinFiniquito("", 18, Element.ALIGN_CENTER, 8, false, null));

            part3.addCell(getCellVinFiniquito(format.format(obj.getPrimaCedidaRescatada()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part3.addCell(getCellVinFiniquito(format.format(obj.getCapitalCedidoRescatado()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part3.addCell(getCellVinFiniquito(format.format(obj.getReservaMatematica()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part3.addCell(getCellVinFiniquito(format.format(obj.getImpuestosRemesas()), 6, Element.ALIGN_RIGHT, 8, false, null));
            part3.addCell(getCellVinFiniquito(format.format(obj.getComisionBrokerRescata()), 6, Element.ALIGN_RIGHT, 8,false, null));
            part3.addCell(getCellVinFiniquito("", 18, Element.ALIGN_RIGHT, 8, false, null));

            part3.addCell(rowBlank);
            part3.addCell(rowBlank);

            document.add(part3);

            PdfPTable acceptanceTable = new PdfPTable(48);
            acceptanceTable.setTotalWidth(Utilities.millimetersToPoints(190));
            acceptanceTable.setLockedWidth(true);
            acceptanceTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            acceptanceTable.addCell(getCellVinFiniquito("AUTORIZACIONES", 48, Element.ALIGN_CENTER, cellFontSize, true, bgGray));
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(rowBlank);
            acceptanceTable.addCell(getCellVinFiniquito("", 5,Element.ALIGN_LEFT, cellFontSize,true,null));
            acceptanceTable.addCell(getCellVinFiniquitoBorder("Gerente Técnico", 17,Element.ALIGN_CENTER, cellFontSize,true,null, Rectangle.BOTTOM));
            acceptanceTable.addCell(getCellVinFiniquito("", 4,Element.ALIGN_LEFT, cellFontSize,true,null));
            acceptanceTable.addCell(getCellVinFiniquitoBorder("Gerente General", 17,Element.ALIGN_CENTER, cellFontSize,true,null, Rectangle.BOTTOM));
            acceptanceTable.addCell(getCellVinFiniquito("", 5,Element.ALIGN_LEFT, cellFontSize,true,null));
            acceptanceTable.addCell(rowBlank);
            document.add(acceptanceTable);

//            PdfPTable firmsFinish = new PdfPTable(1);
//            Image imgFirst = Image.getInstance(properties.getPathImages() + "firmasEscaneadas.jpg");
//            imgFirst.scaleAbsolute(400, 100);
//            PdfPCell firstNameAuthFirm = new PdfPCell();
//            firstNameAuthFirm.setBorder(Rectangle.NO_BORDER);
//            firstNameAuthFirm.setHorizontalAlignment(Element.ALIGN_CENTER);
//            firstNameAuthFirm.addElement(imgFirst);
//            firmsFinish.addCell(firstNameAuthFirm);
//            document.add(firmsFinish);
            document.close();


        }catch (Exception e) {
            logger.error("Error al querer exportar a PDF " + e.getMessage());
        }
        return out.toByteArray();
    }

    //#region GEL - Cells format
    private PdfPCell getCell(String content, int cm, int textAlign) {
        FontFactory.register(properties.getPathFonts() + "times.ttf", "ARIAL");
        Font arialFontSize10 = FontFactory.getFont("ARIAL");
        arialFontSize10.setSize(7); //antes 7-10 // ANTES 8
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(cm);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(content, arialFontSize10);
        p.setAlignment(textAlign);
        cell.addElement(p);
        return cell;
    }

    private PdfPCell getCell(String content, int cm, int textAlign, boolean isBold) {
        FontFactory.register(properties.getPathFonts() + "timesbd.ttf", "ARIAL_BOLD");
        Font arialFontSize10 = FontFactory.getFont("ARIAL_BOLD");
        arialFontSize10.setSize(7); // ANTES 8
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(cm);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(content, arialFontSize10);
        p.setAlignment(textAlign);
        cell.addElement(p);
        return cell;
    }
    //#endregion

    //#region Celdas - VIN
    private PdfPCell getCellVinSize8(String content, int cm, int textAlign, boolean isBold, boolean borderOn) {
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        Font arialFont = FontFactory.getFont("ARIAL");
        if (isBold) {
            FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
            arialFont = FontFactory.getFont("ARIAL_BOLD");
        }
        arialFont.setSize(6);//8
        PdfPCell cell = new PdfPCell();
        if (!borderOn) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        cell.setColspan(cm);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph p = new Paragraph(content, arialFont);
        p.setAlignment(textAlign);
        cell.addElement(p);
        return cell;
    }

    private PdfPCell getCellVinDefault(String content, int cm, int textAlign, int fontSize, boolean isBold, boolean borderOn, boolean middle) {
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        Font arialFont = FontFactory.getFont("ARIAL");
        if (isBold) {
            FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
            arialFont = FontFactory.getFont("ARIAL_BOLD");
        }
        arialFont.setSize(fontSize);
        PdfPCell cell = new PdfPCell();
        if (!borderOn) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        if (middle) {
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.setColspan(cm);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(content, arialFont);
        p.setAlignment(textAlign);
        cell.addElement(p);
        return cell;
    }

    private PdfPCell getCellWithNoMiddleVinSize8(String content, int cm, int textAlign, boolean isBold, boolean borderOn) {
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        Font arialFont = FontFactory.getFont("ARIAL");
        if (isBold) {
            FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
            arialFont = FontFactory.getFont("ARIAL_BOLD");
        }
        arialFont.setSize(6);//8
        PdfPCell cell = new PdfPCell();
        if (!borderOn) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        cell.setColspan(cm);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(content, arialFont);
        p.setAlignment(textAlign);
        cell.addElement(p);
        return cell;
    }

    private PdfPCell getCellVinFiniquito(String content, int colspan, int textAlign, int fontSize, boolean isBold, Color color) {
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        Font arialFont = FontFactory.getFont("ARIAL");
        if (isBold) {
            FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
            arialFont = FontFactory.getFont("ARIAL_BOLD");
        }
        arialFont.setSize(fontSize);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(colspan);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        if (color != null) {
            cell.setBackgroundColor(color);
        }
        Paragraph p = new Paragraph(content, arialFont);
        p.setAlignment(textAlign);
        cell.addElement(p);
        return cell;
    }

    private PdfPCell getCellVinRescission(String content, int colspan, int textAlign, int fontSize, boolean isBold, Color color, int border, boolean noBorder) {
        FontFactory.register(properties.getPathFonts() + "Arial_Italic.ttf", "ARIAL_ITALIC");
        Font arialFont = FontFactory.getFont("ARIAL_ITALIC");
        if (isBold) {
            FontFactory.register(properties.getPathFonts() + "Arial Bold Italic.ttf", "ARIAL");
            arialFont = FontFactory.getFont("ARIAL", fontSize, Color.BLACK);
        }
        arialFont.setSize(fontSize);
        PdfPCell cell = new PdfPCell();
        if (noBorder) {
            cell.setBorder(Rectangle.NO_BORDER);
        } else {
            cell.setBorder(border);
        }
        cell.setColspan(colspan);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        if (color != null) {
            cell.setBackgroundColor(color);
        }
        Paragraph p = new Paragraph(content, arialFont);
        p.setAlignment(textAlign);
        cell.addElement(p);
        return cell;
    }

    private PdfPCell getCellVinFiniquitoBorder(String content, int colspan, int textAlign, int fontSize, boolean isBold, Color color, int border) {
        FontFactory.register(properties.getPathFonts() + "Arial.ttf", "ARIAL");
        Font arialFont = FontFactory.getFont("ARIAL");
        if (isBold) {
            FontFactory.register(properties.getPathFonts() + "Arial_Bold.ttf", "ARIAL_BOLD");
            arialFont = FontFactory.getFont("ARIAL_BOLD");
        }
        arialFont.setSize(fontSize);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(border);
        cell.setColspan(colspan);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        if (color != null) {
            cell.setBackgroundColor(color);
        }
        Paragraph p = new Paragraph(content, arialFont);
        p.setAlignment(textAlign);
        cell.addElement(p);
        return cell;
    }
    //#endregion
}
