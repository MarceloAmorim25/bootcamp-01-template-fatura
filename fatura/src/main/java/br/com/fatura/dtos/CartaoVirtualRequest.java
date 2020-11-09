package br.com.fatura.dtos;


import br.com.fatura.entidades.CartaoVirtual;
import br.com.fatura.repository.CartaoRepository;

public class CartaoVirtualRequest {

    private String numeroCartao;

    @Deprecated
    public CartaoVirtualRequest(){}

    public CartaoVirtualRequest(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public CartaoVirtual toModel(CartaoRepository cartaoRepository){

        var cartao = cartaoRepository.findByNumero(this.numeroCartao);

        return new CartaoVirtual(cartao.get());

    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }
}
