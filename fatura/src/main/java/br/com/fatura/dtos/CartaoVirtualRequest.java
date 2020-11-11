package br.com.fatura.dtos;

import br.com.fatura.entidades.Cartao;
import br.com.fatura.entidades.CartaoVirtual;


public class CartaoVirtualRequest {

    private String numeroCartao;

    @Deprecated
    public CartaoVirtualRequest(){}

    public CartaoVirtualRequest(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public CartaoVirtual toModel(Cartao cartao){
        return new CartaoVirtual(cartao);
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }
}
