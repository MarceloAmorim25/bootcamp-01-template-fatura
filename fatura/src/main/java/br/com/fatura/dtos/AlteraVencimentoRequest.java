package br.com.fatura.dtos;

public class AlteraVencimentoRequest {

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
