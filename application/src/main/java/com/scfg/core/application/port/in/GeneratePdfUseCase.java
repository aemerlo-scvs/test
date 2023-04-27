package com.scfg.core.application.port.in;

import com.scfg.core.domain.dto.credicasas.groupthefont.CoverageFileDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.DjsFileDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.PendingNoteFileDTO;
import com.scfg.core.domain.dto.credicasas.groupthefont.RejectNoteFileDTO;
import com.scfg.core.domain.dto.vin.GenerateCertificateVin;
import com.scfg.core.domain.dto.vin.GenerateDocSettlement;

public interface GeneratePdfUseCase {
    byte[] getPdf();
    byte[] generateTheFountPendingNote(PendingNoteFileDTO pendingNoteFileDTO);
    byte[] generateTheFountRejectNote(RejectNoteFileDTO rejectNoteFileDTO);
    byte[] generateCoverageCertificate(CoverageFileDTO coverageFileDTO);
    byte[] generateDJSDocument(DjsFileDTO djsFileDTO);

    byte[] generateVINCoverageCertificate(GenerateCertificateVin generateCertificateVin);
    byte[] generateVINSettlement(GenerateDocSettlement GenerateDocSettlement);
    byte[] generateVINRescission(GenerateDocSettlement generateDocSettlement);

}
