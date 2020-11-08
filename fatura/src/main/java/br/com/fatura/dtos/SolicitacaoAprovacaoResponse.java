package br.com.fatura.dtos;

public class SolicitacaoAprovacaoResponse {

    private String resultado;

    @Deprecated
    public SolicitacaoAprovacaoResponse(){}

    public SolicitacaoAprovacaoResponse(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
