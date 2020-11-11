package br.com.fatura.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AlteraVencimentoRequest {

    @NotBlank
    @Size(min = 1, max = 31)
    private String dia;

    @Deprecated
    public AlteraVencimentoRequest(){}

    public AlteraVencimentoRequest(String dia) {
        this.dia = dia;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
