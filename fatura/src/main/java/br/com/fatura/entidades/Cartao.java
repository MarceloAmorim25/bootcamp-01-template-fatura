package br.com.fatura.entidades;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    private String numero;

    @NotBlank
    private String email;

    @OneToMany(mappedBy = "cartao")
    private Set<Fatura> faturas;

    public Cartao(String numero, String email) {
        this.numero = numero;
        this.email = email;
    }

}
