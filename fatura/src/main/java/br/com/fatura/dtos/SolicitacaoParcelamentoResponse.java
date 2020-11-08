package br.com.fatura.dtos;

public class SolicitacaoParcelamentoResponse {

    private String resultado;

    @Deprecated
    public SolicitacaoParcelamentoResponse(){}

    public SolicitacaoParcelamentoResponse(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
