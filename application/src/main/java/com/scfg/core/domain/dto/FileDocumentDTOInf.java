package com.scfg.core.domain.dto;

import java.io.Serializable;

public class FileDocumentDTOInf extends FileDocumentDTO implements Serializable {
    private int cantAcumulado;

    private int cantSemanal;

    public int getCantAcumulado() {
        return cantAcumulado;
    }

    public void setCantAcumulado(int cantAcumulado) {
        this.cantAcumulado = cantAcumulado;
    }

    public int getCantSemanal() {
        return cantSemanal;
    }

    public void setCantSemanal(int cantSemanal) {
        this.cantSemanal = cantSemanal;
    }
}
