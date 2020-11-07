package br.com.fatura.repository;

import br.com.fatura.entidades.Cartao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends CrudRepository<Cartao, String> {

    Optional<Cartao> findByNumero(String numero);

}
