package br.com.fatura.dtos;

import br.com.fatura.entidades.Cartao;

import javax.validation.constraints.NotBlank;

public class CartaoDto {

    @NotBlank
    private String id;

    @NotBlank
    private String email;

    @Deprecated
    public CartaoDto(){}

    public CartaoDto(@NotBlank String id, @NotBlank String email) {
        this.id = id;
        this.email = email;
    }

    public Cartao toModel(){
        return new Cartao(id, email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
