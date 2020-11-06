package br.com.fatura.repository;

import br.com.fatura.entidades.Cartao;
import br.com.fatura.entidades.Fatura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaturaRepository extends CrudRepository<Fatura, String> {

    Optional<Fatura> findByCartao(Cartao cartao);

}
