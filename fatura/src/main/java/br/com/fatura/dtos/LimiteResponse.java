package br.com.fatura.dtos;

import java.math.BigDecimal;

public class LimiteResponse {


    private BigDecimal limite;


    @Deprecated
    public LimiteResponse(){

    }

    public LimiteResponse(BigDecimal limite) {
        this.limite = limite;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }
}
